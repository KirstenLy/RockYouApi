package database.external.model

import database.external.model.group.Group
import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple

/** Picture content. */
data class Picture(
    val id: Int,
    val originalContentID: Int?,
    val title: String,
    val url: String,
    val language: LanguageSimple?,
    val availableLanguages: List<LanguageSimple>,
    val authors: List<Author>,
    val user: UserSimple?,
    val tags: List<TagSimple>,
    val translatorName: String?,
    val rating: Int,
    val characters: List<CharacterBasicInfo>,
    val commentsCount: Long,
    val groups: List<Group>
)