package database.internal.entity.story

import database.internal.entity.author.AuthorEntity
import database.internal.entity.chapter.ChapterEntity
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.story.relation.RelationStoryAndAuthorTable
import database.internal.entity.story.relation.RelationStoryAndLanguageTable
import database.internal.entity.story.relation.RelationStoryAndChapterTable
import database.internal.entity.story.relation.RelationStoryAndTagTable
import database.internal.entity.tag.TagEntity
import database.internal.entity.user.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class StoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StoryEntity>(StoryTable)

    val title by StoryTable.title

    val user by UserEntity referencedOn StoryTable.userID

    val authors by AuthorEntity via RelationStoryAndAuthorTable

    val language by LanguageEntity referencedOn StoryTable.languageID

    val availableLanguages by LanguageEntity via RelationStoryAndLanguageTable

    val tags by TagEntity via RelationStoryAndTagTable

    val rating by StoryTable.rating

    val chaptersBase by ChapterEntity via RelationStoryAndChapterTable
}