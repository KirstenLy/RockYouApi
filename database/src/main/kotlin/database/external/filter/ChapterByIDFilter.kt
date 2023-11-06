package database.external.filter

/** @see database.external.contract.ProductionDatabaseAPI.getStoryChapterTextByID */
data class ChapterByIDFilter(
    val chapterID: Int,
    val environmentLangID: Int? = null,
)