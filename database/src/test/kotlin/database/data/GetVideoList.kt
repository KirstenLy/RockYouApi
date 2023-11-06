package database.data

import common.utils.generateRandomTextByUUID
import database.external.filter.VideoListFilter
import database.internal.executor.GetVideoListExecutor
import database.internal.mock.*
import database.internal.model.DBAuthor
import database.utils.*
import database.external.model.language.LanguageSimple
import database.external.model.Video
import java.util.stream.Stream

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.limit] argument. */
internal data class GetVideoListLimitTestData(
    val limit: Long,
    val expectedVideoIDList: List<Int>
)

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.offset] argument. */
internal data class GetVideoListOffsetTestData(
    val offset: Long,
    val limit: Long,
    val expectedVideoIDList: List<Int>
)

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.searchText] argument. */
internal data class GetVideoListSearchTextTestData(
    val searchText: String?,
    val limit: Long,
    val expectedVideoIDList: List<Int>,
)

/**
 * Model of data to test [GetVideoListExecutor] to check [VideoListFilter.environmentLangID] argument
 * Used to check if [environmentID] applied to language and tag names.
 * */
internal data class GetVideoListEnvironmentIDTestData(
    val limit: Long,
    val offset: Long,
    val environmentID: Int?,
    val expectedVideoLanguageID: Int,
    val expectedVideoLanguageName: String,
    val expectedVideoTagID: Int,
    val expectedVideoTagName: String
)

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.languageIDList] argument. */
internal data class GetVideoListLanguageIDTestData(
    val languageIDList: List<Int>,
    val limit: Long,
    val expectedVideoIDList: List<Int>,
)

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.environmentLangID] argument. */
internal data class GetVideoListAvailableLanguageTestData(
    val environmentID: Int?,
    val offset: Long,
    val expectedLanguageListSimple: List<LanguageSimple>
)

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.includedTagIDList] and [VideoListFilter.excludedTagIDList] argument. */
internal data class GetVideoListTagIDTestData(
    val includedTagIDList: List<Int>,
    val excludedTagIDList: List<Int>,
    val limit: Long,
    val expectedVideoIDList: List<Int>,
)

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.authorIDList] argument. */
internal data class GetVideoListAuthorIDTestData(
    val authorIDList: List<Int>,
    val limit: Long,
    val expectedVideoIDList: List<Int>,
)

/** Model of data to test [GetVideoListExecutor] to check [Video.url] argument. */
internal data class GetVideoListURLTestData(
    val offset: Long,
    val expectedVideoURL: String,
)

/** Model of data to test [GetVideoListExecutor] to check [VideoListFilter.userIDList] argument. */
internal data class GetVideoListUserIDTestData(
    val userIDList: List<Int>,
    val limit: Long,
    val expectedVideoIDList: List<Int>,
)

/** Model of data to test [GetVideoListExecutor] to check [Video.rating] argument. */
internal data class GetVideoListRatingTestData(
    val offset: Long,
    val expectedVideoRating: Int,
)

/** Model of data to test [GetVideoListExecutor] to check [Video.commentsCount] argument. */
internal data class GetVideoListCommentCountTestData(
    val offset: Long,
    val expectedVideoCommentCount: Int,
)

