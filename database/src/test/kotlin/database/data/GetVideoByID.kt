package database.data

import database.external.model.*
import database.external.model.group.Group
import database.external.model.group.GroupMember
import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple
import database.internal.executor.ReadVideoByIDExecutor
import database.internal.indexToUserRole
import database.internal.mock.*
import database.utils.*
import java.util.stream.Stream

/** Model of data to test [ReadVideoByIDExecutor] where no data result expected. */
internal data class GetVideoByIDTSimpleTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.id]. */
internal data class GetVideoByIDWithExpectedVideoIDTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedID: Int
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.title]. */
internal data class GetVideoByIDWithExpectedTitleTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedTitle: String
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.url]. */
internal data class GetVideoByIDWithExpectedVideoURLTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedURL: String
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.language]. */
internal data class GetVideoByIDWithExpectedLanguageTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedLanguageSimple: LanguageSimple?
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.availableLanguages]. */
internal data class GetVideoByIDWithExpectedAvailableLanguageTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedAvailableLanguageListSimple: List<LanguageSimple>
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.authors]. */
internal data class GetVideoByIDWithExpectedAuthorsTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedAuthorList: List<Author>
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.tags]. */
internal data class GetVideoByIDWithExpectedTagsTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedTagSimpleList: List<TagSimple>
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.user]. */
internal data class GetVideoByIDWithExpectedUserTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedUserSimple: UserSimple
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.rating]. */
internal data class GetVideoByIDWithExpectedRatingTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedRating: Int
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.commentsCount]. */
internal data class GetVideoByIDWithCommentCountTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedCommentCount: Int
)

/** Model of data to test [ReadVideoByIDExecutor] and check [Video.groups]. */
internal data class GetVideoByIDWithGroupsTestData(
    val videoID: Int,
    val environmentLangID: Int? = null,
    val expectedGroups: List<Group>
)

/** Data creator to test [ReadVideoByIDExecutor].*/
internal object GetVideoByIDTestDataStreamCreator {

