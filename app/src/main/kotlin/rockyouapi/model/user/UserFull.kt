package rockyouapi.model.user

import rockyouapi.model.achievement.AchievementData
import kotlinx.serialization.Serializable

/** Full info about user. */
@Serializable
internal data class UserFull(
    val id: Int,
    val name: String,
    val registrationDate: String,
    val avatarURL: String?,
    val role: Int?,
    val achievementData: AchievementData,
)