package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.CommentListFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleListResult.Data
import declaration.entity.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.DBTest
import rockyouapi.comment.SelectForContent

/**
 * Read comments from database.
 * Each comment has author as [declaration.entity.User].
 * Sorted by DESC order.
 * */
internal class GetCommentsRequestExecutor(private val database: DBTest) {

    suspend fun execute(filter: CommentListFilter): SimpleListResult<Comment> = withContext(Dispatchers.IO) {
        return@withContext try {
            database.selectCommentQueries
                .selectForContent(filter.contentID, filter.limit, filter.offset ?: 0L)
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { it.toComment() }
                ?.let(::Data)
                ?: Data(emptyList())
        } catch (t: NullPointerException) {
            Data(emptyList())
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }

    private fun SelectForContent.toComment() = Comment(
        id = id,
        contentID = contentID,
        text = text,
        userID = userID,
        userName = userName,
    )
}