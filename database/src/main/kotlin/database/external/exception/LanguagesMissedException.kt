package database.external.exception

/**
 * Throw when database not contain information about languages.
 * It's strange situation, database selects will work incorrect.
 * */
class LanguagesMissedException: Throwable()