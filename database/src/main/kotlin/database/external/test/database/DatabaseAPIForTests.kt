package database.external.test.database

import database.external.DatabaseAPI

/** Result of test database initialization. */
class DatabaseAPIForTests(val mainDatabaseAPI: DatabaseAPI, val additionalDatabaseAPI: DatabaseAPIAdditionalScenarios)