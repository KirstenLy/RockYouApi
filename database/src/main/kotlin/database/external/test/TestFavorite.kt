package database.external.test

/**
 * Favorite model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestFavorite(val id: Int, val userID: Int, val contentID: Int)