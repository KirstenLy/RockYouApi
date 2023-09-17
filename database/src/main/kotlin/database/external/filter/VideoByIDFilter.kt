package database.external.filter

/** @see database.external.DatabaseAPI.getPictureByID */
data class VideoByIDFilter(
    val videoID: Int,
    val environmentLangID: Int? = null,
)