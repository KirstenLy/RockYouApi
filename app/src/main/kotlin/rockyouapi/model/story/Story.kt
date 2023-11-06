package rockyouapi.model.story

import kotlinx.serialization.Serializable
import rockyouapi.model.Author
import rockyouapi.model.Character
import rockyouapi.model.ContentTag
import rockyouapi.model.Language
import rockyouapi.model.group.Group
import rockyouapi.model.user.UserSimple

/** Story content. */
@Serializable
internal data class Story(
    val id: Int,
    val title: String,
    val language: Language?,
    val availableLanguages: List<Language>,
    val authors: List<Author>,
    val user: UserSimple?,
    val tags: List<ContentTag>,
    val rating: Int,
    val commentsCount: Long,
    val nodes: List<StoryNode>,
    val groups: List<Group>,
    val characters: List<Character>,
)