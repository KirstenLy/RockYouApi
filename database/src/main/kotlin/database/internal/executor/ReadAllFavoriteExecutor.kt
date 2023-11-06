package database.internal.executor

import database.external.model.test.FavoriteRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import migrations.Favorite
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadAllFavoriteExecutor(private val database: Database) {

    /** Ger all favorite records of all users. */
    suspend fun execute(): List<FavoriteRecord> = withContext(Dispatchers.IO) {
        database.selectFavoriteQueries
            .selectAll()
            .executeAsList()
            .map(Favorite::toFavoriteRecord)
    }
}

private fun Favorite.toFavoriteRecord() = FavoriteRecord(
    id = id,
    userID = userID,
    contentID = contentID
)
