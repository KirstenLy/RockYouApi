package database.internal.executor

import database.external.result.VoteResult
import database.external.ContentType
import database.internal.VoteType
import database.internal.entity.chapter.ChapterTable
import database.internal.entity.content_register.ContentRegisterEntity
import database.internal.entity.picture.PictureTable
import database.internal.entity.story.StoryTable
import database.internal.entity.video.VideoTable
import database.internal.entity.vote_history.VoteHistoryEntity
import database.internal.entity.vote_history.VoteHistoryTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

@Deprecated("")
@Suppress("RemoveRedundantQualifierName")
internal class VoteRequestExecutorLegacy {

    fun execute(userID: Int, contentID: Int, voteType: VoteType): VoteResult {
        val userPreviousVoteForContent = selectPreviousUserVoteForContent(userID, contentID)
        when {

            // User never voted for this content, write vote to history and update content rating
            userPreviousVoteForContent == null -> {
                insertVoteToVoteHistory(userID, contentID, voteType)
                updateContentRating(contentID, voteType)
                return VoteResult.OK
            }

            // User already voted this way
            userPreviousVoteForContent == VoteType.UPVOTE && voteType == VoteType.UPVOTE -> {
                return VoteResult.AlreadyVoted
            }

            // User already voted this way
            userPreviousVoteForContent == VoteType.DOWNVOTE && voteType == VoteType.DOWNVOTE -> {
                return VoteResult.AlreadyDownVoted
            }

            // User take his upvote back
            userPreviousVoteForContent == VoteType.UPVOTE && voteType == VoteType.DOWNVOTE -> {
                removeVoteFromVoteHistory(userID, contentID)
                updateContentRating(contentID, voteType)
                return VoteResult.OK
            }

            // User take his downvote back
            userPreviousVoteForContent == VoteType.DOWNVOTE && voteType == VoteType.UPVOTE -> {
                removeVoteFromVoteHistory(userID, contentID)
                updateContentRating(contentID, voteType)
                return VoteResult.OK
            }
        }
        return VoteResult.OK
    }

    private fun selectPreviousUserVoteForContent(userID: Int, contentID: Int): VoteType? = VoteHistoryEntity
        .find { VoteHistoryTable.userID eq userID and (VoteHistoryTable.contentID eq contentID) }
        .firstOrNull()
        ?.vote
        ?.toVoteType()

    private fun insertVoteToVoteHistory(userID: Int, contentID: Int, voteType: VoteType) {
        VoteHistoryTable.insert {
            it[vote] = voteType.voteValue
            it[VoteHistoryTable.contentID] = contentID
            it[VoteHistoryTable.userID] = userID
        }
    }

    private fun removeVoteFromVoteHistory(userID: Int, contentID: Int) {
        VoteHistoryTable.deleteWhere { VoteHistoryTable.contentID eq contentID and (VoteHistoryTable.userID eq userID) }
    }

    private fun updateContentRating(contentID: Int, voteType: VoteType) {
        val contentType = ContentRegisterEntity
            .findById(contentID)
            ?.contentType
            ?: return

        when (contentType) {
            ContentType.PICTURE.typeID -> updatePictureRating(contentID, voteType)
            ContentType.VIDEO.typeID -> updateVideoRating(contentID, voteType)
            ContentType.STORY.typeID -> updateStoryRating(contentID, voteType)
            ContentType.STORY_CHAPTER.typeID -> updateChapterRating(contentID, voteType)
        }
    }

    private fun updatePictureRating(contentID: Int, voteType: VoteType) {
        PictureTable.update(
            where = { PictureTable.id eq contentID },
            body = {
                it.update(PictureTable.rating, PictureTable.rating.plus(voteType.voteValue.toInt()))
            }
        )
    }

    private fun updateVideoRating(contentID: Int, voteType: VoteType) {
        VideoTable.update(
            where = { VideoTable.id eq contentID },
            body = {
                it.update(VideoTable.rating, VideoTable.rating.plus(voteType.voteValue.toInt()))
            }
        )
    }

    private fun updateStoryRating(contentID: Int, voteType: VoteType) {
        StoryTable.update(
            where = { StoryTable.id eq contentID },
            body = {
                it.update(StoryTable.rating, StoryTable.rating.plus(voteType.voteValue.toInt()))
            }
        )
    }

    private fun updateChapterRating(contentID: Int, voteType: VoteType) {
        ChapterTable.update(
            where = { ChapterTable.id eq contentID },
            body = {
                it.update(ChapterTable.rating, ChapterTable.rating.plus(voteType.voteValue.toInt()))
            }
        )
    }

    private fun Byte.toVoteType() = when (this) {
        VoteType.UPVOTE.voteValue -> VoteType.UPVOTE
        VoteType.DOWNVOTE.voteValue -> VoteType.DOWNVOTE
        else -> null
    }
}