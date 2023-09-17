package database.internal.entity.video.relation

import database.internal.entity.comment.CommentTable
import database.internal.entity.picture.PictureTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationVideoAndCommentTable : IntIdTable("RelationVideoAndComment") {

    val videoID = reference("pictureID", PictureTable)

    val commentID = reference("commentID", CommentTable)
}