package database.data

import common.utils.generateRandomTextByUUID
import database.external.filter.PictureListFilter
import database.internal.executor.ReadPictureListExecutor
import database.internal.mock.*
import database.internal.model.DBAuthor
import database.utils.*
import database.external.model.language.LanguageSimple
import database.external.model.Picture
import java.util.stream.Stream

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.limit] argument. */
internal data class GetPictureListLimitTestData(
    val limit: Long,
    val expectedPictureIDList: List<Int>
)

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.offset] argument. */
internal data class GetPictureListOffsetTestData(
    val offset: Long,
    val limit: Long,
    val expectedPictureIDList: List<Int>
)

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.searchText] argument. */
internal data class GetPictureListSearchTextTestData(
    val searchText: String?,
    val limit: Long,
    val expectedPictureIDList: List<Int>,
)

/**
 * Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.searchText] argument
 * Used to check if [environmentID] applied to language and tag names.
 * */
internal data class GetPictureListEnvironmentIDTestData(
    val environmentID: Int?,
    val expectedPictureLanguageID: Int,
    val expectedPictureLanguageName: String,
    val expectedPictureTagID: Int,
    val expectedPictureTagName: String
)

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.languageIDList] argument. */
internal data class GetPictureListLanguageIDTestData(
    val languageIDList: List<Int>,
    val limit: Long,
    val expectedPictureIDList: List<Int>,
)

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.environmentLangID] argument. */
internal data class GetPictureListAvailableLanguageTestData(
    val environmentID: Int?,
    val offset: Long,
    val expectedLanguageListSimple: List<LanguageSimple>
)

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.includedTagIDList] and [PictureListFilter.excludedTagIDList] argument. */
internal data class GetPictureListTagIDTestData(
    val includedTagIDList: List<Int>,
    val excludedTagIDList: List<Int>,
    val limit: Long,
    val expectedPictureIDList: List<Int>,
)

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.authorIDList] argument. */
internal data class GetPictureListAuthorIDTestData(
    val authorIDList: List<Int>,
    val limit: Long,
    val expectedPictureIDList: List<Int>,
)

/** Model of data to test [ReadPictureListExecutor] to check [Picture.url] argument. */
internal data class GetPictureListURLTestData(
    val offset: Long,
    val expectedPictureURL: String,
)

/** Model of data to test [ReadPictureListExecutor] to check [PictureListFilter.userIDList] argument. */
internal data class GetPictureListUserIDTestData(
    val userIDList: List<Int>,
    val limit: Long,
    val expectedPictureIDList: List<Int>,
)

/** Model of data to test [ReadPictureListExecutor] to check [Picture.rating] argument. */
internal data class GetPictureListRatingTestData(
    val offset: Long,
    val expectedPictureRating: Int,
)

/** Model of data to test [ReadPictureListExecutor] to check [Picture.commentsCount] argument. */
internal data class GetPictureListCommentCountTestData(
    val offset: Long,
    val expectedPictureCommentCount: Int,
)

