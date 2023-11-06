package database.internal.executor

import common.utils.takeIfNotEmpty
import database.external.ContentType
import database.external.filter.ChapterByIDFilter
import database.external.filter.PictureByIDFilter
import database.external.filter.StoryByIDFilter
import database.external.filter.VideoByIDFilter
import database.external.model.Chapter
import database.external.model.Picture
import database.external.model.Video
import database.external.model.story.Story
import database.external.result.GetContentByIDResult
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleOptionalDataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.register.SelectContentTypesForIDs

/** @see execute */
internal class ReadContentByIDListExecutor(
    private val database: Database,
    private val readPictureByIDExecutor: ReadPictureByIDExecutor,
    private val readVideoByIDExecutor: ReadVideoByIDExecutor,
    private val readStoryByIDExecutor: ReadStoryByIDExecutor,
    private val readStoryChapterByIDExecutor: ReadChapterByIDExecutor,
) {

    /**
     * Read content by [contentIDList].
     *
     * Respond as:
     * - [SimpleDataResult.Data] Data read executed correctly.
     * - [SimpleDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentIDList: List<Int>, environmentID: Int?): SimpleDataResult<GetContentByIDResult> {
        return withContext(Dispatchers.IO) {
            try {
                val contentIDWithContentTypeList = database.selectRegisterQueries
                    .selectContentTypesForIDs(contentIDList)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?: return@withContext SimpleDataResult.Data(GetContentByIDResult())

                val pictureList = contentIDWithContentTypeList
                    .filter { it.contentType == ContentType.PICTURE.typeID }
                    .map(SelectContentTypesForIDs::id)
                    .map { readPictureByIDExecutor.execute(PictureByIDFilter(it, environmentID)) }
                    .filterIsInstance<SimpleOptionalDataResult.Data<Picture>>()
                    .map { it.model }

                val videoList = contentIDWithContentTypeList
                    .filter { it.contentType == ContentType.VIDEO.typeID }
                    .map(SelectContentTypesForIDs::id)
                    .map { readVideoByIDExecutor.execute(VideoByIDFilter(it, environmentID)) }
                    .filterIsInstance<SimpleOptionalDataResult.Data<Video>>()
                    .map { it.model }

                val storyList = contentIDWithContentTypeList
                    .filter { it.contentType == ContentType.STORY.typeID }
                    .map(SelectContentTypesForIDs::id)
                    .map { readStoryByIDExecutor.execute(StoryByIDFilter(it, environmentID)) }
                    .filterIsInstance<SimpleOptionalDataResult.Data<Story>>()
                    .map { it.model }

                val chaptersList = contentIDWithContentTypeList
                    .filter { it.contentType == ContentType.STORY_CHAPTER.typeID }
                    .map(SelectContentTypesForIDs::id)
                    .map { readStoryChapterByIDExecutor.execute(ChapterByIDFilter(it, environmentID)) }
                    .filterIsInstance<SimpleOptionalDataResult.Data<Chapter>>()
                    .map { it.model }

                return@withContext SimpleDataResult.Data(
                    GetContentByIDResult(
                        pictures = pictureList,
                        videos = videoList,
                        stories = storyList,
                        chapters = chaptersList
                    )
                )
            } catch (t: Throwable) {
                return@withContext SimpleDataResult.Error(t)
            }
        }
    }
}