package database.data

import database.internal.executor.ReadLanguagesForSingleContentExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [ReadLanguagesForSingleContentExecutor]. */
internal data class GetLanguageListForContentTestData(
    val contentID: Int,
)

/** Model of data to test [ReadLanguagesForSingleContentExecutor] in tests where result expected. */
internal data class GetLanguageListForContentWithExpectedValueTestData(
    val contentID: Int,
    val expectedLanguageIDList: List<Int>
)

/** Data creator to test [ReadLanguagesForSingleContentExecutor].*/
internal object GetLanguageListForContentTestDataStreamCreator {

    /** Data to test [ReadLanguagesForSingleContentExecutor] when [GetLanguageListForContentTestData.contentID] link to not existed content.*/
    @JvmStatic
    fun notExistedContentIDScenarioTestData(): Stream<GetLanguageListForContentTestData> = Stream.of(
        GetLanguageListForContentTestData(
            contentID = -100000,
        ),
        GetLanguageListForContentTestData(
            contentID = -1,
        ),
        GetLanguageListForContentTestData(
            contentID = 0,
        ),
        GetLanguageListForContentTestData(
            contentID = 100000,
        ),
    )

    /** Data to test [ReadLanguagesForSingleContentExecutor] with valid arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<GetLanguageListForContentWithExpectedValueTestData> = Stream.of(
        GetLanguageListForContentWithExpectedValueTestData(
            contentID = picture8.id,
            expectedLanguageIDList = emptyList()
        ),
        GetLanguageListForContentWithExpectedValueTestData(
            contentID = picture9.id,
            expectedLanguageIDList = listOf(language1.id)
        ),
        GetLanguageListForContentWithExpectedValueTestData(
            contentID = picture5.id,
            expectedLanguageIDList = listOf(language3.id, language4.id)
        ),
    )
}