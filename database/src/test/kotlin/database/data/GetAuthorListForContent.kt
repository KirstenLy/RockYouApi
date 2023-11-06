package database.data

import database.internal.executor.ReadAuthorsForSingleContentExecutor
import database.internal.mock.*
import database.external.model.Author
import java.util.stream.Stream

/** Model of data to test [ReadAuthorsForSingleContentExecutor]. */
internal data class GetAuthorListForContentTestData(
    val contentID: Int,
)

/** Model of data to test [ReadAuthorsForSingleContentExecutor] in tests where result expected. */
internal data class GetAuthorListForContentTestDataWithExpectedAuthorList(
    val contentID: Int,
    val expectedAuthorList: List<Author>
)

/** Data creator to test [ReadAuthorsForSingleContentExecutor].*/
internal object GetAuthorListForContentTestDataStreamCreator {

    /** Data to test [ReadAuthorsForSingleContentExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<GetAuthorListForContentTestDataWithExpectedAuthorList> = Stream.of(
        GetAuthorListForContentTestDataWithExpectedAuthorList(
            contentID = picture7.id,
            expectedAuthorList = emptyList()
        ),
        GetAuthorListForContentTestDataWithExpectedAuthorList(
            contentID = picture8.id,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetAuthorListForContentTestDataWithExpectedAuthorList(
            contentID = picture9.id,
            expectedAuthorList = listOf(
                Author(
                    id = author8.id,
                    name = author8.name
                ),
                Author(
                    id = author6.id,
                    name = author6.name
                ),
                Author(
                    id = author4.id,
                    name = author4.name
                ),
                Author(
                    id = author3.id,
                    name = author3.name
                ),
            )
        ),
    )

    /** Data to test [ReadAuthorsForSingleContentExecutor] when [GetAuthorListForContentTestData.contentID] link to not existed content.*/
    @JvmStatic
    fun notExistedContentIDScenarioTestData(): Stream<GetAuthorListForContentTestData> = Stream.of(
        GetAuthorListForContentTestData(
            contentID = -100000
        ),
        GetAuthorListForContentTestData(
            contentID = -1
        ),
        GetAuthorListForContentTestData(
            contentID = 0
        ),
        GetAuthorListForContentTestData(
            contentID = 100000
        ),
    )
}