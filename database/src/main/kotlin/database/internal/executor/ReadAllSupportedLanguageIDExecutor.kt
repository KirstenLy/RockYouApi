package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadAllSupportedLanguageIDExecutor(private val database: Database) {

    /** Get ID of all supported languages. */
    suspend fun execute(): List<Int> = withContext(Dispatchers.IO) {
        database.selectLanguageQueries
            .selectAllLanguageID()
            .executeAsList()
    }
}
