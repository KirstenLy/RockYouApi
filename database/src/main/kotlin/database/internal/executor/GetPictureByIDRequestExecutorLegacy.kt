package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.PictureByIDFilter
import database.external.result.SimpleOptionalDataResult
import database.internal.AvailableLanguageModel
import database.external.ContentType
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.*
import rockyouapi.DBTest

/**
 * Read picture by ID from the database.
 * Return [SimpleOptionalDataResult.DataNotFounded] when:
 *
 * */
internal class GetPictureByIDRequestExecutorLegacy(
    private val database: DBTest,
    private val supportedLanguages: List<AvailableLanguageModel>
) {

    fun execute(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
        try {
            val registerInfo = database.contentRegisterSelectQueries
                .selectRegisterByID(filter.pictureID)
                .executeAsOne()

            if (registerInfo.contentType != ContentType.PICTURE.typeID.toByte()) {
                return SimpleOptionalDataResult.DataNotFounded()
            }

            val pictureInfo = database.pictureQueries.selectAllByID(registerInfo.contentID).executeAsOne()

            val pictureRegisterID = registerInfo.id
            val pictureTitle = pictureInfo.title
            val pictureURL = pictureInfo.url

            val envLangID = when {
                filter.environmentLangID == null -> supportedLanguages.getDefaultLangID()
                supportedLanguages.isLangIDSupported(filter.environmentLangID) -> filter.environmentLangID
                else -> supportedLanguages.getDefaultLangID()
            }

            val pictureLanguage = when (val pictureLangID = pictureInfo.languageID) {
                null -> null
                else -> supportedLanguages.selectByIdAndEnv(pictureLangID.toByte(), envLangID)
            }

            val pictureAvailableLanguages = try {
                emptyList<Lang>()
//                database.relationPictureAndLanguageQueries
//                    .selectAllPictureLanguagesIDs(pictureInfo.id)
//                    .executeAsList()
//                    .takeIfNotEmpty()
//                    ?.map { langID -> supportedLanguages.selectByIdAndEnv(langID, envLangID) }
            } catch (t: NullPointerException) {
                null
            }

            val pictureAuthors = try {
                emptyList<Author>()
//                database.relationPictureAndAuthorQueries
//                    .selectPictureAuthors(pictureInfo.id)
//                    .executeAsList()
//                    .takeIfNotEmpty()
//                    ?.map { Author(it.id, it.name) }
            } catch (t: NullPointerException) {
                null
            }

            val pictureUser = try {
                pictureInfo.userID
                    ?.let(database.userProdQueries::selectByID)
                    ?.executeAsOne()
                    ?.let { User(it.id, it.name) }
            } catch (t: NullPointerException) {
                null
            }

            val pictureTags = try {
                emptyList<Tag>()
//                database.relationPictureAndTagQueries
//                    .selectTagsForPicture(pictureInfo.id)
//                    .executeAsList()
//                    .takeIfNotEmpty()
//                    ?.groupBy(SelectTagsForPicture::id)
//                    ?.map { it.key to it.value.filter { it.langID == envLangID }.first().translation.orEmpty() }
//                    ?.map { Tag(it.first, it.second) }
            } catch (t: NullPointerException) {
                null
            }

            val pictureRating = pictureInfo.rating ?: 0
            val pictureCommentsCount = database.pictureQueries.countCommentsForPicture(pictureRegisterID).executeAsOne()

            val picture = Picture(
                id = pictureRegisterID,
                title = pictureTitle,
                url = pictureURL,
                language = pictureLanguage,
                availableLanguages = pictureAvailableLanguages,
                authors = pictureAuthors,
                user = pictureUser,
                tags = pictureTags,
                rating = pictureRating,
                commentsCount = pictureCommentsCount
            )

            return SimpleOptionalDataResult.Data(picture)
        } catch (t: NullPointerException) {
            return SimpleOptionalDataResult.DataNotFounded()
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }
    }
}