package database.data

import database.internal.executor.ReadLanguagesForMultipleContentExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [ReadLanguagesForMultipleContentExecutor] for simple scenarios.*/
internal data class GetLanguageListForContentListWithoutExpectedValueTestData(
    val contentIDList: List<Int>
)

/** Model of data to test [ReadLanguagesForMultipleContentExecutor] in tests where result expected. */
internal data class GetLanguageListForContentListTestDataWithExpectedAuthorList(
    val contentIDList: List<Int>,
    val expectedLanguageIDListMap: HashMap<Int, List<Int>>
)

/** Data creator to test [ReadLanguagesForMultipleContentExecutor].*/
internal object GetLanguageListForContentListTestDataStreamCreator {

    /** Data to test [ReadLanguagesForMultipleContentExecutor] when [GetLanguageListForContentListWithoutExpectedValueTestData.contentIDList] link to not existed content.*/
    @JvmStatic
    fun notExistedContentListScenarioTestData(): Stream<GetLanguageListForContentListWithoutExpectedValueTestData> =
        Stream.of(
            GetLanguageListForContentListWithoutExpectedValueTestData(
                contentIDList = listOf(-1),
            ),
            GetLanguageListForContentListWithoutExpectedValueTestData(
                contentIDList = listOf(-100, -1),
            ),
            GetLanguageListForContentListWithoutExpectedValueTestData(
                contentIDList = listOf(100000),
            ),
            GetLanguageListForContentListWithoutExpectedValueTestData(
                contentIDList = listOf(0, 100000),
            ),
            GetLanguageListForContentListWithoutExpectedValueTestData(
                contentIDList = listOf(-100000, -100, -1, 0, 100000),
            ),
        )

    /** Data to test [ReadLanguagesForMultipleContentExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<GetLanguageListForContentListTestDataWithExpectedAuthorList> =
        Stream.of(
            GetLanguageListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(picture1.id),
                expectedLanguageIDListMap = hashMapOf()
            ),
            GetLanguageListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(storyChapter8.id),
                expectedLanguageIDListMap = hashMapOf(storyChapter8.id to listOf(language4.id))
            ),
            GetLanguageListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(storyChapter7.id),
                expectedLanguageIDListMap = hashMapOf(storyChapter7.id to listOf(language3.id, language4.id))
            ),
        )
}