package database.internal.entity.picture

import database.internal.entity.lang.LanguageTable
import database.internal.entity.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

/** Table of Picture entity. */
internal object PictureTable : IntIdTable("Picture") {

    val title = varchar("title", 256)

    val url = text("url")

    val languageID = reference("languageID", LanguageTable).nullable()

    val userID = reference("userID", UserTable)

    val rating = integer("rating")
}