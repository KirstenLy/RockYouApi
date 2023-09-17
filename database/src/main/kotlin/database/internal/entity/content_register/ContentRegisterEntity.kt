package database.internal.entity.content_register

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class ContentRegisterEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ContentRegisterEntity>(ContentRegisterTable)

    val contentType by ContentRegisterTable.contentType

    val contentID by ContentRegisterTable.contentID
}