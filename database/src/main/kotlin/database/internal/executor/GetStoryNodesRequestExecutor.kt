package database.internal.executor

import database.external.result.SimpleListResult
import database.internal.AvailableLanguageModel
import declaration.entity.story.Chapter
import declaration.entity.story.ForkVariant
import declaration.entity.story.StoryNode
import kotlinx.serialization.json.Json
import rockyouapi.DBTest
import database.internal.model.StoryNode as DBStoryNode

/**
 * Executor to get chapters.
 * Not optimized well, make a lot of requests.
 * */
internal class GetStoryNodesRequestExecutor(
    private val database: DBTest,
    private val supportedLanguages: List<AvailableLanguageModel>
) {

    suspend fun execute(storyNodesAsJSON: String, environmentLangID: Byte?): SimpleListResult<StoryNode> {
        val storyNodes = try {
            Json.decodeFromString<List<DBStoryNode>>(storyNodesAsJSON)
        } catch (t: Throwable) {
            return SimpleListResult.Error(t)
        }

        throw IllegalStateException()

//        val chaptersIDs = storyNodes.extractAllChaptersIDs().distinct()
//        val chapters = when (val result = getChaptersRequestExecutor.execute(chaptersIDs, environmentLangID)) {
//            is SimpleListResult.Data -> result.data
//            is SimpleListResult.Error -> return SimpleListResult.Error(result.t)
//        }
//
//        return SimpleListResult.Data(storyNodes.toStoryNode(chapters))
    }

    private fun List<DBStoryNode>.extractAllChaptersIDs(): List<Int> {
        val chaptersIDs = mutableListOf<Int>()
        forEach { node ->
            when (node) {
                is DBStoryNode.ChapterNode -> chaptersIDs.add(node.chapter)
                is DBStoryNode.ForkNode -> chaptersIDs.addAll(node.extractAllChaptersIDs())
            }
        }
        return chaptersIDs
    }

    private fun DBStoryNode.ForkNode.extractAllChaptersIDs() = variants
        .map { it.nodes.extractAllChaptersIDs() }
        .flatten()

    private fun List<DBStoryNode>.toStoryNode(chapters: List<Chapter>): List<StoryNode> = map { node ->
        when (node) {
            is DBStoryNode.ChapterNode -> StoryNode.ChapterNode(
                chapter = chapters.first { it.id == node.chapter }
            )

            is DBStoryNode.ForkNode -> StoryNode.ForkNode(
                variants = node.variants.map {
                    ForkVariant(it.variantText, it.nodes.toStoryNode(chapters))
                }
            )
        }
    }
}