package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadRandomContentIDExecutor(private val database: Database) {

    /** Read random contentID. */
    suspend fun execute(): Int = withContext(Dispatchers.IO) {
        database.selectRegisterQueries
            .selectRandomContentID()
            .executeAsOne()
    }
}
