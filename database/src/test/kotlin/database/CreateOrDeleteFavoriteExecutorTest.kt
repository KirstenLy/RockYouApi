package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.AddOrRemoveFavoriteTestData
import database.data.AddOrRemoveFavoriteTestDataWithoutOperation
import database.external.operation.FavoriteOperation
import database.external.reader.readDatabaseConfigurationFromEnv
import database.external.result.AddOrRemoveFavoriteResult
import database.internal.creator.createDB
import database.internal.executor.CreateOrDeleteFavoriteExecutor
import database.utils.KEY_STATIC_MAP_DB
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [CreateOrDeleteFavoriteExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CreateOrDeleteFavoriteExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor: CreateOrDeleteFavoriteExecutor by lazy {
        CreateOrDeleteFavoriteExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.AddOrRemoveFavoriteTestDataStreamCreator#userNotExistScenarioTestData")
    fun add_or_remove_favorite_with_not_existed_user_id_return_user_not_exist(testData: AddOrRemoveFavoriteTestData) {
        runTest {
            executor.execute(
                operation = testData.operation,
                userID = testData.userID,
                contentID = testData.contentID
            ).asUserNotExistOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.AddOrRemoveFavoriteTestDataStreamCreator#contentNotExistScenarioTestData")
    fun add_or_remove_favorite_with_not_existed_content_id_return_content_not_exist(testData: AddOrRemoveFavoriteTestData) {
        runTest {
            executor.execute(
                operation = testData.operation,
                userID = testData.userID,
                contentID = testData.contentID
            ).asContentNotExistOrFailed()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.AddOrRemoveFavoriteTestDataStreamCreator#removeNotExistedRecordScenarioTestData")
    fun add_or_remove_favorite_with_not_existed_record_not_in_favorite(testData: AddOrRemoveFavoriteTestDataWithoutOperation) {
        runTest {
            executor.execute(
                operation = FavoriteOperation.REMOVE,
                userID = testData.userID,
                contentID = testData.contentID,
            ).asNotInFavoriteOrFailed()
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestSimpleAdd {

        @AfterAll
        @BeforeAll
        fun clearFavoriteTable() {
            database.deleteFavoriteQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveFavoriteTestDataStreamCreator#simpleAddToFavoriteScenarioTestData")
        fun add_or_remove_favorite_with_valid_data_return_ok_and_insert_record_to_database(testData: AddOrRemoveFavoriteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = testData.userID,
                    contentID = testData.contentID
                ).asOkOrFailed()


                val insertedFavoriteRecord = database
                    .selectFavoriteQueries
                    .selectAllByUserIDAndContentID(testData.userID, testData.contentID)
                    .executeAsOne()

                assertEquals(insertedFavoriteRecord.userID, testData.userID)
                assertEquals(insertedFavoriteRecord.contentID, testData.contentID)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestDuplicateAdd {

        @AfterAll
        @BeforeAll
        fun clearFavoriteTable() {
            database.deleteFavoriteQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveFavoriteTestDataStreamCreator#duplicateAddScenarioTestData")
        fun add_favorite_with_duplicated_data_return_already_in_favorite_result(testData: AddOrRemoveFavoriteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = testData.userID,
                    contentID = testData.contentID
                ).asOkOrFailed()

                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = testData.userID,
                    contentID = testData.contentID
                ).asAlreadyInFavoriteOrFailed()
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class RemoveExistedRecord {

        @AfterAll
        @BeforeAll
        fun clearFavoriteTable() {
            database.deleteFavoriteQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveFavoriteTestDataStreamCreator#removeExistedRecordScenarioTestData")
        fun remove_favorite_for_existed_record_return_ok_and_delete_record(testData: AddOrRemoveFavoriteTestDataWithoutOperation) {
            runTest {
                database.insertFavoriteQueries.insert(
                    id = null,
                    userID = testData.userID,
                    contentID = testData.contentID
                )

                executor.execute(
                    operation = FavoriteOperation.REMOVE,
                    userID = testData.userID,
                    contentID = testData.contentID,
                ).asOkOrFailed()
            }
        }
    }

    private fun AddOrRemoveFavoriteResult.asOkOrFailed() {
        Assertions.assertInstanceOf(AddOrRemoveFavoriteResult.Ok::class.java, this)
    }

    private fun AddOrRemoveFavoriteResult.asUserNotExistOrFailed() {
        Assertions.assertInstanceOf(AddOrRemoveFavoriteResult.UserNotExists::class.java, this)
    }

    private fun AddOrRemoveFavoriteResult.asContentNotExistOrFailed() {
        Assertions.assertInstanceOf(AddOrRemoveFavoriteResult.ContentNotExists::class.java, this)
    }

    private fun AddOrRemoveFavoriteResult.asAlreadyInFavoriteOrFailed() {
        Assertions.assertInstanceOf(AddOrRemoveFavoriteResult.AlreadyInFavorite::class.java, this)
    }

    private fun AddOrRemoveFavoriteResult.asNotInFavoriteOrFailed() {
        Assertions.assertInstanceOf(AddOrRemoveFavoriteResult.NotInFavorite::class.java, this)
    }
}