    /** Data to test [ReadVideoByIDExecutor] when [GetVideoByIDTSimpleTestData.videoID] link to not existed content. */
    @JvmStatic
    fun notExistedContentScenarioTestData(): Stream<GetVideoByIDTSimpleTestData> = Stream.of(
        GetVideoByIDTSimpleTestData(
            videoID = -1,
            environmentLangID = null,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = -1,
            environmentLangID = -1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = -1,
            environmentLangID = 0,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = -1,
            environmentLangID = 1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = -1,
            environmentLangID = 2,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = -1,
            environmentLangID = 100000,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = 100000,
            environmentLangID = null,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = 100000,
            environmentLangID = -1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = 100000,
            environmentLangID = 0,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = 100000,
            environmentLangID = 1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = 100000,
            environmentLangID = 2,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = 100000,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] when [GetVideoByIDTSimpleTestData.videoID] link to not wrong content. */
    @JvmStatic
    fun wrongContentScenarioTestData(): Stream<GetVideoByIDTSimpleTestData> = Stream.of(
        GetVideoByIDTSimpleTestData(
            videoID = story12.id,
            environmentLangID = null,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = story12.id,
            environmentLangID = -1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = story12.id,
            environmentLangID = 0,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = story12.id,
            environmentLangID = 1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = story12.id,
            environmentLangID = 2,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = story12.id,
            environmentLangID = 100000,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = picture7.id,
            environmentLangID = null,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = picture7.id,
            environmentLangID = -1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = picture7.id,
            environmentLangID = 0,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = picture7.id,
            environmentLangID = 1,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = picture7.id,
            environmentLangID = 2,
        ),
        GetVideoByIDTSimpleTestData(
            videoID = picture7.id,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.id]. */
    @JvmStatic
    fun checkVideoIDScenarioTestData(): Stream<GetVideoByIDWithExpectedVideoIDTestData> = Stream.of(
        GetVideoByIDWithExpectedVideoIDTestData(
            videoID = videoContent10.id,
            environmentLangID = null,
            expectedID = videoContent10.id
        ),
        GetVideoByIDWithExpectedVideoIDTestData(
            videoID = videoContent10.id,
            environmentLangID = -1,
            expectedID = videoContent10.id
        ),
        GetVideoByIDWithExpectedVideoIDTestData(
            videoID = videoContent10.id,
            environmentLangID = 0,
            expectedID = videoContent10.id
        ),
        GetVideoByIDWithExpectedVideoIDTestData(
            videoID = videoContent10.id,
            environmentLangID = language1.id,
            expectedID = videoContent10.id
        ),
        GetVideoByIDWithExpectedVideoIDTestData(
            videoID = videoContent10.id,
            environmentLangID = language2.id,
            expectedID = videoContent10.id
        ),
        GetVideoByIDWithExpectedVideoIDTestData(
            videoID = videoContent10.id,
            environmentLangID = 100000,
            expectedID = videoContent10.id
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.title]. */
    @JvmStatic
    fun checkVideoTitleScenarioTestData(): Stream<GetVideoByIDWithExpectedTitleTestData> = Stream.of(
        GetVideoByIDWithExpectedTitleTestData(
            videoID = videoContent15.id,
            environmentLangID = null,
            expectedTitle = videoContent15.title
        ),
        GetVideoByIDWithExpectedTitleTestData(
            videoID = videoContent15.id,
            environmentLangID = -1,
            expectedTitle = videoContent15.title
        ),
        GetVideoByIDWithExpectedTitleTestData(
            videoID = videoContent15.id,
            environmentLangID = 0,
            expectedTitle = videoContent15.title
        ),
        GetVideoByIDWithExpectedTitleTestData(
            videoID = videoContent15.id,
            environmentLangID = 1,
            expectedTitle = videoContent15.title
        ),
        GetVideoByIDWithExpectedTitleTestData(
            videoID = videoContent15.id,
            environmentLangID = 100000,
            expectedTitle = videoContent15.title
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.url]. */
    @JvmStatic
    fun checkVideoURLScenarioTestData(): Stream<GetVideoByIDWithExpectedVideoURLTestData> = Stream.of(
        GetVideoByIDWithExpectedVideoURLTestData(
            videoID = videoContent2.id,
            environmentLangID = null,
            expectedURL = video2.url
        ),
        GetVideoByIDWithExpectedVideoURLTestData(
            videoID = videoContent2.id,
            environmentLangID = -1,
            expectedURL = video2.url
        ),
        GetVideoByIDWithExpectedVideoURLTestData(
            videoID = videoContent2.id,
            environmentLangID = 0,
            expectedURL = video2.url
        ),
        GetVideoByIDWithExpectedVideoURLTestData(
            videoID = videoContent2.id,
            environmentLangID = language1.id,
            expectedURL = video2.url
        ),
        GetVideoByIDWithExpectedVideoURLTestData(
            videoID = videoContent2.id,
            environmentLangID = 100000,
            expectedURL = video2.url
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.language]. */
    @JvmStatic
    fun checkVideoLanguageScenarioTestData(): Stream<GetVideoByIDWithExpectedLanguageTestData> = Stream.of(
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video4.id,
            environmentLangID = null,
            expectedLanguageSimple = null
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video4.id,
            environmentLangID = -100000,
            expectedLanguageSimple = null
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video4.id,
            environmentLangID = -1,
            expectedLanguageSimple = null
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video4.id,
            environmentLangID = 0,
            expectedLanguageSimple = null
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video4.id,
            environmentLangID = language1.id,
            expectedLanguageSimple = null
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video4.id,
            environmentLangID = language2.id,
            expectedLanguageSimple = null
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video4.id,
            environmentLangID = 100000,
            expectedLanguageSimple = null
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video6.id,
            environmentLangID = null,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video6.id,
            environmentLangID = -1,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video6.id,
            environmentLangID = 0,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video6.id,
            environmentLangID = language1.id,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language1.id)
            )
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video6.id,
            environmentLangID = language2.id,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language2.id)
            )
        ),
        GetVideoByIDWithExpectedLanguageTestData(
            videoID = video6.id,
            environmentLangID = 100000,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.availableLanguages]. */
    @JvmStatic
    fun checkVideoAvailableLanguageScenarioTestData(): Stream<GetVideoByIDWithExpectedAvailableLanguageTestData> =
        Stream.of(
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video4.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = emptyList()
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video4.id,
                environmentLangID = -100000,
                expectedAvailableLanguageListSimple = emptyList()
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video4.id,
                environmentLangID = -1,
                expectedAvailableLanguageListSimple = emptyList()
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video4.id,
                environmentLangID = 0,
                expectedAvailableLanguageListSimple = emptyList()
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video4.id,
                environmentLangID = language1.id,
                expectedAvailableLanguageListSimple = emptyList()
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video4.id,
                environmentLangID = language2.id,
                expectedAvailableLanguageListSimple = emptyList()
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video4.id,
                environmentLangID = 100000,
                expectedAvailableLanguageListSimple = emptyList()
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video1.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video1.id,
                environmentLangID = -100000,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video1.id,
                environmentLangID = -1,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video1.id,
                environmentLangID = 0,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video1.id,
                environmentLangID = language1.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video1.id,
                environmentLangID = language2.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video1.id,
                environmentLangID = 100000,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = -100000,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = -1,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = 0,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = language1.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = language2.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                )
            ),
            GetVideoByIDWithExpectedAvailableLanguageTestData(
                videoID = video9.id,
                environmentLangID = 100000,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
        )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.authors]. */
    @JvmStatic
    fun checkVideoAuthorsScenarioTestData(): Stream<GetVideoByIDWithExpectedAuthorsTestData> = Stream.of(
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video7.id,
            environmentLangID = null,
            expectedAuthorList = emptyList()
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video7.id,
            environmentLangID = -1,
            expectedAuthorList = emptyList()
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video7.id,
            environmentLangID = 0,
            expectedAuthorList = emptyList()
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video7.id,
            environmentLangID = language1.id,
            expectedAuthorList = emptyList()
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video7.id,
            environmentLangID = language2.id,
            expectedAuthorList = emptyList()
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video7.id,
            environmentLangID = 100000,
            expectedAuthorList = emptyList()
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video6.id,
            environmentLangID = null,
            expectedAuthorList = listOf(
                Author(
                    id = author6.id,
                    name = author6.name
                )
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video6.id,
            environmentLangID = -1,
            expectedAuthorList = listOf(
                Author(
                    id = author6.id,
                    name = author6.name
                )
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video6.id,
            environmentLangID = 0,
            expectedAuthorList = listOf(
                Author(
                    id = author6.id,
                    name = author6.name
                )
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video6.id,
            environmentLangID = language1.id,
            expectedAuthorList = listOf(
                Author(
                    id = author6.id,
                    name = author6.name
                )
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video6.id,
            environmentLangID = language2.id,
            expectedAuthorList = listOf(
                Author(
                    id = author6.id,
                    name = author6.name
                )
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video6.id,
            environmentLangID = 100000,
            expectedAuthorList = listOf(
                Author(
                    id = author6.id,
                    name = author6.name
                )
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video14.id,
            environmentLangID = null,
            expectedAuthorList = listOf(
                Author(
                    id = author3.id,
                    name = author3.name
                ),
                Author(
                    id = author2.id,
                    name = author2.name
                ),
                Author(
                    id = author1.id,
                    name = author1.name
                ),
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video14.id,
            environmentLangID = -100000,
            expectedAuthorList = listOf(
                Author(
                    id = author3.id,
                    name = author3.name
                ),
                Author(
                    id = author2.id,
                    name = author2.name
                ),
                Author(
                    id = author1.id,
                    name = author1.name
                ),
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video14.id,
            environmentLangID = -1,
            expectedAuthorList = listOf(
                Author(
                    id = author3.id,
                    name = author3.name
                ),
                Author(
                    id = author2.id,
                    name = author2.name
                ),
                Author(
                    id = author1.id,
                    name = author1.name
                ),
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video14.id,
            environmentLangID = 0,
            expectedAuthorList = listOf(
                Author(
                    id = author3.id,
                    name = author3.name
                ),
                Author(
                    id = author2.id,
                    name = author2.name
                ),
                Author(
                    id = author1.id,
                    name = author1.name
                ),
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video14.id,
            environmentLangID = language1.id,
            expectedAuthorList = listOf(
                Author(
                    id = author3.id,
                    name = author3.name
                ),
                Author(
                    id = author2.id,
                    name = author2.name
                ),
                Author(
                    id = author1.id,
                    name = author1.name
                ),
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video14.id,
            environmentLangID = language2.id,
            expectedAuthorList = listOf(
                Author(
                    id = author3.id,
                    name = author3.name
                ),
                Author(
                    id = author2.id,
                    name = author2.name
                ),
                Author(
                    id = author1.id,
                    name = author1.name
                ),
            )
        ),
        GetVideoByIDWithExpectedAuthorsTestData(
            videoID = video14.id,
            environmentLangID = 100000,
            expectedAuthorList = listOf(
                Author(
                    id = author3.id,
                    name = author3.name
                ),
                Author(
                    id = author2.id,
                    name = author2.name
                ),
                Author(
                    id = author1.id,
                    name = author1.name
                ),
            )
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.tags]. */
    @JvmStatic
    fun checkVideoTagsScenarioTestData(): Stream<GetVideoByIDWithExpectedTagsTestData> = Stream.of(
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video8.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video8.id,
            environmentLangID = -100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video8.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video8.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video8.id,
            environmentLangID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video8.id,
            environmentLangID = language2.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForEnvironment(language2.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video8.id,
            environmentLangID = 100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video10.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag5.id,
                    name = getTagByID(tag5.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video10.id,
            environmentLangID = -100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag5.id,
                    name = getTagByID(tag5.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video10.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag5.id,
                    name = getTagByID(tag5.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video10.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag5.id,
                    name = getTagByID(tag5.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video10.id,
            environmentLangID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag5.id,
                    name = getTagByID(tag5.id)
                        .translations
                        .getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video10.id,
            environmentLangID = language2.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForEnvironment(language2.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag5.id,
                    name = getTagByID(tag5.id)
                        .translations
                        .getTagTranslationForEnvironment(language2.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetVideoByIDWithExpectedTagsTestData(
            videoID = video10.id,
            environmentLangID = 100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag5.id,
                    name = getTagByID(tag5.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.user]. */
    @JvmStatic
    fun checkVideoUserScenarioTestData(): Stream<GetVideoByIDWithExpectedUserTestData> = Stream.of(
        GetVideoByIDWithExpectedUserTestData(
            videoID = video6.id,
            environmentLangID = null,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetVideoByIDWithExpectedUserTestData(
            videoID = video6.id,
            environmentLangID = -1,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetVideoByIDWithExpectedUserTestData(
            videoID = video6.id,
            environmentLangID = 0,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetVideoByIDWithExpectedUserTestData(
            videoID = video6.id,
            environmentLangID = language1.id,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetVideoByIDWithExpectedUserTestData(
            videoID = video6.id,
            environmentLangID = language2.id,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetVideoByIDWithExpectedUserTestData(
            videoID = video6.id,
            environmentLangID = 100000,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.rating]. */
    @JvmStatic
    fun checkVideoRatingScenarioTestData(): Stream<GetVideoByIDWithExpectedRatingTestData> = Stream.of(
        GetVideoByIDWithExpectedRatingTestData(
            videoID = video11.id,
            environmentLangID = null,
            expectedRating = videoContent11.rating
        ),
        GetVideoByIDWithExpectedRatingTestData(
            videoID = video11.id,
            environmentLangID = -1,
            expectedRating = videoContent11.rating
        ),
        GetVideoByIDWithExpectedRatingTestData(
            videoID = video11.id,
            environmentLangID = 0,
            expectedRating = videoContent11.rating
        ),
        GetVideoByIDWithExpectedRatingTestData(
            videoID = video11.id,
            environmentLangID = language1.id,
            expectedRating = videoContent11.rating
        ),
        GetVideoByIDWithExpectedRatingTestData(
            videoID = video11.id,
            environmentLangID = language2.id,
            expectedRating = videoContent11.rating
        ),
        GetVideoByIDWithExpectedRatingTestData(
            videoID = video11.id,
            environmentLangID = 100000,
            expectedRating = videoContent11.rating
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.commentsCount]. */
    @JvmStatic
    fun checkVideoCommentCountScenarioTestData(): Stream<GetVideoByIDWithCommentCountTestData> = Stream.of(
        GetVideoByIDWithCommentCountTestData(
            videoID = videoContent15.id,
            environmentLangID = null,
            expectedCommentCount = allComment.count { it.contentID == videoContent15.id }
        ),
        GetVideoByIDWithCommentCountTestData(
            videoID = videoContent15.id,
            environmentLangID = -1,
            expectedCommentCount = allComment.count { it.contentID == videoContent15.id }
        ),
        GetVideoByIDWithCommentCountTestData(
            videoID = videoContent15.id,
            environmentLangID = 0,
            expectedCommentCount = allComment.count { it.contentID == videoContent15.id }
        ),
        GetVideoByIDWithCommentCountTestData(
            videoID = videoContent15.id,
            environmentLangID = language1.id,
            expectedCommentCount = allComment.count { it.contentID == videoContent15.id }
        ),
        GetVideoByIDWithCommentCountTestData(
            videoID = videoContent15.id,
            environmentLangID = language2.id,
            expectedCommentCount = allComment.count { it.contentID == videoContent15.id }
        ),
        GetVideoByIDWithCommentCountTestData(
            videoID = videoContent15.id,
            environmentLangID = 100000,
            expectedCommentCount = allComment.count { it.contentID == videoContent15.id }
        ),
    )

    /** Data to test [ReadVideoByIDExecutor] to check [Video.groups]. */
    @JvmStatic
    fun checkVideoGroupsScenarioTestData(): Stream<GetVideoByIDWithGroupsTestData> = Stream.of(
        GetVideoByIDWithGroupsTestData(
            videoID = video1.id,
            environmentLangID = null,
            expectedGroups = emptyList()
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = video1.id,
            environmentLangID = -1,
            expectedGroups = emptyList()
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = video1.id,
            environmentLangID = 0,
            expectedGroups = emptyList()
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = video1.id,
            environmentLangID = language1.id,
            expectedGroups = emptyList()
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = video1.id,
            environmentLangID = language2.id,
            expectedGroups = emptyList()
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = video1.id,
            environmentLangID = 100000,
            expectedGroups = emptyList()
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent2.id,
            environmentLangID = null,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                )
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent2.id,
            environmentLangID = -1,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                )
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent2.id,
            environmentLangID = 0,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                )
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent2.id,
            environmentLangID = language1.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                )
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent2.id,
            environmentLangID = language2.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                )
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent2.id,
            environmentLangID = 100000,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                )
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent7.id,
            environmentLangID = null,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                ),
                Group(
                    name = contentGroup4.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember9.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent14.id,
                            name = videoContent14.title,
                            order = contentGroupMember10.orderIDX
                        ),
                    )
                ),
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent7.id,
            environmentLangID = -1,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                ),
                Group(
                    name = contentGroup4.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember9.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent14.id,
                            name = videoContent14.title,
                            order = contentGroupMember10.orderIDX
                        ),
                    )
                ),
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent7.id,
            environmentLangID = 0,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                ),
                Group(
                    name = contentGroup4.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember9.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent14.id,
                            name = videoContent14.title,
                            order = contentGroupMember10.orderIDX
                        ),
                    )
                ),
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent7.id,
            environmentLangID = language1.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                ),
                Group(
                    name = contentGroup4.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember9.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent14.id,
                            name = videoContent14.title,
                            order = contentGroupMember10.orderIDX
                        ),
                    )
                ),
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent7.id,
            environmentLangID = language4.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                ),
                Group(
                    name = contentGroup4.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember9.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent14.id,
                            name = videoContent14.title,
                            order = contentGroupMember10.orderIDX
                        ),
                    )
                ),
            )
        ),
        GetVideoByIDWithGroupsTestData(
            videoID = videoContent7.id,
            environmentLangID = 100000,
            expectedGroups = listOf(
                Group(
                    name = contentGroup3.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent2.id,
                            name = videoContent2.title,
                            order = contentGroupMember7.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember8.orderIDX
                        ),
                    )
                ),
                Group(
                    name = contentGroup4.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = videoContent7.id,
                            name = videoContent7.title,
                            order = contentGroupMember9.orderIDX
                        ),
                        GroupMember(
                            contentID = videoContent14.id,
                            name = videoContent14.title,
                            order = contentGroupMember10.orderIDX
                        ),
                    )
                ),
            )
        ),
    )
}