package database.external.test

/**
 * Comment model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestComment(val id: Int, val contentID: Int, val userID: Int, val userName: String, val text: String)