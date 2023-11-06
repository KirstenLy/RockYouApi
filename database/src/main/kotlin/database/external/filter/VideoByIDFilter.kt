package database.external.filter

/** @see database.external.contract.ProductionDatabaseAPI.getVideoByID */
data class VideoByIDFilter(
    val videoID: Int,
    val environmentLangID: Int? = null,
)