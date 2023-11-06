package rockyouapi.model.achievement

import kotlinx.serialization.Serializable

/** "Repair" achievement data. */
@Serializable
internal data class RepairAchievementData(val isReached: Boolean)