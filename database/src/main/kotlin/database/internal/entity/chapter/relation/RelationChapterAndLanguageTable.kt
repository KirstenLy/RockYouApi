package database.internal.entity.chapter.relation

import database.internal.entity.chapter.ChapterTable
import database.internal.entity.lang.LanguageTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationChapterAndLanguageTable : IntIdTable("RelationChapterAndLanguage") {

    val chapterID = reference("chapterID", ChapterTable)

    val languageID = reference("languageID", LanguageTable)
}