package database.internal.entity.chapter.relation

import database.internal.entity.author.AuthorTable
import database.internal.entity.chapter.ChapterTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationChapterAndAuthorTable : IntIdTable("RelationChapterAndAuthor") {

    val chapterID = reference("chapterID", ChapterTable)

    val authorID = reference("authorID", AuthorTable)
}