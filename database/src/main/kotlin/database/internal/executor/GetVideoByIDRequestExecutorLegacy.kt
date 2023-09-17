package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.VideoByIDFilter
import database.external.result.SimpleOptionalDataResult
import database.internal.AvailableLanguageModel
import database.external.ContentType
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.*
import rockyouapi.DBTest
import rockyouapi.video.SelectTagsForVideo

internal class GetVideoByIDRequestExecutorLegacy(
    private val database: DBTest,
    private val supportedLanguages: List<AvailableLanguageModel>
) {

    fun execute(filter: VideoByIDFilter) : SimpleOptionalDataResult<Video> {
        try {
            val registerInfo = database.contentRegisterSelectQueries
                .selectRegisterByID(filter.videoID)
                .executeAsOne()

            if (registerInfo.contentType != ContentType.VIDEO.typeID.toByte()) {
                return SimpleOptionalDataResult.DataNotFounded()
            }

            val videoInfo = database.videoQueries.selectAllByID(registerInfo.contentID).executeAsOne()

            val videoRegisterID = registerInfo.id
            val videoTitle = videoInfo.title
            val videoURL = videoInfo.url

            val envLangID = when {
                filter.environmentLangID == null -> supportedLanguages.getDefaultLangID()
                supportedLanguages.isLangIDSupported(filter.environmentLangID) -> filter.environmentLangID
                else -> supportedLanguages.getDefaultLangID()
            }

            val videoLanguage = when (val videoLangID = videoInfo.languageID) {
                null -> null
                else -> supportedLanguages.selectByIdAndEnv(videoLangID.toByte(), envLangID)
            }

            val videoAvailableLanguages = try {
                database.relationVideoAndLanguageQueries
                    .selectAllVideoLanguagesIDs(videoInfo.id)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?.map { langID -> supportedLanguages.selectByIdAndEnv(langID.toByte(), envLangID) }
            } catch (t: NullPointerException) {
                null
            }

            val videoAuthors = try {
                database.relationVideoAndAuthorQueries
                    .selectVideoAuthors(videoInfo.id)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?.map { Author(it.id, it.name) }
            } catch (t: NullPointerException) {
                null
            }

            val videoUser = try {
                videoInfo.userID
                    ?.let(database.userProdQueries::selectByID)
                    ?.executeAsOne()
                    ?.let { User(it.id, it.name) }
            } catch (t: NullPointerException) {
                null
            }

            val videoTags = try {
                database.relationVideoAndTagQueries
                    .selectTagsForVideo(videoInfo.id)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?.groupBy(SelectTagsForVideo::id)
                    ?.map { it.key to it.value.filter { it.envID.toByte() == envLangID }.first().translation.orEmpty() }
                    ?.map { Tag(it.first, it.second) }
            } catch (t: NullPointerException) {
                null
            }

            val videoRating = videoInfo.rating ?: 0
            val videoCommentsCount = database.videoQueries.countCommentsForVideo(videoRegisterID).executeAsOne()

            val video = Video(
                id = videoRegisterID,
                title = videoTitle,
                url = videoURL,
                language = videoLanguage,
                availableLanguages = videoAvailableLanguages,
                authors = videoAuthors,
                user = videoUser,
                tags = videoTags,
                rating = videoRating,
                commentsCount = videoCommentsCount
            )

            return SimpleOptionalDataResult.Data(video)
        } catch (t: NullPointerException) {
            return SimpleOptionalDataResult.DataNotFounded()
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }
    }
}