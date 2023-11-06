package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.filter.ChapterByIDFilter
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadAuthorsForSingleContentExecutor
import database.internal.executor.ReadChapterByIDExecutor
import database.internal.executor.ReadLanguagesForSingleContentExecutor
import database.internal.executor.ReadTagsForSingleContentExecutor
import database.internal.mock.allLanguagesAsSupportedLanguages
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asDataNotFoundOrFail
import database.utils.extractModelOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadChapterByIDExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadChapterByIDExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor: ReadChapterByIDExecutor by lazy {
        ReadChapterByIDExecutor(
            database = database,
            languageList = allLanguagesAsSupportedLanguages,
            readAuthorsForSingleContentExecutor = ReadAuthorsForSingleContentExecutor(database),
            readLanguagesForContentExecutor = ReadLanguagesForSingleContentExecutor(database),
            readTagsListForContentExecutor = ReadTagsForSingleContentExecutor(database)
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#notExistedContentScenarioTestData")
    fun chapter_by_id_with_non_existed_chapter_id_return_not_found_result(testData: GetChapterByIDTSimpleTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )
            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#wrongContentScenarioTestData")
    fun chapter_by_id_with_non_wrong_chapter_id_return_not_found_result(testData: GetChapterByIDTSimpleTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterIDScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_id(testData: GetChapterByIDWithExpectedChapterIDTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterID = executor.execute(filter)
                .extractModelOrFail()
                .id

            assertEquals(testData.expectedID, actualChapterID)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterTitleScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_title(testData: GetChapterByIDWithExpectedTitleTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterTitle = executor.execute(filter)
                .extractModelOrFail()
                .title

            assertEquals(testData.expectedTitle, actualChapterTitle)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterStoryIDScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_story_id(testData: GetChapterByIDWithExpectedStoryIDTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterStoryID = executor.execute(filter)
                .extractModelOrFail()
                .storyID

            assertEquals(testData.expectedStoryID, actualChapterStoryID)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterLanguageScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_lang(testData: GetChapterByIDWithExpectedLanguageTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterLanguage = executor.execute(filter)
                .extractModelOrFail()
                .language

            assertEquals(testData.expectedLanguageSimple, actualChapterLanguage)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterAvailableLanguageScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_available_languages(testData: GetChapterByIDWithExpectedAvailableLanguageTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterAvailableLanguageList = executor.execute(filter)
                .extractModelOrFail()
                .availableLanguages

            assertEquals(testData.expectedAvailableLanguageListSimple, actualChapterAvailableLanguageList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterAuthorsScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_authors(testData: GetChapterByIDWithExpectedAuthorsTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterAuthorList = executor.execute(filter)
                .extractModelOrFail()
                .authors

            assertEquals(testData.expectedAuthorList, actualChapterAuthorList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterTagsScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_tags(testData: GetChapterByIDWithExpectedTagsTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterTagList = executor.execute(filter)
                .extractModelOrFail()
                .tags

            assertEquals(testData.expectedTagSimpleList, actualChapterTagList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterUserScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_user(testData: GetChapterByIDWithExpectedUserTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterUser = executor.execute(filter)
                .extractModelOrFail()
                .user

            assertEquals(testData.expectedUserSimple, actualChapterUser)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterRatingScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_rating(testData: GetChapterByIDWithExpectedRatingTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterRating = executor.execute(filter)
                .extractModelOrFail()
                .rating

            assertEquals(testData.expectedRating, actualChapterRating)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetChapterByIDTestDataStreamCreator#checkChapterCommentCountScenarioTestData")
    fun execute_with_correct_chapter_id_return_chapter_with_correct_comments_count(testData: GetChapterByIDWithCommentCountTestData) {
        runTest {
            val filter = ChapterByIDFilter(
                chapterID = testData.chapterID,
                environmentLangID = testData.environmentLangID
            )

            val actualChapterCommentCount = executor.execute(filter)
                .extractModelOrFail()
                .commentsCount
                .toInt()

            assertEquals(testData.expectedCommentCount, actualChapterCommentCount)
        }
    }
}