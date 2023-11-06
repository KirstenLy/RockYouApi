package rockyouapi.model.achievement

import kotlinx.serialization.Serializable

/** Data about user's achievements. */
@Serializable
internal data class AchievementData(
    val repairAchievementData: RepairAchievementData,
    val advisorAchievementData: AdvisorAchievementData,
    val commentatorAchievementData: CommentatorAchievementData,
    val tasterAchievementData: TasterAchievementData,
    val contentMakerAchievementData: ContentMakerAchievementData,
)