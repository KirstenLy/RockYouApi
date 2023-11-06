package rockyouapi.model.achievement

import kotlinx.serialization.Serializable

/** "Advisor" achievement data. */
@Serializable
data class AdvisorAchievementData(val isReached: Boolean)