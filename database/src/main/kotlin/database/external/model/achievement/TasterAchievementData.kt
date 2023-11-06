package database.external.model.achievement

import database.external.model.achievement.additional.TasterAchievementAdditionalData

/** "Taster" achievement data. */
data class TasterAchievementData(
    val isReached: Boolean,
    val additionalData: TasterAchievementAdditionalData?
)