package database.external.model.achievement

import database.external.model.achievement.additional.ContentMakerAchievementAdditionalData

/** "Content maker" achievement data. */
data class ContentMakerAchievementData(
    val isReached: Boolean,
    val additionalData: ContentMakerAchievementAdditionalData?
)