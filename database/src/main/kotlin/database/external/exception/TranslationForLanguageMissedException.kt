package database.external.exception

/**
 * Throw when database not contain information about translation for some language on some language.
 * Rule: every language must be translated for every language.
 * F.e if we have 50 languages, every language must have 50 translations.
 * */
class TranslationForLanguageMissedException: Throwable()