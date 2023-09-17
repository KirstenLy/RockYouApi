package database.external.model

/**
 * Story chapter model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestTag(val id: Int, val translations: List<TestTagTranslation>)