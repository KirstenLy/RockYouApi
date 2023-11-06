package database.internal.executor

import database.external.model.Author
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.relationcontentandauthor.SelectAuthorListForContent

/** @see execute */
internal class ReadAuthorsForSingleContentExecutor(private val database: Database) {

    /**
     * Get authors for content.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentID: Int): SimpleListResult<Author> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectRelationContentAndAuthorQueries
                    .selectAuthorListForContent(contentID)
                    .executeAsList()
                    .map(SelectAuthorListForContent::extractAuthor)
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleListResult.Error(t)
            }
        }
    }
}

private fun SelectAuthorListForContent.extractAuthor() = Author(
    id = authorID,
    name = authorName
)