package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.*
import database.external.filter.PictureByIDFilter
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadAuthorsForSingleContentExecutor
import database.internal.executor.ReadCharactersForSingleContentExecutor
import database.internal.executor.ReadGroupsForContentExecutor
import database.internal.executor.ReadLanguagesForSingleContentExecutor
import database.internal.executor.ReadPictureByIDExecutor
import database.internal.executor.ReadTagsForSingleContentExecutor
import database.internal.mock.allLanguagesAsSupportedLanguages
import database.utils.KEY_STATIC_MAP_DB
import database.utils.asDataNotFoundOrFail
import database.utils.extractModelOrFail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [ReadPictureByIDExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
internal class ReadPictureByIDExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadPictureByIDExecutor(
            database = database,
            languageList = allLanguagesAsSupportedLanguages,
            readAuthorsForSingleContentExecutor = ReadAuthorsForSingleContentExecutor(database),
            readLanguagesForSingleContentExecutor = ReadLanguagesForSingleContentExecutor(database),
            readTagsForSingleContentExecutor = ReadTagsForSingleContentExecutor(database),
            readGroupsForSingleContentExecutor = ReadGroupsForContentExecutor(database),
            readCharactersForSingleContentExecutor = ReadCharactersForSingleContentExecutor(database)
        )
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#notExistedContentScenarioTestData")
    fun picture_by_id_with_non_existed_picture_id_return_not_found_result(testData: GetPictureByIDTSimpleTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#wrongContentScenarioTestData")
    fun picture_by_id_with_wrong_picture_id_return_not_found_result(testData: GetPictureByIDTSimpleTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            executor.execute(filter).asDataNotFoundOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureIDScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_id(testData: GetPictureByIDWithExpectedPictureIDTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureID = executor.execute(filter).extractModelOrFail().id
            assertEquals(testData.expectedID, actualPictureID)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureTitleScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_title(testData: GetPictureByIDWithExpectedTitleTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureTitle = executor.execute(filter).extractModelOrFail().title
            assertEquals(testData.expectedTitle, actualPictureTitle)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureURLScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_url(testData: GetPictureByIDWithExpectedURLTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureURL = executor.execute(filter).extractModelOrFail().url
            assertEquals(testData.expectedURL, actualPictureURL)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureLanguageScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_language_and_translation(testData: GetPictureByIDWithExpectedLanguageTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureLanguage = executor.execute(filter).extractModelOrFail().language
            assertEquals(testData.expectedLanguageSimple, actualPictureLanguage)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureAvailableLanguageScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_available_language_and_translation(testData: GetPictureByIDWithExpectedAvailableLanguageTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureAvailableLanguageList = executor.execute(filter)
                .extractModelOrFail()
                .availableLanguages
            assertEquals(testData.expectedAvailableLanguageListSimple, actualPictureAvailableLanguageList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureAuthorsScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_author_list(testData: GetPictureByIDWithExpectedAuthorsTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureAuthorList = executor.execute(filter).extractModelOrFail().authors
            assertEquals(testData.expectedAuthorList, actualPictureAuthorList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureTagsScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_tag_list_and_tag_translations(testData: GetPictureByIDWithExpectedTagsTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureTagList = executor.execute(filter).extractModelOrFail().tags

            assertEquals(testData.expectedTagSimpleList, actualPictureTagList)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureUserScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_user(testData: GetPictureByIDWithExpectedUserTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureUser = executor.execute(filter).extractModelOrFail().user
            assertEquals(testData.expectedUserSimple, actualPictureUser)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureRatingScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_rating(testData: GetPictureByIDWithExpectedRatingTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureRating = executor.execute(filter).extractModelOrFail().rating
            assertEquals(testData.expectedRating, actualPictureRating)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureCommentCountScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_comment_count(testData: GetPictureByIDWithCommentCountTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureCommentCount = executor.execute(filter)
                .extractModelOrFail()
                .commentsCount
                .toInt()
            assertEquals(testData.expectedCommentCount, actualPictureCommentCount)
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.GetPictureByIDTestDataStreamCreator#checkPictureGroupScenarioTestData")
    fun picture_by_id_with_correct_picture_id_return_picture_with_correct_groups(testData: GetPictureByIDWithGroupsTestData) {
        runTest {
            val filter = PictureByIDFilter(
                pictureID = testData.pictureID,
                environmentLangID = testData.environmentLangID
            )
            val actualPictureGroupList = executor.execute(filter).extractModelOrFail().groups
            assertEquals(testData.expectedGroups, actualPictureGroupList)
        }
    }
}