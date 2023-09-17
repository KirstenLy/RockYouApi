package database.internal.executor

import common.mapListValuesNotNull
import database.external.ContentType
import database.external.filter.StoryListFilter
import database.external.result.SimpleListResult
import database.internal.AvailableLanguageModel
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.story.StoryTable
import database.internal.entity.story.relation.RelationStoryAndAuthorTable
import database.internal.entity.story.relation.RelationStoryAndChapterTable
import database.internal.entity.story.relation.RelationStoryAndLanguageTable
import database.internal.entity.story.relation.RelationStoryAndTagTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.executor.common.*
import database.internal.utils.applyLimitableIfNeeded
import database.internal.utils.findByLangIDOrGetDefault
import database.internal.utils.getDefaultLangID
import declaration.entity.*
import declaration.entity.story.StoryNew
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import rockyouapi.DBTest
import rockyouapi.register.SelectLastNStoriesByContentType
import rockyouapi.story.SelectStoriesAvailableLanguages

/**
 * Executor to get stories.
 * One by one make several requests:
 * - Get
 * - Get
 * - Get
 * */
internal class GetStoriesRequestExecutorLegacy1(
    private val database: DBTest,
    private val supportedLanguages: List<AvailableLanguageModel>,
    private val storyChaptersExecutor: GetChaptersRequestExecutor,
    private val storyNodesRequestExecutor: GetStoryNodesRequestExecutor
) {

    suspend fun execute(filter: StoryListFilter): SimpleListResult<StoryNew> {
        return try {
            val storiesBaseInfo = try {
                database.contentRegisterQueries.selectLastNStoriesByContentType(
                    contentType = ContentType.STORY.typeID.toByte(),
                    limit = filter.limit,
                    offset = filter.offset ?: 0L
                )
                    .executeAsList()
            } catch (t: Throwable) {
                return SimpleListResult.Data(emptyList())
            }

            if (storiesBaseInfo.isEmpty()) {
                return SimpleListResult.Data(emptyList())
            }

            val storiesRegistersIDs = storiesBaseInfo.map(SelectLastNStoriesByContentType::registerID)
            val storiesEntityIDs = storiesBaseInfo.map(SelectLastNStoriesByContentType::entityID)
            if (storiesRegistersIDs.isEmpty() || storiesEntityIDs.isEmpty()) {
                return SimpleListResult.Data(emptyList())
            }

            val storiesLanguagesIDs = storiesBaseInfo.map(SelectLastNStoriesByContentType::storyLanguageID)
            val storiesLanguages = when {
                storiesLanguagesIDs.isEmpty() -> emptyList()
                else -> supportedLanguages.filter { it.languageID.toInt() in storiesLanguagesIDs }
            }
            val groupedStoriesLanguages = hashMapOf<Int, Lang>().also { map ->
                storiesBaseInfo.forEach { storyInfo ->
                    val storyEntityID = storyInfo.entityID
                    val storyLanguageID = storyInfo.storyLanguageID
                    val storyLanguage = storiesLanguages
                        .firstOrNull { lang -> lang.languageID.toInt()  == storyLanguageID }
                        ?: return SimpleListResult.Error(IllegalStateException())
                    val storyLanguageNameTranslation = storyLanguage
                        .translations
                        .findByLangIDOrGetDefault(filter.environmentLangID, supportedLanguages.getDefaultLangID())
                        .translation
                    map[storyEntityID] = Lang(storyLanguageID.toByte(), storyLanguageNameTranslation)
                }
            }

            // Select languages of all stories ang group them by stories
            val storiesAvailableLanguages = try {
                database.relationStoryAndLanguageQueries
                    .selectStoriesAvailableLanguages(storiesEntityIDs)
                    .executeAsList()
            } catch (t: Throwable) {
                return SimpleListResult.Error(t)
            }
            val groupedStoriesAvailableLanguages = hashMapOf<Int, List<Lang>>().also { map ->
                storiesBaseInfo.forEach { storyInfo ->
                    val storyEntityID = storyInfo.entityID
                    val storyAvailableLanguages = storiesAvailableLanguages
                        .asSequence()
                        .filter { it.storyID == storyEntityID }
                        .map(SelectStoriesAvailableLanguages::storyLangID)
                        .map { storyLangID -> supportedLanguages.first { it.languageID.toByte() == storyLangID.toByte() } }
                        .map {
                            it.languageID to it.translations.findByLangIDOrGetDefault(
                                filter.environmentLangID,
                                supportedLanguages.getDefaultLangID()
                            )
                        }
                        .map { Lang(it.first, it.second.translation) }
                        .toList()

                    map[storyEntityID] = storyAvailableLanguages
                }
            }

            // Select users of all stories ang group them by stories
            val storiesUsersIDs = storiesBaseInfo.map(SelectLastNStoriesByContentType::storyUserID)
            val storiesUsers = when {
                storiesUsersIDs.isEmpty() -> emptyList()
                else -> try {
                    database.userProdQueries
                        .selectByIDs(storiesUsersIDs)
                        .executeAsList()
                } catch (t: Throwable) {
                    return SimpleListResult.Error(t)
                }
            }
            val groupedStoriesUsers = hashMapOf<Int, User>().also { map ->
                storiesBaseInfo.forEach { storyInfo ->
                    val storyEntityID = storyInfo.entityID
                    val storyUserID = storyInfo.storyUserID
                    val storyUser = storiesUsers
                        .firstOrNull { user -> user.id == storyUserID }
                        ?: return SimpleListResult.Error(IllegalStateException())
                    map[storyEntityID] = User(storyUser.id, storyUser.name)
                }
            }

            // Select authors for all stories ang group them by stories
            val storiesAuthors = try {
                database.relationStoryAndAuthorQueries
                    .selectStoriesAuthors(storiesEntityIDs)
                    .executeAsList()
            } catch (t: Throwable) {
                return SimpleListResult.Error(t)
            }
            val groupedStoriesAuthors = hashMapOf<Int, List<Author>>().also { map ->
                storiesBaseInfo.forEach { storyInfo ->
                    val storyEntityID = storyInfo.entityID
                    val storyAuthors = storiesAuthors
                        .filter { it.storyID == storyEntityID }
                        .map { Author(it.authorID, it.authorName) }
                    map[storyEntityID] = storyAuthors
                }
            }

            // Select tags for all stories ang group them by stories
            val storiesTags = try {
                database.relationStoryAndTagQueries
                    .selectTagsForStories(storiesEntityIDs)
                    .executeAsList()
            } catch (t: Throwable) {
                return SimpleListResult.Error(t)
            }
            val groupedStoriesTags = hashMapOf<Int, List<Tag>>().also { map ->
                storiesBaseInfo.forEach { storyInfo ->
                    val storyEntityID = storyInfo.entityID
                    val storyTags = storiesTags
                        .filter { it.storyIDID == storyEntityID }
                        .filter { it.tagLangID.toByte() == (filter.environmentLangID ?: supportedLanguages.getDefaultLangID()) }
                        .filter { it.tagTranslation != null }
                        .map { Tag(it.tagID, it.tagTranslation!!) }
                    map[storyEntityID] = storyTags
                }
            }

            // Count comments for all stories ang group results by stories
            val commentsCount = hashMapOf<Int, Long>().also { map ->
                storiesBaseInfo.forEach { storyInfo ->
                    val storyRegisterID = storyInfo.registerID
                    val storyEntityID = storyInfo.entityID
                    val storyCommentsCount = database.storyQueries
                        .countCommentsForStory(storyRegisterID)
                        .executeAsOneOrNull()
                        ?: return SimpleListResult.Error(IllegalStateException())
                    map[storyEntityID] = storyCommentsCount
                }
            }

            val storiesChaptersIDs = selectStoryChaptersIDs(storiesEntityIDs)
            val getStoriesChaptersResult = storyChaptersExecutor.execute(storiesChaptersIDs, null)
//            val storiesChapters = when (getStoriesChaptersResult) {
//                is SimpleDataResult.Data -> getStoriesChaptersResult.data
//                is SimpleDataResult.Error -> hashMapOf()
//            }
//            val dataForResponse = storiesBaseInfo.map {
//                val storyRegisterID = it[ContentRegisterTable.id].value
//                val storyEntityID = it[ContentRegisterTable.contentID]
//                Story(
//                    id = storyRegisterID,
//                    title = it[StoryTable.title],
//                    language = groupedStoriesLanguages[storyEntityID],
//                    availableLanguages = storiesAvailableLanguages[storyEntityID],
//                    authors = storiesAuthors[storyEntityID],
//                    user = groupedStoriesUsers[storyEntityID],
//                    tags = storiesTags[storyEntityID],
//                    rating = it[StoryTable.rating],
//                    commentsCount = commentsCount[storyEntityID]?.toLong() ?: 0,
//                    chapters = storiesChapters[storyEntityID] ?: emptyList()
//                )
//            }

//            SimpleListResult.Data(dataForResponse)
            SimpleListResult.Error(IllegalStateException())
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