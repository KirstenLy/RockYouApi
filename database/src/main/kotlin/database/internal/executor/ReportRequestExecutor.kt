package database.internal.executor

import database.external.result.SimpleUnitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.DBTest
import java.time.LocalDateTime

internal class ReportRequestExecutor(private val database: DBTest) {

    suspend fun execute(contentID: Int, reportText: String, userID: Int?): SimpleUnitResult = withContext(Dispatchers.IO) {
        return@withContext try {
            database.reportQueries.insertForProd(
                contentID = contentID,
                userID = userID,
                reportText = reportText
            )
            SimpleUnitResult.Ok
        } catch (t: Throwable) {
            SimpleUnitResult.Error(t)
        }
    }
}