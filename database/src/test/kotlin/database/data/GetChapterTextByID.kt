package database.data

import database.internal.executor.ReadChapterTextByIDExecutor
import database.internal.mock.*
import database.internal.mock.picture12
import database.internal.mock.story12
import database.internal.mock.storyChapter28
import database.internal.mock.videoContent12
import java.util.stream.Stream

/** Model of data to test [ReadChapterTextByIDExecutor] where no data result expected. */
internal data class GetChapterTextByIDWithoutExpectedValueTestData(
    val chapterID: Int,
)

/** Model of data to test [ReadChapterTextByIDExecutor] with valid data. */
internal data class GetChapterTextByIDTestData(
    val chapterID: Int,
    val expectedChapterText: String
)

/** Data creator to test [ReadChapterTextByIDExecutor].*/
internal object GetChapterTextByIDTestDataStreamCreator {

    /** Data to test [ReadChapterTextByIDExecutor] when [GetChapterTextByIDWithoutExpectedValueTestData.chapterID] link to wrong content. */
    @JvmStatic
    fun wrongContentScenarioTestData(): Stream<GetChapterTextByIDWithoutExpectedValueTestData> = Stream.of(
        GetChapterTextByIDWithoutExpectedValueTestData(
            chapterID = videoContent12.id,
        ),
        GetChapterTextByIDWithoutExpectedValueTestData(
            chapterID = story12.id,
        ),
        GetChapterTextByIDWithoutExpectedValueTestData(
            chapterID = picture12.id,
        )
    )

    /** Data to test [ReadChapterTextByIDExecutor] with correct arguments. */
    @JvmStatic
    fun correctScenarioTestData(): Stream<GetChapterTextByIDTestData> = Stream.of(
        GetChapterTextByIDTestData(
            chapterID = storyChapter28.id,
            expectedChapterText = storyChapter28.text
        ),
        GetChapterTextByIDTestData(
            chapterID = storyChapter8.id,
            expectedChapterText = storyChapter8.text
        ),
        GetChapterTextByIDTestData(
            chapterID = storyChapter14.id,
            expectedChapterText = storyChapter14.text
        ),
    )
}