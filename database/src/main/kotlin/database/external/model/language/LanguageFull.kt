package database.external.model.language

/** Full model of supported language. */
data class LanguageFull(
    val languageID: Int,
    val isDefault: Boolean,
    val translations: List<LanguageTranslation>
)