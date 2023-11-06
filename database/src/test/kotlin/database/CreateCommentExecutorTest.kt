package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.AddCommentTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.external.result.AddCommentResult
import database.internal.creator.createDB
import database.internal.executor.CreateCommentExecutor
import database.utils.KEY_STATIC_MAP_DB
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [CreateCommentExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CreateCommentExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor: CreateCommentExecutor by lazy {
        CreateCommentExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.AddCommentTestDataStreamCreator#userNotExistScenarioTestData")
    fun add_comment_with_not_existed_user_id_return_user_not_exist_error(testData: AddCommentTestData) {
        runTest {
            executor.execute(
                userID = testData.userID,
                contentID = testData.contentID,
                commentText = testData.commentText
            ).asUserNotExistsOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.AddCommentTestDataStreamCreator#contentNotExistScenarioTestData")
    fun add_comment_with_not_existed_content_id_return_content_not_exist_error(testData: AddCommentTestData) {
        runTest {
            executor.execute(
                userID = testData.userID,
                contentID = testData.contentID,
                commentText = testData.commentText
            ).asContentNotExistsOrFailed()
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestAddComment {

        @AfterAll
        @BeforeAll
        fun clearCommentTable() {
            database.deleteCommentQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddCommentTestDataStreamCreator#basicScenarioTestData")
        fun add_comment_with_valid_data_add_comment_to_database(testData: AddCommentTestData) {
            val recordsCountBeforeExecute = database.selectCommentQueries.countForContentAndUser(
                contentID = testData.contentID,
                userID = testData.userID
            )
                .executeAsOne()

            runTest {
                executor.execute(
                    userID = testData.userID,
                    contentID = testData.contentID,
                    commentText = testData.commentText
                ).asOkOrFailed()

                val recordsAfterExecute = database.selectCommentQueries.selectAllByContentAndUser(
                    contentID = testData.contentID,
                    userID = testData.userID
                )
                    .executeAsList()

                // Check is new record presented in the database
                assert(recordsAfterExecute.size.toLong() == recordsCountBeforeExecute + 1)

                val lastInsertedComment = recordsAfterExecute.first()

                assertEquals(testData.contentID, lastInsertedComment.contentID)
                assertEquals(testData.commentText, lastInsertedComment.text)
            }
        }
    }

    private fun AddCommentResult.asOkOrFailed() {
        Assertions.assertInstanceOf(AddCommentResult.Ok::class.java, this)
    }

    private fun AddCommentResult.asUserNotExistsOrFailed() {
        Assertions.assertInstanceOf(AddCommentResult.UserNotExists::class.java, this)
    }

    private fun AddCommentResult.asContentNotExistsOrFailed() {
        Assertions.assertInstanceOf(AddCommentResult.ContentNotExists::class.java, this)
    }
}