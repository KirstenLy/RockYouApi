package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.filter.StoryByIDFilter
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.*
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

/** Test of [ReadChaptersExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadStoryByIDExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        val supportedLanguageList = allLanguagesAsSupportedLanguages
        val readChaptersExecutor = ReadChaptersExecutor(
            database = database,
            languageFullList = supportedLanguageList,
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
        ReadStoryByIDExecutor(
            database = database,
            languageList = supportedLanguageList,
            readChaptersRequestExecutor = readChaptersExecutor,
            readAuthorsForSingleContentExecutor = ReadAuthorsForSingleContentExecutor(database),
            readLanguagesForSingleContentExecutor = ReadLanguagesForSingleContentExecutor(database),
            readTagsForSingleContentExecutor = ReadTagsForSingleContentExecutor(database),
            readGroupsForSingleContentExecutor = ReadGroupsForContentExecutor(database),
            readCharactersForSingleContentExecutor = ReadCharactersForSingleContentExecutor(database)
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#notExistedContentScenarioTestData")
    fun execute_with_non_existed_story_id_return_not_found_result(testData: GetStoryByIDTSimpleTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#wrongContentScenarioTestData")
    fun execute_with_wrong_story_id_return_not_found_result(testData: GetStoryByIDTSimpleTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryIDScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_id(testData: GetStoryByIDWithExpectedPictureIDTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryID = executor.execute(filter).extractModelOrFail().id
            assertEquals(testData.expectedID, actualStoryID)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryTitleScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_title(testData: GetStoryByIDWithExpectedTitleTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryTitle = executor.execute(filter).extractModelOrFail().title
            assertEquals(testData.expectedTitle, actualStoryTitle)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryLanguageScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_language(testData: GetStoryByIDWithExpectedLanguageTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryLanguage = executor.execute(filter).extractModelOrFail().language
            assertEquals(testData.expectedLanguageSimple, actualStoryLanguage)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryAvailableLanguageScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_available_languages(testData: GetStoryByIDWithExpectedAvailableLanguageTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryAvailableLanguageList = executor.execute(filter).extractModelOrFail().availableLanguages
            assertEquals(testData.expectedAvailableLanguageListSimple, actualStoryAvailableLanguageList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryAuthorsScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_authors(testData: GetStoryByIDWithExpectedAuthorListTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryAuthorList = executor.execute(filter)
                .extractModelOrFail()
                .authors

            assertEquals(testData.expectedAuthorList, actualStoryAuthorList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryUserScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_user(testData: GetStoryByIDWithExpectedUserTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryUser = executor.execute(filter).extractModelOrFail().user
            assertEquals(testData.expectedUserSimple, actualStoryUser)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryTagsScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_tags(testData: GetStoryByIDWithExpectedTagsTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryTagList = executor.execute(filter)
                .extractModelOrFail()
                .tags

            assertEquals(testData.expectedTagSimpleList, actualStoryTagList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryRatingScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_rating(testData: GetStoryByIDWithExpectedRatingTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryRating = executor.execute(filter)
                .extractModelOrFail()
                .rating

            assertEquals(testData.expectedRating, actualStoryRating)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryCommentCountScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_comment_count(testData: GetStoryByIDWithCommentCountTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryRating = executor.execute(filter)
                .extractModelOrFail()
                .commentsCount
                .toInt()

            assertEquals(testData.expectedCommentCount, actualStoryRating)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryNodesScenarioTestData")
    fun execute_with_correct_story_id_return_story_with_correct_nodes(testData: GetStoryByIDWithExpectedStoryNodesTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryNodes = executor.execute(filter)
                .extractModelOrFail()
                .nodes

            assertEquals(testData.expectedStoryNodes, actualStoryNodes)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetStoryByIDTestDataStreamCreator#checkStoryGroupsTestData")
    fun execute_with_correct_story_id_return_story_with_correct_groups(testData: GetStoryByIDWithGroupsTestData) {
        runTest {
            val filter = StoryByIDFilter(
                storyID = testData.storyID,
                environmentLangID = testData.environmentLangID
            )

            val actualStoryNodes = executor.execute(filter)
                .extractModelOrFail()
                .groups

            assertEquals(testData.expectedGroups, actualStoryNodes)
        }
    }
}