/** Data creator to test [ReadPictureListExecutor].*/
internal object GetPictureListTestDataStreamCreator {

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.limit] argument. */
    @JvmStatic
    fun checkLimitScenarioTestData(): Stream<GetPictureListLimitTestData> = Stream.of(
        GetPictureListLimitTestData(
            limit = 0,
            expectedPictureIDList = emptyList(),
        ),
        GetPictureListLimitTestData(
            limit = 1,
            expectedPictureIDList = listOf(
                pictureContent15.id
            )
        ),
        GetPictureListLimitTestData(
            limit = 10,
            expectedPictureIDList = listOf(
                pictureContent15.id,
                pictureContent14.id,
                pictureContent13.id,
                pictureContent12.id,
                pictureContent11.id,
                pictureContent10.id,
                pictureContent9.id,
                pictureContent8.id,
                pictureContent7.id,
                pictureContent6.id,
            )
        ),
        GetPictureListLimitTestData(
            limit = 15,
            expectedPictureIDList = listOf(
                pictureContent15.id,
                pictureContent14.id,
                pictureContent13.id,
                pictureContent12.id,
                pictureContent11.id,
                pictureContent10.id,
                pictureContent9.id,
                pictureContent8.id,
                pictureContent7.id,
                pictureContent6.id,
                pictureContent5.id,
                pictureContent4.id,
                pictureContent3.id,
                pictureContent2.id,
                pictureContent1.id,
            )
        ),
        GetPictureListLimitTestData(
            limit = 10000,
            expectedPictureIDList = listOf(
                pictureContent15.id,
                pictureContent14.id,
                pictureContent13.id,
                pictureContent12.id,
                pictureContent11.id,
                pictureContent10.id,
                pictureContent9.id,
                pictureContent8.id,
                pictureContent7.id,
                pictureContent6.id,
                pictureContent5.id,
                pictureContent4.id,
                pictureContent3.id,
                pictureContent2.id,
                pictureContent1.id,
            )
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.offset] argument. */
    @JvmStatic
    fun checkOffsetScenarioTestData(): Stream<GetPictureListOffsetTestData> = Stream.of(
        GetPictureListOffsetTestData(
            offset = 0,
            limit = 1,
            expectedPictureIDList = listOf(pictureContent15.id),
        ),
        GetPictureListOffsetTestData(
            offset = 1,
            limit = 5,
            expectedPictureIDList = listOf(
                pictureContent14.id,
                pictureContent13.id,
                pictureContent12.id,
                pictureContent11.id,
                pictureContent10.id,
            )
        ),
        GetPictureListOffsetTestData(
            offset = 10,
            limit = 1,
            expectedPictureIDList = listOf(
                pictureContent5.id,
            )
        ),
        GetPictureListOffsetTestData(
            offset = 15,
            limit = 10,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListOffsetTestData(
            offset = 10000,
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.searchText] argument. */
    @JvmStatic
    fun checkSearchTextScenarioTestData(): Stream<GetPictureListSearchTextTestData> = Stream.of(
        GetPictureListSearchTextTestData(
            searchText = null,
            limit = 1,
            expectedPictureIDList = listOf(
                picture15.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = null,
            limit = 5,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture13.id,
                picture12.id,
                picture11.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "",
            limit = 1,
            expectedPictureIDList = listOf(
                picture15.id,
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "",
            limit = 5,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture13.id,
                picture12.id,
                picture11.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "    ",
            limit = 1,
            expectedPictureIDList = listOf(
                picture15.id,
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "    ",
            limit = 5,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture13.id,
                picture12.id,
                picture11.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "Golden",
            limit = 5,
            expectedPictureIDList = listOf(
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "  Golden",
            limit = 5,
            expectedPictureIDList = listOf(
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "  Golden      ",
            limit = 5,
            expectedPictureIDList = listOf(
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "Golden      ",
            limit = 5,
            expectedPictureIDList = listOf(
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "Dog",
            limit = 5,
            expectedPictureIDList = listOf(
                picture6.id,
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "  Dog",
            limit = 5,
            expectedPictureIDList = listOf(
                picture6.id,
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "  Dog   ",
            limit = 5,
            expectedPictureIDList = listOf(
                picture6.id,
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "Dog   ",
            limit = 5,
            expectedPictureIDList = listOf(
                picture6.id,
                picture2.id
            )
        ),
        GetPictureListSearchTextTestData(
            searchText = "D o",
            limit = 5,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListSearchTextTestData(
            searchText = "o g",
            limit = 5,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListSearchTextTestData(
            searchText = "D o g",
            limit = 5,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListSearchTextTestData(
            searchText = generateRandomTextByUUID(),
            limit = 5,
            expectedPictureIDList = emptyList()
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.environmentLangID] argument. */
    @JvmStatic
    fun checkEnvironmentIDScenarioTestData(): Stream<GetPictureListEnvironmentIDTestData> = Stream.of(
        GetPictureListEnvironmentIDTestData(
            environmentID = null,
            expectedPictureLanguageID = language2.id,
            expectedPictureLanguageName = language2.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedPictureTagID = tag10.id,
            expectedPictureTagName = tag10.translations.getTagTranslationForDefaultEnvironment(),
        ),
        GetPictureListEnvironmentIDTestData(
            environmentID = -1,
            expectedPictureLanguageID = language2.id,
            expectedPictureLanguageName = language2.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedPictureTagID = tag10.id,
            expectedPictureTagName = tag10.translations.getTagTranslationForDefaultEnvironment(),
        ),
        GetPictureListEnvironmentIDTestData(
            environmentID = 4,
            expectedPictureLanguageID = language2.id,
            expectedPictureLanguageName = language2.translations.getLanguageTranslationForEnvironment(4),
            expectedPictureTagID = tag10.id,
            expectedPictureTagName = tag10.translations.getTagTranslationForEnvironment(4),
        ),
        GetPictureListEnvironmentIDTestData(
            environmentID = 100000,
            expectedPictureLanguageID = language2.id,
            expectedPictureLanguageName = language2.translations.getLanguageTranslationForDefaultEnvironment(),
            expectedPictureTagID = tag10.id,
            expectedPictureTagName = tag10.translations.getTagTranslationForDefaultEnvironment(),
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.languageIDList] argument. */
    @JvmStatic
    fun checkLanguageIDListScenarioTestData(): Stream<GetPictureListLanguageIDTestData> = Stream.of(
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(-1),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(100000),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language2.id),
            limit = 1,
            expectedPictureIDList = listOf(
                picture15.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language2.id),
            limit = 2,
            expectedPictureIDList = listOf(
                picture15.id,
                picture10.id,
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language2.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture10.id,
                picture3.id,
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language1.id, language2.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture12.id,
                picture10.id,
                picture9.id,
                picture6.id,
                picture4.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language2.id, language1.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture12.id,
                picture10.id,
                picture9.id,
                picture6.id,
                picture4.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language1.id, language2.id, language3.id, language4.id),
            limit = 20,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture12.id,
                picture11.id,
                picture10.id,
                picture9.id,
                picture7.id,
                picture6.id,
                picture5.id,
                picture4.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language4.id, language3.id, language2.id, language1.id),
            limit = 20,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture12.id,
                picture11.id,
                picture10.id,
                picture9.id,
                picture7.id,
                picture6.id,
                picture5.id,
                picture4.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language4.id, language3.id, language2.id, language1.id),
            limit = 20,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture12.id,
                picture11.id,
                picture10.id,
                picture9.id,
                picture7.id,
                picture6.id,
                picture5.id,
                picture4.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(language2.id, -1),
            limit = 5,
            expectedPictureIDList = listOf(
                picture15.id,
                picture10.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(-1, language2.id),
            limit = 5,
            expectedPictureIDList = listOf(
                picture15.id,
                picture10.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(-1, -100000, language2.id),
            limit = 5,
            expectedPictureIDList = listOf(
                picture15.id,
                picture10.id,
                picture3.id
            )
        ),
        GetPictureListLanguageIDTestData(
            languageIDList = listOf(100000, language2.id),
            limit = 5,
            expectedPictureIDList = listOf(
                picture15.id,
                picture10.id,
                picture3.id
            )
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [Picture.availableLanguages] argument. */
    @JvmStatic
    fun checkAvailableLanguageIDListScenarioTestData(): Stream<GetPictureListAvailableLanguageTestData> = Stream.of(
        GetPictureListAvailableLanguageTestData(
            environmentID = null,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language2.id,
                    name = getLanguageByID(language2.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = -1,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language2.id,
                    name = getLanguageByID(language2.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = 0,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language2.id,
                    name = getLanguageByID(language2.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = 100000,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language2.id,
                    name = getLanguageByID(language2.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = language1.id,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language2.id,
                    name = getLanguageByID(language2.id)
                        .translations
                        .getLanguageTranslationForEnvironment(language1.id)
                )
            )
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = language4.id,
            offset = 0,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language2.id,
                    name = getLanguageByID(language2.id)
                        .translations
                        .getLanguageTranslationForEnvironment(language4.id)
                )
            )
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = null,
            offset = 1,
            expectedLanguageListSimple = listOf(
                LanguageSimple(
                    id = language1.id,
                    name = getLanguageByID(language1.id)
                        .translations
                        .getLanguageTranslationForDefaultEnvironment()
                )
            )
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = null,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = -1,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = 0,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = 1,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
        GetPictureListAvailableLanguageTestData(
            environmentID = 100000,
            offset = 2,
            expectedLanguageListSimple = emptyList()
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.authorIDList] argument. */
    @JvmStatic
    fun checkAuthorsScenarioTestData(): Stream<GetPictureListAuthorIDTestData> = Stream.of(
        GetPictureListAuthorIDTestData(
            authorIDList = listOf(-1),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListAuthorIDTestData(
            authorIDList = listOf(100000),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListAuthorIDTestData(
            authorIDList = listOf(author8.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture9.id,
                picture6.id,
                picture3.id
            )
        ),
        GetPictureListAuthorIDTestData(
            authorIDList = listOf(author7.id, author8.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture11.id,
                picture9.id,
                picture6.id,
                picture3.id
            )
        ),
        GetPictureListAuthorIDTestData(
            authorIDList = allAuthors.map(DBAuthor::id),
            limit = 20,
            expectedPictureIDList = listOf(
                picture15.id,
                picture14.id,
                picture13.id,
                picture12.id,
                picture11.id,
                picture10.id,
                picture9.id,
                picture8.id,
                picture6.id,
                picture5.id,
                picture3.id,
                picture2.id,
                picture1.id
            )
        ),
        GetPictureListAuthorIDTestData(
            authorIDList = listOf(author7.id, author8.id, -1),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture11.id,
                picture9.id,
                picture6.id,
                picture3.id
            )
        ),
        GetPictureListAuthorIDTestData(
            authorIDList = listOf(author7.id, author8.id, -100000),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture11.id,
                picture9.id,
                picture6.id,
                picture3.id
            )
        ),
        GetPictureListAuthorIDTestData(
            authorIDList = listOf(100000, author7.id, author8.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture11.id,
                picture9.id,
                picture6.id,
                picture3.id
            )
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [Picture.url] argument. */
    @JvmStatic
    fun checkURLScenarioTestData(): Stream<GetPictureListURLTestData> = Stream.of(
        GetPictureListURLTestData(
            offset = 0,
            expectedPictureURL = picture15.url
        ),
        GetPictureListURLTestData(
            offset = 5,
            expectedPictureURL = picture10.url
        ),
        GetPictureListURLTestData(
            offset = 10,
            expectedPictureURL = picture5.url
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.excludedTagIDList] and [PictureListFilter.includedTagIDList] argument. */
    @JvmStatic
    fun checkTagScenarioTestData(): Stream<GetPictureListTagIDTestData> = Stream.of(
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = emptyList(),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = listOf(-1),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = listOf(1),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(-1),
            excludedTagIDList = listOf(1000),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(0),
            excludedTagIDList = emptyList(),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(100000),
            excludedTagIDList = emptyList(),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag10.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture12.id,
                picture10.id,
                picture8.id,
            )
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag10.id),
            excludedTagIDList = listOf(tag10.id),
            limit = 10,
            expectedPictureIDList = emptyList(),
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(-1, tag10.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture12.id,
                picture10.id,
                picture8.id,
            )
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(-1, tag10.id),
            excludedTagIDList = listOf(-1),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture12.id,
                picture10.id,
                picture8.id,
            )
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(-1, tag10.id),
            excludedTagIDList = listOf(-1, tag10.id),
            limit = 10,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag10.id, -1),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture12.id,
                picture10.id,
                picture8.id,
            )
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag10.id, 100000),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture12.id,
                picture10.id,
                picture8.id,
            )
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag10.id, tag6.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture12.id,
                picture10.id,
                picture9.id,
                picture8.id,
                picture7.id,
                picture4.id,
                picture3.id,
            )
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag6.id, tag10.id),
            excludedTagIDList = emptyList(),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture12.id,
                picture10.id,
                picture9.id,
                picture8.id,
                picture7.id,
                picture4.id,
                picture3.id,
            )
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag6.id, tag10.id),
            excludedTagIDList = listOf(tag6.id, tag10.id),
            limit = 10,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListTagIDTestData(
            includedTagIDList = listOf(tag6.id, tag10.id),
            excludedTagIDList = listOf(tag6.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture15.id,
                picture10.id,
                picture8.id,
            )
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [PictureListFilter.userIDList] argument. */
    @JvmStatic
    fun checkUserIDScenarioTestData(): Stream<GetPictureListUserIDTestData> = Stream.of(
        GetPictureListUserIDTestData(
            userIDList = listOf(-1),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListUserIDTestData(
            userIDList = listOf(100000),
            limit = 1,
            expectedPictureIDList = emptyList()
        ),
        GetPictureListUserIDTestData(
            userIDList = listOf(user5.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture12.id,
                picture9.id,
                picture2.id,
            )
        ),
        GetPictureListUserIDTestData(
            userIDList = listOf(user5.id, user6.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture13.id,
                picture12.id,
                picture9.id,
                picture2.id,
            )
        ),
        GetPictureListUserIDTestData(
            userIDList = listOf(user6.id, user5.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture13.id,
                picture12.id,
                picture9.id,
                picture2.id,
            )
        ),
        GetPictureListUserIDTestData(
            userIDList = listOf(user5.id, user6.id, 100000),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture13.id,
                picture12.id,
                picture9.id,
                picture2.id,
            )
        ),
        GetPictureListUserIDTestData(
            userIDList = listOf(-1, user5.id, user6.id),
            limit = 10,
            expectedPictureIDList = listOf(
                picture14.id,
                picture13.id,
                picture12.id,
                picture9.id,
                picture2.id,
            )
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [Picture.rating] argument. */
    @JvmStatic
    fun checkRatingIDScenarioTestData(): Stream<GetPictureListRatingTestData> = Stream.of(
        GetPictureListRatingTestData(
            offset = 0,
            expectedPictureRating = pictureContent15.rating
        ),
        GetPictureListRatingTestData(
            offset = 5,
            expectedPictureRating = pictureContent10.rating
        ),
        GetPictureListRatingTestData(
            offset = 10,
            expectedPictureRating = pictureContent5.rating
        ),
    )

    /** Data to test [ReadPictureListExecutor] to check [Picture.commentsCount] argument. */
    @JvmStatic
    fun checkCommentCountIDScenarioTestData(): Stream<GetPictureListCommentCountTestData> = Stream.of(
        GetPictureListCommentCountTestData(
            offset = 0,
            expectedPictureCommentCount = allComment.count { it.contentID == picture15.id },
        ),
        GetPictureListCommentCountTestData(
            offset = 5,
            expectedPictureCommentCount = allComment.count { it.contentID == picture10.id },
        ),
        GetPictureListCommentCountTestData(
            offset = 10,
            expectedPictureCommentCount = allComment.count { it.contentID == picture5.id },
        ),
    )
}