package database.internal.executor

import database.external.filter.PictureByIDFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.internal.ContentType
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.picture.PictureTable
import database.internal.executor.common.selectByContentType
import declaration.entity.Picture
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin

@Suppress("RemoveRedundantQualifierName")
internal class GetPicturesByTextRequestExecutor(
    private val getPictureByIDRequestExecutorLegacy: GetPictureByIDRequestExecutorLegacy
) {

    fun execute(text: String): SimpleListResult<Picture> {
        val picturesEntityIDs = ContentRegisterTable.leftJoin(
            otherTable = PictureTable,
            onColumn = { ContentRegisterTable.contentID },
            otherColumn = { PictureTable.id }
        )
            .selectByContentType(ContentType.PICTURE)
            .andWhere { PictureTable.title like "%$text%" }
            .mapNotNull { it[ContentRegisterTable.contentID] }

        if (picturesEntityIDs.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

        val response = picturesEntityIDs.mapNotNull {
            when (val getPictureResult = getPictureByIDRequestExecutorLegacy.execute(PictureByIDFilter(it))) {
                is SimpleOptionalDataResult.Data -> getPictureResult.model
                is SimpleOptionalDataResult.DataNotFounded -> null
                is SimpleOptionalDataResult.Error -> null
            }
        }

        return SimpleListResult.Data(response)
    }
}