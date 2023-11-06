package database.data

import database.external.filter.StoryListFilter
import database.internal.executor.ReadStoryListExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [ReadStoryListExecutor] to check [StoryListFilter.limit] argument. */
internal data class GetStoryListLimitTestData(
    val limit: Long,
    val expectedStoryIDList: List<Int>
)

/** Model of data to test [ReadStoryListExecutor] to check [StoryListFilter.offset] argument. */
internal data class GetStoryListOffsetTestData(
    val offset: Long,
    val limit: Long,
    val expectedStoryIDList: List<Int>
)

/** Model of data to test [ReadStoryListExecutor] to check [StoryListFilter.searchText] argument. */
internal data class GetStoryListSearchTextTestData(
    val searchText: String?,
    val limit: Long,
    val expectedStoryIDList: List<Int>,
)

/** Model of data to test [ReadStoryListExecutor] to check [StoryListFilter.languageIDList] argument. */
internal data class GetStoryListLanguageIDTestData(
    val languageIDList: List<Int>,
    val limit: Long,
    val expectedStoryIDList: List<Int>,
)
//
///** Model of data to test [GetPictureListExecutor] to check [PictureListFilter.environmentLangID] argument. */
//internal data class GetPictureListAvailableLanguageTestData(
//    val environmentID: Int?,
//    val offset: Long,
//    val expectedLanguageList: List<Lang>
//)
//
///** Model of data to test [GetPictureListExecutor] to check [PictureListFilter.includedTagIDList] and [PictureListFilter.excludedTagIDList] argument. */
//internal data class GetPictureListTagIDTestData(
//    val includedTagIDList: List<Int>,
//    val excludedTagIDList: List<Int>,
//    val limit: Long,
//    val expectedPictureIDList: List<Int>,
//)
//
///** Model of data to test [GetPictureListExecutor] to check [PictureListFilter.authorIDList] argument. */
//internal data class GetPictureListAuthorIDTestData(
//    val authorIDList: List<Int>,
//    val limit: Long,
//    val expectedPictureIDList: List<Int>,
//)
//
///** Model of data to test [GetPictureListExecutor] to check [Picture.url] argument. */
//internal data class GetPictureListURLTestData(
//    val offset: Long,
//    val expectedPictureURL: String,
//)
//
///** Model of data to test [GetPictureListExecutor] to check [PictureListFilter.userIDList] argument. */
//internal data class GetPictureListUserIDTestData(
//    val userIDList: List<Int>,
//    val limit: Long,
//    val expectedPictureIDList: List<Int>,
//)
//
///** Model of data to test [GetPictureListExecutor] to check [Picture.rating] argument. */
//internal data class GetPictureListRatingTestData(
//    val offset: Long,
//    val expectedPictureRating: Int,
//)
//
///** Model of data to test [GetPictureListExecutor] to check [Picture.commentsCount] argument. */
//internal data class GetPictureListCommentCountTestData(
//    val offset: Long,
//    val expectedPictureCommentCount: Int,
//)

/** Data creator to test [ReadStoryListExecutor].*/
internal object GetStoryListTestDataStreamCreator {

    /** Data to test [ReadStoryListExecutor] to check [StoryListFilter.limit] argument. */
    @JvmStatic
    fun checkLimitScenarioTestData(): Stream<GetStoryListLimitTestData> = Stream.of(
        GetStoryListLimitTestData(
            limit = 0,
            expectedStoryIDList = emptyList(),
        ),
        GetStoryListLimitTestData(
            limit = 1,
            expectedStoryIDList = listOf(
                story15.id
            )
        ),
        GetStoryListLimitTestData(
            limit = 15,
            expectedStoryIDList = listOf(
                story15.id,
                story14.id,
                story13.id,
                story12.id,
                story11.id,
                story10.id,
                story9.id,
                story8.id,
                story7.id,
                story6.id,
                story5.id,
                story4.id,
                story3.id,
                story2.id,
                story1.id,
            )
        ),
        GetStoryListLimitTestData(
            limit = 10000,
            expectedStoryIDList = listOf(
                story15.id,
                story14.id,
                story13.id,
                story12.id,
                story11.id,
                story10.id,
                story9.id,
                story8.id,
                story7.id,
                story6.id,
                story5.id,
                story4.id,
                story3.id,
                story2.id,
                story1.id,
            )
        ),
    )

