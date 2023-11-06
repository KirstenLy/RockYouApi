package rockyouapi.model

import kotlinx.serialization.Serializable
import rockyouapi.model.group.Group
import rockyouapi.model.user.UserSimple

/** Video content. */
@Serializable
internal data class Video(
    val id: Int,
    val title: String,
    val url: String,
    val language: Language?,
    val availableLanguages: List<Language>,
    val authors: List<Author>,
    val user: UserSimple?,
    val tags: List<ContentTag>,
    val rating: Int,
    val commentsCount: Long,
    val groups: List<Group>,
    val characters: List<Character>,
)