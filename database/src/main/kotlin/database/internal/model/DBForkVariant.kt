package database.internal.model

import kotlinx.serialization.Serializable

@Serializable
data class DBForkVariant(val variantText: String, val nodes: List<DBStoryNode>)