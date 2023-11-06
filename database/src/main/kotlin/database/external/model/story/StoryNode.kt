package database.external.model.story

import database.external.model.Chapter

/** Part of [Story], used to construct story as list of object. */
sealed class StoryNode {

    /** Part of story as chapter to read.*/
    data class ChapterNode(val chapter: Chapter) : StoryNode()

    /** Part of story as option to choose next chapter.*/
    data class ForkNode(val variants: List<StoryForkVariant>) : StoryNode()
}