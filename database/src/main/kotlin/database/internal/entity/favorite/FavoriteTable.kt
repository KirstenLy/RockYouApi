package database.internal.entity.favorite

import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object FavoriteTable : IntIdTable("Favorite") {

    val userID = reference("userID", UserTable)

    val contentID = reference("contentID", ContentRegisterTable)
}