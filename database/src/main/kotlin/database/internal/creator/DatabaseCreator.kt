package database.internal.creator

import app.cash.sqldelight.driver.jdbc.JdbcDriver
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.mysql.cj.jdbc.MysqlDataSource
import database.external.DatabaseConfiguration
import database.external.contract.ProductionDatabaseAPI
import database.external.contract.TestDatabaseAPI
import database.internal.mock.fillFullByMockContent
import rockyouapi.Database

/** SelfDocumented. */
internal suspend fun createProductionDatabaseAPI(connectionConfig: DatabaseConfiguration): ProductionDatabaseAPI {
    val database = createDB(connectionConfig)
    return createProductionDatabaseAPI(database)
}

/** SelfDocumented. */
internal suspend fun createProductionDatabaseWithTestAPI(connectionConfig: DatabaseConfiguration): Pair<ProductionDatabaseAPI, TestDatabaseAPI> {
    val database = createDB(connectionConfig)
    val productionDatabase = createProductionDatabaseAPI(database)
    val testDatabase = createProductionDatabaseWithTestAPI(database)
    return productionDatabase to testDatabase
}

/** SelfDocumented. */
internal fun createDB(connectionConfig: DatabaseConfiguration): Database {
    val driver = connectionConfig.createJDBCDriver()

    if (connectionConfig.isNeedToDropTablesAndFillByMocks) {
        return dropDatabaseAndFillItByMocks(driver, connectionConfig.name)
    }

    val isDatabaseExist = driver.executeIsDatabaseExist()
    if (!isDatabaseExist) return createDatabase(driver)

    val currentDatabaseVersion = driver.executeGetDatabaseVersion()
    if (currentDatabaseVersion != Database.Schema.version) {
        Database.Schema.migrate(
            driver = driver,
            oldVersion = currentDatabaseVersion,
            newVersion = Database.Schema.version
        )
    }

    return Database(driver)
}

private fun DatabaseConfiguration.createJDBCDriver() = MysqlDataSource().also {
    it.createDatabaseIfNotExist = true
    it.setURL(url)
    it.user = user
    it.password = password
}
    .asJdbcDriver()

private fun dropDatabaseAndFillItByMocks(driver: JdbcDriver, dbName: String): Database {
    driver.dropDatabase(dbName)
    return createDatabase(driver).also(Database::fillFullByMockContent)
}

private fun createDatabase(driver: JdbcDriver): Database {
    Database.Schema.migrate(
        driver = driver,
        oldVersion = 1,
        newVersion = Database.Schema.version
    )
    return Database(driver)
}