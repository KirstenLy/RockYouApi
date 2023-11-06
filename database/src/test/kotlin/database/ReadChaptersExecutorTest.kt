package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.model.Chapter
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadAuthorsForMultipleContentExecutor
import database.internal.executor.ReadChaptersExecutor
import database.internal.executor.ReadLanguagesForMultipleContentExecutor
import database.internal.executor.ReadTagsForMultipleContentExecutor
import database.internal.mock.allLanguagesAsSupportedLanguages
import database.utils.KEY_STATIC_MAP_DB
import database.utils.extractDataOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadChaptersExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadChaptersExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadChaptersExecutor(
            database = database,
            languageFullList = allLanguagesAsSupportedLanguages,
            readAuthorsForMultipleContentExecutor = ReadAuthorsForMultipleContentExecutor(
                database = database
            ),
            readLanguagesForContentListExecutor = ReadLanguagesForMultipleContentExecutor(
                database = database
            ),
            readTagsForContentListExecutor = ReadTagsForMultipleContentExecutor(
                database = database
            )
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkEmptyChapterIDListScenarioTestData")
    fun execute_without_chapters_return_empty_results(testData: GetChapterListWithoutExpectedValueTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = emptyList(),
                environmentID = testData.environmentID
            )
                .extractDataOrFail()

            assert(getChaptersResult.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkNotExistedChapterIDListScenarioTestData")
    fun execute_with_not_existed_chapters_return_empty_results(testData: GetChapterListWithoutExpectedValueTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()

            assert(getChaptersResult.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkWrongChapterIDListScenarioTestData")
    fun execute_with_non_existed_chapters_return_empty_results(testData: GetChapterListWithoutExpectedValueTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()

            assert(getChaptersResult.isEmpty())
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterIDListScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_id(testData: GetChapterListChapterIDTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::id)

            assertEquals(testData.expectedChapterIDList, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkTitleScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_title(testData: GetChapterListChapterTitleTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::title)

            assertEquals(testData.expectedChapterTitleList, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkStoryIDScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_story_id(testData: GetChapterListChapterStoryIDTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::storyID)

            assertEquals(testData.expectedChapterStoryIDList, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterLanguageScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_language(testData: GetChapterListChapterLanguageTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::language)

            assertEquals(testData.expectedChapterLanguageListSimple, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterAvailableLanguageScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_available_languages(testData: GetChapterListChapterAvailableLanguageTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::availableLanguages)

            assertEquals(testData.expectedChapterAvailableLanguageListSimple, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterAuthorScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_authors(testData: GetChapterListChapterAuthorTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::authors)

            assertEquals(testData.expectedChapterAuthorList, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterTagScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_tags(testData: GetChapterListChapterTagTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::tags)

            assertEquals(testData.expectedChapterTagListSimple, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterUserScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_users(testData: GetChapterListChapterUserTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::user)

            assertEquals(testData.expectedChapterUserListBasicInfo, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterRatingScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_rating(testData: GetChapterListChapterRatingTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::rating)

            assertEquals(testData.expectedChapterRating, getChaptersResult)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterListTestDataStreamCreator#checkChapterCommentCountScenarioTestData")
    fun execute_with_valid_args_return_ok_and_results_with_correct_comments_count(testData: GetChapterListChapterCommentCountTestData) {
        runTest {
            val getChaptersResult = executor.execute(
                chapterIDList = testData.chapterIDList,
                environmentID = testData.environmentID
            )
                .extractDataOrFail()
                .map(Chapter::commentsCount)
                .map(Long::toInt)

            assertEquals(testData.expectedChapterCommentCount, getChaptersResult)
        }
    }
}