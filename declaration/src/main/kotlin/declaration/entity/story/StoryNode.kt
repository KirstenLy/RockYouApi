package declaration.entity.story

import kotlinx.serialization.Serializable

@Serializable
sealed interface StoryNode {

    @Serializable
    data class ChapterNode(val chapter: Chapter) : StoryNode

    @Serializable
    data class ForkNode(val variants: List<ForkVariant>) : StoryNode
}

@Serializable
data class ForkVariant(val variantText: String, val nodes: List<StoryNode>)
