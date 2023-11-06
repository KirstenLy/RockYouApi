package database.internal.executor

import database.external.result.common.SimpleOptionalDataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class ReadChapterTextByIDForTestExecutor(private val database: Database) {

    /**
     * Get chapter text by chapterID.
     *
     * Respond as:
     * - [SimpleOptionalDataResult.Data] Request finished without errors, text founded.
     * - [SimpleOptionalDataResult.DataNotFounded] Request finished without errors, text not founded.
     * - [SimpleOptionalDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(chapterID: Int): String {
        return withContext(Dispatchers.IO) {
            database.selectChapterQueries
                .selectChapterText(chapterID)
                .executeAsOne()
        }
    }
}