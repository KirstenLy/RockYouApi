package database.internal.executor

import database.external.filter.StoryListFilter
import database.external.model.language.LanguageFull
import database.external.model.story.Story
import database.external.model.user.UserSimple
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleListResult
import database.internal.extractAllChaptersIDs
import database.internal.indexToUserRole
import database.internal.model.DBStoryNode
import database.internal.toDeclarationScheme
import database.internal.util.isEmptyCastedToLong
import database.internal.util.notExistedEntityIDIfEmpty
import database.internal.util.resolveEnvironmentLangID
import database.internal.util.selectByIdAndEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import rockyouapi.Database
import rockyouapi.story.SelectSeveralStoryBaseInfo

/** @see execute */
internal class ReadStoryListExecutor(
    private val database: Database,
    private val languageList: List<LanguageFull>,
    private val readAuthorsForMultipleContentExecutor: ReadAuthorsForMultipleContentExecutor,
    private val readLanguagesForMultipleContentExecutor: ReadLanguagesForMultipleContentExecutor,
    private val readTagsForMultipleContentExecutor: ReadTagsForMultipleContentExecutor,
    private val readChaptersExecutor: ReadChaptersExecutor,
    private val readGroupsForMultipleContentExecutor: ReadGroupsForMultipleContentExecutor,
    private val readCharactersForMultipleContentExecutor: ReadCharactersForMultipleContentExecutor
) {

    /**
     * Get stories by [StoryListFilter].
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(filter: StoryListFilter): SimpleListResult<Story> {
        return withContext(Dispatchers.IO) {
            try {
                val baseStoryInfoList = try {
                    database.selectStoryQueries.selectSeveralStoryBaseInfo(
                        limit = filter.limit,
                        offset = filter.offset,

                        searchWord = if (filter.searchText.isNullOrEmpty()) null else "%${filter.searchText}%",

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

                val storyIDs = baseStoryInfoList.map(SelectSeveralStoryBaseInfo::id)

                val storyAuthorMapForContentRequestResult = readAuthorsForMultipleContentExecutor.execute(storyIDs)
                val storyAuthorMapForContent = when (storyAuthorMapForContentRequestResult) {
                    is SimpleDataResult.Data -> storyAuthorMapForContentRequestResult.data
                    is SimpleDataResult.Error -> {
                        return@withContext SimpleListResult.Error(storyAuthorMapForContentRequestResult.t)
                    }
                }

                val storyLanguageMapForContentRequestResult = readLanguagesForMultipleContentExecutor.execute(
                    contentIDList = storyIDs,
                )
                val storyLanguageMapForContent = when (storyLanguageMapForContentRequestResult) {
                    is SimpleDataResult.Data -> storyLanguageMapForContentRequestResult
                        .data
                        .mapValues { (_, languageIDList) ->
                            languageIDList.map { languageID ->
                                languageList.selectByIdAndEnv(languageID, environmentLangID)
                            }
                        }

                    is SimpleDataResult.Error -> {
                        return@withContext SimpleListResult.Error(storyLanguageMapForContentRequestResult.t)
                    }
                }

                val storyTagMapForContentRequestResult = readTagsForMultipleContentExecutor.execute(
                    contentIDList = storyIDs,
                    environmentID = environmentLangID
                )
                val storyTagMapForContent = when (storyTagMapForContentRequestResult) {
                    is SimpleDataResult.Data -> storyTagMapForContentRequestResult.data
                    is SimpleDataResult.Error -> {
                        return@withContext SimpleListResult.Error(storyTagMapForContentRequestResult.t)
                    }
                }

                val storyNodesMapForContent = baseStoryInfoList.associateBy(
                    keySelector = { it.id },
                    valueTransform = {
                        val schemeStoryNodesDB = try {
                            Json.decodeFromString<List<DBStoryNode>>(it.scheme)
                        } catch (t: Throwable) {
                            return@withContext SimpleListResult.Error(t)
                        }

                        val storyChapters = try {
                            val chaptersIDList = schemeStoryNodesDB.extractAllChaptersIDs()
                            val getChaptersResult = readChaptersExecutor.execute(
                                chapterIDList = chaptersIDList,
                                environmentID = filter.environmentLangID
                            )
                            when (getChaptersResult) {
                                is SimpleListResult.Data -> getChaptersResult.data
                                is SimpleListResult.Error -> return@withContext SimpleListResult.Error(getChaptersResult.t)
                            }
                        } catch (t: Throwable) {
                            return@withContext SimpleListResult.Error(t)
                        }

                        schemeStoryNodesDB.toDeclarationScheme(storyChapters)
                    }
                )

                val storyGroupMapForContentRequestResult = readGroupsForMultipleContentExecutor.execute(
                    contentIDList = storyIDs
                )
                val storyGroupMapForContent = when (storyGroupMapForContentRequestResult) {
                    is SimpleDataResult.Data -> storyGroupMapForContentRequestResult.data
                    is SimpleDataResult.Error -> {
                        return@withContext SimpleListResult.Error(storyGroupMapForContentRequestResult.t)
                    }
                }

                val storyCharacterMapForContentRequestResult = readCharactersForMultipleContentExecutor.execute(
                    contentIDs = storyIDs
                )
                val storyCharacterMapForContent = when (storyCharacterMapForContentRequestResult) {
                    is SimpleDataResult.Data -> storyCharacterMapForContentRequestResult.data
                    is SimpleDataResult.Error -> {
                        return@withContext SimpleListResult.Error(storyCharacterMapForContentRequestResult.t)
                    }
                }

                val result = baseStoryInfoList.map { baseStoryInfo ->
                    val storyID = baseStoryInfo.id
                    val storyLanguage = languageList.selectByIdAndEnv(
                        languageID = baseStoryInfo.baseLanguageID,
                        environmentID = environmentLangID
                    )

                    Story(
                        id = storyID,
                        title = baseStoryInfo.title,
                        language = storyLanguage,
                        availableLanguages = storyLanguageMapForContent[storyID].orEmpty(),
                        authors = storyAuthorMapForContent[storyID].orEmpty(),
                        user = baseStoryInfo.extractUser(),
                        tags = storyTagMapForContent[storyID].orEmpty(),
                        rating = baseStoryInfo.rating,
                        commentsCount = baseStoryInfo.commentsCount,
                        nodes = storyNodesMapForContent[storyID].orEmpty(),
                        groups = storyGroupMapForContent[storyID].orEmpty(),
                        characters = storyCharacterMapForContent[storyID].orEmpty()
                    )
                }
                return@withContext SimpleListResult.Data(result)
            } catch (t: Throwable) {
                return@withContext SimpleListResult.Error(t)
            }
        }
    }

    private fun SelectSeveralStoryBaseInfo.extractUser() = UserSimple(
        id = userID,
        name = userName,
        role = indexToUserRole(userRole)
    )
}