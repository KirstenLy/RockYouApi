package database.internal.executor

import database.external.filter.VideoByIDFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.external.ContentType
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.video.VideoTable
import database.internal.executor.common.selectByContentType
import declaration.entity.Video
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin
import rockyouapi.DBTest

internal class GetVideosByTextRequestExecutor(
    private val database: DBTest,
    private val getVideoByIDRequestExecutor: GetVideoByIDRequestExecutor
) {

    fun execute(text: String): SimpleListResult<Video> {
        val videoEntityIDList = try {
            database.contentRegisterQueries
                .selectRegisterIDsByNameAndContentType(ContentType.VIDEO.typeID.toByte(), "%$text%")
                .executeAsList()
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        if (videoEntityIDList.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

        val response = videoEntityIDList.mapNotNull {
            when (val getVideoResult = getVideoByIDRequestExecutor.execute(VideoByIDFilter(it))) {
                is SimpleOptionalDataResult.Data -> getVideoResult.model
                is SimpleOptionalDataResult.DataNotFounded -> null
                is SimpleOptionalDataResult.Error -> null
            }
        }

        return SimpleListResult.Data(response)
    }
}