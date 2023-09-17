package database.internal.executor

import common.mapListValuesNotNull
import database.external.filter.StoryListFilter
import database.external.result.SimpleDataResult
import database.external.result.SimpleListResult
import database.external.ContentType
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.story.StoryTable
import database.internal.entity.story.relation.RelationStoryAndAuthorTable
import database.internal.entity.story.relation.RelationStoryAndChapterTable
import database.internal.entity.story.relation.RelationStoryAndLanguageTable
import database.internal.entity.story.relation.RelationStoryAndTagTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.executor.common.*
import database.internal.executor.common.isLangIDSupported
import database.internal.executor.common.selectCommentsCount
import database.internal.executor.common.selectLanguages
import database.internal.executor.common.selectUsers
import database.internal.utils.applyLimitableIfNeeded
import declaration.entity.Lang
import declaration.entity.story.Story
import declaration.entity.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

/**
 * Executor to get stories.
 * One by one make several requests:
 * - Get
 * - Get
 * - Get
 * */
@Suppress("RemoveRedundantQualifierName")
internal class GetStoriesRequestExecutorLegacy(private val storyChaptersExecutor: GetChaptersRequestExecutorLegacy) {

    fun execute(filter: StoryListFilter): SimpleListResult<Story> {
        return try {
            val storyRegistersWithStoryInfo = selectStoryRegisterAndStoryInfo(filter)
            if (storyRegistersWithStoryInfo.empty()) {
                return SimpleListResult.Data(emptyList())
            }

            val storiesRegistersIDs = storyRegistersWithStoryInfo.map { it[ContentRegisterTable.id].value }
            val storiesEntityIDs = storyRegistersWithStoryInfo.map { it[ContentRegisterTable.contentID] }
            if (storiesRegistersIDs.isEmpty() || storiesEntityIDs.isEmpty()) {
                return SimpleListResult.Data(emptyList())
            }

            val storiesLanguagesIDs = storyRegistersWithStoryInfo.map { it[StoryTable.languageID].value }
            val storiesLanguages = when {
                storiesLanguagesIDs.isEmpty() -> emptyList()
                else -> selectLanguages(storiesLanguagesIDs)
            }
            val groupedStoriesLanguages = hashMapOf<Int, Lang?>().also { map ->
                storyRegistersWithStoryInfo.forEach { storyRow ->
                    val storyEntityID = storyRow[StoryTable.id].value
                    val storyLanguageID = storyRow[StoryTable.languageID].value
                    val storyLanguage = storiesLanguages.firstOrNull { lang -> lang.id == storyLanguageID.toByte() }
                    map[storyEntityID] = storyLanguage
                }
            }

            val storiesAvailableLanguages = selectAvailableLanguages(storiesEntityIDs)

            val storiesUsersIDs = storyRegistersWithStoryInfo.map { it[StoryTable.userID].value }
            val storiesUsers = when {
                storiesUsersIDs.isEmpty() -> emptyList()
                else -> selectUsers(storiesUsersIDs)
            }
            val groupedStoriesUsers = hashMapOf<Int, User?>().also { map ->
                storyRegistersWithStoryInfo.forEach { storyRow ->
                    val storyEntityID = storyRow[StoryTable.id].value
                    val storyUserID = storyRow[StoryTable.userID].value
                    val storyUser = storiesUsers.firstOrNull { user -> user.id == storyUserID }
                    map[storyEntityID] = storyUser
                }
            }

            val storiesAuthors = selectAuthors(storiesEntityIDs)

            val storiesTags = selectTags(storiesEntityIDs, filter.environmentLangID?.toInt())

            val commentsCount = selectCommentsCount(storiesEntityIDs)

            val storiesChaptersIDs = selectStoryChaptersIDs(storiesEntityIDs)
            val getStoriesChaptersResult = storyChaptersExecutor.execute(storiesChaptersIDs, null)
            val storiesChapters = when (getStoriesChaptersResult) {
                is SimpleDataResult.Data -> getStoriesChaptersResult.data
                is SimpleDataResult.Error -> hashMapOf()
            }
            val dataForResponse = storyRegistersWithStoryInfo.map {
                val storyRegisterID = it[ContentRegisterTable.id].value
                val storyEntityID = it[ContentRegisterTable.contentID]
                Story(
                    id = storyRegisterID,
                    title = it[StoryTable.title],
                    language = groupedStoriesLanguages[storyEntityID],
                    availableLanguages = storiesAvailableLanguages[storyEntityID],
                    authors = storiesAuthors[storyEntityID],
                    user = groupedStoriesUsers[storyEntityID],
                    tags = storiesTags[storyEntityID],
                    rating = it[StoryTable.rating],
                    commentsCount = commentsCount[storyEntityID]?.toLong() ?: 0,
                    chapters = storiesChapters[storyEntityID] ?: emptyList()
                )
            }

            SimpleListResult.Data(dataForResponse)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }

    private fun selectStoryRegisterAndStoryInfo(filter: StoryListFilter) = ContentRegisterTable
        .innerJoin(
            otherTable = StoryTable,
            onColumn = { contentID },
            otherColumn = { id },
        )
        .selectByContentType(ContentType.STORY)
        .andWhere { formSelectByLangCondition(filter.langID) }
        .applyLimitableIfNeeded(filter)

    private fun selectAvailableLanguages(storiesIDs: List<Int>) = RelationStoryAndLanguageTable
        .leftJoin(
            otherTable = LanguageTable,
            onColumn = { RelationStoryAndLanguageTable.languageID },
            otherColumn = { LanguageTable.id }
        )
        .select { RelationStoryAndLanguageTable.storyID inList storiesIDs }
        .groupBy { it[RelationStoryAndLanguageTable.storyID].value }
        .mapListValuesNotNull { it.tryToGetLangInfo() }

    private fun selectAuthors(storiesIDs: List<Int>) = RelationStoryAndAuthorTable
        .leftJoin(
            otherTable = AuthorTable,
            onColumn = { RelationStoryAndAuthorTable.authorID },
            otherColumn = { AuthorTable.id }
        )
        .select { RelationStoryAndAuthorTable.storyID inList storiesIDs }
        .groupBy { it[RelationStoryAndAuthorTable.storyID].value }
        .mapListValuesNotNull { it.tryToGetAuthorInfo() }

    private fun selectTags(storiesIDs: List<Int>, environmentLangID: Int?) = RelationStoryAndTagTable
        .leftJoin(
            otherTable = TagTable,
            onColumn = { RelationStoryAndTagTable.tagID },
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
        .select { RelationStoryAndTagTable.storyID inList storiesIDs }
        .andWhere {
            val envLangID = when {
                environmentLangID == null -> Language.ENGLISH.langID
                !isLangIDSupported(environmentLangID) -> Language.ENGLISH.langID
                else -> environmentLangID
            }
            TagTranslationTable.langID eq envLangID
        }
        .groupBy { it[RelationStoryAndTagTable.storyID].value }
        .mapListValuesNotNull { it.tryToGetTag() }

    private fun selectStoryChaptersIDs(storiesRegisterIDs: List<Int>) = RelationStoryAndChapterTable
        .select { RelationStoryAndChapterTable.storyID inList storiesRegisterIDs }
        .map { it[RelationStoryAndChapterTable.chapterID].value }

    private fun formSelectByLangCondition(langID: Int?) = when {
        langID == null -> Op.TRUE
        else -> StoryTable.languageID eq langID
    }
}