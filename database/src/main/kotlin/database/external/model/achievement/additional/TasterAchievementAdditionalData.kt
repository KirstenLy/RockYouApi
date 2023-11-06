package database.external.model.achievement.additional

/**
 * "Taster" achievement additional data.
 *
 * @param totalRateActions - number of user's rate actions.
 * @param requirementRateActions - number or rate actions to get achievement.
 * */
data class TasterAchievementAdditionalData(val totalRateActions: Int, val requirementRateActions: Int)