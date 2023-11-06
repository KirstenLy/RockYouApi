package database.external.reader

import database.external.DatabaseConfiguration

private const val KEY_DEFAULT_DATABASE_NAME = "DATABASE_NAME"
private const val KEY_DEFAULT_DATABASE_URL = "DATABASE_URL"
private const val KEY_DEFAULT_DATABASE_DRIVER = "DATABASE_DRIVER"
private const val KEY_DEFAULT_DATABASE_USER = "DATABASE_USER"
private const val KEY_DEFAULT_DATABASE_PASSWORD = "DATABASE_PASSWORD"
private const val KEY_DEFAULT_NEED_TO_DROP_DB_AND_FILL_BY_MOCKS = "KEY_DEFAULT_NEED_TO_DROP_DB_AND_FILL_BY_MOCKS"

/** Construct [DatabaseConfiguration] by read [System.getenv]. */
fun readDatabaseConfigurationFromEnv(
    databaseNameKey: String = KEY_DEFAULT_DATABASE_NAME,
    databaseURLKey: String = KEY_DEFAULT_DATABASE_URL,
    databaseDriverKey: String = KEY_DEFAULT_DATABASE_DRIVER,
    databaseUserKey: String = KEY_DEFAULT_DATABASE_USER,
    databasePasswordKey: String = KEY_DEFAULT_DATABASE_PASSWORD,
    databaseIsNeedToDropTablesAndFillByMocks: String = KEY_DEFAULT_NEED_TO_DROP_DB_AND_FILL_BY_MOCKS,
) = DatabaseConfiguration(
    name = System.getenv(databaseNameKey).orEmpty(),
    url = System.getenv(databaseURLKey).orEmpty(),
    driver = System.getenv(databaseDriverKey).orEmpty(),
    user = System.getenv(databaseUserKey).orEmpty(),
    password = System.getenv(databasePasswordKey).orEmpty(),
    isNeedToDropTablesAndFillByMocks = System
        .getenv(databaseIsNeedToDropTablesAndFillByMocks)
        .orEmpty()
        .toBooleanStrict(),
)