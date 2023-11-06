package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.reader.readDatabaseConfigurationFromEnv
import database.external.result.CheckUserCredentialsResult
import database.internal.creator.createDB
import database.internal.executor.CheckUserCredentialsExecutor
import database.utils.KEY_STATIC_MAP_DB
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [CheckUserCredentialsExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CheckUserCredentialsExecutorTestBasicInfo {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        CheckUserCredentialsExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.CheckUserCredentialTestDataStreamCreator#notExistedLoginScenarioTestData")
    fun check_user_credentials_with_not_existed_login_return_user_not_exist(testData: CheckUserCredentialTestData) {
        runTest {
            executor.execute(
                login = testData.login,
                password = testData.password
            )
                .asUserNotExistsOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.CheckUserCredentialTestDataStreamCreator#mismatchedPasswordScenarioTestData")
    fun check_user_credentials_with_mismatched_password_return_password_mismatch(testData: CheckUserCredentialTestData) {
        runTest {
            executor.execute(
                login = testData.login,
                password = testData.password
            )
                .asPasswordMismatchOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.CheckUserCredentialTestDataStreamCreator#basicScenarioTestData")
    fun check_user_credentials_with_valid_data_return_ok(testData: CheckUserCredentialTestData) {
        runTest {
            executor.execute(
                login = testData.login,
                password = testData.password
            )
                .asOkOrFail()
        }
    }

    private fun CheckUserCredentialsResult.asOkOrFail() {
        Assertions.assertInstanceOf(CheckUserCredentialsResult.Ok::class.java, this)
    }

    private fun CheckUserCredentialsResult.asUserNotExistsOrFail() {
        Assertions.assertInstanceOf(CheckUserCredentialsResult.UserNotExist::class.java, this)
    }

    private fun CheckUserCredentialsResult.asPasswordMismatchOrFail() {
        Assertions.assertInstanceOf(CheckUserCredentialsResult.PasswordMismatch::class.java, this)
    }
}