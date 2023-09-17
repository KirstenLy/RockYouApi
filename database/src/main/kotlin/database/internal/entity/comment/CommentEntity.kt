package database.internal.entity.comment

import database.internal.entity.user.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class CommentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CommentEntity>(CommentTable)

    val text by CommentTable.text

    val user by UserEntity referencedOn CommentTable.userID
}