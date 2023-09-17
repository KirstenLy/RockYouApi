package database.internal.executor

import common.mapListValues
import database.external.result.SimpleListResult
import database.internal.AvailableLanguageModel
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.Author
import declaration.entity.Tag
import declaration.entity.User
import declaration.entity.story.Chapter
import rockyouapi.DBTest

/**
 * Executor to get chapters.
 * Not optimized well, make a lot of requests.
 * */
internal class GetChaptersRequestExecutor(
    private val database: DBTest,
    private val availableLanguages: List<AvailableLanguageModel>
) {

    fun execute(
        chaptersRegistersIDs: List<Int>,
        envID: Byte?
    ): SimpleListResult<Chapter> {

        if (chaptersRegistersIDs.isEmpty()) return SimpleListResult.Data(emptyList())

        val baseChapterInfoList = try {
            database.selectChapterQueries
                .selectBaseInfoList(chaptersRegistersIDs)
                .executeAsList()
                .ifEmpty { return SimpleListResult.Data(emptyList()) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val environmentLangID = when {
            envID == null -> availableLanguages.getDefaultLangID()
            availableLanguages.isLangIDSupported(envID) -> envID
            else -> availableLanguages.getDefaultLangID()
        }

        val chapterWithAuthorsMap = try {
            database.selectChapterQueries
                .selectChaptersAuthorList(chaptersRegistersIDs)
                .executeAsList()
                .groupBy { it.chapterID }
                .mapListValues { Author(it.authorID, it.authorName) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val chapterWithAvailableLanguagesMap = try {
            database.selectChapterQueries
                .selectChaptersAvailableLanguageList(chaptersRegistersIDs)
                .executeAsList()
                .groupBy { it.chapterID }
                .mapListValues {
                    availableLanguages.selectByIdAndEnv(it.abailableLanguageID.toByte(), environmentLangID)
                }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val chapterWithTagsMap = try {
            database.selectChapterQueries.selectPicturesTagList(
                chapterIDList = chaptersRegistersIDs,
                environmentLangID = environmentLangID.toInt()
            )
                .executeAsList()
                .groupBy { it.chapterID }
                .mapListValues { Tag(id = it.tagID, name = it.tagTranslation) }
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        val result = baseChapterInfoList.map { baseChapterInfo ->
            val chapterID = baseChapterInfo.id
            val chapterLanguage = baseChapterInfo
                .baseLanguageID
                .let { availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID) }

            Chapter(
                id = chapterID,
                storyID = baseChapterInfo.parentStoryID,
                title = baseChapterInfo.title,
                language = chapterLanguage,
                availableLanguages = chapterWithAvailableLanguagesMap[chapterID],
                authors = chapterWithAuthorsMap[chapterID]?.sortedBy { it.id },
                user = User(
                    id = baseChapterInfo.userID,
                    name = baseChapterInfo.userName
                ),
                tags = chapterWithTagsMap[chapterID]?.sortedBy { it.id },
                rating = baseChapterInfo.rating,
                commentsCount = baseChapterInfo.commentsCount
            )
        }
        return SimpleListResult.Data(result)
    }
}