package database.internal.executor

import database.external.result.common.SimpleBooleanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class CheckRefreshTokenExecutor(private val database: Database) {

    /**
     * Check is [refreshToken] exist for user with [userID].
     * Don't check if user exists.
     * Don't check token signature.
     *
     * Respond as:
     * - [SimpleBooleanResult.True] Refresh token exist and matched.
     * - [SimpleBooleanResult.False] Refresh token not exist or not matched with this [userID].
     * - [SimpleBooleanResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(userID: Int, refreshToken: String): SimpleBooleanResult {
        return withContext(Dispatchers.IO) {
            try {
                val userRefreshToken = database.selectAuthQueries
                    .selectRefreshTokenByUserID(userID)
                    .executeAsOneOrNull()
                    ?.refreshToken
                    ?: return@withContext SimpleBooleanResult.False

                if (refreshToken == userRefreshToken) SimpleBooleanResult.True else SimpleBooleanResult.False
            } catch (t: Throwable) {
                SimpleBooleanResult.Error(t)
            }
        }
    }
}