package database.external.model.language

/** Translation for [LanguageFull] for concrete [environmentID]. */
data class LanguageTranslation(val languageID: Int, val environmentID: Int, val translation: String)