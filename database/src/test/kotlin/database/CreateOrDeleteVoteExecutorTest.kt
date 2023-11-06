package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.data.AddOrRemoveVoteTestData
import database.data.AddOrRemoveVoteTestDataWithoutOperation
import database.external.operation.VoteOperation
import database.external.reader.readDatabaseConfigurationFromEnv
import database.external.result.VoteResult
import database.internal.creator.createDB
import database.internal.executor.CreateOrDeleteVoteExecutor
import database.internal.executor.CreateReportExecutor
import database.utils.KEY_STATIC_MAP_DB
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Test of [CreateReportExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CreateOrDeleteVoteExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        CreateOrDeleteVoteExecutor(database)
    }

    @ParameterizedTest
    @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#contentNotExistScenarioTestData")
    fun add_or_remove_vote_with_not_existed_content_id_return_content_not_exist_error(testData: AddOrRemoveVoteTestData) {
        runTest {
            executor.execute(
                contentID = testData.contentID,
                userID = testData.userID,
                voteOperation = testData.operation
            )
                .asContentNotExistOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#userNotExistScenarioTestData")
    fun add_or_remove_vote_with_not_existed_user_id_return_user_not_exist_error(testData: AddOrRemoveVoteTestData) {
        runTest {
            executor.execute(
                contentID = testData.contentID,
                userID = testData.userID,
                voteOperation = testData.operation
            )
                .asUserNotExistOrFail()
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestSimpleUpvote {

        @AfterAll
        @BeforeAll
        fun clearVoteTable() {
            database.deleteVoteHistoryQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#simpleUpvoteScenarioTestData")
        fun add_or_remove_vote_upvote_add_record_to_history_and_return_ok(testData: AddOrRemoveVoteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.UPVOTE
                )
                    .asOkOrFailed()

                val actualRecordAfterUpvote = database.selectVoteHistoryQueries
                    .selectAllByUserIDAndContentID(testData.userID, testData.contentID)
                    .executeAsOne()

                assertEquals(testData.contentID, actualRecordAfterUpvote.contentID)
                assertEquals(testData.userID, actualRecordAfterUpvote.userID)
//                assertEquals(actualRecordAfterUpvote.vote.toInt(), VoteOperation.UPVOTE.operationValue)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestSimpleDownVote {

        @AfterAll
        @BeforeAll
        internal fun clearVoteTable() {
            database.deleteVoteHistoryQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#simpleDownvoteScenarioTestData")
        fun add_or_remove_vote_downvote_add_record_to_history_and_return_ok(testData: AddOrRemoveVoteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.DOWNVOTE
                )
                    .asOkOrFailed()

                val actualRecordAfterUpvote = database.selectVoteHistoryQueries
                    .selectAllByUserIDAndContentID(testData.userID, testData.contentID)
                    .executeAsOne()

                assertEquals(testData.contentID, actualRecordAfterUpvote.contentID)
                assertEquals(testData.userID, actualRecordAfterUpvote.userID)
//                assertEquals(actualRecordAfterUpvote.vote.toInt(), VoteOperation.DOWNVOTE.operationValue)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestDuplicateUpvote {

        @AfterAll
        @BeforeAll
        internal fun clearVoteTable() {
            database.deleteVoteHistoryQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#duplicateUpvoteScenarioTestData")
        fun add_or_remove_vote_upvote_twice_return_already_upvoted(testData: AddOrRemoveVoteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.UPVOTE
                )
                    .asOkOrFailed()

                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.UPVOTE
                )
                    .asAlreadyVotedOrFail()
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestDuplicateDownvote {

        @AfterAll
        @BeforeAll
        fun clearVoteTable() {
            database.deleteVoteHistoryQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#duplicateDownvoteScenarioTestData")
        fun add_or_remove_vote_downvote_twice_return_already_downvoted(testData: AddOrRemoveVoteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.DOWNVOTE
                )
                    .asOkOrFailed()

                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.DOWNVOTE
                )
                    .asAlreadyDownVotedOrFail()
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestMirrorOperation {

        @AfterAll
        @BeforeAll
        fun clearVoteTable() {
            database.deleteVoteHistoryQueries.deleteAll()
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#mirrorOperationScenarioTestData")
        fun add_or_remove_vote_downvote_and_them_upvote_remove_record_from_database_and_return_ok(testData: AddOrRemoveVoteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.DOWNVOTE
                )
                    .asOkOrFailed()

                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.UPVOTE
                )
                    .asOkOrFailed()

                val expectedRecordAfterUpvote = database.selectVoteHistoryQueries
                    .selectAllByUserIDAndContentID(testData.userID, testData.contentID)
                    .executeAsOneOrNull()

                assertNull(expectedRecordAfterUpvote)
            }
        }

        @ParameterizedTest
        @MethodSource("database.data.AddOrRemoveVoteTestDataStreamCreator#mirrorOperationScenarioTestData")
        fun add_or_remove_vote_upvote_and_them_downvote_remove_record_from_database_and_return_ok(testData: AddOrRemoveVoteTestDataWithoutOperation) {
            runTest {
                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.UPVOTE
                )
                    .asOkOrFailed()

                executor.execute(
                    contentID = testData.contentID,
                    userID = testData.userID,
                    voteOperation = VoteOperation.DOWNVOTE
                )
                    .asOkOrFailed()

                val expectedRecordAfterUDownvote = database.selectVoteHistoryQueries
                    .selectAllByUserIDAndContentID(testData.userID, testData.contentID)
                    .executeAsOneOrNull()

                assertNull(expectedRecordAfterUDownvote)
            }
        }
    }

    private fun VoteResult.asOkOrFailed() {
        Assertions.assertInstanceOf(VoteResult.OK::class.java, this)
    }

    private fun VoteResult.asContentNotExistOrFail() {
        Assertions.assertInstanceOf(VoteResult.ContentNotExist::class.java, this)
    }

    private fun VoteResult.asUserNotExistOrFail() {
        Assertions.assertInstanceOf(VoteResult.UserNotExist::class.java, this)
    }

    private fun VoteResult.asAlreadyVotedOrFail() {
        Assertions.assertInstanceOf(VoteResult.AlreadyVoted::class.java, this)
    }

    private fun VoteResult.asAlreadyDownVotedOrFail() {
        Assertions.assertInstanceOf(VoteResult.AlreadyDownVoted::class.java, this)
    }
}