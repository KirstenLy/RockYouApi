package database.internal.executor

import database.external.model.UpdateContentRequest
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.updatecontent.SelectRequestForUser

/** @see execute */
internal class ReadUpdateContentRequestListForUserExecutor(private val database: Database) {

    /** Create request for update existed content info. */
    suspend fun execute(userID: Int): SimpleListResult<UpdateContentRequest> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectUpdateContentInfoQueries
                    .selectRequestForUser(userID)
                    .executeAsList()
                    .map(SelectRequestForUser::toExternal)
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleListResult.Error(t)
            }
        }
    }
}

private fun SelectRequestForUser.toExternal() = UpdateContentRequest(
    contentName = contentTitle,
    requestText = requestText,
    isApproved = isApproved,
    isClosed = isClosed,
    resultText = resultText,
    creationDate = creationDate
)