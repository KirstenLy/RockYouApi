package database.data

import database.external.filter.CommentListFilter
import database.internal.executor.ReadCommentsExecutor
import database.internal.mock.*
import database.external.model.Comment
import java.util.stream.Stream

/** Model of data to test [ReadCommentsExecutor] when result not expected. */
internal data class GetCommentWithoutExpectedValueTestData(
    val contentID: Int,
    val limit: Long,
    val offset: Long,
)

/** Model of data to test [ReadCommentsExecutor]. */
internal data class GetCommentLimitTestData(
    val contentID: Int,
    val limit: Long,
    val offset: Long,
    val expectedCommentList: List<Comment>
)

/** Data creator to test [ReadCommentsExecutor].*/
internal object GetCommentTestDataStreamCreator {

    /** Data to test [ReadCommentsExecutor] to check invalid [CommentListFilter.limit] argument. */
    @JvmStatic
    fun invalidLimitScenarioTestData(): Stream<GetCommentWithoutExpectedValueTestData> = Stream.of(
        GetCommentWithoutExpectedValueTestData(
            limit = -100,
            offset = 1,
            contentID = picture1.id,
        ),
        GetCommentWithoutExpectedValueTestData(
            limit = -1,
            offset = 1,
            contentID = picture1.id,
        ),
    )

    /** Data to test [ReadCommentsExecutor] to check invalid [CommentListFilter.offset] argument. */
    @JvmStatic
    fun invalidOffsetScenarioTestData(): Stream<GetCommentWithoutExpectedValueTestData> = Stream.of(
        GetCommentWithoutExpectedValueTestData(
            limit = 1,
            offset = -100,
            contentID = picture1.id,
        ),
        GetCommentWithoutExpectedValueTestData(
            limit = 1,
            offset = -1,
            contentID = picture1.id,
        ),
    )

    /** Data to test [ReadCommentsExecutor] to test when [CommentListFilter.contentID] link to not existed content. */
    @JvmStatic
    fun notExistedContentIDScenarioTestData(): Stream<GetCommentWithoutExpectedValueTestData> = Stream.of(
        GetCommentWithoutExpectedValueTestData(
            limit = 1,
            offset = 0,
            contentID = -100
        ),
        GetCommentWithoutExpectedValueTestData(
            limit = 1,
            offset = 0,
            contentID = -1,
        ),
        GetCommentWithoutExpectedValueTestData(
            limit = 1,
            offset = 0,
            contentID = 100000,
        ),
    )

    /** Data to test [ReadCommentsExecutor] to check valid [CommentListFilter.limit] argument. */
    @JvmStatic
    fun limitScenarioTestData(): Stream<GetCommentLimitTestData> = Stream.of(
        GetCommentLimitTestData(
            limit = 1,
            offset = 0,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment28.id,
                    contentID = comment28.contentID,
                    userID = user6.id,
                    userName = user6.name,
                    text = comment28.text,
                    creationDate = comment28.creationDate
                )
            ),
        ),
        GetCommentLimitTestData(
            limit = 2,
            offset = 0,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment28.id,
                    contentID = comment28.contentID,
                    userID = user6.id,
                    userName = user6.name,
                    text = comment28.text,
                    creationDate = comment28.creationDate
                ),
                Comment(
                    id = comment25.id,
                    contentID = comment25.contentID,
                    userID = user9.id,
                    userName = user9.name,
                    text = comment25.text,
                    creationDate = comment25.creationDate
                )
            ),
        ),
        GetCommentLimitTestData(
            limit = 10,
            offset = 0,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment28.id,
                    contentID = comment28.contentID,
                    userID = user6.id,
                    userName = user6.name,
                    text = comment28.text,
                    creationDate = comment28.creationDate
                ),
                Comment(
                    id = comment25.id,
                    contentID = comment25.contentID,
                    userID = user9.id,
                    userName = user9.name,
                    text = comment25.text,
                    creationDate = comment25.creationDate
                ),
                Comment(
                    id = comment24.id,
                    contentID = comment24.contentID,
                    userID = user8.id,
                    userName = user8.name,
                    text = comment24.text,
                    creationDate = comment24.creationDate
                ),
                Comment(
                    id = comment1.id,
                    contentID = comment1.contentID,
                    userID = user1.id,
                    userName = user1.name,
                    text = comment1.text,
                    creationDate = comment1.creationDate
                )
            ),
        ),
    )

    /** Data to test [ReadCommentsExecutor] to check valid [CommentListFilter.offset] argument. */
    @JvmStatic
    fun offsetScenarioTestData(): Stream<GetCommentLimitTestData> = Stream.of(
        GetCommentLimitTestData(
            limit = 1,
            offset = 0,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment28.id,
                    contentID = comment28.contentID,
                    userID = user6.id,
                    userName = user6.name,
                    text = comment28.text,
                    creationDate = comment28.creationDate
                )
            ),
        ),
        GetCommentLimitTestData(
            limit = 1,
            offset = 1,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment25.id,
                    contentID = comment25.contentID,
                    userID = user9.id,
                    userName = user9.name,
                    text = comment25.text,
                    creationDate = comment25.creationDate
                )
            ),
        ),
        GetCommentLimitTestData(
            limit = 1,
            offset = 2,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment24.id,
                    contentID = comment24.contentID,
                    userID = user8.id,
                    userName = user8.name,
                    text = comment24.text,
                    creationDate = comment24.creationDate
                ),
            ),
        ),
        GetCommentLimitTestData(
            limit = 1,
            offset = 10,
            contentID = picture1.id,
            expectedCommentList = emptyList()
        ),
    )

    /** Data to test [ReadCommentsExecutor] with valid arguments. */
    @JvmStatic
    fun basicScenarioTestData(): Stream<GetCommentLimitTestData> = Stream.of(
        GetCommentLimitTestData(
            limit = 1,
            offset = 0,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment28.id,
                    contentID = comment28.contentID,
                    userID = user6.id,
                    userName = user6.name,
                    text = comment28.text,
                    creationDate = comment28.creationDate
                )
            ),
        ),
        GetCommentLimitTestData(
            limit = 1,
            offset = 1,
            contentID = picture1.id,
            expectedCommentList = listOf(
                Comment(
                    id = comment25.id,
                    contentID = comment25.contentID,
                    userID = user9.id,
                    userName = user9.name,
                    text = comment25.text,
                    creationDate = comment25.creationDate
                )
            ),
        ),
    )
}