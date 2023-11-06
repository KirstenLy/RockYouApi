package database.external.exception

/**
 * Throw when database not contain information about language translations.
 * It's strange situation, database selects will work incorrect.
 * */
class TranslationsMissedException: Throwable()