package database.external.test

/**
 * Story model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestStory(
    val id: Int,
    val title: String,
    val languageID: Byte,
    val availableLanguagesIDs: List<Byte>,
    val authorsIDs: List<Int>?,
    val tagsIDs: List<Short>,
    val userID: Int,
    val rating: Int,
    val storyNodes: List<TestStoryNode>
)