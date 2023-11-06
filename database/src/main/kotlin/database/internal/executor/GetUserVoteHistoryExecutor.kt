package database.internal.executor

import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import database.external.model.Vote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.votehistory.SelectForUser

/** @see execute */
internal class GetUserVoteHistoryExecutor(private val database: Database) {

    /** Get all votes of all users. */
    suspend fun execute(userID: Int): SimpleListResult<Vote> = withContext(Dispatchers.IO) {
        try {
            database.selectVoteHistoryQueries
                .selectForUser(userID)
                .executeAsList()
                .map(SelectForUser::toVote)
                .let(::Data)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }
}

private fun SelectForUser.toVote() = Vote(contentID, vote.toInt())
