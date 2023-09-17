package rockyouapi.tests.routes.comment

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.appendToParameters
import rockyouapi.getRandomUserWithPassword
import rockyouapi.route.Routes
import rockyouapi.tests.routes.makeAuthLoginRequestWithDecodedResponse
import rockyouapi.tests.routes.makeCommentListRequest
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import java.util.*
import kotlin.test.Test
import kotlin.test.fail

/** @see rockyouapi.route.comment.commentListRoute */
internal class CommentAddRouteTest {

    @Test
    fun add_comment_without_token_must_return_401_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeCommentAddRequestWithoutToken(
                contentID = null,
                comment = null,
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_comment_without_token_must_return_401_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeCommentAddRequestWithoutToken(
                contentID = "1",
                comment = null,
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_comment_without_token_must_return_401_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeCommentAddRequestWithoutToken(
                contentID = null,
                comment = "text",
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_comment_without_token_must_return_401_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeCommentAddRequestWithoutToken(
                contentID = "1",
                comment = "text",
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_comment_without_content_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val authToken = authResponse.token
            val addCommentResponse = client.makeCommentAddRequestWithToken(
                contentID = null,
                comment = UUID.randomUUID().toString(),
                token = authToken
            )

            assert(addCommentResponse.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${addCommentResponse.status}"
            }
        }
    }

    @Test
    fun add_comment_without_content_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val authToken = authResponse.token
            val addCommentResponse = client.makeCommentAddRequestWithToken(
                contentID = null,
                comment = "",
                token = authToken
            )

            assert(addCommentResponse.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${addCommentResponse.status}"
            }
        }
    }

    @Test
    fun add_comment_without_content_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val authToken = authResponse.token
            val addCommentResponse = client.makeCommentAddRequestWithToken(
                contentID = null,
                comment = "123",
                token = authToken
            )

            assert(addCommentResponse.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${addCommentResponse.status}"
            }
        }
    }

    @Test
    fun add_comment_without_comment_text_must_return_400_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val authToken = authResponse.token
            val addCommentResponse = client.makeCommentAddRequestWithToken(
                contentID = "1",
                comment = null,
                token = authToken
            )

            assert(addCommentResponse.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${addCommentResponse.status}"
            }
        }
    }

    @Test
    fun add_comment_without_comment_text_must_return_400_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val authToken = authResponse.token
            val addCommentResponse = client.makeCommentAddRequestWithToken(
                contentID = "9999999999",
                comment = null,
                token = authToken
            )

            assert(addCommentResponse.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${addCommentResponse.status}"
            }
        }
    }

    @Test
    fun add_comment_with_correct_params_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val authToken = authResponse.token
            val contentIDForComment = testEnv.modelsStorage.contentRegisters.random().id
            val commentText = UUID.randomUUID().toString()

            val addCommentResponse = client.makeCommentAddRequestWithToken(
                contentID = contentIDForComment.toString(),
                comment = commentText,
                token = authToken
            )

            assert(addCommentResponse.status == HttpStatusCode.OK) {
                "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${addCommentResponse.status}"
            }

            val comments = client.makeCommentListRequest(
                contentID = contentIDForComment.toString(),
                limit = "1000",
                offset = null,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val addedComment = comments.firstOrNull() ?: fail("Get comment return nothing, some comments expected")

            val isLastAddedCommentAsExpectedByID = addedComment.contentID == contentIDForComment
            val isLastAddedCommentAsExpectedByText = addedComment.text == commentText
            val isLastAddedCommentAsExpectedByUserID = addedComment.userID == userWithPassword.id
            val isLastAddedCommentAsExpectedByUserName = addedComment.userName == userWithPassword.name

            assert(isLastAddedCommentAsExpectedByID)
            assert(isLastAddedCommentAsExpectedByText)
            assert(isLastAddedCommentAsExpectedByUserID)
            assert(isLastAddedCommentAsExpectedByUserName)
        }
    }

    private suspend fun HttpClient.makeCommentAddRequestWithToken(contentID: String?, comment: String?, token: String) =
        post(Routes.CommentAdd.path) {
            bearerAuth(token)
            url {
                appendToParameters(contentID, Routes.CommentAdd.getContentIDArgName())
                appendToParameters(comment, Routes.CommentAdd.getCommentTextArgName())
            }
        }

    private suspend fun HttpClient.makeCommentAddRequestWithoutToken(contentID: String?, comment: String?) =
        post(Routes.CommentAdd.path) {
            url {
                appendToParameters(contentID, Routes.CommentAdd.getContentIDArgName())
                appendToParameters(comment, Routes.CommentAdd.getCommentTextArgName())
            }
        }
}