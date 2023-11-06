package database.internal.executor

import database.external.filter.CommentListFilter
import database.external.model.Comment
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.comment.SelectForContent

/** @see execute */
internal class ReadCommentsExecutor(private val database: Database) {

    /**
     * Read comments by [CommentListFilter].
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors, comments founded.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     *   Throw an SQL error when [CommentListFilter.limit] or [CommentListFilter.offset] negative.
     *   It's caller's responsibility to pass these arguments correct.
     * */
    suspend fun execute(filter: CommentListFilter): SimpleListResult<Comment> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectCommentQueries
                    .selectForContent(filter.contentID, filter.limit, filter.offset)
                    .executeAsList()
                    .map(SelectForContent::toComment)
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleListResult.Error(t)
            }
        }
    }
}

private fun SelectForContent.toComment() = Comment(
    id = id,
    contentID = contentID,
    text = text,
    userID = userID,
    userName = userName,
    creationDate = creationTime
)
