package rockyouapi.model.achievement.additional

import kotlinx.serialization.Serializable

/**
 * "Content maker" achievement additional data.
 *
 * @param totalApprovedContent - number of user's approved content in total.
 * @param requirementApprovedContent - number or approved content to get achievement.
 * */
@Serializable
internal data class ContentMakerAchievementAdditionalData(val totalApprovedContent: Int, val requirementApprovedContent: Int)