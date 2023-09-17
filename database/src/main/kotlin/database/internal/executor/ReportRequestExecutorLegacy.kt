package database.internal.executor

import database.external.result.SimpleUnitResult
import database.internal.entity.report.ReportTable
import org.jetbrains.exposed.sql.insert

@Deprecated("")
internal class ReportRequestExecutorLegacy() {

    fun execute(contentID: Int, reason: String, userID: Int?): SimpleUnitResult {
        return try {
            insertFavorite(contentID, reason, userID)
            SimpleUnitResult.Ok
        } catch (t: Throwable) {
            SimpleUnitResult.Error(t)
        }
    }

    private fun insertFavorite(contentID: Int, reason: String, userID: Int?) {
        ReportTable.insert {
            it[report] = reason
            it[ReportTable.userID] = userID
            it[ReportTable.contentID] = contentID
        }
    }
}