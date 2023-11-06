package database.external.filter

/** @see database.external.contract.ProductionDatabaseAPI.getVideos */
data class VideoListFilter(
    val limit: Long,
    val offset: Long,
    val environmentLangID: Int? = null,
    val searchText: String? = null,
    val languageIDList: List<Int> = emptyList(),
    val authorIDList: List<Int> = emptyList(),
    val includedTagIDList: List<Int> = emptyList(),
    val excludedTagIDList: List<Int> = emptyList(),
    val userIDList: List<Int> = emptyList()
)