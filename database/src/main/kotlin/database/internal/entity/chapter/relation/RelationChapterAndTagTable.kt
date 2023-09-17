package database.internal.entity.chapter.relation

import database.internal.entity.chapter.ChapterTable
import database.internal.entity.tag.TagTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationChapterAndTagTable : IntIdTable("RelationChapterAndTag") {

    val chapterID = reference("chapterID", ChapterTable)

    val tagID = reference("tagID", TagTable)
}