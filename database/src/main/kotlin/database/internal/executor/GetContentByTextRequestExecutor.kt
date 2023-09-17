package database.internal.executor

import database.external.result.SimpleDataResult
import database.external.result.SimpleListResult
import declaration.GetContentByTextResult

internal class GetContentByTextRequestExecutor(
    private val getPicturesByTextRequestExecutor: GetPicturesByTextRequestExecutor,
    private val getVideosByTextRequestExecutor: GetVideosByTextRequestExecutor,
    private val getStoriesByTextRequestExecutor: GetStoriesByTextRequestExecutor,
    private val getChaptersByTextRequestExecutor: GetChaptersByTextRequestExecutor
) {

    suspend fun execute(text: String): SimpleDataResult<GetContentByTextResult> {
        val pictures = when (val getPicturesExecuteResult = getPicturesByTextRequestExecutor.execute(text)) {
            is SimpleListResult.Data -> getPicturesExecuteResult.data
            is SimpleListResult.Error -> emptyList()
        }

        val videos = when (val getVideosExecuteResult = getVideosByTextRequestExecutor.execute(text)) {
            is SimpleListResult.Data -> getVideosExecuteResult.data
            is SimpleListResult.Error -> emptyList()
        }

        val stories = when (val getStoriesExecuteResult = getStoriesByTextRequestExecutor.execute(text)) {
            is SimpleListResult.Data -> getStoriesExecuteResult.data
            is SimpleListResult.Error -> emptyList()
        }

        val chapters = when (val getChaptersExecuteResult = getChaptersByTextRequestExecutor.execute(text)) {
            is SimpleListResult.Data -> getChaptersExecuteResult.data
            is SimpleListResult.Error -> emptyList()
        }

        return SimpleDataResult.Data(
            GetContentByTextResult(
                pictures = pictures,
                videos = videos,
                stories = stories,
                chapters = chapters
            )
        )
    }
}