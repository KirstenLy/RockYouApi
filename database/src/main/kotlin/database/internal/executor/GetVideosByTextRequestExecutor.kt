package database.internal.executor

import database.external.filter.VideoByIDFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.internal.ContentType
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.video.VideoTable
import database.internal.executor.common.selectByContentType
import declaration.entity.Video
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin

@Suppress("RemoveRedundantQualifierName")
internal class GetVideosByTextRequestExecutor(
    private val getVideoByIDRequestExecutor: GetVideoByIDRequestExecutor
) {

    fun execute(text: String): SimpleListResult<Video> {
        val videosEntityIDs = ContentRegisterTable.leftJoin(
            otherTable = VideoTable,
            onColumn = { ContentRegisterTable.contentID },
            otherColumn = { VideoTable.id }
        )
            .selectByContentType(ContentType.VIDEO)
            .andWhere { VideoTable.title like "%$text%" }
            .mapNotNull { it[ContentRegisterTable.contentID] }

        if (videosEntityIDs.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

        val response = videosEntityIDs.mapNotNull {
            when (val getVideoResult = getVideoByIDRequestExecutor.execute(VideoByIDFilter(it))) {
                is SimpleOptionalDataResult.Data -> getVideoResult.model
                is SimpleOptionalDataResult.DataNotFounded -> null
                is SimpleOptionalDataResult.Error -> null
            }
        }

        return SimpleListResult.Data(response)
    }
}