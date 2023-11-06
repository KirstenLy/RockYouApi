package database.internal.executor

import database.external.model.Author
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleDataResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.relationcontentandauthor.SelectAuthorListForContentList

/** @see execute */
internal class ReadAuthorsForMultipleContentExecutor(private val database: Database) {

    /**
     * Read authors for a content list.
     *
     * Respond as:
     * - [SimpleDataResult.Data] Request finished without errors. Contains the map: [contentID <-> ListOfContentAuthors].
     * - [SimpleDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentIDList: List<Int>): SimpleDataResult<Map<Int, List<Author>>> {
        if (contentIDList.isEmpty()) return Data(emptyMap())
        return withContext(Dispatchers.IO) {
            try {
                return@withContext database.selectRelationContentAndAuthorQueries
                    .selectAuthorListForContentList(contentIDList)
                    .executeAsList()
                    .groupBy(
                        keySelector = SelectAuthorListForContentList::contentID,
                        valueTransform = SelectAuthorListForContentList::extractAuthor
                    )
                    .let(::Data)
            } catch (t: Throwable) {
                return@withContext SimpleDataResult.Error(t)
            }
        }
    }
}

private fun SelectAuthorListForContentList.extractAuthor() = Author(
    id = authorID,
    name = authorName
)