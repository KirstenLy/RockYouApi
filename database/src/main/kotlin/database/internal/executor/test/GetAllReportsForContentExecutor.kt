package database.internal.executor.test

import database.external.test.TestReport
import database.external.result.SimpleListResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import migrations.Report
import org.jetbrains.annotations.TestOnly
import rockyouapi.DBTest

/**
 * Read all reports in database.
 * Used for test purpose to analyze if report was actually inserted.
 * */
@TestOnly
internal class GetAllReportsForContentExecutor(private val database: DBTest) {

    suspend fun execute(contentID: Int): SimpleListResult<TestReport> = withContext(Dispatchers.IO) {
        return@withContext try {
            val reports = database.reportQueries
                .selectAllByContentID(contentID)
                .executeAsList()
                .map { it.toTestReport() }

            SimpleListResult.Data(reports)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }

    private fun Report.toTestReport() = TestReport(
        id = id,
        contentID = contentID,
        userID = userID,
        reportText = reportText,
        isClosed = isClosed
    )
}