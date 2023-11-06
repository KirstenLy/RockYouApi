package database.internal.model

/** Language model to fill test database. */
internal data class DBLanguage(
    val id: Int,
    val isDefault: Boolean,
    val translations: List<DBLanguageTranslation>
)