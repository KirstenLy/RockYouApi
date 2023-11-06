package database.internal.executor

import database.external.model.UploadContentRequest
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.upload.SelectAllUploadsForUser

/** @see execute */
internal class ReadUploadRequestsForUserExecutor(private val database: Database) {

    /** Get all requests for new content addition for user. */
    suspend fun execute(userID: Int): SimpleListResult<UploadContentRequest> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectUploadQueries
                    .selectAllUploadsForUser(userID)
                    .executeAsList()
                    .map(SelectAllUploadsForUser::toExternal)
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleListResult.Error(t)
            }
        }
    }
}

private fun SelectAllUploadsForUser.toExternal() = UploadContentRequest(
    fileName = fileName,
    isApproved = isApproved,
    isClosed = isClosed,
    message = message,
    creationDate = creationDate
)