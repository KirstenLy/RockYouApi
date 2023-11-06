package rockyouapi.model.achievement.additional

import kotlinx.serialization.Serializable

/**
 * "Taster" achievement additional data.
 *
 * @param totalRateActions - number of user's rate actions.
 * @param requirementRateActions - number or rate actions to get achievement.
 * */
@Serializable
internal data class TasterAchievementAdditionalData(val totalRateActions: Int, val requirementRateActions: Int)