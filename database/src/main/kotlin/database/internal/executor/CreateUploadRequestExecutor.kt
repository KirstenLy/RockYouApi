package database.internal.executor

import database.external.result.common.SimpleUnitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class CreateUploadRequestExecutor(private val database: Database) {

    /** Create request for add new content. */
    suspend fun execute(userID: Int?, fileName: String): SimpleUnitResult {
        return withContext(Dispatchers.IO) {
            try {
                database.insertUploadQueries.insert(
                    userID = userID,
                    fileName = fileName,
                )
                SimpleUnitResult.Ok
            } catch (t: Throwable) {
                SimpleUnitResult.Error(t)
            }
        }
    }
}