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
import migrations.VoteHistory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.operation.ChangeRatingOperation
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.vote.voteRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class VoteAddOrRemoveRouteTest {

    private fun invalidArguments() = Stream.of(
        VoteAddOrRemoveArguments(null, null),
        VoteAddOrRemoveArguments(null, "null"),
        VoteAddOrRemoveArguments(null, "-1"),
        VoteAddOrRemoveArguments(null, generateRandomTextByUUID()),
        VoteAddOrRemoveArguments(null, ""),
        VoteAddOrRemoveArguments(null, "    "),
        VoteAddOrRemoveArguments("null", null),
        VoteAddOrRemoveArguments("-1", null),
        VoteAddOrRemoveArguments(generateRandomTextByUUID(), null),
        VoteAddOrRemoveArguments("", null),
        VoteAddOrRemoveArguments("      ", null),
        VoteAddOrRemoveArguments("null", "null"),
        VoteAddOrRemoveArguments("-1", "-1"),
        VoteAddOrRemoveArguments("", ""),
        VoteAddOrRemoveArguments("   ", "   "),
        VoteAddOrRemoveArguments(generateRandomTextByUUID(), generateRandomTextByUUID()),
    )

    private fun validArguments() = Stream.of(
        VoteAddOrRemoveArguments("0", "1"),
        VoteAddOrRemoveArguments("1", "0"),
        VoteAddOrRemoveArguments("1", "500"),
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
    fun add_vote_without_token_and_with_invalid_arguments_return_401(invalidArguments: VoteAddOrRemoveArguments) {
        runTestSimple {
            client.makeVoteRequest(
                operationType = invalidArguments.operationType,
                contentID = invalidArguments.contentID,
                token = null
            )
                .unauthorizedOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("validArguments")
    fun add_vote_without_token_and_with_valid_arguments_return_401(validArguments: VoteAddOrRemoveArguments) {
        runTestSimple {
            client.makeVoteRequest(
                operationType = validArguments.operationType,
                contentID = validArguments.contentID,
                token = null
            )
                .unauthorizedOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun add_vote_with_token_and_with_invalid_arguments_return_400(invalidArguments: VoteAddOrRemoveArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeVoteRequest(
                operationType = invalidArguments.operationType,
                contentID = invalidArguments.contentID,
                token = registerResponse.accessToken
            )
                .badRequestOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun add_vote_with_refresh_token_and_with_invalid_arguments_return_401(invalidArguments: VoteAddOrRemoveArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeVoteRequest(
                operationType = invalidArguments.operationType,
                contentID = invalidArguments.contentID,
                token = registerResponse.refreshToken
            )
                .unauthorizedOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("validArguments")
    fun add_vote_with_refresh_token_and_with_valid_arguments_return_401(validArguments: VoteAddOrRemoveArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeVoteRequest(
                operationType = validArguments.operationType,
                contentID = validArguments.contentID,
                token = registerResponse.refreshToken
            )
                .unauthorizedOrFail()
        }
    }

    @RepeatedTest(10)
    fun add_vote_with_token_and_with_valid_arguments_return_200_and_vote_inserted_and_rating_incremented() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val user = registerResponse.user
            val contentIDList = testDatabaseAPI.getAllContentID()
            val voteHistoryBeforeRequest = testDatabaseAPI.getAllVoteHistory()
            val contentIDListUserVoted = voteHistoryBeforeRequest
                .filter { it.userID == user.id }
                .map(VoteHistory::contentID)
                .distinct()
            val contentIDListToVote = contentIDList
                .minus(contentIDListUserVoted.toSet())
                .takeRandomValues(5)

            contentIDListToVote.forEach { contentID ->
                val contentRatingBeforeRequest = testDatabaseAPI.getContentRating(contentID)

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.UPVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                val contentRatingAfterRequest = testDatabaseAPI.getContentRating(contentID)
                assertEquals(contentRatingAfterRequest, contentRatingBeforeRequest + 1)

                val voteHistoryAfterRequest = testDatabaseAPI.getAllVoteHistory()
                val insertedVoteRecord = voteHistoryAfterRequest.last()

                assertEquals(ChangeRatingOperation.UPVOTE.operationArgument, insertedVoteRecord.vote.toInt())
                assertEquals(contentID, insertedVoteRecord.contentID)
                assertEquals(user.id, insertedVoteRecord.userID)
            }
        }
    }

    @RepeatedTest(10)
    fun add_vote_with_token_and_with_valid_arguments_return_403_and_not_change_rating_if_user_already_voted_same_way() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val user = registerResponse.user
            val contentIDList = testDatabaseAPI.getAllContentID()
            val voteHistoryBeforeRequest = testDatabaseAPI.getAllVoteHistory()
            val contentIDListUserVoted = voteHistoryBeforeRequest
                .filter { it.userID == user.id }
                .map(VoteHistory::contentID)
                .distinct()
            val contentIDListToVote = contentIDList
                .minus(contentIDListUserVoted.toSet())
                .takeRandomValues(5)

            contentIDListToVote.forEach { contentID ->
                val contentRatingBeforeRequest = testDatabaseAPI.getContentRating(contentID)

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.UPVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.UPVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .conflictOrFail()

                val contentRatingAfterRequest = testDatabaseAPI.getContentRating(contentID)
                assertEquals(contentRatingAfterRequest, contentRatingBeforeRequest + 1)
            }
        }
    }

    @RepeatedTest(10)
    fun add_vote_with_token_and_with_valid_arguments_return_200_and_delete_record_and_decrement_rating_if_user_already_voted_opposite_way() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val user = registerResponse.user
            val contentIDList = testDatabaseAPI.getAllContentID()
            val voteHistoryBeforeRequest = testDatabaseAPI.getAllVoteHistory()
            val contentIDListUserVoted = voteHistoryBeforeRequest
                .filter { it.userID == user.id }
                .map(VoteHistory::contentID)
                .distinct()
            val contentIDListToVote = contentIDList
                .minus(contentIDListUserVoted.toSet())
                .takeRandomValues(5)

            contentIDListToVote.forEach { contentID ->
                val contentRatingBeforeRequest = testDatabaseAPI.getContentRating(contentID)

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.UPVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.DOWNVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                val contentRatingAfterRequest = testDatabaseAPI.getContentRating(contentID)
                assertEquals(contentRatingAfterRequest, contentRatingBeforeRequest)

                val voteHistoryAfterRequest = testDatabaseAPI.getAllVoteHistory()
                assertEquals(voteHistoryBeforeRequest, voteHistoryAfterRequest)
            }
        }
    }

    @RepeatedTest(10)
    fun remove_vote_with_token_and_with_valid_arguments_return_200_and_vote_inserted_and_rating_decremented() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val user = registerResponse.user
            val contentIDList = testDatabaseAPI.getAllContentID()
            val voteHistoryBeforeRequest = testDatabaseAPI.getAllVoteHistory()
            val contentIDListUserVoted = voteHistoryBeforeRequest
                .filter { it.userID == user.id }
                .map(VoteHistory::contentID)
                .distinct()
            val contentIDListToVote = contentIDList
                .minus(contentIDListUserVoted.toSet())
                .takeRandomValues(5)

            contentIDListToVote.forEach { contentID ->
                val contentRatingBeforeRequest = testDatabaseAPI.getContentRating(contentID)

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.DOWNVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                val contentRatingAfterRequest = testDatabaseAPI.getContentRating(contentID)
                assertEquals(contentRatingAfterRequest, contentRatingBeforeRequest - 1)

                val voteHistoryAfterRequest = testDatabaseAPI.getAllVoteHistory()
                val insertedVoteRecord = voteHistoryAfterRequest.last()

                assertEquals(-1, insertedVoteRecord.vote.toInt())
                assertEquals(contentID, insertedVoteRecord.contentID)
                assertEquals(user.id, insertedVoteRecord.userID)
            }
        }
    }

    @RepeatedTest(10)
    fun remove_vote_with_token_and_with_valid_arguments_return_403_and_not_change_rating_and_not_inserted_record_if_user_already_voted_same_way() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val user = registerResponse.user
            val contentIDList = testDatabaseAPI.getAllContentID()
            val voteHistoryBeforeRequest = testDatabaseAPI.getAllVoteHistory()
            val contentIDListUserVoted = voteHistoryBeforeRequest
                .filter { it.userID == user.id }
                .map(VoteHistory::contentID)
                .distinct()
            val contentIDListToVote = contentIDList
                .minus(contentIDListUserVoted.toSet())
                .takeRandomValues(5)

            contentIDListToVote.forEach { contentID ->
                val contentRatingBeforeRequest = testDatabaseAPI.getContentRating(contentID)

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.DOWNVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.DOWNVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .conflictOrFail()

                val contentRatingAfterRequest = testDatabaseAPI.getContentRating(contentID)
                assertEquals(contentRatingAfterRequest, contentRatingBeforeRequest - 1)
            }
        }
    }

    @RepeatedTest(10)
    fun remove_vote_with_token_and_with_valid_arguments_return_200_and_delete_record_and_increment_rating_if_user_already_voted_opposite_way() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {

            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val user = registerResponse.user
            val contentIDList = testDatabaseAPI.getAllContentID()
            val voteHistoryBeforeRequest = testDatabaseAPI.getAllVoteHistory()
            val contentIDListUserVoted = voteHistoryBeforeRequest
                .filter { it.userID == user.id }
                .map(VoteHistory::contentID)
                .distinct()
            val contentIDListToVote = contentIDList
                .minus(contentIDListUserVoted.toSet())
                .takeRandomValues(5)

            contentIDListToVote.forEach { contentID ->
                val contentRatingBeforeRequest = testDatabaseAPI.getContentRating(contentID)

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.DOWNVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                client.makeVoteRequest(
                    operationType = ChangeRatingOperation.UPVOTE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = registerResponse.accessToken
                )
                    .okOrFail()

                val contentRatingAfterRequest = testDatabaseAPI.getContentRating(contentID)
                assertEquals(contentRatingAfterRequest, contentRatingBeforeRequest)

                val voteHistoryAfterRequest = testDatabaseAPI.getAllVoteHistory()
                assertEquals(voteHistoryBeforeRequest, voteHistoryAfterRequest)
            }
        }
    }

    private suspend fun HttpClient.makeVoteRequest(
        operationType: String?,
        contentID: String?,
        token: String? = null
    ) = post(Routes.Vote.path) {

        token?.let(::bearerAuth)

        setBody(FormDataContent(Parameters.build {
            operationType?.let { append(Routes.Vote.getOperationTypeArgName(), operationType) }
            contentID?.let { append(Routes.Vote.getContentIDArgName(), contentID) }
            build()
        }))

        url {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    internal class VoteAddOrRemoveArguments(
        val operationType: String?,
        val contentID: String?,
    )
}