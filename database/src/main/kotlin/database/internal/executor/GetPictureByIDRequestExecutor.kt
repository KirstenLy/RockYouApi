package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.PictureByIDFilter
import database.external.result.SimpleOptionalDataResult
import database.internal.AvailableLanguageModel
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.Author
import declaration.entity.Picture
import declaration.entity.Tag
import declaration.entity.User
import rockyouapi.DBTest

internal class GetPictureByIDRequestExecutor(
    private val database: DBTest,
    private val availableLanguages: List<AvailableLanguageModel>
) {

    fun execute(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {

        val basePictureInfo = try {
            database.pictureSelectSingleQueries
                .selectPictureBaseInfo(pictureID = filter.pictureID)
                .executeAsOneOrNull()
                ?: return SimpleOptionalDataResult.DataNotFounded()
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val environmentLangID = when {
            filter.environmentLangID == null -> availableLanguages.getDefaultLangID()
            availableLanguages.isLangIDSupported(filter.environmentLangID) -> filter.environmentLangID
            else -> availableLanguages.getDefaultLangID()
        }

        val pictureID = basePictureInfo.registerID

        val pictureLanguage = basePictureInfo
            .baseLanguageID
            ?.let { availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID) }

        val pictureAuthors = try {
            database.pictureSelectSingleQueries
                .selectPictureAuthors(pictureID)
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { Author(it.authorID, it.authorName) }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val pictureAvailableLanguages = try {
            database.pictureSelectSingleQueries
                .selectPictureAvailableLangIDs(pictureID)
                .executeAsList()
                .takeIfNotEmpty()
                ?.map {
                    availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID)
                }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val pictureTags = try {
            database.pictureSelectSingleQueries.selectPictureTags(
                pictureID = pictureID,
                environmentLangID = environmentLangID.toInt()
            )
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { Tag(id = it.tagID, name = it.tagTranslation) }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val picture = Picture(
            id = basePictureInfo.registerID,
            title = basePictureInfo.title,
            url = basePictureInfo.url,
            language = pictureLanguage,
            availableLanguages = pictureAvailableLanguages,
            authors = pictureAuthors,
            user = User(
                id = basePictureInfo.userID,
                name = basePictureInfo.userName
            ),
            tags = pictureTags,
            rating = basePictureInfo.rating,
            commentsCount = basePictureInfo.commentsCount
        )
        return SimpleOptionalDataResult.Data(picture)
    }
}