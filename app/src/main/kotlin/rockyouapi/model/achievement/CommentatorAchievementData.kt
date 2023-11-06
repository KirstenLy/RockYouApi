package rockyouapi.model.achievement

import rockyouapi.model.achievement.additional.CommentatorAchievementAdditionalData
import kotlinx.serialization.Serializable

/** "Commentator" achievement data. */
@Serializable
internal data class CommentatorAchievementData(
    val isReached: Boolean,
    val additionalData: CommentatorAchievementAdditionalData?
)