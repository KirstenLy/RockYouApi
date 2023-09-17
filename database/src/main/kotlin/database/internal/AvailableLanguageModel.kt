package database.internal

data class AvailableLanguageModel(
    val languageID: Byte,
    val isDefault: Boolean,
    val translations: List<LanguageTranslationModel>
)

data class LanguageTranslationModel(val languageID: Byte, val environmentID: Byte, val translation: String)