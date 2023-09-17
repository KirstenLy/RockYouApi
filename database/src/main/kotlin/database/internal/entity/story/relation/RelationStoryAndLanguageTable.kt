package database.internal.entity.story.relation

import database.internal.entity.lang.LanguageTable
import database.internal.entity.story.StoryTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationStoryAndLanguageTable : IntIdTable("RelationStoryAndLanguage") {

    val storyID = reference("storyID", StoryTable)

    val languageID = reference("languageID", LanguageTable)
}