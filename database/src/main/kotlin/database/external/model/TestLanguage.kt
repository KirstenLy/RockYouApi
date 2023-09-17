package database.external.model

/**
 * Language model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestLanguage(val id: Int, val isDefault: Boolean, val translations: List<TestLanguageTranslation>)