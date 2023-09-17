package database.external.model

/**
 * Story chapter model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestStoryChapter(
    val id: Int,
    val title: String,
    val languageID: Int,
    val userID: Int,
    val rating: Int,
)