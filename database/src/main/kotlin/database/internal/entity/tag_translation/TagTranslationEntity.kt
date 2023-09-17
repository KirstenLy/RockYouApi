package database.internal.entity.tag_translation

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class TagTranslationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TagTranslationEntity>(TagTranslationTable)

    val langID by TagTranslationTable.langID

    val translation by TagTranslationTable.translation
}