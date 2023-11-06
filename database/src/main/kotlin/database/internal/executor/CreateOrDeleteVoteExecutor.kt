package database.internal.executor

import database.external.operation.VoteOperation
import database.external.result.VoteResult
import database.internal.value.VoteOperationValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class CreateOrDeleteVoteExecutor(private val database: Database) {

    /**
     * Add or remove record about content from/to vote history.
     * Check user existence for [userID] and content existence for [contentID].
     *
     * Respond as:
     * - [VoteResult.OK] Request completed without errors.
     * - [VoteResult.ContentNotExist] Content with [contentID] not exist.
     * - [VoteResult.UserNotExist] User with [userID] not exist.
     * - [VoteResult.AlreadyVoted] Record with [contentID] && [userID] already exist with [VoteOperation.UPVOTE] intention.
     * - [VoteResult.AlreadyDownVoted] Record with [contentID] && [userID] already exist with [VoteOperation.DOWNVOTE] intention.
     * - [VoteResult.Error] Smith happens on query stage.
     * */
    suspend fun execute(contentID: Int, userID: Int, voteOperation: VoteOperation): VoteResult {
        return withContext(Dispatchers.IO) {
            try {
                if (!isContentExist(contentID)) return@withContext VoteResult.ContentNotExist
                if (!isUserExist(userID)) return@withContext VoteResult.UserNotExist

                val userPreviousVoteForContent = getUserPreviousVote(contentID, userID)
                val isUserPreviousVoteAsCurrentVote = voteOperation == userPreviousVoteForContent

                // User already voted this way
                if (isUserPreviousVoteAsCurrentVote && voteOperation == VoteOperation.UPVOTE) {
                    return@withContext VoteResult.AlreadyVoted
                }

                // User already voted this way
                if (isUserPreviousVoteAsCurrentVote && voteOperation == VoteOperation.DOWNVOTE) {
                    return@withContext VoteResult.AlreadyDownVoted
                }

                // User don't voted for this content, write vote to history and update content rating
                if (userPreviousVoteForContent == null) {
                    database.transaction {
                        insertVoteToVoteHistory(userID, contentID, voteOperation)
                        updateContentRating(contentID, voteOperation)
                    }
                    return@withContext VoteResult.OK
                }

                // The only reason we here are user already vote for content, but the opposite way.
                // He wants to take his vote back, so delete it from history.
                database.transaction {
                    deleteVoteFromVoteHistory(userID, contentID)
                    updateContentRating(contentID, voteOperation)
                }
                return@withContext VoteResult.OK
            } catch (t: Throwable) {
                return@withContext VoteResult.Error(t)
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

    private fun getUserPreviousVote(contentID: Int, userID: Int) = database.selectVoteHistoryQueries
        .selectVoteByUserIDAndContentID(userID, contentID)
        .executeAsOneOrNull()
        ?.toVoteOperation()

    private fun insertVoteToVoteHistory(userID: Int, contentID: Int, voteOperation: VoteOperation) {
        database.insertVoteHistoryQueries.insert(
            id = null,
            userID = userID,
            contentID = contentID,
            vote = voteOperation.toValue()
        )
    }

    private fun deleteVoteFromVoteHistory(userID: Int, contentID: Int) {
        database.deleteVoteHistoryQueries.deleteByUserIDAndContentID(userID, contentID)
    }

    private fun updateContentRating(contentID: Int, voteOperation: VoteOperation) {
        database.updateRegisterQueries.changeRatingOn(
            contentID = contentID,
            value = voteOperation.toValue().toInt()
        )
    }

    private fun Byte.toVoteOperation() = when (this) {
        VoteOperationValue.UPVOTE.value -> VoteOperation.UPVOTE
        VoteOperationValue.DOWNVOTE.value -> VoteOperation.DOWNVOTE
        else -> throw IllegalStateException("Unknown rating value in database: $this")
    }

    private fun VoteOperation.toValue() = when (this) {
        VoteOperation.UPVOTE -> VoteOperationValue.UPVOTE.value
        VoteOperation.DOWNVOTE -> VoteOperationValue.DOWNVOTE.value
    }
}