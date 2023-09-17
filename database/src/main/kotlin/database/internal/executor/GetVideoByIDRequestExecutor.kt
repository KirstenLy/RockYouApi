package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.VideoByIDFilter
import database.external.result.SimpleOptionalDataResult
import database.internal.AvailableLanguageModel
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.Author
import declaration.entity.Tag
import declaration.entity.User
import declaration.entity.Video
import rockyouapi.DBTest

internal class GetVideoByIDRequestExecutor(
    private val database: DBTest,
    private val availableLanguages: List<AvailableLanguageModel>
) {

    fun execute(filter: VideoByIDFilter) : SimpleOptionalDataResult<Video> {
        val baseVideoInfo = try {
            database.videoSelectQueries
                .selectVideoBaseInfo(videoID = filter.videoID)
                .executeAsOneOrNull()
                ?: return SimpleOptionalDataResult.DataNotFounded()
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val environmentLangID = when {
            filter.environmentLangID == null -> availableLanguages.getDefaultLangID()
            availableLanguages.isLangIDSupported(filter.environmentLangID) -> filter.environmentLangID
            else -> availableLanguages.getDefaultLangID()
        }

        val videoID = baseVideoInfo.registerID

        val videoLanguage = baseVideoInfo
            .baseLanguageID
            ?.let { availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID) }

        val videoAuthors = try {
            database.videoSelectQueries
                .selectVideoAuthors(videoID)
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { Author(it.authorID, it.authorName) }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val videoAvailableLanguages = try {
            database.videoSelectQueries
                .selectVideoAvailableLangIDList(videoID)
                .executeAsList()
                .takeIfNotEmpty()
                ?.map {
                    availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID)
                }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val videoTags = try {
            database.videoSelectQueries.selectVideoTags(
                videoID = videoID,
                environmentLangID = environmentLangID.toInt()
            )
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { Tag(id = it.tagID, name = it.tagTranslation) }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val video = Video(
            id = baseVideoInfo.registerID,
            title = baseVideoInfo.title,
            url = baseVideoInfo.url,
            language = videoLanguage,
            availableLanguages = videoAvailableLanguages,
            authors = videoAuthors,
            user = User(
                id = baseVideoInfo.userID,
                name = baseVideoInfo.userName
            ),
            tags = videoTags,
            rating = baseVideoInfo.rating,
            commentsCount = baseVideoInfo.commentsCount
        )
        return SimpleOptionalDataResult.Data(video)
    }
}