package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.comment.commentAddRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentAddRouteTest {

    private fun invalidArguments() = Stream.of(
        AddCommentArguments(null, null),
        AddCommentArguments(null, generateRandomTextByUUID()),
        AddCommentArguments(null, "1"),
        AddCommentArguments(null, "null"),
        AddCommentArguments(generateRandomTextByUUID(), null),
        AddCommentArguments("1", null),
        AddCommentArguments("1", null),
        AddCommentArguments("null", null),
        AddCommentArguments(generateRandomTextByUUID(), null),
        AddCommentArguments("", ""),
        AddCommentArguments("", null),
        AddCommentArguments(null, ""),
        AddCommentArguments("", "   "),
        AddCommentArguments("   ", ""),
        AddCommentArguments("   ", "          "),
        AddCommentArguments("-1", null),
        AddCommentArguments("-1", ""),
        AddCommentArguments(null, "  "),
    )

    private fun validArguments() = Stream.of(
        AddCommentArguments("1", generateRandomTextByUUID()),
        AddCommentArguments("-1", generateRandomTextByUUID()),
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
    fun add_comment_without_token_and_with_invalid_arguments_return_401(invalidArgument: AddCommentArguments) {
        runTestSimple {
            client.makeAddCommentRequest(
                contentID = invalidArgument.contentID,
                comment = invalidArgument.comment,
                token = null
            )
                .unauthorizedOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("validArguments")
    fun add_comment_without_token_and_with_valid_arguments_return_401(validArgument: AddCommentArguments) {
        runTestSimple {
            client.makeAddCommentRequest(
                contentID = validArgument.contentID,
                comment = validArgument.comment,
                token = null
            )
                .unauthorizedOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun add_comment_with_invalid_arguments_return_400(invalidArgument: AddCommentArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeAddCommentRequest(
                contentID = invalidArgument.contentID,
                comment = invalidArgument.comment,
                token = registerResponse.accessToken
            )
                .badRequestOrFail()
        }
    }

    @Test
    fun add_comment_with_refresh_token_and_valid_params_return_401() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val randomContentID = testDatabaseAPI.getRandomContentID()
            val randomCommentText = generateRandomTextByUUID()

            client.makeAddCommentRequest(
                contentID = randomContentID.toString(),
                comment = randomCommentText,
                token = registerResponse.refreshToken
            )
                .unauthorizedOrFail()
        }
    }

    @Test
    fun add_comment_with_access_token_and_valid_params_return_ok_and_put_comment_into_database() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val randomContentID = testDatabaseAPI.getRandomContentID()
            val randomCommentText = generateRandomTextByUUID()

            client.makeAddCommentRequest(
                contentID = randomContentID.toString(),
                comment = randomCommentText,
                token = registerResponse.accessToken
            )
                .okOrFail()

            val addedComment = testDatabaseAPI.getAllCommentForContent(randomContentID).first()

            assertEquals(randomContentID, addedComment.contentID)
            assertEquals(randomCommentText, addedComment.text)
            assertEquals(registerResponse.user.id, addedComment.userID)
            assertEquals(registerResponse.user.name, addedComment.userName)
        }
    }

    private suspend fun HttpClient.makeAddCommentRequest(contentID: String?, comment: String?, token: String?) =
        post(Routes.CommentAdd.path) {

            token?.let(::bearerAuth)

            setBody(FormDataContent(Parameters.build {
                contentID?.let { append(Routes.CommentAdd.getContentIDArgName(), contentID) }
                comment?.let { append(Routes.CommentAdd.getCommentTextArgName(), comment) }
                build()
            }))

            url {
                contentType(ContentType.Application.FormUrlEncoded)
            }
        }

    internal class AddCommentArguments(
        val contentID: String?,
        val comment: String?,
    )
}