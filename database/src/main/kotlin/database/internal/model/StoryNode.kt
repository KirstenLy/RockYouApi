package database.internal.model

import kotlinx.serialization.Serializable

// TODO: Это нужно перепилить в TestModel, наверное
@Serializable // TODO: Переименовать в SchemeStoryNode?
sealed interface StoryNode {

    @Serializable
    data class ChapterNode(val chapter: Int) : StoryNode

    @Serializable
    data class ForkNode(val variants: List<ForkVariant>) : StoryNode
}

@Serializable
data class ForkVariant(val variantText: String, val nodes: List<StoryNode>)
