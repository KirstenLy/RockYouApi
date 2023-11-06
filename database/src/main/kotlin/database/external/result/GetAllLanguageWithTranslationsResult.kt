package database.external.result

import database.external.model.language.LanguageFull

/** @see database.internal.executor.ReadAllLanguageWithTranslationsListExecutor */
sealed interface GetAllLanguageWithTranslationsResult {

    /** Supported languages fetched. */
    class Data(val languageFullList: List<LanguageFull>) : GetAllLanguageWithTranslationsResult

    /** Languages not exists. */
    data object LanguagesNotExist : GetAllLanguageWithTranslationsResult

    /** Default language missed. */
    data object DefaultLanguageMissed : GetAllLanguageWithTranslationsResult

    /** Translations for languages not exists. */
    data object TranslationsNotExist : GetAllLanguageWithTranslationsResult

    /** Some language missed translation. */
    data object TranslationsMissed : GetAllLanguageWithTranslationsResult

    /** Error. */
    class Error(val t: Throwable) : GetAllLanguageWithTranslationsResult
}