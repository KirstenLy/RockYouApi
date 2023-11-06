package rockyouapi.model.achievement.additional

import kotlinx.serialization.Serializable

/**
 * "Commentator" achievement additional data.
 *
 * @param totalCommentCount - number of user's comment in total.
 * @param requirementCommentCount - number or comment to get achievement.
 * */
@Serializable
internal data class CommentatorAchievementAdditionalData(val totalCommentCount: Int, val requirementCommentCount: Int)