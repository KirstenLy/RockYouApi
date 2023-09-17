package database.internal.entity.auth_data

import org.jetbrains.exposed.dao.id.IntIdTable

/** Table for store users auth info. */
internal object AuthDataTable : IntIdTable("AuthData") {

    val login = varchar("login", 256)

    val password = varchar("password", 256)
}