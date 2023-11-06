package database.internal.executor

import database.external.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadRandomContentIDForEntityExecutor(private val database: Database) {

    /** Read random contentID for concrete entity by [ContentType]. */
    suspend fun execute(contentType: ContentType): Int = withContext(Dispatchers.IO) {
        database.selectRegisterQueries
            .selectRandomContentIDForEntity(contentType.typeID)
            .executeAsOne()
    }
}
