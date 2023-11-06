package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetChapterTextByIDTestData
import database.data.GetChapterTextByIDWithoutExpectedValueTestData
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadChapterTextByIDExecutor
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asDataNotFoundOrFail
import database.utils.extractModelOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

/** Test of [ReadChapterTextByIDExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadChapterTextByIDExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadChapterTextByIDExecutor(database)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1000, -10, -1, 0, 100000])
    fun execute_with_non_existed_chapter_id_return_data_not_found_result(testData: Int) {
        runTest {
            executor.execute(testData).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterTextByIDTestDataStreamCreator#wrongContentScenarioTestData")
    fun execute_with_wrong_chapter_id_return_data_found_result(testData: GetChapterTextByIDWithoutExpectedValueTestData) {
        runTest {
            executor.execute(testData.chapterID).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterTextByIDTestDataStreamCreator#correctScenarioTestData")
    fun execute_with_correct_chapter_id_return_ok_with_correct_text(testData: GetChapterTextByIDTestData) {
        runTest {
            val getChapterTextResult = executor.execute(testData.chapterID).extractModelOrFail()
            assertEquals(testData.expectedChapterText, getChapterTextResult)
        }
    }
}