package database.external

/** Describe connection to database.*/
data class DatabaseConfiguration(
    val name: String,
    val url: String,
    val driver: String,
    val user: String,
    val password: String,
    val isNeedToDropTablesAndFillByMocks: Boolean
)