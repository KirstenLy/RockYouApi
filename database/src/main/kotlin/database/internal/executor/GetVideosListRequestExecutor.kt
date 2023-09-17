package database.internal.executor

import common.mapListValues
import common.takeIfNotEmpty
import database.external.filter.VideoByIDFilter
import database.external.filter.VideoListFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.external.ContentType
import database.internal.AvailableLanguageModel
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.*
import rockyouapi.DBTest
import rockyouapi.picture.SelectLastBaseInfoListWithLangID
import rockyouapi.register.SelectLastNByContentType

internal class GetVideosListRequestExecutor(
    private val database: DBTest,
    private val availableLanguages: List<AvailableLanguageModel>
) {

    fun execute(filter: VideoListFilter) : SimpleListResult<Video> {
        val baseVideoInfoList = try {
            val basePictureRequest = if (filter.langID != null) {
                database.videoSelectQueries.selectLastBaseInfoListWithLangID(
                    limit = filter.limit,
                    offset = filter.offset ?: 0,
                    langID = filter.langID,
                    mapper = { registerID, title, url, userID, userName, baseLanguageID, rating, commentsCount ->
                        SelectLastBaseInfoListWithLangID(
                            registerID = registerID,
                            title = title,
                            url = url,
                            userID = userID,
                            userName = userName,
                            baseLanguageID = baseLanguageID,
                            rating = rating,
                            commentsCount = commentsCount,
                        )

                    }
                )
            } else {
                database.videoSelectQueries.selectLastBaseInfoList(
                    limit = filter.limit,
                    offset = filter.offset ?: 0,
                    mapper = { registerID, title, url, userID, userName, baseLanguageID, rating, commentsCount ->
                        SelectLastBaseInfoListWithLangID(
                            registerID = registerID,
                            title = title,
                            url = url,
                            userID = userID,
                            userName = userName,
                            baseLanguageID = baseLanguageID,
                            rating = rating,
                            commentsCount = commentsCount,
                        )
                    }
                )
            }

            basePictureRequest
                .executeAsList()
                .ifEmpty { return SimpleListResult.Data(emptyList()) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val environmentLangID = when {
            filter.environmentLangID == null -> availableLanguages.getDefaultLangID()
            availableLanguages.isLangIDSupported(filter.environmentLangID) -> filter.environmentLangID
            else -> availableLanguages.getDefaultLangID()
        }

        val videoIDList = baseVideoInfoList.map(SelectLastBaseInfoListWithLangID::registerID)
        val videoWithAuthorsMap = try {
            database.videoSelectQueries
                .selectVideosAuthorList(videoIDList)
                .executeAsList()
                .groupBy { it.videoID }
                .mapListValues { Author(it.authorID, it.authorName) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val videoWithAvailableLanguagesMap = try {
            database.videoSelectQueries
                .selectVideosAvailableLanguageList(videoIDList)
                .executeAsList()
                .groupBy { it.videoID }
                .mapListValues {
                    availableLanguages.selectByIdAndEnv(it.abailableLanguageID.toByte(), environmentLangID)
                }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val videoWithTagsMap = try {
            database.videoSelectQueries
                .selectVideosTagList(
                    videoIDList = videoIDList,
                    environmentLangID = environmentLangID.toInt()
                )
                .executeAsList()
                .groupBy { it.videoID }
                .mapListValues { Tag(id = it.tagID, name = it.tagTranslation) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val result = baseVideoInfoList.map { baseVideoInfo ->
            val videoID = baseVideoInfo.registerID
            val videoLanguage = baseVideoInfo
                .baseLanguageID
                ?.let { availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID) }

            Video(
                id = baseVideoInfo.registerID,
                title = baseVideoInfo.title,
                url = baseVideoInfo.url,
                language = videoLanguage,
                availableLanguages = videoWithAvailableLanguagesMap[videoID],
                authors = videoWithAuthorsMap[videoID]?.sortedBy { it.id },
                user = User(
                    id = baseVideoInfo.userID,
                    name = baseVideoInfo.userName
                ),
                tags = videoWithTagsMap[videoID]?.sortedBy { it.id },
                rating = baseVideoInfo.rating,
                commentsCount = baseVideoInfo.commentsCount
            )
        }
        return SimpleListResult.Data(result)
    }
}