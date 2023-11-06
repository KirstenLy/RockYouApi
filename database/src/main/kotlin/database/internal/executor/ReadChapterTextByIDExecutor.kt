package database.internal.executor

import database.external.result.common.SimpleOptionalDataResult
import database.external.result.common.SimpleOptionalDataResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class ReadChapterTextByIDExecutor(private val database: Database) {

    /**
     * Read chapter text by chapterID.
     *
     * Respond as:
     * - [SimpleOptionalDataResult.Data] Request finished without errors, text founded.
     * - [SimpleOptionalDataResult.DataNotFounded] Request finished without errors, text not founded.
     * - [SimpleOptionalDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(chapterID: Int): SimpleOptionalDataResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectChapterQueries
                    .selectChapterText(chapterID)
                    .executeAsOneOrNull()
                    ?.let(::Data)
                    ?: SimpleOptionalDataResult.DataNotFounded()
            } catch (t: Throwable) {
                SimpleOptionalDataResult.Error(t)
            }
        }
    }
}