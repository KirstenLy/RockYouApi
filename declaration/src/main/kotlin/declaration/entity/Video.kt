package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: Int,
    val title: String,
    val url: String,
    val language: Lang?,
    val availableLanguages: List<Lang>?,
    val authors: List<Author>?,
    val user: User?,
    val tags: List<Tag>?,
    val rating: Int,
    val commentsCount: Long
)