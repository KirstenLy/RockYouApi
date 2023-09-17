package database.internal.creator.test

import database.external.DatabaseAPI
import database.external.test.database.DatabaseAPIForTests
import database.internal.DatabaseAPIAdditionalScenariosImpl
import database.internal.creator.production.createDB
import database.internal.creator.production.createDBForTest
import database.internal.executor.test.GetAllReportsForContentExecutor
import database.internal.executor.test.GetAllStoriesNodesExecutor
import database.internal.executor.test.GetAllVotesExecutor
import database.internal.test.model.TestModelsStorage
import declaration.DatabaseConfiguration
import rockyouapi.DBTest

/** Create in a - memory database for test purposes. */
internal fun initTestDatabaseApi(dataBuilder: TestDBBuilder.() -> DatabaseAPI): DatabaseAPIForTests {
    val database = connectToDatabase()
    val mainDatabaseAPI = dataBuilder(TestDBBuilder(database))
    val additionalDatabaseAPI = createAdditionalDatabaseAPI(database)
    return DatabaseAPIForTests(mainDatabaseAPI, additionalDatabaseAPI)
}

//dbDriver=kekDriver;
//dbPassword=QeAdWsQeAdWs!2;
//dbURL=jdbc:mysql://localhost:3306/DBTest;
// dbUser=root;

//    val driver = JdbcSqliteDriver(
//        url = JdbcSqliteDriver.IN_MEMORY,
//        properties = Properties().apply { put("foreign_keys", "true") }
//    )

//    DBTest.Schema.create(driver)
//    return DBTest(driver)

internal fun connectToDatabase(): DBTest {
    val db = createDB(
        connectionConfig = DatabaseConfiguration(
            url = "jdbc:mysql://localhost:3306/DBTest",
            driver = "kekDriver",
            user = "root",
            password = "QeAdWsQeAdWs!2",
            isNeedToDropTablesAndFillByMocks = true
        )
    )

    return db
}

internal fun connectToDatabaseForTest(): Pair<DBTest, TestModelsStorage> {
    val db = createDBForTest(
        connectionConfig = DatabaseConfiguration(
            url = "jdbc:mysql://localhost:3306/DBTest",
            driver = "kekDriver",
            user = "root",
            password = "QeAdWsQeAdWs!2",
            isNeedToDropTablesAndFillByMocks = true
        )
    )

    return db
}


private fun createAdditionalDatabaseAPI(database: DBTest) = DatabaseAPIAdditionalScenariosImpl(
    getAllReportsForContentExecutor = GetAllReportsForContentExecutor(database),
    getAllVotesExecutor = GetAllVotesExecutor(database),
    getAllStoriesNodesExecutor = GetAllStoriesNodesExecutor(database)
)