package database.internal.executor

import database.external.result.SimpleUnitResult
import rockyouapi.DBTest

/**
 * Insert comment to database.
 * When [execute] called with empty comment text, do nothing and return "Ok" result.
 *
 * Throw SQL exceptions if [execute] called with not existed userID or contentID.
 * */
internal class AddCommentRequestExecutor(private val database: DBTest) {

    fun execute(userID: Int, contentID: Int, commentText: String): SimpleUnitResult {
        if (commentText.isEmpty()) return SimpleUnitResult.Ok
        return try {
            database.insertCommentQueries.insert(
                id = null,
                contentID = contentID,
                userID = userID,
                text = commentText
            )
            SimpleUnitResult.Ok
        } catch (t: Throwable) {
            SimpleUnitResult.Error(t)
        }
    }
}