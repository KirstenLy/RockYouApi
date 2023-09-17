package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.PictureByIDFilter
import database.external.filter.PictureListFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.external.ContentType
import declaration.entity.Picture
import rockyouapi.DBTest
import rockyouapi.register.SelectLastNByContentType

internal class GetPicturesRequestExecutorLegacy(
    private val database: DBTest,
    private val getPictureByIDRequestExecutor: GetPictureByIDRequestExecutor
) {

    fun execute(filter: PictureListFilter): SimpleListResult<Picture> {
        return try {
            val picturesRegisterIDs = try {
                database.contentRegisterQueries
                    .selectLastNByContentType(
                        contentType = ContentType.PICTURE.typeID.toByte(),
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

            val pictures = picturesRegisterIDs.mapNotNull {
                try {
                    getPictureByIDRequestExecutor.execute(
                        filter = PictureByIDFilter(
                            pictureID = it,
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

            SimpleListResult.Data(pictures)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }
}