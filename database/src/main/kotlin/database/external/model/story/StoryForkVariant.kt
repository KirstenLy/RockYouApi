package database.external.model.story

/** Option to choose to continue read story. */
data class StoryForkVariant(val variantText: String, val nodes: List<StoryNode>)