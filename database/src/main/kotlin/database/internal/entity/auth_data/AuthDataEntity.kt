package database.internal.entity.auth_data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class AuthDataEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthDataEntity>(AuthDataTable)

    val name by AuthDataTable.login

    val password by AuthDataTable.password
}