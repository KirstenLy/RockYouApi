package database.data

import database.internal.executor.CreateReportExecutor
import database.internal.executor.ReadAuthorsForSingleContentExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [CreateReportExecutor]. */
internal data class CreateReportTestData(
    val contentID: Int,
    val userID: Int?,
    val reportText: String,
)

/** Data creator to test [CreateReportExecutor].*/
internal object CreateReportTestDataStreamCreator {

    /** Data to test [ReadAuthorsForSingleContentExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<CreateReportTestData> = Stream.of(
        CreateReportTestData(
            userID = null,
            contentID = picture1.id,
            reportText = "Report #1 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = null,
            contentID = videoContent1.id,
            reportText = "Report #2 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = null,
            contentID = story1.id,
            reportText = "Report #3 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = null,
            contentID = storyChapter1.id,
            reportText = "Report #4 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user1.id,
            contentID = picture1.id,
            reportText = "Report #1 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user1.id,
            contentID = videoContent1.id,
            reportText = "Report #2 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user1.id,
            contentID = story1.id,
            reportText = "Report #3 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user1.id,
            contentID = storyChapter1.id,
            reportText = "Report #4 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user1.id,
            contentID = picture1.id,
            reportText = "Report #1 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user2.id,
            contentID = videoContent1.id,
            reportText = "Report #2 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user3.id,
            contentID = story1.id,
            reportText = "Report #3 from basicScenarioTestData"
        ),
        CreateReportTestData(
            userID = user4.id,
            contentID = storyChapter1.id,
            reportText = "Report #4 from basicScenarioTestData"
        ),
    )

    /** Data to test [ReadAuthorsForSingleContentExecutor] when [CreateReportTestData.userID] link to not existed user.*/
    @JvmStatic
    fun notExistedUserIDScenarioTestData() = Stream.of(
        CreateReportTestData(
            userID = -100000,
            contentID = picture1.id,
            reportText = "Report #1 from notExistedUserIDScenarioTestData"
        ),
        CreateReportTestData(
            userID = -1,
            contentID = videoContent1.id,
            reportText = "Report #2 from notExistedUserIDScenarioTestData"
        ),
        CreateReportTestData(
            userID = 0,
            contentID = story1.id,
            reportText = "Report #3 from notExistedUserIDScenarioTestData"
        ),
        CreateReportTestData(
            userID = 100000,
            contentID = storyChapter1.id,
            reportText = "Report #4 from notExistedUserIDScenarioTestData"
        ),
    )

    /** Data to test [ReadAuthorsForSingleContentExecutor] when [CreateReportTestData.contentID] link to not existed content.*/
    @JvmStatic
    fun notExistedContentIDScenarioTestData(): Stream<CreateReportTestData> = Stream.of(
        CreateReportTestData(
            userID = user5.id,
            contentID = -100000,
            reportText = "Report #1 from notExistedContentIDScenarioTestData"
        ),
        CreateReportTestData(
            userID = user5.id,
            contentID = -1,
            reportText = "Report #2 from notExistedContentIDScenarioTestData"
        ),
        CreateReportTestData(
            userID = user5.id,
            contentID = 0,
            reportText = "Report #3 from notExistedContentIDScenarioTestData"
        ),
        CreateReportTestData(
            userID = user5.id,
            contentID = 100000,
            reportText = "Report #4 from notExistedContentIDScenarioTestData"
        ),
    )
}