package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.filter.VideoListFilter
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.*
import database.internal.executor.ReadAuthorsForMultipleContentExecutor
import database.internal.executor.ReadGroupsForMultipleContentExecutor
import database.internal.executor.ReadLanguagesForMultipleContentExecutor
import database.internal.executor.ReadTagsForMultipleContentExecutor
import database.internal.executor.GetVideoListExecutor
import database.internal.mock.allLanguagesAsSupportedLanguages
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asErrorOrFailed
import database.utils.extractDataOrFail
import database.external.model.Video
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

/** Test of [GetVideoListExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
internal class GetVideoListExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        GetVideoListExecutor(
            database = database,
            languageFullList = allLanguagesAsSupportedLanguages,
            readAuthorsForMultipleContentExecutor = ReadAuthorsForMultipleContentExecutor(database),
            readLanguagesForMultipleContentExecutor = ReadLanguagesForMultipleContentExecutor(database),
            readTagsForMultipleContentExecutor = ReadTagsForMultipleContentExecutor(database),
            readGroupsForMultipleContentExecutor = ReadGroupsForMultipleContentExecutor(database),
            readCharactersForMultipleContentExecutor = ReadCharactersForMultipleContentExecutor(database)
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkLimitScenarioTestData")
    fun video_list_with_valid_list_argument_return_correct_video_id(testData: GetVideoListLimitTestData) {
        runTest {
            val filter = createFilterForLimitTest(testData.limit)

            val actualVideoIDList = executor.execute(filter)
                .extractDataOrFail()
                .map(Video::id)

            assertEquals(testData.expectedVideoIDList, actualVideoIDList)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1000, -10, -1])
    fun video_list_with_invalid_limit_argument_return_error(testData: Long) {
        runTest {
            val filter = createFilterForLimitTest(testData)
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkOffsetScenarioTestData")
    fun video_list_with_valid_offset_argument_return_correct_video_id(testData: GetVideoListOffsetTestData) {
        runTest {
            val filter = createFilterForOffsetTest(
                offset = testData.offset,
                limit = testData.limit
            )

            val actualPictureList = executor.execute(filter)
                .extractDataOrFail()
                .map(Video::id)

            assertEquals(testData.expectedVideoIDList, actualPictureList)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1000, -10, -1])
    fun video_list_with_invalid_offset_argument_return_error(testData: Long) {
        runTest {
            val filter = createFilterForOffsetTest(testData, 1)
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkSearchTextScenarioTestData")
    fun video_list_with_valid_search_text_argument_return_correct_video_id(testValidData: GetVideoListSearchTextTestData) {
        runTest {
            val filter = createFilterForSearchTextTest(
                searchText = testValidData.searchText,
                limit = testValidData.limit
            )

            val actualVideoList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Video::id)

            assertEquals(testValidData.expectedVideoIDList, actualVideoList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkEnvironmentIDScenarioTestData")
    fun video_list_with_valid_environment_id_return_video_with_correct_translations(testData: GetVideoListEnvironmentIDTestData) {
        runTest {
            val filter = createFilterForEnvironmentIDTest(
                limit = testData.limit,
                offset = testData.offset,
                environmentID = testData.environmentID
            )

            val actualVideo = executor
                .execute(filter)
                .extractDataOrFail()
                .first()

            val actualVideoLanguage = actualVideo.language
            assertEquals(testData.expectedVideoLanguageID, actualVideoLanguage?.id)
            assertEquals(testData.expectedVideoLanguageName, actualVideoLanguage?.name)

            val actualPictureTag = actualVideo.tags.first()
            assertEquals(testData.expectedVideoTagID, actualPictureTag.id)
            assertEquals(testData.expectedVideoTagName, actualPictureTag.name)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkLanguageIDListScenarioTestData")
    fun video_list_with_valid_language_id_return_video_with_correct_id(testData: GetVideoListLanguageIDTestData) {
        runTest {
            val filter = createFilterForLanguageIDTest(
                languageIDList = testData.languageIDList,
                limit = testData.limit
            )

            val actualVideoDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Video::id)

            assertEquals(testData.expectedVideoIDList, actualVideoDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkAvailableLanguageIDListScenarioTestData")
    fun video_list_return_video_with_correct_available_language_id_and_translations(data: GetVideoListAvailableLanguageTestData) {
        runTest {
            val filter = createFilterForAvailableLanguageTest(
                environmentID = data.environmentID,
                offset = data.offset
            )

            val actualVideoAvailableLanguageList = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .availableLanguages

            assertEquals(data.expectedLanguageListSimple, actualVideoAvailableLanguageList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkAuthorsScenarioTestData")
    fun video_list_with_valid_author_id_return_video_with_correct_id(testData: GetVideoListAuthorIDTestData) {
        runTest {
            val filter = createFilterForAuthorIDTest(
                authorIDList = testData.authorIDList,
                limit = testData.limit
            )

            val actualVideoIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Video::id)

            assertEquals(testData.expectedVideoIDList, actualVideoIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkTagScenarioTestData")
    fun video_list_with_valid_tag_id_return_video_with_correct_id(data: GetVideoListTagIDTestData) {
        runTest {
            val filter = createFilterForTagIDTest(
                includedTagIDList = data.includedTagIDList,
                excludedTagIDList = data.excludedTagIDList,
                limit = data.limit
            )

            val actualVideoIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Video::id)

            assertEquals(data.expectedVideoIDList, actualVideoIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkUserIDScenarioTestData")
    fun video_list_with_valid_user_id_return_video_with_correct_id(data: GetVideoListUserIDTestData) {
        runTest {
            val filter = createFilterForUserIDTest(
                userIDList = data.userIDList,
                limit = data.limit
            )

            val actualVideoIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Video::id)

            assertEquals(data.expectedVideoIDList, actualVideoIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkURLScenarioTestData")
    fun video_list_return_video_with_correct_url(data: GetVideoListURLTestData) {
        runTest {
            val filter = createFilterForURLTest(
                offset = data.offset,
            )

            val actualVideoURL = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .url

            assertEquals(data.expectedVideoURL, actualVideoURL)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkRatingIDScenarioTestData")
    fun video_list_return_video_with_correct_rating(data: GetVideoListRatingTestData) {
        runTest {
            val filter = createFilterForRatingTest(
                offset = data.offset
            )

            val actualVideoRating = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .rating

            assertEquals(data.expectedVideoRating, actualVideoRating)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetVideoListTestDataStreamCreator#checkCommentCountIDScenarioTestData")
    fun video_list_return_video_with_correct_comment_count(data: GetVideoListCommentCountTestData) {
        runTest {
            val filter = createFilterForCommentCountTest(
                offset = data.offset
            )

            val actualVideoCommentCount = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .commentsCount
                .toInt()

            assertEquals(data.expectedVideoCommentCount, actualVideoCommentCount)
        }
    }

    private fun createFilterForLimitTest(limit: Long) = VideoListFilter(
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

    private fun createFilterForOffsetTest(offset: Long, limit: Long) = VideoListFilter(
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

    private fun createFilterForEnvironmentIDTest(offset: Long, limit: Long, environmentID: Int?) = VideoListFilter(
        limit = limit,
        offset = offset,
        environmentLangID = environmentID,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForSearchTextTest(searchText: String?, limit: Long) = VideoListFilter(
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

    private fun createFilterForLanguageIDTest(languageIDList: List<Int>, limit: Long) = VideoListFilter(
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

    private fun createFilterForAvailableLanguageTest(environmentID: Int?, offset: Long) = VideoListFilter(
        limit = 1,
        offset = offset,
        environmentLangID = environmentID,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForAuthorIDTest(authorIDList: List<Int>, limit: Long) = VideoListFilter(
        limit = limit,
        offset = 0,
        environmentLangID = null,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = authorIDList,
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForTagIDTest(includedTagIDList: List<Int>, excludedTagIDList: List<Int>, limit: Long) =
        VideoListFilter(
            limit = limit,
            offset = 0,
            environmentLangID = null,
            searchText = null,
            languageIDList = emptyList(),
            authorIDList = emptyList(),
            includedTagIDList = includedTagIDList,
            excludedTagIDList = excludedTagIDList,
            userIDList = emptyList()
        )

    private fun createFilterForUserIDTest(userIDList: List<Int>, limit: Long) = VideoListFilter(
        limit = limit,
        offset = 0,
        environmentLangID = null,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = userIDList
    )

    private fun createFilterForURLTest(offset: Long) = VideoListFilter(
        limit = 1,
        offset = offset,
        environmentLangID = null,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForRatingTest(offset: Long) = VideoListFilter(
        limit = 1,
        offset = offset,
        environmentLangID = null,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForCommentCountTest(offset: Long) = VideoListFilter(
        limit = 1,
        offset = offset,
        environmentLangID = null,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )
}