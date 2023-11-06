package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadRandomUserNameExecutor(private val database: Database) {

    /** Read name of random user. */
    suspend fun execute(): String = withContext(Dispatchers.IO) {
        database.selectUserQueries
            .selectRandomUser()
            .executeAsOne()
            .name
    }
}
