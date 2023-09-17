package database.internal.executor

import database.external.result.SimpleUnitResult
import database.internal.entity.comment.CommentTable
import org.jetbrains.exposed.sql.insert

@Suppress("RemoveRedundantQualifierName")
internal class AddCommentRequestExecutor {

    fun execute(userID: Int, contentID: Int, commentText: String): SimpleUnitResult {
        return try {
            CommentTable.insert {
                it[CommentTable.contentID] = contentID
                it[CommentTable.userID] = userID
                it[CommentTable.text] = commentText
            }
            SimpleUnitResult.Ok
        } catch (t: Throwable) {
            SimpleUnitResult.Error(t)
        }
    }
}