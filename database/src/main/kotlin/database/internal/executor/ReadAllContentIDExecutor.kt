package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadAllContentIDExecutor(private val database: Database) {

    /** Get all existed content ID. */
    suspend fun execute(): List<Int> = withContext(Dispatchers.IO) {
        database.selectRegisterQueries
            .selectAllContentID()
            .executeAsList()
    }
}
