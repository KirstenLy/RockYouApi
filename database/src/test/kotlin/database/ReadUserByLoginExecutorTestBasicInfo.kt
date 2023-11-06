package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetUserByLoginTestData
import database.data.GetUserByLoginWithoutExpectedValueTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadUserByLoginExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asDataNotFoundOrFail
import database.utils.extractModelOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadUserByLoginExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadUserByLoginExecutorTestBasicInfo {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadUserByLoginExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.GetUserByLoginTestDataStreamCreator#notExistedUserLoginScenarioTestData")
    fun execute_with_non_existed_user_name_return_user_not_found(testData: GetUserByLoginWithoutExpectedValueTestData) {
        runTest {
            executor.execute(testData.login).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetUserByLoginTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_existed_user_name_return_correct_model(testData: GetUserByLoginTestData) {
        runTest {
            val actualUser = executor.execute(testData.login).extractModelOrFail()
            assertEquals(testData.expectedUserSimple, actualUser)
        }
    }
}