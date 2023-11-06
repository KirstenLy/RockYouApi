package database.internal.executor

import database.external.result.AddCommentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import java.time.LocalDateTime

/** @see execute */
internal class CreateCommentExecutor(private val database: Database) {

    /**
     * Insert comment to the database.
     * Check user existence for [userID] and content existence for [contentID].
     * Don't validate [commentText].
     *
     * Respond as:
     * - [AddCommentResult.Ok] Comment added.
     * - [AddCommentResult.UserNotExists] User by [userID] not exists.
     * - [AddCommentResult.ContentNotExists] Content by [contentID] not exists.
     * - [AddCommentResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(userID: Int, contentID: Int, commentText: String): AddCommentResult {
        return withContext(Dispatchers.IO) {
            try {
                val isUserExists = database.selectUserQueries
                    .isUserExistByID(userID)
                    .executeAsOneOrNull()
                    ?: return@withContext AddCommentResult.UserNotExists

                if (!isUserExists) {
                    return@withContext AddCommentResult.UserNotExists
                }

                val isContentExists = database.selectRegisterQueries
                    .isContentExistByID(contentID)
                    .executeAsOneOrNull()
                    ?: return@withContext AddCommentResult.ContentNotExists

                if (!isContentExists) {
                    return@withContext AddCommentResult.ContentNotExists
                }

                database.insertCommentQueries.insert(
                    id = null,
                    contentID = contentID,
                    userID = userID,
                    text = commentText,
                    creationDate = LocalDateTime.now()
                )

                return@withContext AddCommentResult.Ok
            } catch (t: Throwable) {
                return@withContext AddCommentResult.Error(t)
            }
        }
    }
}