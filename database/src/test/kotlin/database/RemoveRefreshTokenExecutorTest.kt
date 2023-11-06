package database

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import database.data.RemoveRefreshTokenWithoutExpectedValueTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.RemoveRefreshTokenExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asOkOrFailed
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [RemoveRefreshTokenExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RemoveRefreshTokenExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        RemoveRefreshTokenExecutor(database)
    }

    @AfterEach
    fun clearAuthTable() {
        database.deleteAuthQueries.deleteAllAuthData()
    }

    @RepeatedTest(5)
    fun execute_with_non_existed_token_return_ok() {
        runTest {
            executor.execute(generateRandomTextByUUID()).asOkOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.RemoveRefreshTokenTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_existed_token_return_ok_and_token_removed(testData: RemoveRefreshTokenWithoutExpectedValueTestData) {
        runTest {
            val tokenToInsertAndRemove = generateRandomTextByUUID()
            val tokenToInsertAndRemoveOwnerID = testData.userID

            // Insert token to check it deletion on next step
            database.insertAuthQueries.insert(
                id = null,
                userID = tokenToInsertAndRemoveOwnerID,
                passwordHash = generateRandomTextByUUID(),
                refreshToken = tokenToInsertAndRemove
            )

            executor.execute(tokenToInsertAndRemove).asOkOrFailed()

            val isTokenExist = database.selectAuthQueries
                .selectRefreshTokenByUserID(tokenToInsertAndRemoveOwnerID)
                .executeAsOneOrNull()
                ?.refreshToken != null

            assertFalse(isTokenExist)
        }
    }
}