package database.internal.executor

import database.external.ContentType
import database.external.result.SimpleListResult
import declaration.entity.story.Chapter
import rockyouapi.DBTest

internal class GetChaptersByTextRequestExecutor(
    private val database: DBTest,
    private val getStoryByIDRequestExecutor: GetChaptersRequestExecutor
) {

    suspend fun execute(text: String): SimpleListResult<Chapter> {
        val chapterEntityIDList = try {
            database.contentRegisterQueries
                .selectRegisterIDsByNameAndContentType(ContentType.STORY_CHAPTER.typeID.toByte(), "%$text%")
                .executeAsList()
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        if (chapterEntityIDList.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

        val response = chapterEntityIDList.mapNotNull {
            when (val getStoriesResult = getStoryByIDRequestExecutor.execute(listOf(it), null)) {
                is SimpleListResult.Data -> getStoriesResult.data
                is SimpleListResult.Error -> null
            }
        }
            .flatten()
            .distinct()

        return SimpleListResult.Data(response)
    }
}