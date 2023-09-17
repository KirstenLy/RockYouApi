package rockyouapi.tests.routes.comment

import common.takeRandomValues
import common.thisIfLessAndValueIfMore
import common.toZeroIfNegative
import database.external.test.TestComment
import database.external.test.TestContentRegister
import declaration.entity.Comment
import io.ktor.http.*
import rockyouapi.countCommentsByContentID
import rockyouapi.getCommentsByContentID
import rockyouapi.tests.routes.makeCommentListRequest
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import kotlin.test.Test
import kotlin.test.fail

/** @see rockyouapi.route.comment.commentListRoute */
internal class CommentListRouteTest {

    @Test
    fun comments_list_without_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = null,
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "-1",
                limit = null,
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "0.5",
                limit = null,
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = null,
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "99999999999999999",
                limit = null,
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = "1",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = "Broken",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = null,
                offset = "1",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = null,
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = "1",
                offset = "1",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_content_id_must_return_400_test_10() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = "Broken",
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "Broken",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "-1",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "0",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "0.5",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "Broken",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = "Broken",
                offset = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "Broken",
                offset = "1",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_limit_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "Broken",
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_offset_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = null,
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_offset_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = null,
                offset = "-1",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_offset_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = null,
                offset = "0.5",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_offset_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "1",
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_offset_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = null,
                limit = "Broken",
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_offset_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "1",
                limit = null,
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_invalid_offset_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeCommentListRequest(
                contentID = "Broken",
                limit = null,
                offset = "Broken",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                }
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun comments_list_with_correct_data_work_correct_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val contentIDs = testEnv.modelsStorage
                .contentRegisters
                .takeRandomValues(minValue = 10)
                .map(TestContentRegister::id)
            val comments = testEnv.modelsStorage.comments

            contentIDs.forEach { contentID ->
                val expectedCommentsCount = comments.countCommentsByContentID(contentID)

                println("Request with ID: $contentID")
                println("Expected comments size for $contentID: $expectedCommentsCount")

                val response = client.makeCommentListRequest(
                    contentID = contentID.toString(),
                    limit = "1000",
                    offset = null,
                    onFinishedByError = {
                        fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                    }
                )

                val actualCommentsCount = response.size
                assert(expectedCommentsCount == actualCommentsCount) {
                    "Expected comments size: $expectedCommentsCount, Actual comments size: $actualCommentsCount"
                }

                val isAllActualCommentsBelongToContent = response.all { it.contentID == contentID }
                assert(isAllActualCommentsBelongToContent) { "Some comments somehow belong to another contentID!" }

                val commentsForThisContent = comments.getCommentsByContentID(contentID).reversed()
                val isCommentsDataAsExpected = commentsForThisContent
                    .zip(response)
                    .all { (testComment, actualComment) -> testComment.compareToDomainComment(actualComment) }
                assert(isCommentsDataAsExpected) { "Some comments data mismatch exist!" }
            }
        }
    }

    @Test
    fun comments_list_with_correct_data_work_correct_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val contentIDs = testEnv.modelsStorage
                .contentRegisters
                .takeRandomValues(minValue = 10)
                .map(TestContentRegister::id)
            val limits = listOf("1", "2", "5", "10", "20", "50", "1000")

            val comments = testEnv.modelsStorage.comments

            contentIDs.forEach { contentID ->
                val limit = limits.random()
                val expectedCommentsCount = comments
                    .countCommentsByContentID(contentID)
                    .thisIfLessAndValueIfMore(limit.toInt())

                println("Request with ID: $contentID, Limit presented: $limit")
                println("Expected comments size for $contentID: $expectedCommentsCount")

                val response = client.makeCommentListRequest(
                    contentID = contentID.toString(),
                    limit = limit,
                    offset = null,
                    onFinishedByError = {
                        fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                    }
                )

                val actualCommentsCount = response.size
                assert(expectedCommentsCount == actualCommentsCount) {
                    "Expected comments size: $expectedCommentsCount, Actual comments size: $actualCommentsCount"
                }

                val isAllActualCommentsBelongToContent = response.all { it.contentID == contentID }
                assert(isAllActualCommentsBelongToContent) { "Some comments somehow belong to another contentID!" }

                val commentsForThisContent = comments.getCommentsByContentID(contentID).reversed().take(limit.toInt())
                val isCommentsDataAsExpected = commentsForThisContent
                    .zip(response)
                    .all { (testComment, actualComment) -> testComment.compareToDomainComment(actualComment) }
                assert(isCommentsDataAsExpected) { "Some comments data mismatch exist!" }
            }
        }
    }

    @Test
    fun comments_list_with_correct_data_work_correct_test_3() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val contentIDs = testEnv.modelsStorage
                .contentRegisters
                .takeRandomValues(minValue = 10)
                .map(TestContentRegister::id)
            val limits = listOf("1", "2", "5", "10", "20", "50", "1000")
            val offsets = listOf("1", "2", "5", "10")

            val comments = testEnv.modelsStorage.comments

            contentIDs.forEach { contentID ->
                val limit = limits.random()
                val offset = offsets.random()
                val expectedCommentsCount = comments
                    .countCommentsByContentID(contentID)
                    .minus(offset.toInt())
                    .toZeroIfNegative()
                    .thisIfLessAndValueIfMore(limit.toInt())

                println("Request with ID: $contentID, Limit presented: $limit, Offset presented: $offset")
                println("Expected comments size for $contentID: $expectedCommentsCount")

                val response = client.makeCommentListRequest(
                    contentID = contentID.toString(),
                    limit = limit,
                    offset = offset,
                    onFinishedByError = {
                        fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                    }
                )

                val actualCommentsCount = response.size
                assert(expectedCommentsCount == actualCommentsCount) {
                    "Expected comments size: $expectedCommentsCount, Actual comments size: $actualCommentsCount"
                }

                val isAllActualCommentsBelongToContent = response.all { it.contentID == contentID }
                assert(isAllActualCommentsBelongToContent) { "Some comments somehow belong to another contentID!" }

                val commentsForThisContent = comments.getCommentsByContentID(contentID)
                    .reversed()
                    .drop(offset.toInt())
                    .take(limit.toInt())
                val isCommentsDataAsExpected = commentsForThisContent
                    .zip(response)
                    .all { (testComment, actualComment) -> testComment.compareToDomainComment(actualComment) }
                assert(isCommentsDataAsExpected) { "Some comments data mismatch exist!" }
            }
        }
    }

    private fun TestComment.compareToDomainComment(comment: Comment) = id == comment.id
            && contentID == comment.contentID
            && userID == comment.userID
            && userName == comment.userName
            && text == comment.text
}