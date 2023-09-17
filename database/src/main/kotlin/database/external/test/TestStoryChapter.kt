package database.external.test

import kotlinx.serialization.Serializable

/**
 * Story chapter model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
@Serializable
data class TestStoryChapter(
    val id: Int,
    val storyID: Int,
    val title: String,
    val languageID: Byte,
    val availableLanguagesIDs: List<Byte>,
    val userID: Int,
    val authorsIDs: List<Int>?,
    val tagsIDs: List<Short>,
    val rating: Int,
    val text: String,
)