package database.external.model.achievement.additional

/**
 * "Content maker" achievement additional data.
 *
 * @param totalApprovedContent - number of user's approved content in total.
 * @param requirementApprovedContent - number or approved content to get achievement.
 * */
data class ContentMakerAchievementAdditionalData(val totalApprovedContent: Int, val requirementApprovedContent: Int)