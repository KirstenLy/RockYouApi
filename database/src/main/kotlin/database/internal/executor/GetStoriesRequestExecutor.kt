package database.internal.executor

import database.external.ContentType
import database.external.filter.StoryByIDFilter
import database.external.filter.StoryListFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.internal.AvailableLanguageModel
import declaration.entity.story.StoryNew
import rockyouapi.DBTest

/**
 * Executor to get stories.
 * One by one make several requests:
 * - Get
 * - Get
 * - Get
 * */
internal class GetStoriesRequestExecutor(
    private val database: DBTest,
    private val supportedLanguages: List<AvailableLanguageModel>,
    private val storyByIDRequestExecutor: GetStoryByIDRequestExecutor
) {

    suspend fun execute(filter: StoryListFilter): SimpleListResult<StoryNew> {
        return try {
            val storiesRegisterIDs = try {
                database.contentRegisterQueries.selectLastNRegisterIDsByContentType(
                    contentType = ContentType.STORY.typeID.toByte(),
                    limit = filter.limit,
                    offset = filter.offset ?: 0L
                )
                    .executeAsList()
            } catch (t: Throwable) {
                return SimpleListResult.Data(emptyList())
            }

            val stories = storiesRegisterIDs.map {
                val readStoryByIDRequestResult = storyByIDRequestExecutor.execute(
                    StoryByIDFilter(
                        storyID = it,
                        environmentLangID = filter.environmentLangID
                    )
                )
                when (readStoryByIDRequestResult) {
                    is SimpleOptionalDataResult.Data -> readStoryByIDRequestResult.model
                    is SimpleOptionalDataResult.DataNotFounded -> return SimpleListResult.Error(IllegalStateException())
                    is SimpleOptionalDataResult.Error -> return SimpleListResult.Error(readStoryByIDRequestResult.t)
                }
            }

            SimpleListResult.Data(stories)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }
}