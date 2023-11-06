package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadRandomContentIDListExecutor(private val database: Database) {

    /** Read several random content ID. */
    suspend fun execute(size: Long): List<Int> = withContext(Dispatchers.IO) {
        database.selectRegisterQueries
            .selectRandomContentIDListByArg(size)
            .executeAsList()
    }
}
