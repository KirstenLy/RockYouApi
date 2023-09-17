package database

import database.external.operation.FavoriteOperation
import database.external.result.AddOrRemoveFavoriteResult
import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.AddOrRemoveFavoriteRequestExecutor
import database.internal.test.model.TestModelsStorage
import rockyouapi.DBTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.fail

/** Test of [AddOrRemoveFavoriteRequestExecutor]. */
internal class AddOrRemoveFavoriteExecutorTest {

    @Test
    fun execute_add_with_non_existed_user_id_return_error() {
        runInEnvironment { _, storage, executor ->

            repeat(10) {
                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = Random.nextInt(from = 100, until = Int.MAX_VALUE),
                    contentID = storage.contentRegisters.random().id
                ).asErrorOrFailed()
            }
        }
    }

    @Test
    fun execute_add_with_non_existed_content_id_return_error() {
        runInEnvironment { _, storage, executor ->

            repeat(10) {
                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = storage.users.random().id,
                    contentID = Random.nextInt(from = 1000, until = Int.MAX_VALUE),
                ).asErrorOrFailed()
            }
        }
    }

    @Test
    fun execute_add_duplicate_return_already_in_favorite_result() {
        runInEnvironment { database, storage, executor ->

            repeat(10) {
                var randomUserID = storage.users.random().id
                var randomContentID = storage.contentRegisters.random().id

                var isValidArgumentsFound = false

                // Every user can add content to favorite only one time, so pair [userID - contentID] is a unique index.
                // This cycle finds valid args for execute method to prevent SQL error on insert by duplicate index.
                while (!isValidArgumentsFound) {
                    val isRecordForThisArgumentAlreadyExist = database
                        .selectFavoriteQueries
                        .selectOneByUserIDAndContentID(randomUserID, randomContentID)
                        .executeAsOneOrNull() != null

                    if (isRecordForThisArgumentAlreadyExist) {
                        randomUserID = storage.users.random().id
                        randomContentID = storage.contentRegisters.random().id
                    } else {
                        isValidArgumentsFound = true
                    }
                }

                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = randomUserID,
                    contentID = randomContentID
                ).asOkOrFailed()

                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = randomUserID,
                    contentID = randomContentID
                ).asAlreadyInFavoriteOrFailed()
            }
        }
    }

    @Test
    fun execute_add_with_valid_argument_work_as_expected() {
        runInEnvironment { database, storage, executor ->

            repeat(10) {
                var randomUserID = storage.users.random().id
                var randomContentID = storage.contentRegisters.random().id

                var isValidArgumentsFound = false

                // Every user can add content to favorite only one time, so pair [userID - contentID] is a unique index.
                // This cycle finds valid args for execute method to prevent SQL error on insert by duplicate index.
                while (!isValidArgumentsFound) {
                    val isRecordForThisArgumentAlreadyExist = database
                        .selectFavoriteQueries
                        .selectOneByUserIDAndContentID(randomUserID, randomContentID)
                        .executeAsOneOrNull() != null

                    if (isRecordForThisArgumentAlreadyExist) {
                        randomUserID = storage.users.random().id
                        randomContentID = storage.contentRegisters.random().id
                    } else {
                        isValidArgumentsFound = true
                    }
                }

                executor.execute(
                    operation = FavoriteOperation.ADD,
                    userID = randomUserID,
                    contentID = randomContentID
                ).asOkOrFailed()

                val allFavoriteRecords = database
                    .selectFavoriteQueries
                    .selectAllByUserIDAndContentID(randomUserID, randomContentID)
                    .executeAsList()

                assert(allFavoriteRecords.size == 1) {
                    buildString {
                        append("Incorrect result. Favorite records in database mismatch.")
                        appendLine()
                        append("Actual favorite records: ${allFavoriteRecords.size}. Expected favorite records: 1")
                    }
                }
            }
        }
    }

    @Test
    fun execute_remove_with_non_existed_user_id_return_not_in_favorite_result() {
        runInEnvironment { _, storage, executor ->

            repeat(10) {
                executor.execute(
                    operation = FavoriteOperation.REMOVE,
                    userID = Random.nextInt(from = 100, until = Int.MAX_VALUE),
                    contentID = storage.contentRegisters.random().id
                ).asNotInFavoriteOrFailed()
            }
        }
    }

    @Test
    fun execute_remove_with_non_existed_content_id_return_not_in_favorite_result() {
        runInEnvironment { _, storage, executor ->

            repeat(10) {
                executor.execute(
                    operation = FavoriteOperation.REMOVE,
                    userID = storage.users.random().id,
                    contentID = Random.nextInt(from = 1000, until = Int.MAX_VALUE),
                ).asNotInFavoriteOrFailed()
            }
        }
    }

    @Test
    fun execute_remove_with_non_existed_record_return_not_in_favorite_result() {
        runInEnvironment { database, storage, executor ->

            repeat(10) {

                var randomUserID = storage.users.random().id
                var randomContentID = storage.contentRegisters.random().id

                var isValidArgumentsFound = false

                // Find args to delete with valid userID and contentID when user has NOT contentID in favorite.
                while (!isValidArgumentsFound) {
                    val isRecordForThisArgumentAlreadyExist = database
                        .selectFavoriteQueries
                        .selectOneByUserIDAndContentID(randomUserID, randomContentID)
                        .executeAsOneOrNull() != null

                    if (isRecordForThisArgumentAlreadyExist) {
                        randomUserID = storage.users.random().id
                        randomContentID = storage.contentRegisters.random().id
                    } else {
                        isValidArgumentsFound = true
                    }
                }

                executor.execute(
                    operation = FavoriteOperation.REMOVE,
                    userID = randomUserID,
                    contentID = randomContentID,
                ).asNotInFavoriteOrFailed()
            }
        }
    }

    @Test
    fun execute_remove_with_existed_record_work_as_expected() {
        runInEnvironment { database, storage, executor ->

            repeat(10) {

                var randomUserID = storage.users.random().id
                var randomContentID = storage.contentRegisters.random().id

                var isValidArgumentsFound = false

                // Find args to delete with valid userID and contentID when user has NOT contentID in favorite.
                while (!isValidArgumentsFound) {
                    val isRecordForThisArgumentAlreadyExist = database
                        .selectFavoriteQueries
                        .selectOneByUserIDAndContentID(randomUserID, randomContentID)
                        .executeAsOneOrNull() != null

                    if (isRecordForThisArgumentAlreadyExist) {
                        randomUserID = storage.users.random().id
                        randomContentID = storage.contentRegisters.random().id
                    } else {
                        isValidArgumentsFound = true
                    }
                }

                database.insertFavoriteQueries.insert(
                    id = null,
                    userID = randomUserID,
                    contentID = randomContentID
                )

                executor.execute(
                    operation = FavoriteOperation.REMOVE,
                    userID = randomUserID,
                    contentID = randomContentID,
                ).asOkOrFailed()
            }
        }
    }

    private fun runInEnvironment(action: (database: DBTest, storage: TestModelsStorage, executor: AddOrRemoveFavoriteRequestExecutor) -> Unit) {
        val (database, storage) = connectToDatabaseForTest()
        val executor = AddOrRemoveFavoriteRequestExecutor(database)
        action(database, storage, executor)
    }

    private fun AddOrRemoveFavoriteResult.asErrorOrFailed() = (this as? AddOrRemoveFavoriteResult.Error) ?: fail(
        "Incorrect result. Actual result: $this. Expected result: Error"
    )

    private fun AddOrRemoveFavoriteResult.asAlreadyInFavoriteOrFailed() = (this as? AddOrRemoveFavoriteResult.AlreadyInFavorite) ?: fail(
        "Incorrect result. Actual result: $this. Expected result: AlreadyInFavorite"
    )

    private fun AddOrRemoveFavoriteResult.asNotInFavoriteOrFailed() = (this as? AddOrRemoveFavoriteResult.NotInFavorite) ?: fail(
        "Incorrect result. Actual result: $this. Expected result: NotInFavorite"
    )

    private fun AddOrRemoveFavoriteResult.asOkOrFailed() = (this as? AddOrRemoveFavoriteResult.Ok) ?: fail(
        "Incorrect result. Actual result: $this. Expected result: Ok"
    )
}