package database.internal.executor

import database.external.filter.PictureByIDFilter
import database.external.model.language.LanguageFull
import database.external.model.Picture
import database.external.model.user.UserSimple
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleOptionalDataResult
import database.internal.indexToUserRole
import database.internal.util.resolveEnvironmentLangID
import database.internal.util.selectByIdAndEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.picture.SelectOnePictureBaseInfo

/** @see execute */
internal class ReadPictureByIDExecutor(
    private val database: Database,
    private val languageList: List<LanguageFull>,
    private val readAuthorsForSingleContentExecutor: ReadAuthorsForSingleContentExecutor,
    private val readLanguagesForSingleContentExecutor: ReadLanguagesForSingleContentExecutor,
    private val readTagsForSingleContentExecutor: ReadTagsForSingleContentExecutor,
    private val readGroupsForSingleContentExecutor: ReadGroupsForContentExecutor,
    private val readCharactersForSingleContentExecutor: ReadCharactersForSingleContentExecutor
) {

    /**
     * Get picture by [PictureByIDFilter].
     *
     * Respond as:
     * - [SimpleOptionalDataResult.Data] Request finished without errors, picture founded.
     * - [SimpleOptionalDataResult.DataNotFounded] Request finished without errors, picture not founded.
     * - [SimpleOptionalDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
        return withContext(Dispatchers.IO) {

            val basePictureInfo = try {
                database.selectPictureQueries
                    .selectOnePictureBaseInfo(filter.pictureID)
                    .executeAsOneOrNull()
                    ?: return@withContext SimpleOptionalDataResult.DataNotFounded()
            } catch (t: Throwable) {
                return@withContext SimpleOptionalDataResult.Error(t)
            }

            val environmentLangID = try {
                resolveEnvironmentLangID(
                    supposedEnvironmentLangID = filter.environmentLangID,
                    availableLanguageList = languageList
                )
            } catch (t: Throwable) {
                return@withContext SimpleOptionalDataResult.Error(t)
            }

            val pictureLanguageID = basePictureInfo.baseLanguageID
            val pictureLanguage = pictureLanguageID?.let {
                languageList.selectByIdAndEnv(it, environmentLangID)
            }

            val pictureAuthorsRequestResult = readAuthorsForSingleContentExecutor.execute(basePictureInfo.id)
            val pictureAuthors = when (pictureAuthorsRequestResult) {
                is SimpleListResult.Data -> pictureAuthorsRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(pictureAuthorsRequestResult.t)
                }
            }

            val pictureLanguagesRequestResult = readLanguagesForSingleContentExecutor.execute(
                contentID = basePictureInfo.id,
            )
            val pictureLanguageList = when (pictureLanguagesRequestResult) {
                is SimpleListResult.Data -> {
                    pictureLanguagesRequestResult
                        .data
                        .map { languageList.selectByIdAndEnv(it, environmentLangID) }
                }

                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(pictureLanguagesRequestResult.t)
                }
            }

            val pictureTagsRequestResult = readTagsForSingleContentExecutor.execute(
                contentID = basePictureInfo.id,
                environmentID = environmentLangID
            )
            val pictureTags = when (pictureTagsRequestResult) {
                is SimpleListResult.Data -> pictureTagsRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(pictureTagsRequestResult.t)
                }
            }

            val pictureGroupRequestResult = readGroupsForSingleContentExecutor.execute(basePictureInfo.id)
            val pictureGroup = when (pictureGroupRequestResult) {
                is SimpleListResult.Data -> pictureGroupRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(pictureGroupRequestResult.t)
                }
            }

            val pictureCharactersRequestResult = readCharactersForSingleContentExecutor.execute(basePictureInfo.id)
            val pictureCharacters = when (pictureCharactersRequestResult) {
                is SimpleListResult.Data -> pictureCharactersRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(pictureCharactersRequestResult.t)
                }
            }

            val picture = Picture(
                id = basePictureInfo.id,
                originalContentID = null,
                title = basePictureInfo.title,
                url = basePictureInfo.url,
                translatorName = null,
                language = pictureLanguage,
                availableLanguages = pictureLanguageList,
                authors = pictureAuthors,
                user = basePictureInfo.extractUser(),
                tags = pictureTags,
                rating = basePictureInfo.rating,
                commentsCount = basePictureInfo.commentsCount,
                groups = pictureGroup,
                characters = pictureCharacters
            )
            return@withContext SimpleOptionalDataResult.Data(picture)
        }
    }

    private fun SelectOnePictureBaseInfo.extractUser() = UserSimple(
        id = userID,
        name = userName,
        role = indexToUserRole(userRole)
    )
}