package database.internal.executor

import database.external.operation.FavoriteOperation
import database.external.result.AddOrRemoveFavoriteResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class CreateOrDeleteFavoriteExecutor(private val database: Database) {

    /**
     * Insert or delete favorite record to/from the database.
     * Check user existence for [userID] and content existence for [contentID].
     *
     * Respond as:
     * - [AddOrRemoveFavoriteResult.Ok] Content added to favorite history.
     * - [AddOrRemoveFavoriteResult.UserNotExists] User by [userID] not exists.
     * - [AddOrRemoveFavoriteResult.ContentNotExists] Content by [contentID] not exists.
     * - [AddOrRemoveFavoriteResult.AlreadyInFavorite] Content already added in favorite for user with [userID],
     *                                                 no new record inserted.
     * - [AddOrRemoveFavoriteResult.NotInFavorite] Content is not in favorite by [userID], so no record deleted.
     * - [AddOrRemoveFavoriteResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(operation: FavoriteOperation, userID: Int, contentID: Int): AddOrRemoveFavoriteResult {
        return withContext(Dispatchers.IO) {
            try {
                when (operation) {
                    FavoriteOperation.ADD -> {
                        if (!isUserExist(userID)) return@withContext AddOrRemoveFavoriteResult.UserNotExists
                        if (!isContentExist(contentID)) return@withContext AddOrRemoveFavoriteResult.ContentNotExists
                        if (isRecordExist(userID, contentID)) {
                            return@withContext AddOrRemoveFavoriteResult.AlreadyInFavorite
                        }

                        database.insertFavoriteQueries.insert(
                            id = null,
                            userID = userID,
                            contentID = contentID
                        )
                        return@withContext AddOrRemoveFavoriteResult.Ok
                    }

                    FavoriteOperation.REMOVE -> {
                        if (!isUserExist(userID)) return@withContext AddOrRemoveFavoriteResult.UserNotExists
                        if (!isContentExist(contentID)) return@withContext AddOrRemoveFavoriteResult.ContentNotExists
                        if (!isRecordExist(userID, contentID)) {
                            return@withContext AddOrRemoveFavoriteResult.NotInFavorite
                        }

                        database.deleteFavoriteQueries.deleteByUserIDAndContentID(
                            userID = userID,
                            contentID = contentID
                        )
                        return@withContext AddOrRemoveFavoriteResult.Ok
                    }
                }
            } catch (t: Throwable) {
                return@withContext AddOrRemoveFavoriteResult.Error(t)
            }
        }
    }

    private fun isContentExist(contentID: Int) = database
        .selectRegisterQueries
        .isContentExistByID(contentID)
        .executeAsOne()

    private fun isUserExist(userID: Int) = database
        .selectUserQueries
        .isUserExistByID(userID)
        .executeAsOne()

    private fun isRecordExist(userID: Int, contentID: Int) = database
        .selectFavoriteQueries
        .isRecordExistByUserIDAndContentID(userID, contentID)
        .executeAsOne()
}