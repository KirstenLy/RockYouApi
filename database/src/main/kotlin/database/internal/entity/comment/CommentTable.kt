package database.internal.entity.comment

import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object CommentTable : IntIdTable("Comment") {

    val contentID = reference("contentID", ContentRegisterTable)

    val userID = reference("userID", UserTable)

    val text = text("text")
}