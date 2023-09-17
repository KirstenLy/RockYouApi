package database.internal.executor

import common.mapListValuesNotNull
import database.external.result.SimpleDataResult
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.chapter.ChapterTable
import database.internal.entity.chapter.relation.RelationChapterAndAuthorTable
import database.internal.entity.chapter.relation.RelationChapterAndLanguageTable
import database.internal.entity.chapter.relation.RelationChapterAndTagTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.executor.common.*
import declaration.entity.Lang
import declaration.entity.story.Chapter
import declaration.entity.User
import org.jetbrains.exposed.sql.*

/**
 * Executor to get stories chapters.
 * One by one make several requests to database:
 * - Get
 * - Get
 * - Get
 * */
@Suppress("RemoveRedundantQualifierName")
internal class GetChaptersRequestExecutorLegacy {

    fun execute(
        chaptersRegistersIDs: List<Int>,
        environmentLangID: Int?
    ): SimpleDataResult<Map<Int, List<Chapter>>> {
        return try {
            if (chaptersRegistersIDs.isEmpty()) {
                return SimpleDataResult.Data(hashMapOf())
            }

            val storyChaptersRegistersWithChaptersInfo = selectChaptersRegistersWithChaptersInfo(chaptersRegistersIDs)
            if (storyChaptersRegistersWithChaptersInfo.empty()) {
                return SimpleDataResult.Data(hashMapOf())
            }

            val chaptersEntityIDs = storyChaptersRegistersWithChaptersInfo.map { it[ContentRegisterTable.contentID] }
            if (chaptersEntityIDs.isEmpty()) {
                return SimpleDataResult.Data(hashMapOf())
            }

            // Select languages of all chapters ang group them by chapters
            val storiesLanguagesIDs = storyChaptersRegistersWithChaptersInfo.map { it[ChapterTable.languageID].value }
            val storiesLanguages = when {
                storiesLanguagesIDs.isEmpty() -> emptyList()
                else -> selectLanguages(storiesLanguagesIDs)
            }
            val groupedChaptersLanguages = hashMapOf<Int, Lang?>().also { map ->
                storyChaptersRegistersWithChaptersInfo.forEach { chapterRow ->
                    val chapterEntityID = chapterRow[ChapterTable.id].value
                    val chapterLanguageID = chapterRow[ChapterTable.languageID].value
                    val chapterLanguage = storiesLanguages.firstOrNull { lang -> lang.id == chapterLanguageID.toByte() }
                    map[chapterEntityID] = chapterLanguage
                }
            }

            // Select available languages for all chapters. They already grouped by chapters.
            val chaptersAvailableLanguages = selectAvailableLanguages(chaptersEntityIDs)

            // Select users of all chapters ang group them by chapters
            val chaptersUsersIDs = storyChaptersRegistersWithChaptersInfo.map { it[ChapterTable.userID].value }
            val chaptersUsers = when {
                chaptersUsersIDs.isEmpty() -> emptyList()
                else -> selectUsers(chaptersUsersIDs)
            }
            val groupedChaptersUsers = hashMapOf<Int, User?>().also { map ->
                storyChaptersRegistersWithChaptersInfo.forEach { chapterRow ->
                    val chapterEntityID = chapterRow[ChapterTable.id].value
                    val chapterUserID = chapterRow[ChapterTable.userID].value
                    val chapterUser = chaptersUsers.firstOrNull { user -> user.id == chapterUserID }
                    map[chapterEntityID] = chapterUser
                }
            }

            // Select authors for all chapters. They already grouped by chapters.
            val storiesAuthors = selectAuthors(chaptersEntityIDs)

            // Select tags for all chapters. They already grouped by chapters.
            val chaptersTags = selectTags(chaptersEntityIDs, environmentLangID)

            // Select comments count for all chapters. They already grouped by chapters.
            val commentsCount = selectCommentsCount(chaptersEntityIDs)

            val allChapters = storyChaptersRegistersWithChaptersInfo.map { chapterRow ->
                val chapterRegisterID = chapterRow[ContentRegisterTable.id].value
                val chapterEntityID = chapterRow[ContentRegisterTable.contentID]
                Chapter(
                    id = chapterRegisterID,
                    storyID = 1,
                    title = chapterRow[ChapterTable.title],
                    language = groupedChaptersLanguages[chapterEntityID],
                    availableLanguages = chaptersAvailableLanguages[chapterEntityID],
                    authors = storiesAuthors[chapterEntityID],
                    user = groupedChaptersUsers[chapterEntityID],
                    tags = chaptersTags[chapterEntityID],
                    rating = chapterRow[ChapterTable.rating],
                    commentsCount = commentsCount[chapterEntityID]?.toLong() ?: 0,
                )
            }

            val dataForResponse = allChapters.groupBy { it.id }
            SimpleDataResult.Data(dataForResponse)
        } catch (t: Throwable) {
            SimpleDataResult.Error(t)
        }
    }

    private fun selectChaptersRegistersWithChaptersInfo(storyRegistersIDs: List<Int>) = ContentRegisterTable
        .innerJoin(
            otherTable = ChapterTable,
            onColumn = { contentID },
            otherColumn = { id },
        )
        .select { ContentRegisterTable.id inList storyRegistersIDs }

    private fun selectAvailableLanguages(chaptersIDs: List<Int>) = RelationChapterAndLanguageTable
        .leftJoin(
            otherTable = LanguageTable,
            onColumn = { RelationChapterAndLanguageTable.languageID },
            otherColumn = { LanguageTable.id }
        )
        .select { RelationChapterAndLanguageTable.chapterID inList chaptersIDs }
        .groupBy { it[RelationChapterAndLanguageTable.chapterID].value }
        .mapListValuesNotNull { it.tryToGetLangInfo() }

    private fun selectAuthors(chaptersIDs: List<Int>) = RelationChapterAndAuthorTable
        .leftJoin(
            otherTable = AuthorTable,
            onColumn = { RelationChapterAndAuthorTable.authorID },
            otherColumn = { AuthorTable.id }
        )
        .select { RelationChapterAndAuthorTable.chapterID inList chaptersIDs }
        .groupBy { it[RelationChapterAndAuthorTable.chapterID].value }
        .mapListValuesNotNull { it.tryToGetAuthorInfo() }

    private fun selectTags(chaptersIDs: List<Int>, environmentLangID: Int?) = RelationChapterAndTagTable
        .leftJoin(
            otherTable = TagTable,
            onColumn = { RelationChapterAndTagTable.tagID },
            otherColumn = { TagTable.id }
        )
        .leftJoin(
            otherTable = RelationTagAndTranslationTable,
            onColumn = { TagTable.id },
            otherColumn = { RelationTagAndTranslationTable.tagID }
        )
        .leftJoin(
            otherTable = TagTranslationTable,
            onColumn = { RelationTagAndTranslationTable.translationID },
            otherColumn = { TagTranslationTable.id },
        )
        .select { RelationChapterAndTagTable.chapterID inList chaptersIDs }
        .andWhere {
            val envLangID = when {
                environmentLangID == null -> Language.ENGLISH.langID
                !isLangIDSupported(environmentLangID) -> Language.ENGLISH.langID
                else -> environmentLangID
            }
            TagTranslationTable.langID eq envLangID
        }
        .groupBy { it[RelationChapterAndTagTable.chapterID].value }
        .mapListValuesNotNull { it.tryToGetTag() }
}