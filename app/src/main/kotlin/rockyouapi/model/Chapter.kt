package rockyouapi.model

import kotlinx.serialization.Serializable
import rockyouapi.model.story.Story
import rockyouapi.model.user.UserSimple

/** Chapter of [Story]. */
@Serializable
internal data class Chapter(
    val id: Int,
    val storyID: Int,
    val title: String,
    val language: Language,
    val availableLanguages: List<Language>,
    val authors: List<Author>,
    val user: UserSimple?,
    val tags: List<ContentTag>,
    val rating: Int,
    val commentsCount: Long,
)