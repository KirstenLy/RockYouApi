package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.PutOrReplaceRefreshTokenWithoutExpectedValueTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.external.result.UpdateRefreshTokenResult
import database.internal.creator.createDB
import database.internal.executor.UpdateRefreshTokenExecutor
import database.utils.KEY_STATIC_MAP_DB
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [UpdateRefreshTokenExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UpdateRefreshTokenExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        UpdateRefreshTokenExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.PutOrReplaceRefreshTokenTestDataStreamCreator#notExistedUserScenarioTestData")
    fun execute_with_non_existed_user_id_return_error(testData: PutOrReplaceRefreshTokenWithoutExpectedValueTestData) {
        runTest {
            executor.execute(
                userID = testData.userID,
                refreshToken = testData.refreshToken
            )
                .asUserNotExistOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.PutOrReplaceRefreshTokenTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_existed_user_id_return_ok_and_update_or_insert_record(testData: PutOrReplaceRefreshTokenWithoutExpectedValueTestData) {
        runTest {
            val tokenOwnerID = testData.userID
            val tokenToInsert = testData.refreshToken

            executor.execute(
                userID = tokenOwnerID,
                refreshToken = tokenToInsert
            )
                .asOkOrFail()

            val actualToken = database.selectAuthQueries
                .selectRefreshTokenByUserID(tokenOwnerID)
                .executeAsOne()
                .refreshToken

            assertEquals(tokenToInsert, actualToken)
        }
    }

    private fun UpdateRefreshTokenResult.asOkOrFail() {
        Assertions.assertInstanceOf(UpdateRefreshTokenResult.Ok::class.java, this)
    }

    private fun UpdateRefreshTokenResult.asUserNotExistOrFail() {
        Assertions.assertInstanceOf(UpdateRefreshTokenResult.UserNotExists::class.java, this)
    }
}