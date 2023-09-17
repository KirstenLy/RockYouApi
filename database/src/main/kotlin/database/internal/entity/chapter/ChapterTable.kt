package database.internal.entity.chapter

import database.internal.entity.chapter_text.ChapterTextTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

/** Table of story chapter entity. */
internal object ChapterTable : IntIdTable("Chapter") {

    /** Chapter title. */
    val title = varchar("title", 256)

    /** Reference to chapter text. Chapter always has text.*/
    val textID = reference("textID", ChapterTextTable)

    /** Reference to chapter language. Chapter always has language.*/
    val languageID = reference("languageID", LanguageTable)

    /** Reference to user loaded chapter. Chapter always has user. */
    val userID = reference("userID", UserTable)

    /** Chapter rating. Can be negative. */
    val rating = integer("rating")
}