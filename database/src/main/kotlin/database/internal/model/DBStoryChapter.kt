package database.internal.model

/** Story chapter model to fill test database. */
internal data class DBStoryChapter(
    val id: Int,
    val storyID: Int,
    val languageID: Int,
    val availableLanguagesIDs: List<Int>,
    val userID: Int,
    val authorsIDs: List<Int>?,
    val tagsIDs: List<Int>,
    val text: String,
)