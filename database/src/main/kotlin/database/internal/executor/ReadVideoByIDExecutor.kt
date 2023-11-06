package database.internal.executor

import database.external.filter.VideoByIDFilter
import database.external.model.language.LanguageFull
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleOptionalDataResult
import database.internal.indexToUserRole
import database.internal.util.resolveEnvironmentLangID
import database.internal.util.selectByIdAndEnv
import database.external.model.user.UserSimple
import database.external.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.video.SelectOneVideoBaseInfo

/** @see execute */
internal class ReadVideoByIDExecutor(
    private val database: Database,
    private val languageList: List<LanguageFull>,
    private val readAuthorsForSingleContentExecutor: ReadAuthorsForSingleContentExecutor,
    private val readLanguagesForSingleContentExecutor: ReadLanguagesForSingleContentExecutor,
    private val readTagsForSingleContentExecutor: ReadTagsForSingleContentExecutor,
    private val readGroupsForContentExecutor: ReadGroupsForContentExecutor,
    private val readCharactersForSingleContentExecutor: ReadCharactersForSingleContentExecutor
) {

    /**
     * Get video by [VideoByIDFilter].
     *
     * Respond as:
     * - [SimpleOptionalDataResult.Data] Request finished without errors, picture founded.
     * - [SimpleOptionalDataResult.DataNotFounded] Request finished without errors, picture not founded.
     * - [SimpleOptionalDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(filter: VideoByIDFilter): SimpleOptionalDataResult<Video> {
        return withContext(Dispatchers.IO) {
            val baseVideoInfo = try {
                database.selectVideoQueries
                    .selectOneVideoBaseInfo(videoID = filter.videoID)
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

            val videoLanguage = baseVideoInfo
                .baseLanguageID
                ?.let { languageList.selectByIdAndEnv(it, environmentLangID) }

            val videoAuthorListRequestResult = readAuthorsForSingleContentExecutor.execute(baseVideoInfo.id)
            val videoAuthors = when (videoAuthorListRequestResult) {
                is SimpleListResult.Data -> videoAuthorListRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(videoAuthorListRequestResult.t)
                }
            }

            val videoLanguageListRequestResult = readLanguagesForSingleContentExecutor.execute(
                contentID = baseVideoInfo.id,
            )
            val videoLanguageList = when (videoLanguageListRequestResult) {
                is SimpleListResult.Data -> videoLanguageListRequestResult
                    .data
                    .map { languageList.selectByIdAndEnv(it, environmentLangID) }

                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(videoLanguageListRequestResult.t)
                }
            }

            val videoTagListRequestResult = readTagsForSingleContentExecutor.execute(
                contentID = baseVideoInfo.id,
                environmentID = environmentLangID
            )
            val videoTagList = when (videoTagListRequestResult) {
                is SimpleListResult.Data -> videoTagListRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(videoTagListRequestResult.t)
                }
            }

            val videoGroupRequestResult = readGroupsForContentExecutor.execute(baseVideoInfo.id)
            val videoGroupList = when (videoGroupRequestResult) {
                is SimpleListResult.Data -> videoGroupRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(videoGroupRequestResult.t)
                }
            }

            val videoCharactersRequestResult = readCharactersForSingleContentExecutor.execute(baseVideoInfo.id)
            val videoCharacters = when (videoCharactersRequestResult) {
                is SimpleListResult.Data -> videoCharactersRequestResult.data
                is SimpleListResult.Error -> {
                    return@withContext SimpleOptionalDataResult.Error(videoCharactersRequestResult.t)
                }
            }

            val video = Video(
                id = baseVideoInfo.id,
                title = baseVideoInfo.title,
                url = baseVideoInfo.url,
                language = videoLanguage,
                availableLanguages = videoLanguageList,
                authors = videoAuthors,
                user = baseVideoInfo.extractUser(),
                tags = videoTagList,
                rating = baseVideoInfo.rating,
                commentsCount = baseVideoInfo.commentsCount,
                groups = videoGroupList,
                characters = videoCharacters
            )
            return@withContext SimpleOptionalDataResult.Data(video)
        }
    }

    private fun SelectOneVideoBaseInfo.extractUser() = UserSimple(
        id = userID,
        name = userName,
        role = indexToUserRole(userRole)
    )
}