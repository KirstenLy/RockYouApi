package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetTagListForContentTestData
import database.data.GetTagListForContentTestDataWithExpectedTagList
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadTagsForSingleContentExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.extractDataOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadTagsForSingleContentExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetContentTagListForExecutorTestSimple {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadTagsForSingleContentExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.GetTagListForContentTestDataStreamCreator#notExistedContentIDScenarioTestData")
    fun execute_with_non_existed_content_id_return_empty_result(testData: GetTagListForContentTestData) {
        runTest {
            val actualTagList = executor.execute(
                contentID = testData.contentID,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()

            assert(actualTagList.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetTagListForContentTestDataStreamCreator#notExistedEnvironmentIDScenarioTestData")
    fun execute_with_non_existed_environment_id_return_empty_result(testData: GetTagListForContentTestData) {
        runTest {
            val actualTagList = executor.execute(
                contentID = testData.contentID,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()

            assert(actualTagList.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetTagListForContentTestDataStreamCreator#basicScenarioTestData")
    fun execute_with_existed_content_id_return_correct_result(testData: GetTagListForContentTestDataWithExpectedTagList) {
        runTest {
            val actualTagListForContent = executor.execute(
                contentID = testData.contentID,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()

            assertEquals(testData.expectedTagSimpleList, actualTagListForContent)
        }
    }
}