/** Data creator to test [GetVideoListExecutor].*/
internal object GetVideoListTestDataStreamCreator {

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.limit] argument. */
    @JvmStatic
    fun checkLimitScenarioTestData(): Stream<GetVideoListLimitTestData> = Stream.of(
        GetVideoListLimitTestData(
            limit = 0,
            expectedVideoIDList = emptyList(),
        ),
        GetVideoListLimitTestData(
            limit = 1,
            expectedVideoIDList = listOf(video15.id)
        ),
        GetVideoListLimitTestData(
            limit = 10,
            expectedVideoIDList = listOf(
                videoContent15.id,
                videoContent14.id,
                videoContent13.id,
                videoContent12.id,
                videoContent11.id,
                videoContent10.id,
                videoContent9.id,
                videoContent8.id,
                videoContent7.id,
                videoContent6.id,
            )
        ),
        GetVideoListLimitTestData(
            limit = 15,
            expectedVideoIDList = listOf(
                videoContent15.id,
                videoContent14.id,
                videoContent13.id,
                videoContent12.id,
                videoContent11.id,
                videoContent10.id,
                videoContent9.id,
                videoContent8.id,
                videoContent7.id,
                videoContent6.id,
                videoContent5.id,
                videoContent4.id,
                videoContent3.id,
                videoContent2.id,
                videoContent1.id,
            )
        ),
        GetVideoListLimitTestData(
            limit = 10000,
            expectedVideoIDList = listOf(
                videoContent15.id,
                videoContent14.id,
                videoContent13.id,
                videoContent12.id,
                videoContent11.id,
                videoContent10.id,
                videoContent9.id,
                videoContent8.id,
                videoContent7.id,
                videoContent6.id,
                videoContent5.id,
                videoContent4.id,
                videoContent3.id,
                videoContent2.id,
                videoContent1.id,
            )
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.offset] argument. */
    @JvmStatic
    fun checkOffsetScenarioTestData(): Stream<GetVideoListOffsetTestData> = Stream.of(
        GetVideoListOffsetTestData(
            offset = 0,
            limit = 1,
            expectedVideoIDList = listOf(videoContent15.id),
        ),
        GetVideoListOffsetTestData(
            offset = 1,
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent14.id,
                videoContent13.id,
                videoContent12.id,
                videoContent11.id,
                videoContent10.id,
            )
        ),
        GetVideoListOffsetTestData(
            offset = 10,
            limit = 1,
            expectedVideoIDList = listOf(
                videoContent5.id,
            )
        ),
        GetVideoListOffsetTestData(
            offset = 15,
            limit = 10,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListOffsetTestData(
            offset = 10000,
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.searchText] argument. */
    @JvmStatic
    fun checkSearchTextScenarioTestData(): Stream<GetVideoListSearchTextTestData> = Stream.of(
        GetVideoListSearchTextTestData(
            searchText = null,
            limit = 1,
            expectedVideoIDList = listOf(video15.id)
        ),
        GetVideoListSearchTextTestData(
            searchText = null,
            limit = 5,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video13.id,
                video12.id,
                video11.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "",
            limit = 1,
            expectedVideoIDList = listOf(
                video15.id,
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "",
            limit = 5,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video13.id,
                video12.id,
                video11.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "    ",
            limit = 5,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video13.id,
                video12.id,
                video11.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "    ",
            limit = 5,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video13.id,
                video12.id,
                video11.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "Cristiano",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent2.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "  Cristiano",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent2.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "  Cristiano      ",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent2.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "Cristiano      ",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent2.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "dog",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent12.id,
                videoContent5.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "  dog",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent12.id,
                videoContent5.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "  dog   ",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent12.id,
                videoContent5.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "Dog   ",
            limit = 5,
            expectedVideoIDList = listOf(
                videoContent12.id,
                videoContent5.id
            )
        ),
        GetVideoListSearchTextTestData(
            searchText = "D o",
            limit = 5,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListSearchTextTestData(
            searchText = "o g",
            limit = 5,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListSearchTextTestData(
            searchText = "D o g",
            limit = 5,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListSearchTextTestData(
            searchText = generateRandomTextByUUID(),
            limit = 5,
            expectedVideoIDList = emptyList()
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.searchText] argument. */
    @JvmStatic
    fun checkEnvironmentIDScenarioTestData(): Stream<GetVideoListEnvironmentIDTestData> = Stream.of(
        GetVideoListEnvironmentIDTestData(
            limit = 1,
            offset = 3,
            environmentID = null,
            expectedVideoLanguageID = language4.id,
            expectedVideoLanguageName = language4.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedVideoTagID = tag2.id,
            expectedVideoTagName = tag2.translations.getTagTranslationForDefaultEnvironment(),
        ),
        GetVideoListEnvironmentIDTestData(
            limit = 1,
            offset = 3,
            environmentID = -100000,
            expectedVideoLanguageID = language4.id,
            expectedVideoLanguageName = language4.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedVideoTagID = tag2.id,
            expectedVideoTagName = tag2.translations.getTagTranslationForDefaultEnvironment(),
        ),
        GetVideoListEnvironmentIDTestData(
            limit = 1,
            offset = 3,
            environmentID = -1,
            expectedVideoLanguageID = language4.id,
            expectedVideoLanguageName = language4.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedVideoTagID = tag2.id,
            expectedVideoTagName = tag2.translations.getTagTranslationForDefaultEnvironment(),
        ),
        GetVideoListEnvironmentIDTestData(
            limit = 1,
            offset = 3,
            environmentID = 0,
            expectedVideoLanguageID = language4.id,
            expectedVideoLanguageName = language4.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedVideoTagID = tag2.id,
            expectedVideoTagName = tag2.translations.getTagTranslationForDefaultEnvironment(),
        ),
        GetVideoListEnvironmentIDTestData(
            limit = 1,
            offset = 3,
            environmentID = language1.id,
            expectedVideoLanguageID = language4.id,
            expectedVideoLanguageName = language4.translations.getLanguageTranslationForEnvironment(language1.id),
            expectedVideoTagID = tag2.id,
            expectedVideoTagName = tag2.translations.getTagTranslationForEnvironment(language1.id)
        ),
        GetVideoListEnvironmentIDTestData(
            limit = 1,
            offset = 3,
            environmentID = language2.id,
            expectedVideoLanguageID = language4.id,
            expectedVideoLanguageName = language4.translations.getLanguageTranslationForEnvironment(language2.id),
            expectedVideoTagID = tag2.id,
            expectedVideoTagName = tag2.translations.getTagTranslationForEnvironment(language2.id),
        ),
        GetVideoListEnvironmentIDTestData(
            limit = 1,
            offset = 3,
            environmentID = 100000,
            expectedVideoLanguageID = language4.id,
            expectedVideoLanguageName = language4.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedVideoTagID = tag2.id,
            expectedVideoTagName = tag2.translations.getTagTranslationForDefaultEnvironment(),
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.languageIDList] argument. */
    @JvmStatic
    fun checkLanguageIDListScenarioTestData(): Stream<GetVideoListLanguageIDTestData> = Stream.of(
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(-1),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(100000),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language2.id),
            limit = 1,
            expectedVideoIDList = listOf(video14.id)
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language2.id),
            limit = 2,
            expectedVideoIDList = listOf(
                video14.id,
                video3.id,
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language2.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video14.id,
                video3.id,
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language1.id, language2.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video14.id,
                video6.id,
                video3.id,
                video1.id,
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language2.id, language1.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video14.id,
                video6.id,
                video3.id,
                video1.id,
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language1.id, language2.id, language3.id, language4.id),
            limit = 20,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video12.id,
                video10.id,
                video9.id,
                video7.id,
                video6.id,
                video3.id,
                video2.id,
                video1.id
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language4.id, language3.id, language2.id, language1.id),
            limit = 20,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video12.id,
                video10.id,
                video9.id,
                video7.id,
                video6.id,
                video3.id,
                video2.id,
                video1.id
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language4.id, language3.id, language2.id, language1.id),
            limit = 20,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video12.id,
                video10.id,
                video9.id,
                video7.id,
                video6.id,
                video3.id,
                video2.id,
                video1.id
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(language2.id, -1),
            limit = 5,
            expectedVideoIDList = listOf(
                video14.id,
                video3.id,
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(-1, language2.id),
            limit = 5,
            expectedVideoIDList = listOf(
                video14.id,
                video3.id,
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(-1, -100000, language2.id),
            limit = 5,
            expectedVideoIDList = listOf(
                video14.id,
                video3.id,
            )
        ),
        GetVideoListLanguageIDTestData(
            languageIDList = listOf(100000, language2.id),
            limit = 5,
            expectedVideoIDList = listOf(
                video14.id,
                video3.id,
            )
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [Video.availableLanguages] argument. */
    @JvmStatic
    fun checkAvailableLanguageIDListScenarioTestData(): Stream<GetVideoListAvailableLanguageTestData> = Stream.of(
        GetVideoListAvailableLanguageTestData(
            environmentID = null,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language3.id,
                    name = getLanguageByID(language3.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = -1,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language3.id,
                    name = getLanguageByID(language3.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = 0,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language3.id,
                    name = getLanguageByID(language3.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = language1.id,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language3.id,
                    name = getLanguageByID(language3.id)
                        .translations
                        .getLanguageTranslationForEnvironment(language1.id)
                )
            )
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = language2.id,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language3.id,
                    name = getLanguageByID(language3.id)
                        .translations
                        .getLanguageTranslationForEnvironment(language2.id)
                )
            )
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = 100000,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language3.id,
                    name = getLanguageByID(language3.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = null,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = -1,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = 0,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = language1.id,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = language2.id,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetVideoListAvailableLanguageTestData(
            environmentID = 100000,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.authorIDList] argument. */
    @JvmStatic
    fun checkAuthorsScenarioTestData(): Stream<GetVideoListAuthorIDTestData> = Stream.of(
        GetVideoListAuthorIDTestData(
            authorIDList = listOf(-1),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListAuthorIDTestData(
            authorIDList = listOf(100000),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListAuthorIDTestData(
            authorIDList = listOf(author8.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video13.id,
                video9.id,
            )
        ),
        GetVideoListAuthorIDTestData(
            authorIDList = listOf(author7.id, author8.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video13.id,
                video9.id,
            )
        ),
        GetVideoListAuthorIDTestData(
            authorIDList = allAuthors.map(DBAuthor::id),
            limit = 20,
            expectedVideoIDList = listOf(
                video15.id,
                video14.id,
                video13.id,
                video12.id,
                video11.id,
                video9.id,
                video6.id,
                video5.id,
                video3.id,
                video2.id,
                video1.id
            )
        ),
        GetVideoListAuthorIDTestData(
            authorIDList = listOf(author7.id, author8.id, -1),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video13.id,
                video9.id,
            )
        ),
        GetVideoListAuthorIDTestData(
            authorIDList = listOf(author7.id, author8.id, -100000),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video13.id,
                video9.id,
            )
        ),
        GetVideoListAuthorIDTestData(
            authorIDList = listOf(100000, author7.id, author8.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video13.id,
                video9.id,
            )
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [Video.url] argument. */
    @JvmStatic
    fun checkURLScenarioTestData(): Stream<GetVideoListURLTestData> = Stream.of(
        GetVideoListURLTestData(
            offset = 0,
            expectedVideoURL = video15.url
        ),
        GetVideoListURLTestData(
            offset = 5,
            expectedVideoURL = video10.url
        ),
        GetVideoListURLTestData(
            offset = 10,
            expectedVideoURL = video15.url
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.excludedTagIDList] and [VideoListFilter.includedTagIDList] argument. */
    @JvmStatic
    fun checkTagScenarioTestData(): Stream<GetVideoListTagIDTestData> = Stream.of(
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = emptyList(),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = listOf(-1),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = listOf(1),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = listOf(1000),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(0),
            excludedTagIDList = emptyList(),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(100000),
            excludedTagIDList = emptyList(),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag10.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video8.id,
                video4.id,
                video1.id,
            )
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag10.id),
            excludedTagIDList = listOf(tag10.id),
            limit = 10,
            expectedVideoIDList = emptyList(),
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(-1, tag10.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video8.id,
                video4.id,
                video1.id,
            )
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(-1, tag10.id),
            excludedTagIDList = listOf(-1),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video8.id,
                video4.id,
                video1.id,
            )
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(-1, tag10.id),
            excludedTagIDList = listOf(-1, tag10.id),
            limit = 10,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag10.id, -1),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video8.id,
                video4.id,
                video1.id,
            )
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag10.id, 100000),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video8.id,
                video4.id,
                video1.id,
            )
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag10.id, tag6.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video10.id,
                video9.id,
                video8.id,
                video4.id,
                video3.id,
                video1.id,
            )
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag6.id, tag10.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video10.id,
                video9.id,
                video8.id,
                video4.id,
                video3.id,
                video1.id,
            )
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag6.id, tag10.id),
            excludedTagIDList = listOf(tag6.id, tag10.id),
            limit = 10,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListTagIDTestData(
            includedTagIDList = listOf(tag6.id, tag10.id),
            excludedTagIDList = listOf(tag6.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video15.id,
                video8.id,
                video4.id,
                video1.id,
            )
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [VideoListFilter.userIDList] argument. */
    @JvmStatic
    fun checkUserIDScenarioTestData(): Stream<GetVideoListUserIDTestData> = Stream.of(
        GetVideoListUserIDTestData(
            userIDList = listOf(-1),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListUserIDTestData(
            userIDList = listOf(100000),
            limit = 1,
            expectedVideoIDList = emptyList()
        ),
        GetVideoListUserIDTestData(
            userIDList = listOf(user5.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video11.id,
                video8.id,
            )
        ),
        GetVideoListUserIDTestData(
            userIDList = listOf(user5.id, user6.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video11.id,
                video8.id,
                video6.id,
            )
        ),
        GetVideoListUserIDTestData(
            userIDList = listOf(user6.id, user5.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video11.id,
                video8.id,
                video6.id,
            )
        ),
        GetVideoListUserIDTestData(
            userIDList = listOf(user5.id, user6.id, 100000),
            limit = 10,
            expectedVideoIDList = listOf(
                video11.id,
                video8.id,
                video6.id,
            )
        ),
        GetVideoListUserIDTestData(
            userIDList = listOf(-1, user5.id, user6.id),
            limit = 10,
            expectedVideoIDList = listOf(
                video11.id,
                video8.id,
                video6.id,
            )
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [Video.rating] argument. */
    @JvmStatic
    fun checkRatingIDScenarioTestData(): Stream<GetVideoListRatingTestData> = Stream.of(
        GetVideoListRatingTestData(
            offset = 0,
            expectedVideoRating = videoContent15.rating
        ),
        GetVideoListRatingTestData(
            offset = 5,
            expectedVideoRating = videoContent10.rating
        ),
        GetVideoListRatingTestData(
            offset = 10,
            expectedVideoRating = videoContent5.rating
        ),
    )

    /** Data to test [GetVideoListExecutor] to check [Video.commentsCount] argument. */
    @JvmStatic
    fun checkCommentCountIDScenarioTestData(): Stream<GetVideoListCommentCountTestData> = Stream.of(
        GetVideoListCommentCountTestData(
            offset = 0,
            expectedVideoCommentCount = allComment.count { it.contentID == video15.id },
        ),
        GetVideoListCommentCountTestData(
            offset = 5,
            expectedVideoCommentCount = allComment.count { it.contentID == video10.id },
        ),
        GetVideoListCommentCountTestData(
            offset = 10,
            expectedVideoCommentCount = allComment.count { it.contentID == video5.id },
        ),
    )
}