package database.external.model.achievement

/** Data about user's achievements. */
data class AchievementData(
    val repairAchievementData: RepairAchievementData,
    val advisorAchievementData: AdvisorAchievementData,
    val commentatorAchievementData: CommentatorAchievementData,
    val tasterAchievementData: TasterAchievementData,
    val contentMakerAchievementData: ContentMakerAchievementData,
)