    /** Data to test [ReadStoryListExecutor] to check [StoryListFilter.offset] argument. */
    @JvmStatic
    fun checkOffsetScenarioTestData(): Stream<GetStoryListOffsetTestData> = Stream.of(
        GetStoryListOffsetTestData(
            offset = 0,
            limit = 1,
            expectedStoryIDList = listOf(story15.id),
        ),
        GetStoryListOffsetTestData(
            offset = 1,
            limit = 5,
            expectedStoryIDList = listOf(
                story14.id,
                story13.id,
                story12.id,
                story11.id,
                story10.id,
            )
        ),
        GetStoryListOffsetTestData(
            offset = 10,
            limit = 1,
            expectedStoryIDList = listOf(
                story5.id,
            )
        ),
        GetStoryListOffsetTestData(
            offset = 15,
            limit = 10,
            expectedStoryIDList = emptyList()
        ),
        GetStoryListOffsetTestData(
            offset = 10000,
            limit = 1,
            expectedStoryIDList = emptyList()
        ),
    )

    /** Data to test [ReadStoryListExecutor] to check [StoryListFilter.searchText] argument. */
    @JvmStatic
    fun checkSearchTextScenarioTestData(): Stream<GetStoryListSearchTextTestData> = Stream.of(
        GetStoryListSearchTextTestData(
            searchText = null,
            limit = 1,
            expectedStoryIDList = listOf(
                story15.id
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = null,
            limit = 5,
            expectedStoryIDList = listOf(
                story15.id,
                story14.id,
                story13.id,
                story12.id,
                story11.id
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "",
            limit = 1,
            expectedStoryIDList = listOf(
                story15.id,
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "",
            limit = 5,
            expectedStoryIDList = listOf(
                story15.id,
                story14.id,
                story13.id,
                story12.id,
                story11.id
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "    ",
            limit = 1,
            expectedStoryIDList = listOf(
                story15.id,
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "    ",
            limit = 5,
            expectedStoryIDList = listOf(
                story15.id,
                story14.id,
                story13.id,
                story12.id,
                story11.id
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "techie",
            limit = 5,
            expectedStoryIDList = listOf(
                storyContent3.id
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "  techie",
            limit = 5,
            expectedStoryIDList = listOf(
                storyContent3.id
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "  techie      ",
            limit = 5,
            expectedStoryIDList = listOf(
                storyContent3.id
            )
        ),
        GetStoryListSearchTextTestData(
            searchText = "techie      ",
            limit = 5,
            expectedStoryIDList = listOf(
                storyContent3.id
            )
        ),
    )

    /** Data to test [ReadStoryListExecutor] to check [StoryListFilter.languageIDList] argument. */
    @JvmStatic
    fun checkLanguageIDListScenarioTestData(): Stream<GetStoryListLanguageIDTestData> = Stream.of(
        GetStoryListLanguageIDTestData(
            languageIDList = listOf(-1),
            limit = 1,
            expectedStoryIDList = emptyList()
        ),
        GetStoryListLanguageIDTestData(
            languageIDList = listOf(100000),
            limit = 1,
            expectedStoryIDList = emptyList()
        ),
        GetStoryListLanguageIDTestData(
            languageIDList = listOf(language3.id),
            limit = 1,
            expectedStoryIDList = listOf(
                story15.id
            )
        ),
        GetStoryListLanguageIDTestData(
            languageIDList = listOf(language3.id),
            limit = 2,
            expectedStoryIDList = listOf(
                story15.id,
                story12.id,
            )
        ),
        GetStoryListLanguageIDTestData(
            languageIDList = listOf(language3.id),
            limit = 10,
            expectedStoryIDList = listOf(
                story15.id,
                story12.id,
                story11.id,
                story9.id,
                story7.id,
                story2.id
            )
        ),
        GetStoryListLanguageIDTestData(
            languageIDList = listOf(language3.id, language2.id),
            limit = 10,
            expectedStoryIDList = listOf(
                story15.id,
                story14.id,
                story12.id,
                story11.id,
                story9.id,
                story8.id,
                story7.id,
                story3.id,
                story2.id,
                story1.id,
            )
        ),
    )
}