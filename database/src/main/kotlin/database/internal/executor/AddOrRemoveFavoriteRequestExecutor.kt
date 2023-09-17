package database.internal.executor

import database.external.operation.FavoriteOperation
import database.external.result.AddOrRemoveFavoriteResult
import rockyouapi.DBTest

/**
 * Add or remove comment to the database.
 * Throw SQL exceptions if [execute] called with not existed userID or contentID.
 * */
internal class AddOrRemoveFavoriteRequestExecutor(private val database: DBTest) {

    fun execute(operation: FavoriteOperation, userID: Int, contentID: Int): AddOrRemoveFavoriteResult {
        try {
            when (operation) {
                FavoriteOperation.ADD -> {
                    val isRecordAlreadyExist = database.selectFavoriteQueries
                        .selectOneByUserIDAndContentID(userID, contentID)
                        .executeAsOneOrNull() != null

                    if (isRecordAlreadyExist) {
                        return AddOrRemoveFavoriteResult.AlreadyInFavorite
                    }

                    database.insertFavoriteQueries.insert(
                        id = null,
                        userID = userID,
                        contentID = contentID
                    )
                    return AddOrRemoveFavoriteResult.Ok
                }

                FavoriteOperation.REMOVE -> {
                    val isRecordNotExist = database.selectFavoriteQueries
                        .selectOneByUserIDAndContentID(userID, contentID)
                        .executeAsOneOrNull() == null

                    if (isRecordNotExist) {
                        return AddOrRemoveFavoriteResult.NotInFavorite
                    }

                    database.deleteFavoriteQueries.removeByUserIDAndContentID(
                        userID = userID,
                        contentID = contentID
                    )
                    return AddOrRemoveFavoriteResult.Ok
                }
            }
        } catch (t: Throwable) {
            return AddOrRemoveFavoriteResult.Error(t)
        }
    }
}