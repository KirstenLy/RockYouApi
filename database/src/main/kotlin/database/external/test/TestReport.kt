package database.external.test

/**
 * Report model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestReport(
    val id: Int,
    val contentID: Int,
    val userID: Int?,
    val reportText: String,
    val isClosed: Boolean,
)