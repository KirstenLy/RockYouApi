package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetLanguageListForContentListTestDataWithExpectedAuthorList
import database.data.GetLanguageListForContentListWithoutExpectedValueTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadLanguagesForMultipleContentExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asDataOrFailed
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadLanguagesForMultipleContentExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadLanguagesForMultipleContentExecutorTestSimple {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadLanguagesForMultipleContentExecutor(database)
    }

    @Test
    fun execute_with_empty_content_id_list_return_empty_result() {
        runTest {
            val actualLanguageIDMapResult = executor.execute(
                contentIDList = emptyList(),
            )
                .asDataOrFailed()

            assertEquals(emptyMap<Int, List<Int>>(), actualLanguageIDMapResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetLanguageListForContentListTestDataStreamCreator#notExistedContentListScenarioTestData")
    fun execute_with_non_existed_content_id_list_return_empty_result(testData: GetLanguageListForContentListWithoutExpectedValueTestData) {
        runTest {
            val actualLanguageIDMapResult = executor.execute(
                contentIDList = testData.contentIDList,
            )
                .asDataOrFailed()

            assertEquals(emptyMap<Int, List<Int>>(), actualLanguageIDMapResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetLanguageListForContentListTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_existed_content_id_list_return_correct_filled_result(testData: GetLanguageListForContentListTestDataWithExpectedAuthorList) {
        runTest {
            val actualLanguageIDMapResult = executor.execute(
                contentIDList = testData.contentIDList,
            )
                .asDataOrFailed()

            assertEquals(testData.expectedLanguageIDListMap, actualLanguageIDMapResult)
        }
    }
}