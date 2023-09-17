package database.external.test

/**
 * User's vote model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestVote(
    val id: Int,
    val contentID: Int,
    val userID: Int,
    val vote: Int,
)