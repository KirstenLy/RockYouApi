package database.internal.entity.lang

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class LanguageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LanguageEntity>(LanguageTable)

    val isDefault by LanguageTable.isDefault
}