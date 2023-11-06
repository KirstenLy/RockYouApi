package database.external.model.user

import database.external.model.achievement.AchievementData
import java.time.LocalDateTime

/** Full model of user. */
data class UserFull(
    val id: Int,
    val name: String,
    val registrationDate: LocalDateTime,
    val avatarURL: String?,
    val role: UserRole?,
    val achievementData: AchievementData,
)