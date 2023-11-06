package database.internal.executor

import database.external.filter.PictureListFilter
import database.external.model.language.LanguageFull
import database.external.model.Picture
import database.external.model.user.UserSimple
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleListResult
import database.internal.indexToUserRole
import database.internal.util.isEmptyCastedToLong
import database.internal.util.notExistedEntityIDIfEmpty
import database.internal.util.resolveEnvironmentLangID
import database.internal.util.selectByIdAndEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.picture.SelectSeveralPictureBaseInfo

/** @see execute */
internal class ReadPictureListExecutor(
    private val database: Database,
    private val languageList: List<LanguageFull>,
    private val readAuthorsForMultipleContentExecutor: ReadAuthorsForMultipleContentExecutor,
    private val readLanguagesForMultipleContentExecutor: ReadLanguagesForMultipleContentExecutor,
    private val readTagsForMultipleContentExecutor: ReadTagsForMultipleContentExecutor,
    private val readGroupsForMultipleContentExecutor: ReadGroupsForMultipleContentExecutor,
    private val readCharactersForMultipleContentExecutor: ReadCharactersForMultipleContentExecutor
) {

    /**
     * Read picture lists by their ID.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(filter: PictureListFilter): SimpleListResult<Picture> {
        return withContext(Dispatchers.IO) {

            val basePictureInfoList = try {
                database.selectPictureQueries.selectSeveralPictureBaseInfo(
                    limit = filter.limit,
                    offset = filter.offset,

                    searchWord = if (filter.searchText.isNullOrBlank()) null else "%${filter.searchText.trim()}%",

                    isLanguageListEmpty = filter.languageIDList.isEmptyCastedToLong(),
                    isAuthorListEmpty = filter.authorIDList.isEmptyCastedToLong(),
                    isIncludedTagListEmpty = filter.includedTagIDList.isEmptyCastedToLong(),
                    isExcludedTagListEmpty = filter.excludedTagIDList.isEmptyCastedToLong(),
                    isUserListEmpty = filter.userIDList.isEmptyCastedToLong(),

                    languageIDList = filter.languageIDList.notExistedEntityIDIfEmpty(),
                    authorIDList = filter.authorIDList.notExistedEntityIDIfEmpty(),
                    includedTagIDList = filter.includedTagIDList.notExistedEntityIDIfEmpty(),
                    excludedTagIDList = filter.excludedTagIDList.notExistedEntityIDIfEmpty(),
                    userIDList = filter.userIDList.notExistedEntityIDIfEmpty()
                )
                    .executeAsList()
                    .ifEmpty { return@withContext SimpleListResult.Data(emptyList()) }
            } catch (t: Throwable) {
                return@withContext SimpleListResult.Error(t)
            }

            val environmentLangID = try {
                resolveEnvironmentLangID(
                    supposedEnvironmentLangID = filter.environmentLangID,
                    availableLanguageList = languageList
                )
            } catch (t: Throwable) {
                return@withContext SimpleListResult.Error(t)
            }

            val picturesIDs = basePictureInfoList.map(SelectSeveralPictureBaseInfo::id)

            val pictureAuthorMapForContentRequestResult = readAuthorsForMultipleContentExecutor.execute(picturesIDs)
            val pictureAuthorMapForContent = when (pictureAuthorMapForContentRequestResult) {
                is SimpleDataResult.Data -> pictureAuthorMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(pictureAuthorMapForContentRequestResult.t)
                }
            }

            val pictureLanguageMapForContentRequestResult = readLanguagesForMultipleContentExecutor.execute(
                contentIDList = picturesIDs,
            )
            val pictureLanguageMapForContent = when (pictureLanguageMapForContentRequestResult) {
                is SimpleDataResult.Data -> pictureLanguageMapForContentRequestResult
                    .data
                    .mapValues { (_, languageIDList) ->
                        languageIDList.map { languageID ->
                            languageList.selectByIdAndEnv(languageID, environmentLangID)
                        }
                    }

                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(pictureLanguageMapForContentRequestResult.t)
                }
            }

            val pictureTagMapForContentRequestResult = readTagsForMultipleContentExecutor.execute(
                contentIDList = picturesIDs,
                environmentID = environmentLangID
            )
            val pictureTagMapForContent = when (pictureTagMapForContentRequestResult) {
                is SimpleDataResult.Data -> pictureTagMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(pictureTagMapForContentRequestResult.t)
                }
            }

            val pictureGroupMapForContentRequestResult = readGroupsForMultipleContentExecutor.execute(
                contentIDList = picturesIDs
            )
            val pictureGroupMapForContent = when (pictureGroupMapForContentRequestResult) {
                is SimpleDataResult.Data -> pictureGroupMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(pictureGroupMapForContentRequestResult.t)
                }
            }

            val pictureCharacterMapForContentRequestResult = readCharactersForMultipleContentExecutor.execute(
                contentIDs = picturesIDs
            )
            val pictureCharacterMapForContent = when (pictureCharacterMapForContentRequestResult) {
                is SimpleDataResult.Data -> pictureCharacterMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(pictureCharacterMapForContentRequestResult.t)
                }
            }

            val result = basePictureInfoList.map { basePictureInfo ->
                val pictureID = basePictureInfo.id
                val pictureLanguage = basePictureInfo
                    .baseLanguageID
                    ?.let { languageList.selectByIdAndEnv(it, environmentLangID) }

                Picture(
                    id = pictureID,
                    originalContentID = null,
                    title = basePictureInfo.title,
                    url = basePictureInfo.url,
                    translatorName = null,
                    language = pictureLanguage,
                    availableLanguages = pictureLanguageMapForContent[pictureID].orEmpty(),
                    authors = pictureAuthorMapForContent[pictureID].orEmpty(),
                    user = basePictureInfo.extractUser(),
                    tags = pictureTagMapForContent[pictureID].orEmpty(),
                    rating = basePictureInfo.rating,
                    commentsCount = basePictureInfo.commentsCount,
                    groups = pictureGroupMapForContent[pictureID].orEmpty(),
                    characters = pictureCharacterMapForContent[pictureID].orEmpty()
                )
            }

            return@withContext SimpleListResult.Data(result)
        }
    }

    private fun SelectSeveralPictureBaseInfo.extractUser() = UserSimple(
        id = userID,
        name = userName,
        role = indexToUserRole(userRole)
    )
}