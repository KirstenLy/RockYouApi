package database.internal.executor

import database.external.filter.StoryByIDFilter
import database.external.model.language.LanguageFull
import database.external.model.story.Story
import database.external.model.user.UserSimple
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleOptionalDataResult
import database.internal.extractAllChaptersIDs
import database.internal.indexToUserRole
import database.internal.toDeclarationScheme
import database.internal.util.resolveEnvironmentLangID
import database.internal.util.selectByIdAndEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import rockyouapi.Database
import rockyouapi.story.SelectOneStoryBaseInfo
import database.internal.model.DBStoryNode as SchemeStoryNode

/** @see execute */
internal class ReadStoryByIDExecutor(
    private val database: Database,
    private val languageList: List<LanguageFull>,
    private val readChaptersRequestExecutor: ReadChaptersExecutor,
    private val readAuthorsForSingleContentExecutor: ReadAuthorsForSingleContentExecutor,
    private val readLanguagesForSingleContentExecutor: ReadLanguagesForSingleContentExecutor,
    private val readTagsForSingleContentExecutor: ReadTagsForSingleContentExecutor,
    private val readGroupsForSingleContentExecutor: ReadGroupsForContentExecutor,
    private val readCharactersForSingleContentExecutor: ReadCharactersForSingleContentExecutor
) {

    /**
     * Read story by [StoryByIDFilter].
     *
     * Respond as:
     * - [SimpleOptionalDataResult.Data] Request finished without errors, story founded.
     * - [SimpleOptionalDataResult.DataNotFounded] Request finished without errors, story not founded.
     * - [SimpleOptionalDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(filter: StoryByIDFilter): SimpleOptionalDataResult<Story> {
        return withContext(Dispatchers.IO) {

            val baseStoryInfo = try {
                database.selectStoryQueries
                    .selectOneStoryBaseInfo(filter.storyID)
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

            val storyLanguage = baseStoryInfo
                .languageID
                .let { languageList.selectByIdAndEnv(it, environmentLangID) }

            val storyAuthorsRequestResult = readAuthorsForSingleContentExecutor.execute(baseStoryInfo.id)
            val storyAuthors = when (storyAuthorsRequestResult) {
                is SimpleListResult.Data -> storyAuthorsRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(storyAuthorsRequestResult.t)
                }
            }

            val storyLanguagesRequestResult = readLanguagesForSingleContentExecutor.execute(
                contentID = baseStoryInfo.id,
            )
            val storyLanguageList = when (storyLanguagesRequestResult) {
                is SimpleListResult.Data -> storyLanguagesRequestResult
                    .data
                    .map { languageList.selectByIdAndEnv(it, environmentLangID) }

                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(storyLanguagesRequestResult.t)
                }
            }

            val storyTagsRequestResult = readTagsForSingleContentExecutor.execute(
                contentID = baseStoryInfo.id,
                environmentID = environmentLangID
            )
            val storyTagList = when (storyTagsRequestResult) {
                is SimpleListResult.Data -> storyTagsRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(storyTagsRequestResult.t)
                }
            }


            val storyNodesScheme = try {
                Json.decodeFromString<List<SchemeStoryNode>>(baseStoryInfo.scheme)
            } catch (t: Throwable) {
                return@withContext SimpleOptionalDataResult.Error(t)
            }

            val storyChapters = try {
                val chaptersIDList = storyNodesScheme.extractAllChaptersIDs()
                val getChaptersResult = readChaptersRequestExecutor.execute(
                    chapterIDList = chaptersIDList,
                    environmentID = filter.environmentLangID
                )
                when (getChaptersResult) {
                    is SimpleListResult.Data -> getChaptersResult.data
                    is SimpleListResult.Error -> return@withContext SimpleOptionalDataResult.Error(getChaptersResult.t)
                }
            } catch (t: Throwable) {
                return@withContext SimpleOptionalDataResult.Error(t)
            }

            val storyNodes = storyNodesScheme.toDeclarationScheme(storyChapters)

            val storyGroupRequestResult = readGroupsForSingleContentExecutor.execute(baseStoryInfo.id)
            val storyGroupList = when (storyGroupRequestResult) {
                is SimpleListResult.Data -> storyGroupRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(storyGroupRequestResult.t)
                }
            }

            val storyCharactersRequestResult = readCharactersForSingleContentExecutor.execute(baseStoryInfo.id)
            val storyCharacters = when (storyCharactersRequestResult) {
                is SimpleListResult.Data -> storyCharactersRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(storyCharactersRequestResult.t)
                }
            }

            val story = Story(
                id = baseStoryInfo.id,
                title = baseStoryInfo.title,
                language = storyLanguage,
                availableLanguages = storyLanguageList,
                authors = storyAuthors,
                user = baseStoryInfo.extractUser(),
                tags = storyTagList,
                rating = baseStoryInfo.rating,
                commentsCount = baseStoryInfo.commentsCount,
                nodes = storyNodes,
                groups = storyGroupList,
                characters = storyCharacters
            )

            return@withContext SimpleOptionalDataResult.Data(story)
        }
    }

    private fun SelectOneStoryBaseInfo.extractUser() = UserSimple(
        id = userID,
        name = userName,
        role = indexToUserRole(userRole)
    )
}