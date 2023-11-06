package database.internal.executor

import database.external.result.ReportResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class CreateReportExecutor(private val database: Database) {

    /**
     * Create report for content.
     * Check user existence for [userID] and content existence for [contentID].
     * Don't validate [reportText].
     * [userID] can be passed with null as report from anonymous user.
     *
     * Respond as:
     * - [ReportResult.Ok] Request completed without errors.
     * - [ReportResult.UserNotExist] User with [userID] not exist.
     * - [ReportResult.ContentNotExist] Content with [contentID] not exist.
     * - [ReportResult.Error] Smith happens on query stage.
     * */
    suspend fun execute(contentID: Int, reportText: String, userID: Int?): ReportResult {
        return withContext(Dispatchers.IO) {
            try {
                if (!isContentExist(contentID)) {
                    return@withContext ReportResult.ContentNotExist
                }

                if (userID != null && !isUserExist(userID)) {
                    return@withContext ReportResult.UserNotExist
                }

                database.insertReportQueries.insert(
                    id = null,
                    contentID = contentID,
                    userID = userID,
                    reportText = reportText
                )
                return@withContext ReportResult.Ok
            } catch (t: Throwable) {
                return@withContext ReportResult.Error(t)
            }
        }
    }

    private fun isContentExist(contentID: Int) = database
        .selectRegisterQueries
        .isContentExistByID(contentID)
        .executeAsOne()

    private fun isUserExist(userID: Int) = database
        .selectUserQueries
        .isUserExistByID(userID)
        .executeAsOne()
}