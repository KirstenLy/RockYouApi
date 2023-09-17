package database.external.test

/**
 * Video model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestVideo(
    val id: Int,
    val title: String,
    val url: String,
    val languageID: Byte?,
    val availableLanguagesIDs: List<Byte>?,
    val authorsIDs: List<Int>?,
    val userID: Int,
    val tagsIDs: List<Short>?,
    val rating: Int,
)