package rockyouapi.model.achievement

import rockyouapi.model.achievement.additional.ContentMakerAchievementAdditionalData
import kotlinx.serialization.Serializable

/** "Content maker" achievement data. */
@Serializable
internal data class ContentMakerAchievementData(
    val isReached: Boolean,
    val additionalData: ContentMakerAchievementAdditionalData?
)