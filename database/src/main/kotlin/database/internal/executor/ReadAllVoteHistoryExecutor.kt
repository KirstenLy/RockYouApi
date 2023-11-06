package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import migrations.VoteHistory
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadAllVoteHistoryExecutor(private val database: Database) {

    /** Get all votes of all users. */
    suspend fun execute(): List<VoteHistory> = withContext(Dispatchers.IO) {
        database.selectVoteHistoryQueries
            .selectAll()
            .executeAsList()
    }
}
