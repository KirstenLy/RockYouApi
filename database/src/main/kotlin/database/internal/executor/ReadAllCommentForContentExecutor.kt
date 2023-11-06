package database.internal.executor

import database.external.model.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database
import rockyouapi.comment.SelectAllForContent
import java.time.LocalDateTime

/** @see execute */
@TestOnly
internal class ReadAllCommentForContentExecutor(private val database: Database) {

    /** Read all comments from database for content by [contentID]. */
    suspend fun execute(contentID: Int): List<Comment> = withContext(Dispatchers.IO) {
        database.selectCommentQueries
            .selectAllForContent(contentID)
            .executeAsList()
            .map(SelectAllForContent::toComment)
    }
}

private fun SelectAllForContent.toComment() = Comment(
    id = id,
    contentID = contentID,
    userID = userID,
    userName = userName,
    text = text,
    creationDate = LocalDateTime.now()
)