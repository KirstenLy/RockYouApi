package database.external.model.achievement

import database.external.model.achievement.additional.CommentatorAchievementAdditionalData

/** "Commentator" achievement data. */
data class CommentatorAchievementData(
    val isReached: Boolean,
    val additionalData: CommentatorAchievementAdditionalData?
)