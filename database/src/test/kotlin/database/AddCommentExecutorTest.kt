package database

import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.AddCommentRequestExecutor
import database.internal.test.model.TestModelsStorage
import database.utils.asErrorOrFailed
import database.utils.asOkOrFailed
import rockyouapi.DBTest
import java.util.UUID
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.fail

/** Test of [AddCommentRequestExecutor]. */
internal class AddCommentExecutorTest {

    @Test
    fun execute_with_non_existed_user_id_return_error() {
        runInEnvironment { _, storage, executor ->

            repeat(10) {
                executor.execute(
                    userID = Random.nextInt(from = 100, until = Int.MAX_VALUE),
                    contentID = storage.contentRegisters.random().id,
                    commentText = UUID.randomUUID().toString()
                ).asErrorOrFailed()
            }
        }
    }

    @Test
    fun execute_with_non_existed_content_id_return_error() {
        runInEnvironment { _, storage, executor ->

            repeat(10) {
                executor.execute(
                    userID = storage.users.random().id,
                    contentID = Random.nextInt(from = 1000, until = Int.MAX_VALUE),
                    commentText = UUID.randomUUID().toString()
                ).asErrorOrFailed()
            }
        }
    }

    @Test
    fun execute_with_empty_text_return_ok_but_insert_not_happen() {
        runInEnvironment { database, storage, executor ->

            repeat(10) {
                val randomUserID = storage.users.random().id
                val randomContentID = storage.contentRegisters.random().id

                val recordsCountBeforeExecute = database
                    .countCommentQueries
                    .countForContentAndUser(
                        contentID = randomContentID,
                        userID = randomUserID
                    )
                    .executeAsOneOrNull()
                    ?: fail("Unexpected state, can't count comment")

                executor.execute(
                    userID = storage.users.random().id,
                    contentID = storage.contentRegisters.random().id,
                    commentText = ""
                ).asOkOrFailed()

                val recordsCountAfterExecute = database
                    .countCommentQueries
                    .countForContentAndUser(
                        contentID = randomContentID,
                        userID = randomUserID
                    )
                    .executeAsOneOrNull()
                    ?: fail("Unexpected state, can't count comment")

                assert(recordsCountAfterExecute == recordsCountBeforeExecute) {
                    buildString {
                        append("Incorrect result. Comment number after insert mismatch.")
                        appendLine()
                        append("Actual comment number: $recordsCountAfterExecute. Expected comment number: $recordsCountBeforeExecute")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_args_work_correct() {
        runInEnvironment { database, storage, executor ->

            repeat(10) {
                val randomUserID = storage.users.random().id
                val randomContentID = storage.contentRegisters.random().id

                val recordsCountBeforeExecute = database
                    .countCommentQueries
                    .countForContentAndUser(
                        contentID = randomContentID,
                        userID = randomUserID
                    )
                    .executeAsOneOrNull()
                    ?: fail("Unexpected state, can't count comment")

                executor.execute(
                    userID = randomUserID,
                    contentID = randomContentID,
                    commentText = UUID.randomUUID().toString()
                ).asOkOrFailed()

                val recordsAfterExecute = database
                    .countCommentQueries
                    .countForContentAndUser(
                        contentID = randomContentID,
                        userID = randomUserID
                    )
                    .executeAsOneOrNull()
                    ?: fail("Unexpected state, can't count comment")

                // Check is new record presented in the database
                assert(recordsAfterExecute == recordsCountBeforeExecute + 1) {
                    buildString {
                        append("Incorrect result. Comment number after insert mismatch.")
                        appendLine()
                        append("Actual comment number: $recordsAfterExecute Expected comment number: ${recordsCountBeforeExecute + 1}")
                    }
                }
            }
        }
    }

    private fun runInEnvironment(action: (database: DBTest, storage: TestModelsStorage, executor: AddCommentRequestExecutor) -> Unit) {
        val (database, storage) = connectToDatabaseForTest()
        val executor = AddCommentRequestExecutor(database)
        action(database, storage, executor)
    }
}