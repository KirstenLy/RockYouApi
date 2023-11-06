package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetAuthorListForContentListTestData
import database.data.GetAuthorListForContentListTestDataWithExpectedAuthorList
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadAuthorsForMultipleContentExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asDataOrFailed
import database.external.model.Author
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadAuthorsForMultipleContentExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadAuthorsForMultipleContentExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadAuthorsForMultipleContentExecutor(database)
    }

    @Test
    fun execute_with_empty_content_id_list_return_data_with_empty_result() {
        runTest {
            val actualResult = executor.execute(emptyList()).asDataOrFailed()
            assertEquals(emptyMap<Int, List<Author>>(), actualResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetAuthorListForContentListTestDataStreamCreator#notExistedContentListScenarioTestData")
    fun execute_with_non_existed_content_id_list_return_data_with_empty_result(testData: GetAuthorListForContentListTestData) {
        runTest {
            val actualResult = executor.execute(testData.contentIDList).asDataOrFailed()
            assertEquals(emptyMap<Int, List<Author>>(), actualResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetAuthorListForContentListTestDataStreamCreator#existedContentIDScenarioTestData")
    fun execute_with_existed_content_id_list_return_correct_filled_data_result(testData: GetAuthorListForContentListTestDataWithExpectedAuthorList) {
        runTest {
            val actualResult = executor.execute(testData.contentIDList).asDataOrFailed()
            assertEquals(testData.expectedAuthorListMap, actualResult)
        }
    }
}