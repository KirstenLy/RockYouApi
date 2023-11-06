package database.data

import database.internal.executor.ReadAuthorsForMultipleContentExecutor
import database.internal.mock.author1
import database.internal.mock.author7
import database.internal.mock.author8
import database.internal.mock.author9
import database.external.model.Author
import java.util.stream.Stream

/** Model of data to test [ReadAuthorsForMultipleContentExecutor]. */
internal data class GetAuthorListForContentListTestData(
    val contentIDList: List<Int>
)

/** Model of data to test [ReadAuthorsForMultipleContentExecutor] in tests where result expected. */
internal data class GetAuthorListForContentListTestDataWithExpectedAuthorList(
    val contentIDList: List<Int>,
    val expectedAuthorListMap: HashMap<Int, List<Author>>
)

/** Data creator to test [ReadAuthorsForMultipleContentExecutor].*/
internal object GetAuthorListForContentListTestDataStreamCreator {

    /** Data to test [ReadAuthorsForMultipleContentExecutor] with expected arguments.*/
    @JvmStatic
    fun existedContentIDScenarioTestData(): Stream<GetAuthorListForContentListTestDataWithExpectedAuthorList> =
        Stream.of(
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(11),
                expectedAuthorListMap = hashMapOf()
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(11, -1),
                expectedAuthorListMap = hashMapOf()
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(1),
                expectedAuthorListMap = hashMapOf(
                    1 to listOf(
                        Author(
                            id = author1.id,
                            name = author1.name
                        )
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(1, 11),
                expectedAuthorListMap = hashMapOf(
                    1 to listOf(
                        Author(
                            id = author1.id,
                            name = author1.name
                        )
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(2),
                expectedAuthorListMap = hashMapOf(
                    2 to listOf(
                        Author(
                            id = author1.id,
                            name = author1.name
                        )
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(2, -1),
                expectedAuthorListMap = hashMapOf(
                    2 to listOf(
                        Author(
                            id = author1.id,
                            name = author1.name
                        )
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(2, -1, 11),
                expectedAuthorListMap = hashMapOf(
                    2 to listOf(
                        Author(
                            id = author1.id,
                            name = author1.name
                        )
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(5),
                expectedAuthorListMap = hashMapOf(
                    5 to listOf(
                        Author(
                            id = author9.id,
                            name = author9.name
                        ),
                        Author(
                            id = author8.id,
                            name = author8.name
                        ),
                        Author(
                            id = author7.id,
                            name = author7.name
                        ),
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(5, 5),
                expectedAuthorListMap = hashMapOf(
                    5 to listOf(
                        Author(
                            id = author9.id,
                            name = author9.name
                        ),
                        Author(
                            id = author8.id,
                            name = author8.name
                        ),
                        Author(
                            id = author7.id,
                            name = author7.name
                        ),
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(5, 5, 100000),
                expectedAuthorListMap = hashMapOf(
                    5 to listOf(
                        Author(
                            id = author9.id,
                            name = author9.name
                        ),
                        Author(
                            id = author8.id,
                            name = author8.name
                        ),
                        Author(
                            id = author7.id,
                            name = author7.name
                        ),
                    )
                )
            ),
            GetAuthorListForContentListTestDataWithExpectedAuthorList(
                contentIDList = listOf(5, 5, 1),
                expectedAuthorListMap = hashMapOf(
                    5 to listOf(
                        Author(
                            id = author9.id,
                            name = author9.name
                        ),
                        Author(
                            id = author8.id,
                            name = author8.name
                        ),
                        Author(
                            id = author7.id,
                            name = author7.name
                        ),
                    ),
                    1 to listOf(
                        Author(
                            id = author1.id,
                            name = author1.name
                        )
                    )
                )
            ),
        )

    /** Data to test [ReadAuthorsForMultipleContentExecutor] when [GetAuthorListForContentListTestData.contentIDList] link to not existed content.*/
    @JvmStatic
    fun notExistedContentListScenarioTestData(): Stream<GetAuthorListForContentListTestData> = Stream.of(
        GetAuthorListForContentListTestData(
            contentIDList = listOf(-1),
        ),
        GetAuthorListForContentListTestData(
            contentIDList = listOf(-100, -1),
        ),
        GetAuthorListForContentListTestData(
            contentIDList = listOf(100000),
        ),
        GetAuthorListForContentListTestData(
            contentIDList = listOf(0, 100000),
        ),
        GetAuthorListForContentListTestData(
            contentIDList = listOf(-100000, -100, -1, 0, 100000),
        ),
    )
}