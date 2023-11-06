package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetCommentLimitTestData
import database.data.GetCommentWithoutExpectedValueTestData
import database.external.filter.CommentListFilter
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadCommentsExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asErrorOrFailed
import database.utils.extractDataOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadCommentsExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetCommentExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadCommentsExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.GetCommentTestDataStreamCreator#invalidLimitScenarioTestData")
    fun execute_with_invalid_limit_return_error(testData: GetCommentWithoutExpectedValueTestData) {
        runTest {
            val filter = CommentListFilter(
                contentID = testData.contentID,
                offset = testData.offset,
                limit = testData.limit
            )
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetCommentTestDataStreamCreator#invalidOffsetScenarioTestData")
    fun execute_with_invalid_offset_return_error(testData: GetCommentWithoutExpectedValueTestData) {
        runTest {
            val filter = CommentListFilter(
                contentID = testData.contentID,
                offset = testData.offset,
                limit = testData.limit
            )
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetCommentTestDataStreamCreator#notExistedContentIDScenarioTestData")
    fun execute_with_non_existed_content_id_return_empty_data(testData: GetCommentWithoutExpectedValueTestData) {
        runTest {
            val filter = CommentListFilter(
                contentID = testData.contentID,
                offset = testData.offset,
                limit = testData.limit
            )
            val actualCommentList = executor.execute(filter).extractDataOrFail()
            assert(actualCommentList.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetCommentTestDataStreamCreator#limitScenarioTestData")
    fun execute_with_correct_limit_return_ok_with_correct_data(testData: GetCommentLimitTestData) {
        runTest {
            val filter = CommentListFilter(
                contentID = testData.contentID,
                offset = testData.offset,
                limit = testData.limit
            )
            val actualCommentList = executor.execute(filter).extractDataOrFail()
            assertEquals(testData.expectedCommentList, actualCommentList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetCommentTestDataStreamCreator#offsetScenarioTestData")
    fun execute_with_correct_offset_return_ok_with_correct_data(testData: GetCommentLimitTestData) {
        runTest {
            val filter = CommentListFilter(
                contentID = testData.contentID,
                offset = testData.offset,
                limit = testData.limit
            )
            val actualCommentList = executor.execute(filter).extractDataOrFail()
            assertEquals(testData.expectedCommentList, actualCommentList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetCommentTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_correct_arguments_return_correct_data(testData: GetCommentLimitTestData) {
        runTest {
            val filter = CommentListFilter(
                contentID = testData.contentID,
                offset = testData.offset,
                limit = testData.limit
            )
            val actualCommentList = executor.execute(filter).extractDataOrFail()
            assertEquals(testData.expectedCommentList, actualCommentList)
        }
    }
}