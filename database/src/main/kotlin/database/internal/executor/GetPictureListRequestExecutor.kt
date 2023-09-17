package database.internal.executor

import common.mapListValues
import database.external.filter.PictureListFilter
import database.external.result.SimpleListResult
import database.internal.AvailableLanguageModel
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.Author
import declaration.entity.Picture
import declaration.entity.Tag
import declaration.entity.User
import rockyouapi.DBTest
import rockyouapi.picture.SelectLastBaseInfoList
import rockyouapi.picture.SelectLastBaseInfoListWithLangID

internal class GetPictureListRequestExecutor(
    private val database: DBTest,
    private val availableLanguages: List<AvailableLanguageModel>
) {

    fun execute(filter: PictureListFilter): SimpleListResult<Picture> {
        val basePictureInfoList = try {
            val basePictureRequest = if (filter.langID != null) {
                database.pictureQueries.selectLastBaseInfoListWithLangID(
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
                database.pictureQueries.selectLastBaseInfoList(
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

        val picturesIDList = basePictureInfoList.map(SelectLastBaseInfoListWithLangID::registerID)
        val pictureWithAuthorsMap = try {
            database.pictureQueries
                .selectPicturesAuthorList(picturesIDList)
                .executeAsList()
                .groupBy { it.pictureID }
                .mapListValues { Author(it.authorID, it.authorName) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val pictureWithAvailableLanguagesMap = try {
            database.pictureQueries
                .selectPicturesAvailableLanguageList(picturesIDList)
                .executeAsList()
                .groupBy { it.pictureID }
                .mapListValues {
                    availableLanguages.selectByIdAndEnv(it.abailableLanguageID.toByte(), environmentLangID)
                }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val pictureWithTagsMap = try {
            database.pictureQueries
                .selectPicturesTagList(
                    picturesIDList = picturesIDList,
                    environmentLangID = environmentLangID.toInt()
                )
                .executeAsList()
                .groupBy { it.pictureID }
                .mapListValues { Tag(id = it.tagID, name = it.tagTranslation) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val result = basePictureInfoList.map { basePictureInfo ->
            val pictureID = basePictureInfo.registerID
            val pictureLanguage = basePictureInfo
                .baseLanguageID
                ?.let { availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID) }

            Picture(
                id = basePictureInfo.registerID,
                title = basePictureInfo.title,
                url = basePictureInfo.url,
                language = pictureLanguage,
                availableLanguages = pictureWithAvailableLanguagesMap[pictureID],
                authors = pictureWithAuthorsMap[pictureID]?.sortedBy { it.id },
                user = User(
                    id = basePictureInfo.userID,
                    name = basePictureInfo.userName
                ),
                tags = pictureWithTagsMap[pictureID]?.sortedBy { it.id },
                rating = basePictureInfo.rating,
                commentsCount = basePictureInfo.commentsCount
            )
        }
        return SimpleListResult.Data(result)
    }
}