package database.external.model

/**
 * Video model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestVideo(
    val id: Int,
    val title: String,
    val url: String,
    val languageID: Int?,
    val userID: Int,
    val rating: Int,
)