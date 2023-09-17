package database.internal.entity.author

import org.jetbrains.exposed.dao.id.IntIdTable

internal object AuthorTable : IntIdTable("Author") {

    val name = varchar("name", 256)
}