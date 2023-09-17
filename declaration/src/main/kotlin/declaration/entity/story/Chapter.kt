package declaration.entity.story

import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Tag
import declaration.entity.User
import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    val id: Int,
    val storyID: Int,
    val title: String,
    val language: Lang?,
    val availableLanguages: List<Lang>?,
    val authors: List<Author>?,
    val user: User?,
    val tags: List<Tag>?,
    val rating: Int,
    val commentsCount: Long
)