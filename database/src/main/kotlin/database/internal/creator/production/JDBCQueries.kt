package database.internal.creator.production

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.driver.jdbc.JdbcDriver
import kotlin.jvm.Throws

/** Check if table "DBVersion" exist. If not, it's parsed as the database does not exist. */
internal fun JdbcDriver.executeIsDatabaseExist(): Boolean {
    return executeQuery(
        identifier = null,
        sql = "SHOW TABLES LIKE 'DBVersion';",
        parameters = 0,
        mapper = { cursor -> QueryResult.Value(cursor.next().value) }
    ).value
}

/**
 * Read a database version from "DBVersion" table.
 * @return database version
 * @throws IllegalStateException if failed to read data.
 * */
@Throws(IllegalStateException::class)
internal fun JdbcDriver.executeGetDatabaseVersion(): Long {
    return executeQuery(
        identifier = null,
        sql = "SELECT * FROM DBVersion LIMIT 1;",
        parameters = 0,
        mapper = { cursor ->
            val isDataExist = cursor.next().value
            if (!isDataExist) throw IllegalStateException("No data in DBVersion table, or table not exist")

            val dbVersion = cursor
                .getLong(0)
                ?: throw IllegalStateException("No data fetched from DBVersion table")

            QueryResult.Value(dbVersion)
        }).value
}

/** Execute "DROP DATABASE" query */
internal fun JdbcDriver.dropDatabase(dbName: String) {
    execute(
        identifier = null,
        sql = "DROP DATABASE $dbName;",
        parameters = 0,
    ).value
}