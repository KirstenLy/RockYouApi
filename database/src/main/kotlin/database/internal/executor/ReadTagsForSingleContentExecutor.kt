package database.internal.executor

import database.external.model.tag.TagSimple
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.relationcontentandtag.SelectContentTagList

/** @see execute */
internal class ReadTagsForSingleContentExecutor(private val database: Database) {

    /**
     * Get tag list for content.
     * If incorrect data passed(not existed [contentID] of [environmentID]) return [SimpleListResult.Data] with emptyList.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentID: Int, environmentID: Int): SimpleListResult<TagSimple> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectRelationContentAndTagQueries
                    .selectContentTagList(contentID, environmentID)
                    .executeAsList()
                    .map(SelectContentTagList::extractTag)
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleListResult.Error(t)
            }
        }
    }
}

private fun SelectContentTagList.extractTag() = TagSimple(
    id = tagID,
    name = translation,
    isOneOfMainTag = isOneOfMainTag
)