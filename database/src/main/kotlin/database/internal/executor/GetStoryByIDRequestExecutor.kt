package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.StoryByIDFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.internal.AvailableLanguageModel
import database.internal.model.StoryNode as SchemeStoryNode
import database.internal.model.ForkVariant as SchemeForkVariant
import declaration.entity.story.StoryNode as DeclarationStoryNode
import declaration.entity.story.ForkVariant as DeclarationForkVariant
import database.internal.utils.getDefaultLangID
import database.internal.utils.isLangIDSupported
import database.internal.utils.selectByIdAndEnv
import declaration.entity.Author
import declaration.entity.Tag
import declaration.entity.User
import declaration.entity.story.Chapter
import declaration.entity.story.StoryNew
import kotlinx.serialization.json.Json
import rockyouapi.DBTest

internal class GetStoryByIDRequestExecutor(
    private val database: DBTest,
    private val availableLanguages: List<AvailableLanguageModel>,
    private val storyChaptersRequestExecutor: GetChaptersRequestExecutor
) {

    fun execute(filter: StoryByIDFilter): SimpleOptionalDataResult<StoryNew> {
        val baseStoryInfo = try {
            database.selectStoryQueries
                .selectStoryBaseInfo(registerID = filter.storyID)
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

        val storyID = baseStoryInfo.id

        val storyLanguage = baseStoryInfo
            .languageID
            .let { availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID) }

        val storyAuthors = try {
            database.selectStoryQueries
                .selectStoryAuthors(storyID)
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { Author(it.authorID, it.authorName) }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val storyAvailableLanguages = try {
            database.selectStoryQueries
                .selectStoryAvailableLangIDs(storyID)
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { availableLanguages.selectByIdAndEnv(it.toByte(), environmentLangID) }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val storyTags = try {
            database.selectStoryQueries.selectStoryTags(
                storyID = storyID,
                environmentLangID = environmentLangID.toInt()
            )
                .executeAsList()
                .takeIfNotEmpty()
                ?.map { Tag(id = it.tagID, name = it.tagTranslation) }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val storyNodesScheme = try {
            Json.decodeFromString<List<SchemeStoryNode>>(baseStoryInfo.scheme)
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val storyChapters = try {
            val chaptersIDList = storyNodesScheme.extractAllChaptersIDs()
            val getChaptersResult = storyChaptersRequestExecutor.execute(
                chaptersRegistersIDs = chaptersIDList,
                envID = filter.environmentLangID
            )
            when (getChaptersResult) {
                is SimpleListResult.Data -> getChaptersResult.data
                is SimpleListResult.Error -> return SimpleOptionalDataResult.Error(getChaptersResult.t)
            }
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        val storyNodes = storyNodesScheme.toDeclarationScheme(storyChapters)

        val story = StoryNew(
            id = baseStoryInfo.id,
            title = baseStoryInfo.title,
            language = storyLanguage,
            availableLanguages = storyAvailableLanguages,
            authors = storyAuthors,
            user = User(
                id = baseStoryInfo.userID,
                name = baseStoryInfo.userName
            ),
            tags = storyTags,
            rating = baseStoryInfo.rating,
            commentsCount = baseStoryInfo.commentsCount,
            nodes = storyNodes
        )

        return SimpleOptionalDataResult.Data(story)
    }

    private fun List<SchemeStoryNode>.extractAllChaptersIDs(): List<Int> {
        val chaptersIDs = mutableListOf<Int>()
        forEach { node ->
            when (node) {
                is SchemeStoryNode.ChapterNode -> chaptersIDs.add(node.chapter)
                is SchemeStoryNode.ForkNode -> chaptersIDs.addAll(node.extractAllChaptersIDs())
            }
        }
        return chaptersIDs
    }

    private fun SchemeStoryNode.ForkNode.extractAllChaptersIDs() = variants
        .map { it.nodes.extractAllChaptersIDs() }
        .flatten()

    private fun List<SchemeStoryNode>.toDeclarationScheme(chapters: List<Chapter>): List<DeclarationStoryNode> {
        val resultScheme = mutableListOf<DeclarationStoryNode>()
        forEach { node ->
            when (node) {
                is SchemeStoryNode.ChapterNode -> {
                    val chapter = chapters.first { it.id == node.chapter }
                    resultScheme.add(DeclarationStoryNode.ChapterNode(chapter))
                }

                is SchemeStoryNode.ForkNode -> {
                    val forkNode = DeclarationStoryNode.ForkNode(
                        variants = node.variants.toDeclarationForkVariants(chapters)
                    )
                    resultScheme.add(forkNode)
                }
            }
        }
        return resultScheme
    }

    private fun List<SchemeForkVariant>.toDeclarationForkVariants(chapters: List<Chapter>): List<DeclarationForkVariant> {
        val resultScheme = mutableListOf<DeclarationForkVariant>()
        forEach { forkVariant ->
            resultScheme.add(
                DeclarationForkVariant(
                    variantText = forkVariant.variantText,
                    nodes = forkVariant.nodes.toDeclarationScheme(chapters)
                )
            )
        }
        return resultScheme
    }
}