package database.internal.entity.chapter_text

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class ChapterTextEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChapterTextEntity>(ChapterTextTable)

    val text by ChapterTextTable.text
}