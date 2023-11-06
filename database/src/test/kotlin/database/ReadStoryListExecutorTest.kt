package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.GetStoryListLanguageIDTestData
import database.data.GetStoryListLimitTestData
import database.data.GetStoryListOffsetTestData
import database.external.filter.StoryListFilter
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.*
import database.internal.mock.allLanguagesAsSupportedLanguages
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asErrorOrFailed
import database.utils.extractDataOrFail
import database.external.model.story.Story
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

/** Test of [ReadStoryListExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
internal class ReadStoryListExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadStoryListExecutor(
            database = database,
            languageList = allLanguagesAsSupportedLanguages,
            readAuthorsForMultipleContentExecutor = ReadAuthorsForMultipleContentExecutor(database),
            readLanguagesForMultipleContentExecutor = ReadLanguagesForMultipleContentExecutor(database),
            readTagsForMultipleContentExecutor = ReadTagsForMultipleContentExecutor(database),
            readGroupsForMultipleContentExecutor = ReadGroupsForMultipleContentExecutor(database),
            readCharactersForMultipleContentExecutor = ReadCharactersForMultipleContentExecutor(database),
            readChaptersExecutor = ReadChaptersExecutor(
                database = database,
                languageFullList = allLanguagesAsSupportedLanguages,
                readAuthorsForMultipleContentExecutor = ReadAuthorsForMultipleContentExecutor(database),
                readLanguagesForContentListExecutor = ReadLanguagesForMultipleContentExecutor(database),
                readTagsForContentListExecutor = ReadTagsForMultipleContentExecutor(database),
            )
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryListTestDataStreamCreator#checkLimitScenarioTestData")
    fun story_list_with_valid_list_argument_return_correct_story_id(testData: GetStoryListLimitTestData) {
        runTest {
            val filter = createFilterForLimitTest(testData.limit)

            val actualStoryIDList = executor.execute(filter)
                .extractDataOrFail()
                .map(Story::id)

            assertEquals(testData.expectedStoryIDList, actualStoryIDList)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1000, -10, -1])
    fun story_list_with_invalid_limit_argument_return_error(testData: Long) {
        runTest {
            val filter = createFilterForLimitTest(testData)
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryListTestDataStreamCreator#checkOffsetScenarioTestData")
    fun story_list_with_valid_offset_argument_return_correct_story_id(testData: GetStoryListOffsetTestData) {
        runTest {
            val filter = createFilterForOffsetTest(
                offset = testData.offset,
                limit = testData.limit
            )

            val actualStoryIDList = executor.execute(filter)
                .extractDataOrFail()
                .map(Story::id)

            assertEquals(testData.expectedStoryIDList, actualStoryIDList)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1000, -10, -1])
    fun story_list_with_invalid_offset_argument_return_error(testData: Long) {
        runTest {
            val filter = createFilterForOffsetTest(testData, 1)
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryListTestDataStreamCreator#checkLanguageIDListScenarioTestData")
    fun story_list_with_valid_language_id_return_story_with_correct_id(testValidData: GetStoryListLanguageIDTestData) {
        runTest {
            val filter = createFilterForLanguageIDTest(
                languageIDList = testValidData.languageIDList,
                limit = testValidData.limit
            )

            val actualStoryIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Story::id)

            assertEquals(testValidData.expectedStoryIDList, actualStoryIDList)
        }
    }

    private fun createFilterForLimitTest(limit: Long) = StoryListFilter(
        limit = limit,
        offset = 0,
        environmentLangID = null,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForOffsetTest(offset: Long, limit: Long) = StoryListFilter(
        limit = limit,
        offset = offset,
        environmentLangID = null,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForSearchTextTest(searchText: String?, limit: Long) = StoryListFilter(
        limit = limit,
        offset = 0,
        environmentLangID = null,
        searchText = searchText,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForLanguageIDTest(languageIDList: List<Int>, limit: Long) = StoryListFilter(
        limit = limit,
        offset = 0,
        environmentLangID = null,
        searchText = null,
        languageIDList = languageIDList,
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )
}