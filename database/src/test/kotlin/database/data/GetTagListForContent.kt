package database.data

import database.internal.executor.ReadTagsForSingleContentExecutor
import database.internal.mock.*
import database.utils.getTagTranslationForEnvironment
import database.external.model.tag.TagSimple
import java.util.stream.Stream

/** Model of data to test [ReadTagsForSingleContentExecutor]. */
internal data class GetTagListForContentTestData(
    val contentID: Int,
    val environmentID: Int
)

/** Model of data to test [ReadTagsForSingleContentExecutor] in tests where result expected. */
internal data class GetTagListForContentTestDataWithExpectedTagList(
    val contentID: Int,
    val environmentID: Int,
    val expectedTagSimpleList: List<TagSimple>
)

/** Data creator to test [ReadTagsForSingleContentExecutor].*/
internal object GetTagListForContentTestDataStreamCreator {

    /** Data to test [ReadTagsForSingleContentExecutor] when [GetTagListForContentTestData.contentID] link to not existed content.*/
    @JvmStatic
    fun notExistedContentIDScenarioTestData(): Stream<GetTagListForContentTestData> = Stream.of(
        GetTagListForContentTestData(
            contentID = -100000,
            environmentID = language1.id
        ),
        GetTagListForContentTestData(
            contentID = -1,
            environmentID = language1.id
        ),
        GetTagListForContentTestData(
            contentID = 0,
            environmentID = language1.id
        ),
        GetTagListForContentTestData(
            contentID = 100000,
            environmentID = 100000
        ),
        GetTagListForContentTestData(
            contentID = -100000,
            environmentID = 100000
        ),
        GetTagListForContentTestData(
            contentID = -1,
            environmentID = 100000
        ),
        GetTagListForContentTestData(
            contentID = 0,
            environmentID = 100000
        ),
        GetTagListForContentTestData(
            contentID = 100000,
            environmentID = 100000
        ),
    )

    /** Data to test [ReadTagsForSingleContentExecutor] when [GetTagListForContentTestData.environmentID] link to not existed language id.*/
    @JvmStatic
    fun notExistedEnvironmentIDScenarioTestData(): Stream<GetTagListForContentTestData> = Stream.of(
        GetTagListForContentTestData(
            contentID = picture4.id,
            environmentID = -100000
        ),
        GetTagListForContentTestData(
            contentID = picture4.id,
            environmentID = -1
        ),
        GetTagListForContentTestData(
            contentID = picture4.id,
            environmentID = 0
        ),
        GetTagListForContentTestData(
            contentID = picture4.id,
            environmentID = 100000
        ),
    )

    /** Data to test [ReadTagsForSingleContentExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<GetTagListForContentTestDataWithExpectedTagList> = Stream.of(
        GetTagListForContentTestDataWithExpectedTagList(
            contentID = story9.id,
            environmentID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag3.id,
                    name = tag3.translations.getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                )
            )
        ),
        GetTagListForContentTestDataWithExpectedTagList(
            contentID = story9.id,
            environmentID = language4.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag3.id,
                    name = tag3.translations.getTagTranslationForEnvironment(language4.id),
                    isOneOfMainTag = false
                )
            )
        ),
        GetTagListForContentTestDataWithExpectedTagList(
            contentID = picture4.id,
            environmentID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = tag7.translations.getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag6.id,
                    name = tag6.translations.getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag4.id,
                    name = tag4.translations.getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetTagListForContentTestDataWithExpectedTagList(
            contentID = picture4.id,
            environmentID = language3.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = tag7.translations.getTagTranslationForEnvironment(language3.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag6.id,
                    name = tag6.translations.getTagTranslationForEnvironment(language3.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag4.id,
                    name = tag4.translations.getTagTranslationForEnvironment(language3.id),
                    isOneOfMainTag = false
                ),
            )
        ),
    )
}