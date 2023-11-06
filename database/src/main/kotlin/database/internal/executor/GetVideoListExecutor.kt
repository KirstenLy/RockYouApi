package database.internal.executor

import database.external.filter.VideoListFilter
import database.external.model.language.LanguageFull
import database.external.model.user.UserSimple
import database.external.model.Video
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
import rockyouapi.video.SelectSeveralVideoBaseInfo

/** @see execute */
internal class GetVideoListExecutor(
    private val database: Database,
    private val languageFullList: List<LanguageFull>,
    private val readAuthorsForMultipleContentExecutor: ReadAuthorsForMultipleContentExecutor,
    private val readLanguagesForMultipleContentExecutor: ReadLanguagesForMultipleContentExecutor,
    private val readTagsForMultipleContentExecutor: ReadTagsForMultipleContentExecutor,
    private val readGroupsForMultipleContentExecutor: ReadGroupsForMultipleContentExecutor,
    private val readCharactersForMultipleContentExecutor: ReadCharactersForMultipleContentExecutor
) {

    /**
     * Get video list by [VideoListFilter].
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(filter: VideoListFilter): SimpleListResult<Video> {
        return withContext(Dispatchers.IO) {

            val baseVideoInfoList = try {
                database.selectVideoQueries.selectSeveralVideoBaseInfo(
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
                    availableLanguageList = languageFullList
                )
            } catch (t: Throwable) {
                return@withContext SimpleListResult.Error(t)
            }

            val videoIDList = baseVideoInfoList.map(SelectSeveralVideoBaseInfo::id)

            val videoAuthorMapForContentRequestResult = readAuthorsForMultipleContentExecutor.execute(videoIDList)
            val videoAuthorMapForContent = when (videoAuthorMapForContentRequestResult) {
                is SimpleDataResult.Data -> videoAuthorMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(videoAuthorMapForContentRequestResult.t)
                }
            }

            val videoLanguageMapForContentRequestResult = readLanguagesForMultipleContentExecutor.execute(
                contentIDList = videoIDList,
            )
            val videoLanguageMapForContent = when (videoLanguageMapForContentRequestResult) {
                is SimpleDataResult.Data -> videoLanguageMapForContentRequestResult
                    .data
                    .mapValues { (_, languageIDList) ->
                        languageIDList.map { languageID ->
                            languageFullList.selectByIdAndEnv(languageID, environmentLangID)
                        }
                    }

                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(videoLanguageMapForContentRequestResult.t)
                }
            }

            val videoTagMapForContentRequestResult = readTagsForMultipleContentExecutor.execute(
                contentIDList = videoIDList,
                environmentID = environmentLangID
            )
            val videoTagMapForContent = when (videoTagMapForContentRequestResult) {
                is SimpleDataResult.Data -> videoTagMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(videoTagMapForContentRequestResult.t)
                }
            }

            val videoGroupMapForContentRequestResult = readGroupsForMultipleContentExecutor.execute(
                contentIDList = videoIDList
            )
            val videoGroupMapForContent = when (videoGroupMapForContentRequestResult) {
                is SimpleDataResult.Data -> videoGroupMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(videoGroupMapForContentRequestResult.t)
                }
            }

            val videoCharacterMapForContentRequestResult = readCharactersForMultipleContentExecutor.execute(
                contentIDs = videoIDList
            )
            val videoCharacterMapForContent = when (videoCharacterMapForContentRequestResult) {
                is SimpleDataResult.Data -> videoCharacterMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(videoCharacterMapForContentRequestResult.t)
                }
            }

            val result = baseVideoInfoList.map { baseVideoInfo ->
                val videoID = baseVideoInfo.id
                val videoLanguage = baseVideoInfo
                    .baseLanguageID
                    ?.let { languageFullList.selectByIdAndEnv(it, environmentLangID) }

                Video(
                    id = videoID,
                    title = baseVideoInfo.title,
                    url = baseVideoInfo.url,
                    language = videoLanguage,
                    availableLanguages = videoLanguageMapForContent[videoID].orEmpty(),
                    authors = videoAuthorMapForContent[videoID].orEmpty(),
                    user = baseVideoInfo.extractUser(),
                    tags = videoTagMapForContent[videoID].orEmpty(),
                    rating = baseVideoInfo.rating,
                    commentsCount = baseVideoInfo.commentsCount,
                    groups = videoGroupMapForContent[videoID].orEmpty(),
                    characters = videoCharacterMapForContent[videoID].orEmpty()
                )
            }

            return@withContext SimpleListResult.Data(result)
        }
    }

    private fun SelectSeveralVideoBaseInfo.extractUser() = UserSimple(
        id = userID,
        name = userName,
        role = indexToUserRole(userRole)
    )
}