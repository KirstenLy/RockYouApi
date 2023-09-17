package database.external.model

/** // TODO: Поубирать из названий часть "Model"
 * Content register model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestComment(val contentID: Int, val userID: Int, val text: String)