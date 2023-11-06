package rockyouapi.model.story

import kotlinx.serialization.Serializable
import rockyouapi.model.Chapter

/** Part of [Story], used to construct story as list of object. */
@Serializable
internal sealed class StoryNode {

    /** Part of story as chapter to read.*/
    @Serializable
    data class ChapterNode(val chapter: Chapter) : StoryNode()

    /** Part of story as option to choose next chapter.*/
    @Serializable
    data class ForkNode(val variants: List<StoryForkVariant>) : StoryNode()
}