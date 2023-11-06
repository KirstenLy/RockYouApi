package database.external.model

import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple

/** Chapter of [Story]. */
data class Chapter(
    val id: Int,
    val storyID: Int,
    val title: String,
    val language: LanguageSimple,
    val availableLanguages: List<LanguageSimple>,
    val authors: List<Author>,
    val user: UserSimple?,
    val tags: List<TagSimple>,
    val rating: Int,
    val commentsCount: Long,
)