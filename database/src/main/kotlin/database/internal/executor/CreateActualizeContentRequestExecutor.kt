package database.internal.executor

import database.external.result.ActualizeContentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class CreateActualizeContentRequestExecutor(private val database: Database) {

    /**
     * Create request to actualize content info.
     * Check user existence for [userID] and content existence for [contentID].
     * Don't validate [requestText].
     * [userID] can be passed with null as request from anonymous user.
     *
     * Respond as:
     * - [ActualizeContentResult.Ok] Request saved without errors.
     * - [ActualizeContentResult.UserNotExist] User with [userID] not exist.
     * - [ActualizeContentResult.ContentNotExist] Content with [contentID] not exist.
     * - [ActualizeContentResult.Error] Smith happens on query stage.
     * */
    suspend fun execute(contentID: Int, requestText: String, userID: Int?): ActualizeContentResult {
        return withContext(Dispatchers.IO) {
            try {
                if (!isContentExist(contentID)) {
                    return@withContext ActualizeContentResult.ContentNotExist
                }

                if (userID != null && !isUserExist(userID)) {
                    return@withContext ActualizeContentResult.UserNotExist
                }

                database.insertUpdateContentInfoQueries.insert(
                    contentID = contentID,
                    requestText = requestText,
                    userID = userID
                )

                ActualizeContentResult.Ok
            } catch (t: Throwable) {
                ActualizeContentResult.Error(t)
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