package database.external.exception

/**
 * Throw when no one language marked as "default".
 * It's strange situation, database selects will work incorrect with invalid environmentID.
 * */
class DefaultLanguageMissedException: Throwable()