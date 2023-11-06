package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetLanguageListForContentTestData
import database.data.GetLanguageListForContentWithExpectedValueTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadLanguagesForSingleContentExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.extractDataOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadLanguagesForSingleContentExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadLanguagesForSingleContentExecutorTestSimple {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadLanguagesForSingleContentExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.GetLanguageListForContentTestDataStreamCreator#notExistedContentIDScenarioTestData")
    fun execute_with_non_existed_content_id_return_empty_result(testData: GetLanguageListForContentTestData) {
        runTest {
            val actualLanguageList = executor.execute(
                contentID = testData.contentID,
            )
                .extractDataOrFail()

            assert(actualLanguageList.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetLanguageListForContentTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_existed_content_id_return_data_with_correct_result(testData: GetLanguageListForContentWithExpectedValueTestData) {
        runTest {
            val actualLanguageListForContent = executor.execute(
                contentID = testData.contentID,
            )
                .extractDataOrFail()

            assertEquals(testData.expectedLanguageIDList, actualLanguageListForContent)
        }
    }
}