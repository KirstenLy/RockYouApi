package database.internal.executor.test

import database.external.test.TestVote
import database.external.result.SimpleListResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import migrations.VoteHistory
import org.jetbrains.annotations.TestOnly
import rockyouapi.DBTest

/**
 * Read all votes from database.
 * Used for test purpose to analyze if vote was actually inserted or deleted.
 * */
@TestOnly
internal class GetAllVotesExecutor(private val database: DBTest) {

    suspend fun execute(): SimpleListResult<TestVote> = withContext(Dispatchers.IO) {
        return@withContext try {
            val reports = database.voteHistoryQueries
                .selectAll()
                .executeAsList()
                .map { it.toTestReport() }

            SimpleListResult.Data(reports)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }

    private fun VoteHistory.toTestReport() = TestVote(
        id = id,
        contentID = contentID,
        userID = userID,
        vote = vote.toInt()
    )
}