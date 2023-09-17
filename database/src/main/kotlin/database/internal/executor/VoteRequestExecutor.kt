package database.internal.executor

import database.external.operation.VoteOperation
import database.external.result.VoteResult
import database.external.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.DBTest

internal class VoteRequestExecutor(private val database: DBTest) {

    suspend fun execute(userID: Int, contentID: Int, voteOperation: VoteOperation): VoteResult =
        withContext(Dispatchers.IO) {
            val isContentExist = try {
                database.contentRegisterQueries
                    .selectContentIDAndContentTypeByID(contentID)
                    .executeAsOneOrNull() != null
            } catch (t: Throwable) {
                false
            }

            if (!isContentExist) return@withContext VoteResult.ContentNotExist

            val userPreviousVoteForContent = try {
                database.voteHistoryQueries
                    .selectAllByUserIDAndContentID(userID, contentID)
                    .executeAsOneOrNull()
                    ?.vote?.toInt()?.toVoteOperation()
            } catch (t: Throwable) {
                return@withContext VoteResult.Error(t)
            }

            when {

                // User never voted for this content, write vote to history and update content rating
                userPreviousVoteForContent == null -> {
                    try {
                        database.transaction {
                            insertVoteToVoteHistory(userID, contentID, voteOperation)
                            updateContentRating(contentID, voteOperation)
                        }
                    } catch (t: Throwable) {
                        return@withContext VoteResult.Error(t)
                    }

                    return@withContext VoteResult.OK
                }

                // User already voted this way
                userPreviousVoteForContent == VoteOperation.UPVOTE && voteOperation == VoteOperation.UPVOTE -> {
                    return@withContext VoteResult.AlreadyVoted
                }

                // User already voted this way
                userPreviousVoteForContent == VoteOperation.DOWNVOTE && voteOperation == VoteOperation.DOWNVOTE -> {
                    return@withContext VoteResult.AlreadyDownVoted
                }

                // User take his upvote back
                userPreviousVoteForContent == VoteOperation.UPVOTE && voteOperation == VoteOperation.DOWNVOTE -> {
                    removeVoteFromVoteHistory(userID, contentID)
                    updateContentRating(contentID, voteOperation)
                    return@withContext VoteResult.OK
                }

                // User take his downvote back
                userPreviousVoteForContent == VoteOperation.DOWNVOTE && voteOperation == VoteOperation.UPVOTE -> {
                    removeVoteFromVoteHistory(userID, contentID)
                    updateContentRating(contentID, voteOperation)
                    return@withContext VoteResult.OK
                }
            }
            return@withContext VoteResult.OK
        }

    private fun insertVoteToVoteHistory(userID: Int, contentID: Int, voteOperation: VoteOperation) {
        database.voteHistoryQueries.insertForProd(
            userID = userID,
            contentID = contentID,
            vote = voteOperation.toByte()
        )
    }

    private fun removeVoteFromVoteHistory(userID: Int, contentID: Int) {
        database.voteHistoryQueries.removeByUserIDAndContentID(userID, contentID)
    }

    private fun updateContentRating(registerID: Int, voteOperation: VoteOperation) {
        val (contentID, contentType) = database.contentRegisterQueries
            .selectContentIDAndContentTypeByID(registerID)
            .executeAsOneOrNull()
            ?: throw IllegalStateException("Can't read content type for content with ID: $registerID")

        when (contentType) {
            ContentType.PICTURE.typeID.toByte() -> updatePictureRating(contentID, voteOperation)
            ContentType.VIDEO.typeID.toByte() -> updateVideoRating(contentID, voteOperation)
            ContentType.STORY.typeID.toByte() -> updateStoryRating(contentID, voteOperation)
            ContentType.STORY_CHAPTER.typeID.toByte() -> updateChapterRating(contentID, voteOperation)
        }
    }

    private fun updatePictureRating(contentID: Int, voteOperation: VoteOperation) {
        val oldPictureRating = database.pictureQueries
            .selectRatingByEntityID(contentID)
            .executeAsOneOrNull()
            ?: throw IllegalStateException("Can't read picture rating for content with ID: $contentID")

        database.pictureQueries.updateRating(
            id = contentID,
            rating = oldPictureRating + voteOperation.toByte()
        )
    }

    private fun updateVideoRating(contentID: Int, voteOperation: VoteOperation) {
        val oldVideoRating = database.videoQueries
            .selectRatingByID(contentID)
            .executeAsOneOrNull()
            ?: throw IllegalStateException("Can't read picture rating for content with ID: $contentID")

        database.videoQueries.updateRating(
            id = contentID,
            rating = oldVideoRating + voteOperation.toByte()
        )
    }

    private fun updateStoryRating(contentID: Int, voteOperation: VoteOperation) {
//        val oldStoryRating = database.storyQueries
//            .selectRatingByID(contentID)
//            .executeAsOneOrNull()
//            ?.rating
//            ?: throw IllegalStateException("Can't read picture rating for content with ID: $contentID")
//
//        database.storyQueries.updateRating(
//            id = contentID,
//            rating = oldStoryRating + voteOperation.toByte()
//        )
    }

    private fun updateChapterRating(contentID: Int, voteOperation: VoteOperation) {
//        val oldChapterRating = database.storyQueries
//            .selectRatingByID(contentID)
//            .executeAsOneOrNull()
//            ?.rating
//            ?: throw IllegalStateException("Can't read picture rating for content with ID: $contentID")
//
//        database.chapterQueries.updateRating(
//            id = contentID,
//            rating = oldChapterRating + voteOperation.toByte()
//        )
    }

    private fun VoteOperation.toByte() :Byte = when (this) {
        VoteOperation.UPVOTE -> 1
        VoteOperation.DOWNVOTE -> -1
    }

    private fun VoteOperation.toEnum() :Byte = when (this) {
        VoteOperation.UPVOTE -> 1
        VoteOperation.DOWNVOTE -> -1
    }

    private fun Int.toVoteOperation() = when (this) {
        1 -> VoteOperation.UPVOTE
        -1 -> VoteOperation.DOWNVOTE
        else -> throw IllegalStateException("Unknown rating value in database: $this")
    }
}