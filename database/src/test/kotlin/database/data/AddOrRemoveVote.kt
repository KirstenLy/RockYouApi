package database.data

import database.external.operation.VoteOperation
import database.internal.executor.CreateOrDeleteVoteExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [CreateOrDeleteVoteExecutor]. */
internal data class AddOrRemoveVoteTestData(
    val operation: VoteOperation,
    val userID: Int,
    val contentID: Int
)

/** Model of data to test [CreateOrDeleteVoteExecutor] in test where operation argument not required. */
internal data class AddOrRemoveVoteTestDataWithoutOperation(
    val userID: Int,
    val contentID: Int
)

/** Data creator to test [CreateOrDeleteVoteExecutor].*/
internal object AddOrRemoveVoteTestDataStreamCreator {

    /** Data to test [CreateOrDeleteVoteExecutor] in valid upvote scenario.*/
    @JvmStatic
    fun simpleUpvoteScenarioTestData(): Stream<AddOrRemoveVoteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user2.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user5.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user7.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user8.id,
            contentID = picture1.id,
        ),
    )

    /** Data to test [CreateOrDeleteVoteExecutor] in valid downvote scenario.*/
    @JvmStatic
    fun simpleDownvoteScenarioTestData(): Stream<AddOrRemoveVoteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user2.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user5.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user7.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user8.id,
            contentID = picture1.id,
        ),
    )

    /** Data to test [CreateOrDeleteVoteExecutor] when [AddOrRemoveVoteTestData.userID] link to not existed user. */
    @JvmStatic
    fun userNotExistScenarioTestData(): Stream<AddOrRemoveVoteTestData> = Stream.of(
        AddOrRemoveVoteTestData(
            operation = VoteOperation.UPVOTE,
            userID = -1,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.DOWNVOTE,
            userID = -1,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.UPVOTE,
            userID = 0,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.DOWNVOTE,
            userID = 0,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.UPVOTE,
            userID = 100000,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.DOWNVOTE,
            userID = 100000,
            contentID = picture1.id,
        ),
    )

    /** Data to test [CreateOrDeleteVoteExecutor] when [AddOrRemoveVoteTestData.contentID] link to not existed content. */
    @JvmStatic
    fun contentNotExistScenarioTestData(): Stream<AddOrRemoveVoteTestData> = Stream.of(
        AddOrRemoveVoteTestData(
            operation = VoteOperation.UPVOTE,
            userID = user8.id,
            contentID = -100000
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.DOWNVOTE,
            userID = user8.id,
            contentID = -100000
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.UPVOTE,
            userID = user8.id,
            contentID = -1
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.DOWNVOTE,
            userID = user8.id,
            contentID = -1
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.UPVOTE,
            userID = user8.id,
            contentID = 0
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.DOWNVOTE,
            userID = user8.id,
            contentID = 0
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.UPVOTE,
            userID = user8.id,
            contentID = 100000
        ),
        AddOrRemoveVoteTestData(
            operation = VoteOperation.DOWNVOTE,
            userID = user8.id,
            contentID = 100000
        ),
    )

    /** Data to test [CreateOrDeleteVoteExecutor] when data inserted and then deleted.  */
    @JvmStatic
    fun mirrorOperationScenarioTestData(): Stream<AddOrRemoveVoteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user1.id,
            contentID = videoContent1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user9.id,
            contentID = story1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user3.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user2.id,
            contentID = storyChapter1.id,
        ),
    )

    /** Data to test [CreateOrDeleteVoteExecutor] when test data inserted twice.  */
    @JvmStatic
    fun duplicateUpvoteScenarioTestData(): Stream<AddOrRemoveVoteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user1.id,
            contentID = videoContent1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user9.id,
            contentID = story1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user3.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user2.id,
            contentID = storyChapter1.id,
        ),
    )

    /** Data to test [CreateOrDeleteVoteExecutor] when test data deleted twice.  */
    @JvmStatic
    fun duplicateDownvoteScenarioTestData(): Stream<AddOrRemoveVoteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user1.id,
            contentID = videoContent1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user9.id,
            contentID = story1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user3.id,
            contentID = picture1.id,
        ),
        AddOrRemoveVoteTestDataWithoutOperation(
            userID = user2.id,
            contentID = storyChapter1.id,
        ),
    )
}