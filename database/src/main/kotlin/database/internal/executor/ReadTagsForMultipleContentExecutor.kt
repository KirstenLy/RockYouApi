package database.internal.executor

import database.external.model.tag.TagSimple
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleDataResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.relationcontentandtag.SelectContentTagListForContentList

/** @see execute */
internal class ReadTagsForMultipleContentExecutor(private val database: Database) {

    /**
     * Get a tag list for a content list.
     *
     * Respond as:
     * - [SimpleDataResult.Data] Request finished without errors. Contains the map: [contentID <-> ListOfContentTag].
     * - [SimpleDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentIDList: List<Int>, environmentID: Int): SimpleDataResult<Map<Int, List<TagSimple>>> {
        if (contentIDList.isEmpty()) return Data(emptyMap())
        return withContext(Dispatchers.IO) {
            try {
                database.selectRelationContentAndTagQueries
                    .selectContentTagListForContentList(
                        contentIDList = contentIDList,
                        environmentID = environmentID
                    )
                    .executeAsList()
                    .groupBy(
                        keySelector = SelectContentTagListForContentList::contentID,
                        valueTransform = SelectContentTagListForContentList::extractTag
                    )
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleDataResult.Error(t)
            }
        }
    }
}

private fun SelectContentTagListForContentList.extractTag() = TagSimple(
    id = tagID,
    name = translation,
    isOneOfMainTag = isOneOfMainTag
)