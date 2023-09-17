package database.internal.entity.picture.relation

import database.internal.entity.comment.CommentTable
import database.internal.entity.picture.PictureTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationPictureAndCommentTable : IntIdTable("RelationPictureAndComment") {

    val pictureID = reference("pictureID", PictureTable)

    val commentID = reference("commentID", CommentTable)
}