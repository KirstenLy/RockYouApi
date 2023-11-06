package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.CheckRefreshTokenTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.external.result.common.SimpleBooleanResult
import database.internal.creator.createDB
import database.internal.executor.CheckRefreshTokenExecutor
import database.utils.KEY_STATIC_MAP_DB
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [CheckRefreshTokenExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CheckRefreshTokenExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy { CheckRefreshTokenExecutor(database) }

    @ParameterizedTest
    @MethodSource("database.data.CheckRefreshTokenTestDataStreamCreator#notExistedUserScenarioTestData")
    fun check_refresh_token_with_not_existed_user_return_false(testData: CheckRefreshTokenTestData) {
        runTest {
            executor.execute(
                userID = testData.userID,
                refreshToken = testData.refreshToken
            )
                .asFalseOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.CheckRefreshTokenTestDataStreamCreator#notExistedTokenScenarioTestData")
    fun check_refresh_token_with_not_existed_token_return_false(testData: CheckRefreshTokenTestData) {
        runTest {
            executor.execute(
                userID = testData.userID,
                refreshToken = testData.refreshToken
            )
                .asFalseOrFail()
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestExistedUserAndExistedTokenBasicInfo {

        @AfterAll
        internal fun clearAllUserTokens() {
            database.updateAuthQueries.clearRefreshTokenForAllUser()
        }

        @ParameterizedTest
        @MethodSource("database.data.CheckRefreshTokenTestDataStreamCreator#basicScenarioTestData")
        fun add_or_remove_vote_upvote_add_record_to_history_and_return_ok(testData: CheckRefreshTokenTestData) {
            runTest {
                database.updateAuthQueries.updateRefreshTokenForUser(
                    userID = testData.userID,
                    refreshToken = testData.refreshToken,
                )

                executor.execute(
                    userID = testData.userID,
                    refreshToken = testData.refreshToken
                )
                    .asTrueOrFail()
            }
        }
    }

    private fun SimpleBooleanResult.asFalseOrFail() {
        Assertions.assertInstanceOf(SimpleBooleanResult.False::class.java, this)
    }

    private fun SimpleBooleanResult.asTrueOrFail() {
        Assertions.assertInstanceOf(SimpleBooleanResult.True::class.java, this)
    }
}