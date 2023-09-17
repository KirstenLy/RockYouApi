package database.internal.executor

import database.external.filter.PictureByIDFilter
import database.external.result.SimpleOptionalDataResult
import database.internal.ContentType
import declaration.entity.*
import rockyouapi.DBTest
import rockyouapi.SelectPictureByID
import rockyouapi.language.Language

/**
 * Read picture by ID from the database.
 * Return [SimpleOptionalDataResult.DataNotFounded] when:
 *
 * */
internal class GetPictureByIDRequestExecutor(
    private val database: DBTest,
    private val supportedLanguages: List<Language>
) {

    fun execute(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
        try {
            val contentType = database.contentRegisterQueries
                .selectContentTypeByID(filter.pictureID)
                .executeAsOne()

            if (contentType != ContentType.PICTURE.typeID) {
                return SimpleOptionalDataResult.DataNotFounded()
            }
        } catch (t: NullPointerException) {
            return SimpleOptionalDataResult.DataNotFounded()
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        try {
            val pictureRows = database.contentRegisterQueries
                .selectPictureByID(filter.pictureID)
                .executeAsList()

            if (pictureRows.isEmpty()) {
                return SimpleOptionalDataResult.DataNotFounded()
            }

            if (pictureRows.any { it.registerID != filter.pictureID }) {
                return SimpleOptionalDataResult.DataNotFounded()
            }

            val pictureTitle = pictureRows.map(SelectPictureByID::title)
                .distinct()
                .firstOrNull()
                ?: return SimpleOptionalDataResult.DataNotFounded()

            val pictureURL = pictureRows.map(SelectPictureByID::url)
                .distinct()
                .firstOrNull()
                ?: return SimpleOptionalDataResult.DataNotFounded()

            val envLangID = when (filter.environmentLangID) {
                null -> supportedLanguages.first(Language::isDefault).id
                !in supportedLanguages.map(Language::id) -> supportedLanguages.first(Language::isDefault).id
                else -> filter.environmentLangID
            }
            val availableLanguages = pictureRows
                .filter { it.availableLanguageEnvID == envLangID }
                .map { Lang(it.availableLanguageID, it.availableLanguageTranslation) }
                .distinct()

            val pictureLanguageID = pictureRows.map(SelectPictureByID::langID)
                .distinct()
                .firstOrNull()

            val pictureLanguage = when (pictureLanguageID) {
                null -> null
                !in availableLanguages.map { it.id } -> null
                else -> availableLanguages.first { it.id == pictureLanguageID }
            }

            val pictureAuthors = pictureRows
                .map { Author(it.authorID, it.authorName) }
                .distinct()

            val pictureUser = pictureRows
                .map { User(it.userID, it.userName) }
                .distinct()
                .firstOrNull()
                ?: return SimpleOptionalDataResult.DataNotFounded()

            val pictureTags = pictureRows
                .asSequence()
                .filter { it.tagLangID == envLangID }
                .filter { it.tagTranslation != null }
                .map { Tag(it.tagID, it.tagTranslation!!) }
                .distinct()
                .toList()

            val pictureRating = pictureRows.map(SelectPictureByID::rating)
                .distinct()
                .firstOrNull()
                ?: 0

            val pictureCommentsCount = pictureRows.map(SelectPictureByID::commentsCount)
                .distinct()
                .firstOrNull()
                ?: 0

            val picture = Picture(
                id = filter.pictureID,
                title = pictureTitle,
                url = pictureURL,
                language = pictureLanguage,
                availableLanguages = availableLanguages,
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