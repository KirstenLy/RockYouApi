package database.internal.entity.chapter

import database.internal.entity.author.AuthorEntity
import database.internal.entity.chapter.relation.RelationChapterAndAuthorTable
import database.internal.entity.chapter.relation.RelationChapterAndLanguageTable
import database.internal.entity.chapter.relation.RelationChapterAndTagTable
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.tag.TagEntity
import database.internal.entity.user.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class ChapterEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChapterEntity>(ChapterTable)

    val title by ChapterTable.title

    val user by UserEntity referencedOn ChapterTable.userID

    val authors by AuthorEntity via RelationChapterAndAuthorTable

    val language by LanguageEntity referencedOn ChapterTable.languageID

    val availableLanguages by LanguageEntity via RelationChapterAndLanguageTable

    val tags by TagEntity via RelationChapterAndTagTable

    val rating by ChapterTable.rating
}