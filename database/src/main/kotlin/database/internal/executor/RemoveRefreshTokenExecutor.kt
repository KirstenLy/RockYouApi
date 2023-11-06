package database.internal.executor

import database.external.result.common.SimpleUnitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class RemoveRefreshTokenExecutor(private val database: Database) {

    /**
     * Remove [refreshToken] from database.
     *
     * Respond as:
     * - [SimpleUnitResult.Ok] Request completed without errors.
     * - [SimpleUnitResult.Error] Smith happens on query stage.
     * */
    suspend fun execute(refreshToken: String): SimpleUnitResult {
        return withContext(Dispatchers.IO) {
            try {
                database.deleteAuthQueries.deleteRefreshToken(refreshToken)
                SimpleUnitResult.Ok
            } catch (t: Throwable) {
                SimpleUnitResult.Error(t)
            }
        }
    }
}