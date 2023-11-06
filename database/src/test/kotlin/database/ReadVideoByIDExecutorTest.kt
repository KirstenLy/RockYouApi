package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.filter.VideoByIDFilter
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

/** Test of [ReadVideoByIDExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReadVideoByIDExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadVideoByIDExecutor(
            database = database,
            languageList = allLanguagesAsSupportedLanguages,
            readAuthorsForSingleContentExecutor = ReadAuthorsForSingleContentExecutor(database),
            readLanguagesForSingleContentExecutor = ReadLanguagesForSingleContentExecutor(database),
            readTagsForSingleContentExecutor = ReadTagsForSingleContentExecutor(database),
            readGroupsForContentExecutor = ReadGroupsForContentExecutor(database),
            readCharactersForSingleContentExecutor = ReadCharactersForSingleContentExecutor(database)
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#notExistedContentScenarioTestData")
    fun execute_with_non_existed_video_id_return_not_found_result(testData: GetVideoByIDTSimpleTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )
            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#wrongContentScenarioTestData")
    fun execute_with_wrong_video_id_return_not_found_result(testData: GetVideoByIDTSimpleTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )
            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoIDScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_id(testData: GetVideoByIDWithExpectedVideoIDTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoID = executor.execute(filter).extractModelOrFail().id
            assertEquals(testData.expectedID, actualVideoID)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoTitleScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_title(testData: GetVideoByIDWithExpectedTitleTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoTitle = executor.execute(filter).extractModelOrFail().title
            assertEquals(testData.expectedTitle, actualVideoTitle)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoURLScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_url(testData: GetVideoByIDWithExpectedVideoURLTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoURL = executor.execute(filter).extractModelOrFail().url
            assertEquals(testData.expectedURL, actualVideoURL)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoLanguageScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_lang(testData: GetVideoByIDWithExpectedLanguageTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoLanguage = executor.execute(filter).extractModelOrFail().language
            assertEquals(testData.expectedLanguageSimple, actualVideoLanguage)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoAvailableLanguageScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_available_languages(testData: GetVideoByIDWithExpectedAvailableLanguageTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoAvailableLanguageList = executor.execute(filter)
                .extractModelOrFail()
                .availableLanguages
                .orEmpty()

            assertEquals(testData.expectedAvailableLanguageListSimple, actualVideoAvailableLanguageList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoAuthorsScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_authors(testData: GetVideoByIDWithExpectedAuthorsTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoAuthorList = executor.execute(filter)
                .extractModelOrFail()
                .authors

            assertEquals(testData.expectedAuthorList, actualVideoAuthorList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoTagsScenarioTestData")
    fun execute_with_correct_video_id_return_picture_with_correct_tags(testData: GetVideoByIDWithExpectedTagsTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoTagList = executor.execute(filter)
                .extractModelOrFail()
                .tags

            assertEquals(testData.expectedTagSimpleList, actualVideoTagList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoUserScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_user(testData: GetVideoByIDWithExpectedUserTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoUser = executor.execute(filter)
                .extractModelOrFail()
                .user

            assertEquals(testData.expectedUserSimple, actualVideoUser)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoRatingScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_rating(testData: GetVideoByIDWithExpectedRatingTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoRating = executor.execute(filter)
                .extractModelOrFail()
                .rating

            assertEquals(testData.expectedRating, actualVideoRating)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoCommentCountScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_comments_count(testData: GetVideoByIDWithCommentCountTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoCommentCount = executor.execute(filter)
                .extractModelOrFail()
                .commentsCount
                .toInt()

            assertEquals(testData.expectedCommentCount, actualVideoCommentCount)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoByIDTestDataStreamCreator#checkVideoGroupsScenarioTestData")
    fun execute_with_correct_video_id_return_video_with_correct_groups(testData: GetVideoByIDWithGroupsTestData) {
        runTest {
            val filter = VideoByIDFilter(
                videoID = testData.videoID,
                environmentLangID = testData.environmentLangID
            )

            val actualVideoGroupList = executor.execute(filter).extractModelOrFail().groups
            assertEquals(testData.expectedGroups, actualVideoGroupList)
        }
    }
}