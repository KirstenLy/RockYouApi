package database.internal.model

/** Video model to fill test database. */
internal data class DBVideo(
    val id: Int,
    val url: String,
    val languageID: Int?,
    val availableLanguagesIDs: List<Int>?,
    val authorsIDs: List<Int>?,
    val userID: Int,
    val tagsIDs: List<Int>?,
)