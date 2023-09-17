package database.internal.executor

import database.external.result.SimpleUnitResult
import database.internal.entity.favorite.FavoriteTable
import org.jetbrains.exposed.sql.insert

internal class AddToFavoriteRequestExecutor {

    fun execute(userID: Int, contentID: Int): SimpleUnitResult {
        return try {
            insertFavorite(userID, contentID)
            SimpleUnitResult.Ok
        } catch (t: Throwable) {
            SimpleUnitResult.Error(t)
        }
    }

    private fun insertFavorite(userID: Int, contentID: Int) {
        FavoriteTable.insert {
            it[FavoriteTable.userID] = userID
            it[FavoriteTable.contentID] = contentID
        }
    }
}