package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetAuthorListForContentTestData
import database.data.GetAuthorListForContentTestDataWithExpectedAuthorList
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadAuthorsForSingleContentExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.extractDataOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadAuthorsForSingleContentExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadAuthorsForSingleContentExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadAuthorsForSingleContentExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.GetAuthorListForContentTestDataStreamCreator#notExistedContentIDScenarioTestData")
    fun get_author_list_for_content_with_not_existed_content_return_empty_result(testData: GetAuthorListForContentTestData) {
        runTest {
            val actualAuthorList = executor
                .execute(testData.contentID)
                .extractDataOrFail()

            assert(actualAuthorList.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetAuthorListForContentTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_existed_content_id_return_data_with_correct_result(testData: GetAuthorListForContentTestDataWithExpectedAuthorList) {
        runTest {
            val actualAuthorListForContent = executor
                .execute(testData.contentID)
                .extractDataOrFail()
            assertEquals(testData.expectedAuthorList, actualAuthorListForContent)
        }
    }
}