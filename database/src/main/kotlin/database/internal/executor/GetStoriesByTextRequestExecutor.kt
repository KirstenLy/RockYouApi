package database.internal.executor

import database.external.ContentType
import database.external.filter.StoryByIDFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import declaration.entity.story.StoryNew
import rockyouapi.DBTest

internal class GetStoriesByTextRequestExecutor(
    private val database: DBTest,
    private val getStoryByIDRequestExecutor: GetStoryByIDRequestExecutor
) {

    suspend fun execute(text: String): SimpleListResult<StoryNew> {
        val storyEntityIDList = try {
            database.contentRegisterQueries
                .selectRegisterIDsByNameAndContentType(ContentType.STORY.typeID.toByte(), "%$text%")
                .executeAsList()
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        if (storyEntityIDList.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

        val response = storyEntityIDList.mapNotNull {
            when (val getStoriesResult = getStoryByIDRequestExecutor.execute(StoryByIDFilter(it))) {
                is SimpleOptionalDataResult.Data -> getStoriesResult.model
                is SimpleOptionalDataResult.DataNotFounded -> null
                is SimpleOptionalDataResult.Error -> null
            }
        }

        return SimpleListResult.Data(response)
    }
}