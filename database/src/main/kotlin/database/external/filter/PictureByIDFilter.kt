package database.external.filter

/** @see database.external.DatabaseAPI.getPictureByID */
data class PictureByIDFilter(
    val pictureID: Int,
    val environmentLangID: Int? = null,
)