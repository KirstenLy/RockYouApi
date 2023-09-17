package rockyouapi.tests.routes.vote

import common.takeRandomValues
import common.times
import database.external.test.TestContentRegister
import database.external.operation.VoteOperation
import database.external.result.SimpleListResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import rockyouapi.*
import rockyouapi.appendToParameters
import rockyouapi.getRandomUserWithPassword
import rockyouapi.route.Routes
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import rockyouapi.tests.routes.makeAuthLoginRequestWithDecodedResponse
import java.util.UUID
import kotlin.test.Test
import kotlin.test.fail

/** @see rockyouapi.route.vote.voteRoute */ // TODO: Тут недоделано, нужно по факту проверять добавлена ли запись в БД
internal class VoteTest {

    @Test
    fun vote_without_token_return_401_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVoteRequest(
                operationType = null,
                contentID = null,
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_without_token_return_401_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVoteRequest(
                operationType = "1",
                contentID = null,
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_without_token_return_401_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVoteRequest(
                operationType = "4143243",
                contentID = null,
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_without_token_return_401_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVoteRequest(
                operationType = null,
                contentID = "1",
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_without_token_return_401_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVoteRequest(
                operationType = null,
                contentID = "2312321214421421431",
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_without_token_return_401_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVoteRequest(
                operationType = "1",
                contentID = "1",
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_without_token_return_401_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVoteRequest(
                operationType = UUID.randomUUID().toString(),
                contentID = UUID.randomUUID().toString(),
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_with_token_and_invalid_args_return_400_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val response = client.makeVoteRequest(
                operationType = UUID.randomUUID().toString(),
                contentID = UUID.randomUUID().toString(),
                token = authResponse.token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_with_token_and_invalid_args_return_400_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val response = client.makeVoteRequest(
                operationType = "123",
                contentID = "1",
                token = authResponse.token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_with_token_and_invalid_args_return_400_test_3() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val response = client.makeVoteRequest(
                operationType = "1",
                contentID = "Broken",
                token = authResponse.token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_with_token_and_non_existed_content_id_return_404_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val response = client.makeVoteRequest(
                operationType = "1",
                contentID = "99999999",
                token = authResponse.token
            )

            assert(response.status == HttpStatusCode.NotFound) {
                "Expected response status: ${HttpStatusCode.NotFound}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_with_token_and_non_existed_content_id_return_404_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val response = client.makeVoteRequest(
                operationType = "0",
                contentID = "99999999",
                token = authResponse.token
            )

            assert(response.status == HttpStatusCode.NotFound) {
                "Expected response status: ${HttpStatusCode.NotFound}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun vote_with_token_and_invalid_operation_type_return_400() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val invalidOperationTypes = listOf("-1", "-99999", "0.5", "2", "222222")
            invalidOperationTypes.forEach {
                val response = client.makeVoteRequest(
                    operationType = it,
                    contentID = "1",
                    token = authResponse.token
                )

                assert(response.status == HttpStatusCode.BadRequest) {
                    "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
                }
            }
        }
    }

    @Test
    fun vote_with_token_and_valid_arguments_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val contentIDs = testEnv.modelsStorage
                .contentRegisters
                .map(TestContentRegister::id)
                .times(5)
                .takeRandomValues()

            contentIDs.forEach { contentID ->
                val allVotesBeforeRequest = testEnv.readAllVotes()

                val isUpvoteRecordForThisContentExistedBeforeRequest = allVotesBeforeRequest.any {
                    it.userID == userWithPassword.id && it.contentID == contentID && it.vote == 1
                }
                val isDownVoteRecordForThisContentExistedBeforeRequest = allVotesBeforeRequest.any {
                    it.userID == userWithPassword.id && it.contentID == contentID && it.vote == -1
                }
                val isNoVoteRecordForThisContentExistedBeforeRequest = allVotesBeforeRequest.none {
                    it.userID == userWithPassword.id && it.contentID == contentID
                }

                val isUnexpectedSituationInVoteHistory =
                    isUpvoteRecordForThisContentExistedBeforeRequest && isDownVoteRecordForThisContentExistedBeforeRequest

                assert(!isUnexpectedSituationInVoteHistory) { "Unexpected situation, user upvote and downvote for one content" }

                val operationType = VoteOperation.entries.random()

                if (operationType == VoteOperation.UPVOTE) {
                    val expectedResponse = when {
                        isUpvoteRecordForThisContentExistedBeforeRequest -> HttpStatusCode.Conflict
                        isDownVoteRecordForThisContentExistedBeforeRequest -> HttpStatusCode.OK
                        isNoVoteRecordForThisContentExistedBeforeRequest -> HttpStatusCode.OK
                        else -> fail("Unexpected situation, can't understand is any vote in history ore not")
                    }
                    val response = client.makeVoteRequest(
                        operationType = "1",
                        contentID = contentID.toString(),
                        token = authResponse.token
                    )

                    assert(response.status == expectedResponse) {
                        "Expected response status: $expectedResponse, Actual response status: ${response.status}"
                    }

                    val allVotesAfterRequest = testEnv.readAllVotes()

                    when {
                        isUpvoteRecordForThisContentExistedBeforeRequest -> {
                            val isUpvoteRecordStillExist = allVotesAfterRequest.any {
                                it.userID == userWithPassword.id && it.contentID == contentID && it.vote == 1
                            }
                            assert(isUpvoteRecordStillExist) { "Upvote content record expected in database, but it not exist" }
                        }

                        isDownVoteRecordForThisContentExistedBeforeRequest -> {
                            val isNoVoteRecordInHistory = allVotesAfterRequest.none {
                                it.userID == userWithPassword.id && it.contentID == contentID
                            }
                            assert(isNoVoteRecordInHistory) { "User upvote after downvote, but vote record still in database when it must be deleted" }
                        }

                        isNoVoteRecordForThisContentExistedBeforeRequest -> {
                            val isUpvoteRecordExist = allVotesAfterRequest.any {
                                it.userID == userWithPassword.id && it.contentID == contentID && it.vote == 1
                            }
                            assert(isUpvoteRecordExist) { "Upvote content record expected in database, but it not exist" }
                        }
                    }
                }

                if (operationType == VoteOperation.DOWNVOTE) {
                    val expectedResponse = when {
                        isUpvoteRecordForThisContentExistedBeforeRequest -> HttpStatusCode.OK
                        isDownVoteRecordForThisContentExistedBeforeRequest -> HttpStatusCode.Conflict
                        isNoVoteRecordForThisContentExistedBeforeRequest -> HttpStatusCode.OK
                        else -> fail("Unexpected situation, can't understand is any vote in history ore not")
                    }
                    val response = client.makeVoteRequest(
                        operationType = "0",
                        contentID = contentID.toString(),
                        token = authResponse.token
                    )

                    assert(response.status == expectedResponse) {
                        "Expected response status: $expectedResponse, Actual response status: ${response.status}"
                    }

                    val allVotesAfterRequest = testEnv.readAllVotes()

                    when {
                        isUpvoteRecordForThisContentExistedBeforeRequest -> {
                            val isNoVoteRecordInHistory = allVotesAfterRequest.none {
                                it.userID == userWithPassword.id && it.contentID == contentID
                            }
                            assert(isNoVoteRecordInHistory) { "User downvote after upvote, but vote record still in database when it must be deleted" }
                        }

                        isDownVoteRecordForThisContentExistedBeforeRequest -> {
                            val isDownVoteRecordStillExist = allVotesAfterRequest.any {
                                it.userID == userWithPassword.id && it.contentID == contentID && it.vote == -1
                            }
                            assert(isDownVoteRecordStillExist) { "Downvote content record expected in database, but it not exist" }
                        }

                        isNoVoteRecordForThisContentExistedBeforeRequest -> {
                            val isDownVoteRecordExist = allVotesAfterRequest.any {
                                it.userID == userWithPassword.id && it.contentID == contentID && it.vote == -1
                            }
                            assert(isDownVoteRecordExist) { "Downvote content record expected in database, but it not exist" }
                        }
                    }
                }
            }
        }
    }

    private suspend fun TestEnv.readAllVotes() = testDBApi
        .readAllVotes()
        .let {
            when (it) {
                is SimpleListResult.Data -> it.data
                is SimpleListResult.Error -> fail("Reason: ${it.t.message.orEmpty()}")
            }
        }

    private suspend fun HttpClient.makeVoteRequest(
        operationType: String?,
        contentID: String?,
        token: String? = null
    ): HttpResponse {
        return post(Routes.Vote.path) {
            token?.let(::bearerAuth)
            url {
                appendToParameters(operationType, Routes.Vote.getOperationTypeArgName())
                appendToParameters(contentID, Routes.Vote.getContentIDArgName())
            }
        }
    }
}