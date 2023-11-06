package database.internal.executor

import database.external.model.language.LanguageFull
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleListResult
import database.internal.util.resolveEnvironmentLangID
import database.internal.util.selectByIdAndEnv
import database.external.model.Chapter
import database.external.model.user.UserSimple
import database.internal.indexToUserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class ReadChaptersExecutor(
    private val database: Database,
    private val languageFullList: List<LanguageFull>,
    private val readAuthorsForMultipleContentExecutor: ReadAuthorsForMultipleContentExecutor,
    private val readLanguagesForContentListExecutor: ReadLanguagesForMultipleContentExecutor,
    private val readTagsForContentListExecutor: ReadTagsForMultipleContentExecutor
) {

    /**
     * Read chapter lists by their ID.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(chapterIDList: List<Int>, environmentID: Int?): SimpleListResult<Chapter> {

        if (chapterIDList.isEmpty()) return SimpleListResult.Data(emptyList())

        return withContext(Dispatchers.IO) {
            val baseChapterInfoList = try {
                database.selectChapterQueries
                    .selectChapterBaseInfoList(chapterIDList)
                    .executeAsList()
                    .ifEmpty { return@withContext SimpleListResult.Data(emptyList()) }
            } catch (t: Throwable) {
                return@withContext SimpleListResult.Error(t)
            }

            val actualEnvironmentID = try {
                resolveEnvironmentLangID(
                    supposedEnvironmentLangID = environmentID,
                    availableLanguageList = languageFullList
                )
            } catch (t: Throwable) {
                return@withContext SimpleListResult.Error(t)
            }

            val chapterAuthorMapForContentRequestResult = readAuthorsForMultipleContentExecutor.execute(chapterIDList)
            val chapterAuthorMapForContent = when (chapterAuthorMapForContentRequestResult) {
                is SimpleDataResult.Data -> chapterAuthorMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(chapterAuthorMapForContentRequestResult.t)
                }
            }

            val chapterLanguageMapForContentRequestResult = readLanguagesForContentListExecutor.execute(
                contentIDList = chapterIDList,
            )
            val chapterLanguageMapForContent = when (chapterLanguageMapForContentRequestResult) {
                is SimpleDataResult.Data -> chapterLanguageMapForContentRequestResult
                    .data
                    .mapValues { (_, languageIDList) ->
                        languageIDList.map {  languageID ->
                            languageFullList.selectByIdAndEnv(languageID, actualEnvironmentID)
                        }
                    }
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(chapterLanguageMapForContentRequestResult.t)
                }
            }

            val chapterTagMapForContentRequestResult = readTagsForContentListExecutor.execute(
                contentIDList = chapterIDList,
                environmentID = actualEnvironmentID
            )
            val chapterTagMapForContent = when (chapterTagMapForContentRequestResult) {
                is SimpleDataResult.Data -> chapterTagMapForContentRequestResult.data
                is SimpleDataResult.Error -> {
                    return@withContext SimpleListResult.Error(chapterTagMapForContentRequestResult.t)
                }
            }

            val result = baseChapterInfoList.map { baseChapterInfo ->
                val chapterID = baseChapterInfo.id
                val chapterLanguage = baseChapterInfo
                    .baseLanguageID
                    .let { languageFullList.selectByIdAndEnv(it, actualEnvironmentID) }

                Chapter(
                    id = chapterID,
                    storyID = baseChapterInfo.storyID,
                    title = baseChapterInfo.title,
                    language = chapterLanguage,
                    availableLanguages = chapterLanguageMapForContent[chapterID].orEmpty(),
                    authors = chapterAuthorMapForContent[chapterID].orEmpty(),
                    user = UserSimple(
                        id = baseChapterInfo.userID,
                        name = baseChapterInfo.userName,
                        role = indexToUserRole(baseChapterInfo.userRole)
                    ),
                    tags = chapterTagMapForContent[chapterID].orEmpty(),
                    rating = baseChapterInfo.rating,
                    commentsCount = baseChapterInfo.commentsCount
                )
            }
            return@withContext SimpleListResult.Data(result)
        }
    }
}