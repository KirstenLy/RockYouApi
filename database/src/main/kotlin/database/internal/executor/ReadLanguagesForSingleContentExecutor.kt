package database.internal.executor

import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class ReadLanguagesForSingleContentExecutor(private val database: Database) {

    /**
     * Read language ID list for content.
     * If not existed [contentID] passed, return [SimpleListResult.Data] with emptyList.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentID: Int): SimpleListResult<Int> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectRelationContentAndLanguageQueries
                    .selectContentLanguageIDList(contentID)
                    .executeAsList()
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleListResult.Error(t)
            }
        }
    }
}