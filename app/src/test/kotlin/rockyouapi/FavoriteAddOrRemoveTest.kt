package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import common.utils.takeRandomValues
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.operation.FavoriteOperation
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.favorite.addOrRemoveFavoriteRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FavoriteAddOrRemoveTest {

    private fun invalidArguments() = Stream.of(
        AddOrRemoveFavoriteArguments(null, null),
        AddOrRemoveFavoriteArguments("", ""),
        AddOrRemoveFavoriteArguments("null", "null"),
        AddOrRemoveFavoriteArguments("  ", "  "),
        AddOrRemoveFavoriteArguments("", " "),
        AddOrRemoveFavoriteArguments(" ", ""),
        AddOrRemoveFavoriteArguments(generateRandomTextByUUID(), null),
        AddOrRemoveFavoriteArguments(null, generateRandomTextByUUID()),
        AddOrRemoveFavoriteArguments(generateRandomTextByUUID(), generateRandomTextByUUID()),
        AddOrRemoveFavoriteArguments("1", null),
        AddOrRemoveFavoriteArguments(null, "1"),
        AddOrRemoveFavoriteArguments("abc", null),
        AddOrRemoveFavoriteArguments(null, "abc"),
        AddOrRemoveFavoriteArguments("-100", null),
        AddOrRemoveFavoriteArguments("1000", null),
        AddOrRemoveFavoriteArguments("-1", "1"),
        AddOrRemoveFavoriteArguments(generateRandomTextByUUID(), "1"),
    )

    private val databaseAPI by lazy {
        runBlocking {
            getOrCreateValue(KEY_STATIC_MAP_DB) {
                connectToProductionDatabaseWithTestApi(readDatabaseConfigurationFromEnv())
            }
        }
    }

    private val productionDatabaseAPI get() = databaseAPI.first

    private val testDatabaseAPI get() = databaseAPI.second

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun add_or_remove_favorite_without_token_and_with_invalid_arguments_return_401(invalidArguments: AddOrRemoveFavoriteArguments) {
        runTestSimple {
            client.makeAddOrRemoveFavoriteRequest(
                operationType = invalidArguments.operationType,
                contentID = invalidArguments.contentID,
                token = null
            )
                .unauthorizedOrFail()
        }
    }

    @RepeatedTest(10)
    fun add_or_remove_favorite_without_token_and_with_valid_arguments_return_401() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            client.makeAddOrRemoveFavoriteRequest(
                operationType = FavoriteOperation.entries.random().operationArgument.toString(),
                contentID = testDatabaseAPI.getRandomContentID().toString(),
                token = null
            )
                .unauthorizedOrFail()
        }
    }

    @RepeatedTest(10)
    fun add_or_remove_favorite_with_refresh_token_and_with_valid_arguments_return_401() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeAddOrRemoveFavoriteRequest(
                operationType = FavoriteOperation.entries.random().operationArgument.toString(),
                contentID = testDatabaseAPI.getRandomContentID().toString(),
                token = registerResponse.refreshToken
            )
                .unauthorizedOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun add_or_remove_favorite_with_token_and_invalid_arguments_return_400(invalidArguments: AddOrRemoveFavoriteArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeAddOrRemoveFavoriteRequest(
                operationType = invalidArguments.operationType,
                contentID = invalidArguments.contentID,
                token = registerResponse.accessToken
            )
                .badRequestOrFail()
        }
    }

    @RepeatedTest(10)
    fun add_favorite_with_token_and_valid_args_return_200_and_insert_record_to_database_when_same_record_not_exist() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val userID = registerResponse.user.id
            val contentIDList = testDatabaseAPI.getAllContentID()
            val favoriteRecordListBeforeCall = testDatabaseAPI.getAllFavoriteRecord()
            val userFavoriteContentIDList = favoriteRecordListBeforeCall
                .filter { it.userID == userID }
                .map(database.external.model.test.FavoriteRecord::contentID)
            val contentIDUserNotAddAsFavorite = contentIDList
                .filter { it !in userFavoriteContentIDList }
                .random()

            client.makeAddOrRemoveFavoriteRequest(
                operationType = FavoriteOperation.ADD.operationArgument.toString(),
                contentID = contentIDUserNotAddAsFavorite.toString(),
                token = registerResponse.accessToken
            )
                .okOrFail()

            val favoriteRecordListAfterCall = testDatabaseAPI.getAllFavoriteRecord()
            val lastFavoriteRecord = favoriteRecordListAfterCall.last()

            assertEquals(userID, lastFavoriteRecord.userID)
            assertEquals(contentIDUserNotAddAsFavorite, lastFavoriteRecord.contentID)
        }
    }

    @RepeatedTest(10)
    fun add_favorite_with_token_and_valid_args_return_409_if_user_already_has_content_as_favorite() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val userID = registerResponse.user.id
            val contentIDList = testDatabaseAPI.getAllContentID()
            val favoriteRecordListBeforeCall = testDatabaseAPI.getAllFavoriteRecord()
            val userFavoriteContentIDList = favoriteRecordListBeforeCall
                .filter { it.userID == userID }
                .map(database.external.model.test.FavoriteRecord::contentID)
            val contentIDListUserNotAddAsFavorite = contentIDList
                .filter { it !in userFavoriteContentIDList }
                .takeRandomValues(5)

            contentIDListUserNotAddAsFavorite.forEach { contentID ->
                client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.ADD.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()
            }

            contentIDListUserNotAddAsFavorite.forEach { contentID ->
                client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.ADD.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .conflictOrFail()
            }
        }
    }

    @RepeatedTest(10)
    fun remove_favorite_with_token_and_valid_args_return_409_and_remove_record_from_database_when_record_exist() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val userID = registerResponse.user.id
            val contentIDList = testDatabaseAPI.getAllContentID()
            val favoriteRecordListBeforeCall = testDatabaseAPI.getAllFavoriteRecord()
            val userFavoriteContentIDList = favoriteRecordListBeforeCall
                .filter { it.userID == userID }
                .map(database.external.model.test.FavoriteRecord::contentID)
            val contentIDListUserNotAddAsFavorite = contentIDList
                .filter { it !in userFavoriteContentIDList }
                .takeRandomValues(5)

            contentIDListUserNotAddAsFavorite.forEach { contentID ->
                client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.ADD.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()
            }

            contentIDListUserNotAddAsFavorite.forEach { contentID ->
                client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.REMOVE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()
            }

            val favoriteRecordListAfterCall = testDatabaseAPI.getAllFavoriteRecord()
            assertEquals(favoriteRecordListBeforeCall, favoriteRecordListAfterCall)
        }
    }

    @RepeatedTest(10)
    fun add_favorite_with_token_and_valid_args_return_409_if_user_not_have_content_in_favorite() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val userID = registerResponse.user.id
            val contentIDList = testDatabaseAPI.getAllContentID()
            val favoriteRecordListBeforeCall = testDatabaseAPI.getAllFavoriteRecord()
            val userFavoriteContentIDList = favoriteRecordListBeforeCall
                .filter { it.userID == userID }
                .map(database.external.model.test.FavoriteRecord::contentID)
            val contentIDListUserNotAddAsFavorite = contentIDList
                .filter { it !in userFavoriteContentIDList }
                .takeRandomValues(5)

            contentIDListUserNotAddAsFavorite.forEach { contentID ->
                client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.REMOVE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .conflictOrFail()
            }

            val favoriteRecordListAfterCall = testDatabaseAPI.getAllFavoriteRecord()
            assertEquals(favoriteRecordListBeforeCall, favoriteRecordListAfterCall)
        }
    }

    private suspend fun HttpClient.makeAddOrRemoveFavoriteRequest(
        operationType: String?,
        contentID: String?,
        token: String?
    ) = post(Routes.FavoriteAddOrRemove.path) {

        token?.let(::bearerAuth)

        setBody(FormDataContent(Parameters.build {
            operationType?.let { append(Routes.FavoriteAddOrRemove.getOperationTypeArgName(), operationType) }
            contentID?.let { append(Routes.FavoriteAddOrRemove.getContentIDArgName(), contentID) }
            build()
        }))

        url {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    internal class AddOrRemoveFavoriteArguments(
        val operationType: String?,
        val contentID: String?,
    )
}