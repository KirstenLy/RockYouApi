package database.external.model.achievement.additional

/**
 * "Commentator" achievement additional data.
 *
 * @param totalCommentCount - number of user's comment in total.
 * @param requirementCommentCount - number or comment to get achievement.
 * */
data class CommentatorAchievementAdditionalData(val totalCommentCount: Int, val requirementCommentCount: Int)