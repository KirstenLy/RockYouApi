package database.internal.entity.chapter_text

import org.jetbrains.exposed.dao.id.IntIdTable

internal object ChapterTextTable : IntIdTable("ChapterText") {

    val text = text("text")
}