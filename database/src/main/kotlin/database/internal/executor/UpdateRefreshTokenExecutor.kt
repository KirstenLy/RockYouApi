package database.internal.executor

import database.external.result.UpdateRefreshTokenResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class UpdateRefreshTokenExecutor(private val database: Database) {

    /**
     * Update [refreshToken] for [userID].
     *
     * Respond as:
     * - [UpdateRefreshTokenResult.Ok] Request completed without errors.
     * - [UpdateRefreshTokenResult.UserNotExists] User is not exist.
     * - [UpdateRefreshTokenResult.Error] Smith happens on query stage.
     * */
    suspend fun execute(userID: Int, refreshToken: String?): UpdateRefreshTokenResult {
        return withContext(Dispatchers.IO) {
            try {
                val isUserExist = database.selectUserQueries
                    .isUserExistByID(userID)
                    .executeAsOne()

                if (!isUserExist) {
                    return@withContext UpdateRefreshTokenResult.UserNotExists
                }

                database.updateAuthQueries.updateRefreshTokenForUser(
                    userID = userID,
                    refreshToken = refreshToken
                )
                return@withContext UpdateRefreshTokenResult.Ok
            } catch (t: Throwable) {
                return@withContext UpdateRefreshTokenResult.Error(t)
            }
        }
    }
}