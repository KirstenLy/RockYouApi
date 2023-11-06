package database.external.model.story

import database.external.model.Author
import database.external.model.CharacterBasicInfo
import database.external.model.group.Group
import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple

/** Story content. */
data class Story(
    val id: Int,
    val title: String,
    val language: LanguageSimple?,
    val availableLanguages: List<LanguageSimple>,
    val authors: List<Author>,
    val user: UserSimple?,
    val tags: List<TagSimple>,
    val rating: Int,
    val commentsCount: Long,
    val nodes: List<StoryNode>,
    val groups: List<Group>,
    val characters: List<CharacterBasicInfo>,
)