package database.external.model

/**
 * Story model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestStory(
    val id: Int,
    val title: String,
    val languageID: Int,
    val userID: Int,
    val rating: Int,
)