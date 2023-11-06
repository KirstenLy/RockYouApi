package database.internal.executor

import database.external.filter.ChapterByIDFilter
import database.external.model.language.LanguageFull
import database.external.model.Chapter
import database.external.model.user.UserSimple
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleOptionalDataResult
import database.internal.indexToUserRole
import database.internal.util.resolveEnvironmentLangID
import database.internal.util.selectByIdAndEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.chapter.SelectOneChapterBaseInfo

/** @see execute */
internal class ReadChapterByIDExecutor(
    private val database: Database,
    private val languageList: List<LanguageFull>,
    private val readAuthorsForSingleContentExecutor: ReadAuthorsForSingleContentExecutor,
    private val readLanguagesForSingleContentExecutor: ReadLanguagesForSingleContentExecutor,
    private val readTagsForSingleContentExecutor: ReadTagsForSingleContentExecutor
) {

    /**
     * Read chapter by ID.
     *
     * Respond as:
     * - [SimpleOptionalDataResult.Data] Request finished without errors.
     * - [SimpleOptionalDataResult.DataNotFounded] Request finished, but chapter not founded.
     * - [SimpleOptionalDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(filter: ChapterByIDFilter): SimpleOptionalDataResult<Chapter> {
        return withContext(Dispatchers.IO) {

            val baseChapterInfo = try {
                database.selectChapterQueries
                    .selectOneChapterBaseInfo(filter.chapterID)
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

            val chapterLanguage = languageList.selectByIdAndEnv(
                languageID = baseChapterInfo.baseLanguageID,
                environmentID = environmentLangID
            )

            val chapterAuthorsRequestResult = readAuthorsForSingleContentExecutor.execute(baseChapterInfo.id)
            val chapterAuthors = when (chapterAuthorsRequestResult) {
                is SimpleListResult.Data -> chapterAuthorsRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(chapterAuthorsRequestResult.t)
                }
            }

            val chapterLanguagesRequestResult = readLanguagesForSingleContentExecutor.execute(
                contentID = baseChapterInfo.id,
            )
            val chapterLanguages = when (chapterLanguagesRequestResult) {
                is SimpleListResult.Data -> chapterLanguagesRequestResult
                    .data
                    .map { languageList.selectByIdAndEnv(it, environmentLangID) }

                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(chapterLanguagesRequestResult.t)
                }
            }

            val chapterTagsRequestResult = readTagsForSingleContentExecutor.execute(
                contentID = baseChapterInfo.id,
                environmentID = environmentLangID
            )
            val chapterTags = when (chapterTagsRequestResult) {
                is SimpleListResult.Data -> chapterTagsRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(chapterTagsRequestResult.t)
                }
            }

            val chapter = Chapter(
                id = baseChapterInfo.id,
                storyID = baseChapterInfo.storyID,
                title = baseChapterInfo.title,
                language = chapterLanguage,
                availableLanguages = chapterLanguages,
                authors = chapterAuthors,
                user = baseChapterInfo.extractUser(),
                tags = chapterTags,
                rating = baseChapterInfo.rating,
                commentsCount = baseChapterInfo.commentsCount
            )
            return@withContext SimpleOptionalDataResult.Data(chapter)
        }
    }

    private fun SelectOneChapterBaseInfo.extractUser() = UserSimple(
        id = userID,
        name = userName,
        role = indexToUserRole(userRole)
    )
}