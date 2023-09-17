package database.internal.creator

import database.external.DatabaseAPI

/**
 * Database connected to real database created into file system when test start.
 * Database created, connected and filled by builder.
 * */
internal fun initTestDatabaseApi(dataBuilder: TestDBBuilder.() -> DatabaseAPI): DatabaseAPI {
    return dataBuilder(TestDBBuilder())
}