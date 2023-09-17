package declaration

/** Describe connection to database.*/
data class DatabaseConnectionConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String
)