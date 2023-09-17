package database.external.filter

/** @see database.external.DatabaseAPI.getStoryByID */
data class StoryByIDFilter(
    val storyID: Int,
    val environmentLangID: Byte? = null,
)