package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.reader.readDatabaseConfigurationFromEnv
import database.external.result.ReportResult
import database.internal.creator.createDB
import database.internal.executor.CreateReportExecutor
import database.utils.KEY_STATIC_MAP_DB
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [CreateReportExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CreateReportExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        CreateReportExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.CreateReportTestDataStreamCreator#notExistedContentIDScenarioTestData")
    fun create_report_with_not_existed_content_return_content_not_exist(testData: CreateReportTestData) {
        runTest {
            executor.execute(
                contentID = testData.contentID,
                userID = testData.userID,
                reportText = testData.reportText,
            )
                .asContentNotExistOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.CreateReportTestDataStreamCreator#notExistedUserIDScenarioTestData")
    fun create_report_with_not_existed_user_return_user_not_exist(testData: CreateReportTestData) {
        runTest {
            executor.execute(
                contentID = testData.contentID,
                userID = testData.userID,
                reportText = testData.reportText,
            )
                .asUserNotExistOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.CreateReportTestDataStreamCreator#basicScenarioTestData")
    fun create_report_with_null_or_not_null_user_return_ok_and_insert_record(testData: CreateReportTestData) {
        runTest {
            executor.execute(
                contentID = testData.contentID,
                userID = testData.userID,
                reportText = testData.reportText,
            )
                .asOkOrFailed()

            val actualReportRecord = database.selectReportQueries
                .selectAllByContentID(testData.contentID)
                .executeAsList()
                .first()

            assertEquals(testData.contentID, actualReportRecord.contentID)
            assertEquals(testData.userID, actualReportRecord.userID)
            assertEquals(testData.reportText, actualReportRecord.reportText)
            assertEquals(false, actualReportRecord.isClosed)
        }
    }

    private fun ReportResult.asUserNotExistOrFailed() {
        Assertions.assertInstanceOf(ReportResult.UserNotExist::class.java, this)
    }

    private fun ReportResult.asContentNotExistOrFailed() {
        Assertions.assertInstanceOf(ReportResult.ContentNotExist::class.java, this)
    }

    private fun ReportResult.asOkOrFailed() {
        Assertions.assertInstanceOf(ReportResult.Ok::class.java, this)
    }
}