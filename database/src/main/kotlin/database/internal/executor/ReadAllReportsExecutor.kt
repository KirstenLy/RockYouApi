package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import migrations.Report
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadAllReportsExecutor(private val database: Database) {

    /** Get all reports af all users. */
    suspend fun execute(): List<database.external.model.test.ReportRecord> = withContext(Dispatchers.IO) {
        database.selectReportQueries
            .selectAll()
            .executeAsList()
            .map(Report::toReportRecord)
    }
}

private fun Report.toReportRecord() = database.external.model.test.ReportRecord(
    id = id,
    contentID = contentID,
    userID = userID,
    reportText = reportText,
    isClosed = isClosed,
    creationDate = creationDate
)