package database.internal.model

import kotlinx.serialization.Serializable

@Serializable
sealed class DBStoryNode {

    @Serializable
    data class Chapter(val chapter: Int) : DBStoryNode()

    @Serializable
    data class ForkNode(val variants: List<DBForkVariant>) : DBStoryNode()
}