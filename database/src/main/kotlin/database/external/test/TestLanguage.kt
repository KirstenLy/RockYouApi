package database.external.test

/**
 * Language model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestLanguage(val id: Byte, val isDefault: Boolean, val translations: List<TestLanguageTranslation>)