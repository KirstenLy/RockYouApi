package database.external

import database.external.test.database.DatabaseAPIForTests
import database.external.test.database.TestDatabaseBuilder
import database.internal.creator.test.initMockDatabaseApi
import database.internal.creator.production.initProductionDatabase
import database.internal.creator.test.initTestDatabaseApi
import declaration.DatabaseConfiguration

/** API of Database feature.*/
object DatabaseFeature {

    /**
     * Init database API.
     * Connect to a filled database.
     * Don't make any migrations. It will fall by scheme mismatch.
     * */
    fun connectToProductionDatabase(connectionConfig: DatabaseConfiguration): DatabaseAPI {
        return initProductionDatabase(connectionConfig)
    }

    /**
     * Create test database and init test API.
     * It's in memory SQLite database, constructed and filled in runtime.
     * @return main [DatabaseAPI] and [database.external.test.database.DatabaseAPIAdditionalScenarios] to help read
     * records from a database for test purposes.
     * */
    fun connectToTestDatabase(dataBuilder: TestDatabaseBuilder.() -> DatabaseAPI): DatabaseAPIForTests {
        return initTestDatabaseApi(dataBuilder)
    }

    /**
     * Return mock API.
     * Don't make any connections. Respond by invalid data.
     * */
    fun getMockDatabase(): DatabaseAPI {
        return initMockDatabaseApi()
    }
}