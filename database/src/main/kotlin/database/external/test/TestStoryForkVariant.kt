package database.external.test

import kotlinx.serialization.Serializable

@Serializable
data class TestStoryForkVariant(val variantText: String, val nodes: List<TestStoryNode>)