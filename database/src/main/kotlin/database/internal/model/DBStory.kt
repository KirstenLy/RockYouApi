package database.internal.model

/** Story model to fill test database. */
internal data class DBStory(
    val id: Int,
    val languageID: Int,
    val availableLanguagesIDs: List<Int>,
    val authorsIDs: List<Int>?,
    val tagsIDs: List<Int>,
    val userID: Int,
    val scheme: String
)