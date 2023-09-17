package database.external.model

/**
 * Content register model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestContentRegister(
    val id: Int,
    val contentType: Int,
    val contentID: Int,
)