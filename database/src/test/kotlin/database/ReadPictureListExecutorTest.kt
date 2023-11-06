package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.filter.PictureListFilter
import database.external.model.Picture
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.*
import database.internal.executor.ReadAuthorsForMultipleContentExecutor
import database.internal.executor.ReadGroupsForMultipleContentExecutor
import database.internal.executor.ReadLanguagesForMultipleContentExecutor
import database.internal.executor.ReadPictureListExecutor
import database.internal.executor.ReadTagsForMultipleContentExecutor
import database.internal.mock.allLanguagesAsSupportedLanguages
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asErrorOrFailed
import database.utils.extractDataOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

/** Test of [ReadPictureListExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
internal class ReadPictureListExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadPictureListExecutor(
            database = database,
            languageList = allLanguagesAsSupportedLanguages,
            readAuthorsForMultipleContentExecutor = ReadAuthorsForMultipleContentExecutor(database),
            readLanguagesForMultipleContentExecutor = ReadLanguagesForMultipleContentExecutor(database),
            readTagsForMultipleContentExecutor = ReadTagsForMultipleContentExecutor(database),
            readGroupsForMultipleContentExecutor = ReadGroupsForMultipleContentExecutor(database),
            readCharactersForMultipleContentExecutor = ReadCharactersForMultipleContentExecutor(database)
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkLimitScenarioTestData")
    fun picture_list_with_valid_list_argument_return_correct_picture_id(testData: GetPictureListLimitTestData) {
        runTest {
            val filter = createFilterForLimitTest(testData.limit)

            val actualPictureIDList = executor.execute(filter)
                .extractDataOrFail()
                .map(Picture::id)

            assertEquals(testData.expectedPictureIDList, actualPictureIDList)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1000, -10, -1])
    fun picture_list_with_invalid_limit_argument_return_error(testData: Long) {
        runTest {
            val filter = createFilterForLimitTest(testData)
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkOffsetScenarioTestData")
    fun picture_list_with_valid_offset_argument_return_correct_picture_id(testData: GetPictureListOffsetTestData) {
        runTest {
            val filter = createFilterForOffsetTest(
                offset = testData.offset,
                limit = testData.limit
            )

            val actualPictureList = executor.execute(filter)
                .extractDataOrFail()
                .map(Picture::id)

            assertEquals(testData.expectedPictureIDList, actualPictureList)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1000, -10, -1])
    fun picture_list_with_invalid_offset_argument_return_error(testData: Long) {
        runTest {
            val filter = createFilterForOffsetTest(testData, 1)
            executor.execute(filter).asErrorOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkSearchTextScenarioTestData")
    fun picture_list_with_valid_search_text_argument_return_correct_picture_id(testValidData: GetPictureListSearchTextTestData) {
        runTest {
            val filter = createFilterForSearchTextTest(
                searchText = testValidData.searchText,
                limit = testValidData.limit
            )

            val actualPictureIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Picture::id)

            assertEquals(testValidData.expectedPictureIDList, actualPictureIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkEnvironmentIDScenarioTestData")
    fun picture_list_with_valid_environment_id_return_picture_with_correct_translations(testData: GetPictureListEnvironmentIDTestData) {
        runTest {
            val filter = createFilterForEnvironmentIDTest(testData.environmentID)

            val actualPicture = executor
                .execute(filter)
                .extractDataOrFail()
                .first()

            val actualPictureLanguage = actualPicture.language
            assertEquals(testData.expectedPictureLanguageID, actualPictureLanguage?.id)
            assertEquals(testData.expectedPictureLanguageName, actualPictureLanguage?.name)

            val actualPictureTag = actualPicture.tags.first()
            assertEquals(testData.expectedPictureTagID, actualPictureTag.id)
            assertEquals(testData.expectedPictureTagName, actualPictureTag.name)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkLanguageIDListScenarioTestData")
    fun picture_list_with_valid_language_id_return_picture_with_correct_id(testData: GetPictureListLanguageIDTestData) {
        runTest {
            val filter = createFilterForLanguageIDTest(
                languageIDList = testData.languageIDList,
                limit = testData.limit
            )

            val actualPictureIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Picture::id)

            assertEquals(testData.expectedPictureIDList, actualPictureIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkAvailableLanguageIDListScenarioTestData")
    fun picture_list_return_picture_with_correct_available_language_id_and_translations(data: GetPictureListAvailableLanguageTestData) {
        runTest {
            val filter = createFilterForAvailableLanguageTest(
                environmentID = data.environmentID,
                offset = data.offset
            )

            val actualPictureAvailableLanguageList = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .availableLanguages

            assertEquals(data.expectedLanguageListSimple, actualPictureAvailableLanguageList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkAuthorsScenarioTestData")
    fun picture_list_with_valid_author_id_return_picture_with_correct_id(testData: GetPictureListAuthorIDTestData) {
        runTest {
            val filter = createFilterForAuthorIDTest(
                authorIDList = testData.authorIDList,
                limit = testData.limit
            )

            val actualPictureIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Picture::id)

            assertEquals(testData.expectedPictureIDList, actualPictureIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkTagScenarioTestData")
    fun picture_list_with_valid_tag_id_return_picture_with_correct_id(data: GetPictureListTagIDTestData) {
        runTest {
            val filter = createFilterForTagIDTest(
                includedTagIDList = data.includedTagIDList,
                excludedTagIDList = data.excludedTagIDList,
                limit = data.limit
            )

            val actualPictureIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Picture::id)

            assertEquals(data.expectedPictureIDList, actualPictureIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkUserIDScenarioTestData")
    fun picture_list_with_valid_user_id_return_picture_with_correct_id(data: GetPictureListUserIDTestData) {
        runTest {
            val filter = createFilterForUserIDTest(
                userIDList = data.userIDList,
                limit = data.limit
            )

            val actualPictureIDList = executor
                .execute(filter)
                .extractDataOrFail()
                .map(Picture::id)

            assertEquals(data.expectedPictureIDList, actualPictureIDList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkURLScenarioTestData")
    fun picture_list_return_picture_with_correct_url(data: GetPictureListURLTestData) {
        runTest {
            val filter = createFilterForURLTest(
                offset = data.offset,
            )

            val actualPictureURL = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .url

            assertEquals(data.expectedPictureURL, actualPictureURL)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkRatingIDScenarioTestData")
    fun picture_list_return_picture_with_correct_rating(data: GetPictureListRatingTestData) {
        runTest {
            val filter = createFilterForRatingTest(
                offset = data.offset
            )

            val actualPictureRating = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .rating

            assertEquals(data.expectedPictureRating, actualPictureRating)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureListTestDataStreamCreator#checkCommentCountIDScenarioTestData")
    fun picture_list_return_picture_with_correct_comment_count(data: GetPictureListCommentCountTestData) {
        runTest {
            val filter = createFilterForCommentCountTest(
                offset = data.offset
            )

            val actualPictureCommentCount = executor
                .execute(filter)
                .extractDataOrFail()
                .single()
                .commentsCount
                .toInt()

            assertEquals(data.expectedPictureCommentCount, actualPictureCommentCount)
        }
    }

    private fun createFilterForLimitTest(limit: Long) = PictureListFilter(
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

    private fun createFilterForOffsetTest(offset: Long, limit: Long) = PictureListFilter(
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

    private fun createFilterForEnvironmentIDTest(environmentID: Int?) = PictureListFilter(
        limit = 1,
        offset = 0,
        environmentLangID = environmentID,
        searchText = null,
        languageIDList = emptyList(),
        authorIDList = emptyList(),
        includedTagIDList = emptyList(),
        excludedTagIDList = emptyList(),
        userIDList = emptyList()
    )

    private fun createFilterForSearchTextTest(searchText: String?, limit: Long) = PictureListFilter(
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

    private fun createFilterForLanguageIDTest(languageIDList: List<Int>, limit: Long) = PictureListFilter(
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

    private fun createFilterForAvailableLanguageTest(environmentID: Int?, offset: Long) = PictureListFilter(
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

    private fun createFilterForAuthorIDTest(authorIDList: List<Int>, limit: Long) = PictureListFilter(
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
        PictureListFilter(
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

    private fun createFilterForUserIDTest(userIDList: List<Int>, limit: Long) = PictureListFilter(
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

    private fun createFilterForURLTest(offset: Long) = PictureListFilter(
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

    private fun createFilterForRatingTest(offset: Long) = PictureListFilter(
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

    private fun createFilterForCommentCountTest(offset: Long) = PictureListFilter(
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