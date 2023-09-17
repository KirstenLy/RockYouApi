package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.VideoByIDFilter
import database.external.filter.VideoListFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.external.ContentType
import declaration.entity.Video
import rockyouapi.DBTest
import rockyouapi.register.SelectLastNByContentType

internal class GetVideosRequestExecutorLegacy1(
    private val database: DBTest,
    private val getVideoByIDRequestExecutor: GetVideoByIDRequestExecutor
) {

    fun execute(filter: VideoListFilter) : SimpleListResult<Video> {
        return try {
            val videosRegisterIDs = try {
                database.contentRegisterQueries
                    .selectLastNByContentType(
                        contentType = ContentType.VIDEO.typeID.toByte(),
                        limit = filter.limit,
                        offset = filter.offset ?: 0L
                    )
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?.map(SelectLastNByContentType::registerID)
                    ?: return SimpleListResult.Data(emptyList())
            } catch (t: NullPointerException) {
                return SimpleListResult.Data(emptyList())
            }

            val videos = videosRegisterIDs.mapNotNull {
                try {
                    getVideoByIDRequestExecutor.execute(
                        filter = VideoByIDFilter(
                            videoID = it,
                            environmentLangID = filter.environmentLangID
                        )
                    )
                } catch (t: NullPointerException) {
                    null
                }
            }.mapNotNull {
                when (it) {
                    is SimpleOptionalDataResult.Data -> it.model
                    else -> null
                }
            }

            SimpleListResult.Data(videos)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }
}