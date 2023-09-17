package declaration.entity.story

import declaration.entity.*
import kotlinx.serialization.Serializable

@Serializable
data class Story(
    val id: Int,
    val title: String,
    val language: Lang?,
    val availableLanguages: List<Lang>?,
    val authors: List<Author>?,
    val user: User?,
    val tags: List<Tag>?,
    val rating: Int,
    val commentsCount: Long,
    val chapters: List<Chapter>
)