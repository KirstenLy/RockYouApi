package rockyouapi.model.story

import kotlinx.serialization.Serializable

/** Option to choose to continue read story. */
@Serializable
internal data class StoryForkVariant(val variantText: String, val nodes: List<StoryNode>)