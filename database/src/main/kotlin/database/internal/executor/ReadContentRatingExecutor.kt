package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadContentRatingExecutor(private val database: Database) {

    /** Read rating of content by [contentID]. */
    suspend fun execute(contentID: Int) = withContext(Dispatchers.IO) {
        database.selectRegisterQueries
            .selectContentRating(contentID)
            .executeAsOne()
    }
}
