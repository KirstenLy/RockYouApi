package database.data

import database.internal.executor.CreateCommentExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [CreateCommentExecutor]. */
internal data class AddCommentTestData(
    val userID: Int,
    val contentID: Int,
    val commentText: String
)

/** Data creator to test [CreateCommentExecutor].*/
internal object AddCommentTestDataStreamCreator {

    /** Data to test [CreateCommentExecutor] when data is valid. */
    @JvmStatic
    fun basicScenarioTestData(): Stream<AddCommentTestData> = Stream.of(
        AddCommentTestData(
            userID = user4.id,
            contentID = videoContent12.id,
            commentText = "Test comment #1 from AddCommentTestData model"
        ),
        AddCommentTestData(
            userID = user5.id,
            contentID = picture12.id,
            commentText = "Test comment #2 from AddCommentTestData model"
        ),
        AddCommentTestData(
            userID = user6.id,
            contentID = story12.id,
            commentText = "Test comment #3 from AddCommentTestData model"
        ),
    )

    /** Data to test [CreateCommentExecutor] when [AddCommentTestData.userID] link to not existed user. */
    @JvmStatic
    fun userNotExistScenarioTestData(): Stream<AddCommentTestData> = Stream.of(
        AddCommentTestData(
            userID = -1,
            contentID = picture1.id,
            commentText = "Test comment #1 from AddCommentNotExistedUserTestData model"
        ),
        AddCommentTestData(
            userID = 0,
            contentID = picture1.id,
            commentText = "Test comment #2 from AddCommentNotExistedUserTestData model"
        ),
        AddCommentTestData(
            userID = 100000,
            contentID = picture1.id,
            commentText = "Test comment #3 from AddCommentNotExistedUserTestData model"
        ),
    )

    /** Data to test [CreateCommentExecutor] when [AddCommentTestData.contentID] link to not existed content. */
    @JvmStatic
    fun contentNotExistScenarioTestData(): Stream<AddCommentTestData> = Stream.of(
        AddCommentTestData(
            userID = user5.id,
            contentID = -1,
            commentText = "Test comment #1 from AddCommentNotExistedContentTestData model"
        ),
        AddCommentTestData(
            userID = user5.id,
            contentID = 0,
            commentText = "Test comment #2 from AddCommentNotExistedContentTestData model"
        ),
        AddCommentTestData(
            userID = user5.id,
            contentID = 100000,
            commentText = "Test comment #3 from AddCommentNotExistedContentTestData model"
        ),
    )
}