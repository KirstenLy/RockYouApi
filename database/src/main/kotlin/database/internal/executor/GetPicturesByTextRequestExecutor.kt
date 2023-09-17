package database.internal.executor

import database.external.ContentType
import database.external.filter.PictureByIDFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import declaration.entity.Picture
import rockyouapi.DBTest

internal class GetPicturesByTextRequestExecutor(
    private val database: DBTest,
    private val getPictureByIDRequestExecutor: GetPictureByIDRequestExecutor
) {

    fun execute(text: String): SimpleListResult<Picture> {
        val picturesEntityIDs = try {
            database.contentRegisterQueries
                .selectRegisterIDsByNameAndContentType(ContentType.PICTURE.typeID.toByte(), "%$text%")
                .executeAsList()
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        if (picturesEntityIDs.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

        val response = picturesEntityIDs.mapNotNull {
            when (val getPictureResult = getPictureByIDRequestExecutor.execute(PictureByIDFilter(it))) {
                is SimpleOptionalDataResult.Data -> getPictureResult.model
                is SimpleOptionalDataResult.DataNotFounded -> null
                is SimpleOptionalDataResult.Error -> null
            }
        }

        return SimpleListResult.Data(response)
    }
}