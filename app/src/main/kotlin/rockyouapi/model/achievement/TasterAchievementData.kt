package rockyouapi.model.achievement

import rockyouapi.model.achievement.additional.TasterAchievementAdditionalData
import kotlinx.serialization.Serializable

/** "Taster" achievement data. */
@Serializable
internal data class TasterAchievementData(
    val isReached: Boolean,
    val additionalData: TasterAchievementAdditionalData?
)