package database.internal.entity.user

import org.jetbrains.exposed.dao.id.IntIdTable

internal object UserTable : IntIdTable("User") {

    val name = varchar("name", 256)
}