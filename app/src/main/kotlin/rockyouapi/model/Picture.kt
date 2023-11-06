package rockyouapi.model

import kotlinx.serialization.Serializable
import rockyouapi.model.group.Group
import rockyouapi.model.user.UserSimple

/** Picture content. */
@Serializable
internal data class Picture(
    val id: Int,
    val originalContentID: Int?,
    val title: String,
    val url: String,
    val language: Language?,
    val availableLanguages: List<Language>,
    val authors: List<Author>,
    val user: UserSimple?,
    val tags: List<ContentTag>,
    val translatorName: String?,
    val rating: Int,
    val characters: List<Character>,
    val commentsCount: Long,
    val groups: List<Group>
)