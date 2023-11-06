package database.external.filter

/** @see database.external.contract.ProductionDatabaseAPI.getStoryByID */
data class StoryByIDFilter(
    val storyID: Int,
    val environmentLangID: Int? = null,
)