package database.internal.executor

import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class ReadUserFavoriteExecutor(private val database: Database) {

    /** Get all votes of all users. */
    suspend fun execute(userID: Int): SimpleListResult<Int> = withContext(Dispatchers.IO) {
        try {
            database.selectFavoriteQueries
                .selectForUser(userID)
                .executeAsList()
                .let(::Data)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }
}