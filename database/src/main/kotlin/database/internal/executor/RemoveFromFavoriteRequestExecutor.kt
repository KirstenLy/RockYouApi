package database.internal.executor

import database.external.result.SimpleUnitResult
import database.internal.entity.favorite.FavoriteTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

internal class RemoveFromFavoriteRequestExecutor {

    fun execute(userID: Int, contentID: Int): SimpleUnitResult {
        return try {
            deleteFavorite(userID, contentID)
            SimpleUnitResult.Ok
        } catch (t: Throwable) {
            SimpleUnitResult.Error(t)
        }
    }

    private fun deleteFavorite(userID: Int, contentID: Int) {
        FavoriteTable.deleteWhere {
            FavoriteTable.userID eq userID and (FavoriteTable.contentID eq contentID)
        }
    }
}