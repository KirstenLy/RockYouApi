package database.internal.entity.story

import database.internal.entity.lang.LanguageTable
import database.internal.entity.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object StoryTable : IntIdTable("Story") {

    val title = varchar("title", 256)

    val languageID = reference("languageID", LanguageTable)

    val userID = reference("userID", UserTable)

    val rating = integer("rating")
}