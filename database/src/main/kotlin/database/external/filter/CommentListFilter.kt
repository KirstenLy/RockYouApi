package database.external.filter

/** @see database.external.contract.ProductionDatabaseAPI.getComments */
data class CommentListFilter(
    val contentID: Int,
    val offset: Long,
    val limit: Long
)