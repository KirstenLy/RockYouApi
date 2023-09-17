package database.internal.entity.tag

import database.internal.entity.tag_translation.TagTranslationEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class TagEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TagEntity>(TagTable)

    val translationInfo by TagTranslationEntity via RelationTagAndTranslationTable
}