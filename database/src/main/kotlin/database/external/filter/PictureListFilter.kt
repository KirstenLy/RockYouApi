package database.external.filter

/** @see database.external.contract.ProductionDatabaseAPI.getPictures */
data class PictureListFilter(
    val limit: Long,
    val offset: Long,
    val searchText: String? = null,
    val environmentLangID: Int? = null,
    val languageIDList: List<Int> = emptyList(),
    val authorIDList: List<Int> = emptyList(),
    val includedTagIDList: List<Int> = emptyList(),
    val excludedTagIDList: List<Int> = emptyList(),
    val userIDList: List<Int> = emptyList()
)