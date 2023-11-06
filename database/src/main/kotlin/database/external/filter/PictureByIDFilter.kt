package database.external.filter

/** @see database.external.contract.ProductionDatabaseAPI.getPictureByID */
data class PictureByIDFilter(
    val pictureID: Int,
    val environmentLangID: Int? = null,
)