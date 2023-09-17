package database.internal.executor

import database.external.result.SimpleUnitResult
import database.internal.entity.report.ReportTable
import org.jetbrains.exposed.sql.insert
import rockyouapi.DBTest
import kotlin.random.Random

internal class TestGetPictureByIDRequestExecutor(private val db: DBTest) {

    fun execute(id: Int, contentID: Int, reason: String, userID: Int?): SimpleUnitResult {
        return try {
            db.reportQueries.insert(
                id = Random.nextInt(),
                contentID = contentID,
                userID = userID,
                reportText = reason
            )
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