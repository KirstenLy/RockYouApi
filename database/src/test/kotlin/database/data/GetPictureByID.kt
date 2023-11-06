package database.data

import database.external.model.*
import database.external.model.group.Group
import database.external.model.group.GroupMember
import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple
import database.internal.executor.ReadPictureByIDExecutor
import database.internal.indexToUserRole
import database.internal.mock.*
import database.utils.*
import java.util.stream.Stream

/** Model of data to test [ReadPictureByIDExecutor] where no data result expected. */
internal data class GetPictureByIDTSimpleTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.id]. */
internal data class GetPictureByIDWithExpectedPictureIDTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedID: Int
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.title]. */
internal data class GetPictureByIDWithExpectedTitleTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedTitle: String
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.url]. */
internal data class GetPictureByIDWithExpectedURLTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedURL: String
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.language]. */
internal data class GetPictureByIDWithExpectedLanguageTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedLanguageSimple: LanguageSimple?
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.availableLanguages]. */
internal data class GetPictureByIDWithExpectedAvailableLanguageTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedAvailableLanguageListSimple: List<LanguageSimple>
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.authors]. */
internal data class GetPictureByIDWithExpectedAuthorsTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedAuthorList: List<Author>
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.tags]. */
internal data class GetPictureByIDWithExpectedTagsTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedTagSimpleList: List<TagSimple>
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.user]. */
internal data class GetPictureByIDWithExpectedUserTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedUserSimple: UserSimple
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.rating]. */
internal data class GetPictureByIDWithExpectedRatingTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedRating: Int
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.commentsCount]. */
internal data class GetPictureByIDWithCommentCountTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedCommentCount: Int
)

/** Model of data to test [ReadPictureByIDExecutor] and check [Picture.groups]. */
internal data class GetPictureByIDWithGroupsTestData(
    val pictureID: Int,
    val environmentLangID: Int? = null,
    val expectedGroups: List<Group>
)

/** Data creator to test [ReadPictureByIDExecutor].*/
internal object GetPictureByIDTestDataStreamCreator {

    /** Data to test [ReadPictureByIDExecutor] when [GetPictureByIDTSimpleTestData.pictureID] link to not existed content. */
    @JvmStatic
    fun notExistedContentScenarioTestData(): Stream<GetPictureByIDTSimpleTestData> = Stream.of(
        GetPictureByIDTSimpleTestData(
            pictureID = -1,
            environmentLangID = null,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = -1,
            environmentLangID = -1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = -1,
            environmentLangID = 0,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = -1,
            environmentLangID = 1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = -1,
            environmentLangID = 2,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = -1,
            environmentLangID = 100000,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = 100000,
            environmentLangID = null,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = 100000,
            environmentLangID = -1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = 100000,
            environmentLangID = 0,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = 100000,
            environmentLangID = 1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = 100000,
            environmentLangID = 2,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = 100000,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] when [GetPictureByIDTSimpleTestData.pictureID] link to not picture content type. */
    @JvmStatic
    fun wrongContentScenarioTestData(): Stream<GetPictureByIDTSimpleTestData> = Stream.of(
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = null,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = -1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 0,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 2,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 100000,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = null,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = -1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 0,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 1,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 2,
        ),
        GetPictureByIDTSimpleTestData(
            pictureID = videoContent12.id,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.id]. */
    @JvmStatic
    fun checkPictureIDScenarioTestData(): Stream<GetPictureByIDWithExpectedPictureIDTestData> = Stream.of(
        GetPictureByIDWithExpectedPictureIDTestData(
            pictureID = pictureContent9.id,
            environmentLangID = null,
            expectedID = pictureContent9.id
        ),
        GetPictureByIDWithExpectedPictureIDTestData(
            pictureID = pictureContent9.id,
            environmentLangID = -1,
            expectedID = pictureContent9.id
        ),
        GetPictureByIDWithExpectedPictureIDTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedID = pictureContent9.id
        ),
        GetPictureByIDWithExpectedPictureIDTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 1,
            expectedID = pictureContent9.id
        ),
        GetPictureByIDWithExpectedPictureIDTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 100000,
            expectedID = pictureContent9.id
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.title]. */
    @JvmStatic
    fun checkPictureTitleScenarioTestData(): Stream<GetPictureByIDWithExpectedTitleTestData> = Stream.of(
        GetPictureByIDWithExpectedTitleTestData(
            pictureID = pictureContent9.id,
            environmentLangID = null,
            expectedTitle = pictureContent9.title
        ),
        GetPictureByIDWithExpectedTitleTestData(
            pictureID = pictureContent9.id,
            environmentLangID = -1,
            expectedTitle = pictureContent9.title
        ),
        GetPictureByIDWithExpectedTitleTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedTitle = pictureContent9.title
        ),
        GetPictureByIDWithExpectedTitleTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 1,
            expectedTitle = pictureContent9.title
        ),
        GetPictureByIDWithExpectedTitleTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 100000,
            expectedTitle = pictureContent9.title
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.url]. */
    @JvmStatic
    fun checkPictureURLScenarioTestData(): Stream<GetPictureByIDWithExpectedURLTestData> = Stream.of(
        GetPictureByIDWithExpectedURLTestData(
            pictureID = pictureContent9.id,
            environmentLangID = null,
            expectedURL = picture9.url
        ),
        GetPictureByIDWithExpectedURLTestData(
            pictureID = pictureContent9.id,
            environmentLangID = -1,
            expectedURL = picture9.url
        ),
        GetPictureByIDWithExpectedURLTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedURL = picture9.url
        ),
        GetPictureByIDWithExpectedURLTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 1,
            expectedURL = picture9.url
        ),
        GetPictureByIDWithExpectedURLTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 100000,
            expectedURL = picture9.url
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.language]. */
    @JvmStatic
    fun checkPictureLanguageScenarioTestData(): Stream<GetPictureByIDWithExpectedLanguageTestData> = Stream.of(
        GetPictureByIDWithExpectedLanguageTestData(
            pictureID = pictureContent9.id,
            environmentLangID = null,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetPictureByIDWithExpectedLanguageTestData(
            pictureID = pictureContent9.id,
            environmentLangID = -1,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetPictureByIDWithExpectedLanguageTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetPictureByIDWithExpectedLanguageTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetPictureByIDWithExpectedLanguageTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 1,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language1.id)
            )
        ),
        GetPictureByIDWithExpectedLanguageTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 2,
            expectedLanguageSimple = LanguageSimple(
                id = language1.id,
                name = getLanguageByID(language1.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language2.id)
            )
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.availableLanguages]. */
    @JvmStatic
    fun checkPictureAvailableLanguageScenarioTestData(): Stream<GetPictureByIDWithExpectedAvailableLanguageTestData> =
        Stream.of(
            GetPictureByIDWithExpectedAvailableLanguageTestData(
                pictureID = picture7.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetPictureByIDWithExpectedAvailableLanguageTestData(
                pictureID = picture7.id,
                environmentLangID = -1,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetPictureByIDWithExpectedAvailableLanguageTestData(
                pictureID = picture7.id,
                environmentLangID = 0,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetPictureByIDWithExpectedAvailableLanguageTestData(
                pictureID = picture7.id,
                environmentLangID = 1,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(1)
                    ),
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForEnvironment(1)
                    ),
                )
            ),
            GetPictureByIDWithExpectedAvailableLanguageTestData(
                pictureID = picture7.id,
                environmentLangID = 2,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(2)
                    ),
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForEnvironment(2)
                    ),
                )
            ),
        )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.authors]. */
    @JvmStatic
    fun checkPictureAuthorsScenarioTestData(): Stream<GetPictureByIDWithExpectedAuthorsTestData> = Stream.of(
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture7.id,
            environmentLangID = null,
            expectedAuthorList = emptyList()
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture7.id,
            environmentLangID = -1,
            expectedAuthorList = emptyList()
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture7.id,
            environmentLangID = 0,
            expectedAuthorList = emptyList()
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture7.id,
            environmentLangID = 1,
            expectedAuthorList = emptyList()
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture7.id,
            environmentLangID = 2,
            expectedAuthorList = emptyList()
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture7.id,
            environmentLangID = 100000,
            expectedAuthorList = emptyList()
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture8.id,
            environmentLangID = null,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture8.id,
            environmentLangID = -1,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture8.id,
            environmentLangID = 0,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture8.id,
            environmentLangID = 1,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture8.id,
            environmentLangID = 2,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture8.id,
            environmentLangID = 100000,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture9.id,
            environmentLangID = null,
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
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture9.id,
            environmentLangID = -1,
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
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture9.id,
            environmentLangID = 0,
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
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture9.id,
            environmentLangID = 1,
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
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture9.id,
            environmentLangID = 2,
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
        GetPictureByIDWithExpectedAuthorsTestData(
            pictureID = picture9.id,
            environmentLangID = 100000,
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

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.tags]. */
    @JvmStatic
    fun checkPictureTagsScenarioTestData(): Stream<GetPictureByIDWithExpectedTagsTestData> = Stream.of(
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture7.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture7.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture7.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture7.id,
            environmentLangID = 1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForEnvironment(1),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture7.id,
            environmentLangID = 2,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag6.id,
                    name = getTagByID(tag6.id)
                        .translations
                        .getTagTranslationForEnvironment(2),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture8.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture8.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture8.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture8.id,
            environmentLangID = 1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForEnvironment(1),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForEnvironment(1),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture8.id,
            environmentLangID = 2,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForEnvironment(2),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForEnvironment(2),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetPictureByIDWithExpectedTagsTestData(
            pictureID = picture8.id,
            environmentLangID = 100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag10.id,
                    name = getTagByID(tag10.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.user]. */
    @JvmStatic
    fun checkPictureUserScenarioTestData(): Stream<GetPictureByIDWithExpectedUserTestData> = Stream.of(
        GetPictureByIDWithExpectedUserTestData(
            pictureID = picture9.id,
            environmentLangID = null,
            expectedUserSimple = UserSimple(
                id = user5.id,
                name = user5.name,
                role = indexToUserRole(user5.role)
            )
        ),
        GetPictureByIDWithExpectedUserTestData(
            pictureID = pictureContent9.id,
            environmentLangID = -1,
            expectedUserSimple = UserSimple(
                id = user5.id,
                name = user5.name,
                role = indexToUserRole(user5.role)
            )
        ),
        GetPictureByIDWithExpectedUserTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedUserSimple = UserSimple(
                id = user5.id,
                name = user5.name,
                role = indexToUserRole(user5.role)
            )
        ),
        GetPictureByIDWithExpectedUserTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 1,
            expectedUserSimple = UserSimple(
                id = user5.id,
                name = user5.name,
                role = indexToUserRole(user5.role)
            )
        ),
        GetPictureByIDWithExpectedUserTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 2,
            expectedUserSimple = UserSimple(
                id = user5.id,
                name = user5.name,
                role = indexToUserRole(user5.role)
            )
        ),
        GetPictureByIDWithExpectedUserTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 100000,
            expectedUserSimple = UserSimple(
                id = user5.id,
                name = user5.name,
                role = indexToUserRole(user5.role)
            )
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.rating]. */
    @JvmStatic
    fun checkPictureRatingScenarioTestData(): Stream<GetPictureByIDWithExpectedRatingTestData> = Stream.of(
        GetPictureByIDWithExpectedRatingTestData(
            pictureID = picture9.id,
            environmentLangID = null,
            expectedRating = pictureContent9.rating
        ),
        GetPictureByIDWithExpectedRatingTestData(
            pictureID = pictureContent9.id,
            environmentLangID = -1,
            expectedRating = pictureContent9.rating
        ),
        GetPictureByIDWithExpectedRatingTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedRating = pictureContent9.rating
        ),
        GetPictureByIDWithExpectedRatingTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 1,
            expectedRating = pictureContent9.rating
        ),
        GetPictureByIDWithExpectedRatingTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 2,
            expectedRating = pictureContent9.rating
        ),
        GetPictureByIDWithExpectedRatingTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 100000,
            expectedRating = pictureContent9.rating
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.commentsCount]. */
    @JvmStatic
    fun checkPictureCommentCountScenarioTestData(): Stream<GetPictureByIDWithCommentCountTestData> = Stream.of(
        GetPictureByIDWithCommentCountTestData(
            pictureID = picture9.id,
            environmentLangID = null,
            expectedCommentCount = allComment.count { it.contentID == picture9.id }
        ),
        GetPictureByIDWithCommentCountTestData(
            pictureID = pictureContent9.id,
            environmentLangID = -1,
            expectedCommentCount = allComment.count { it.contentID == picture9.id }
        ),
        GetPictureByIDWithCommentCountTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 0,
            expectedCommentCount = allComment.count { it.contentID == picture9.id }
        ),
        GetPictureByIDWithCommentCountTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 1,
            expectedCommentCount = allComment.count { it.contentID == picture9.id }
        ),
        GetPictureByIDWithCommentCountTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 2,
            expectedCommentCount = allComment.count { it.contentID == picture9.id }
        ),
        GetPictureByIDWithCommentCountTestData(
            pictureID = pictureContent9.id,
            environmentLangID = 100000,
            expectedCommentCount = allComment.count { it.contentID == picture9.id }
        ),
    )

    /** Data to test [ReadPictureByIDExecutor] to check [Picture.groups]. */
    @JvmStatic
    fun checkPictureGroupScenarioTestData(): Stream<GetPictureByIDWithGroupsTestData> = Stream.of(
        GetPictureByIDWithGroupsTestData(
            pictureID = picture8.id,
            environmentLangID = null,
            expectedGroups = emptyList()
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture8.id,
            environmentLangID = -1,
            expectedGroups = emptyList()
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture8.id,
            environmentLangID = 0,
            expectedGroups = emptyList()
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture8.id,
            environmentLangID = language1.id,
            expectedGroups = emptyList()
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture8.id,
            environmentLangID = language2.id,
            expectedGroups = emptyList()
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture8.id,
            environmentLangID = 100000,
            expectedGroups = emptyList()
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture1.id,
            environmentLangID = null,
            expectedGroups = listOf(
                Group(
                    name = contentGroup1.title,
                    memberList = listOf(
                        GroupMember(
                            name = pictureContent9.title,
                            contentID = contentGroupMember1.contentID,
                            order = contentGroupMember1.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent1.title,
                            contentID = contentGroupMember2.contentID,
                            order = contentGroupMember2.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent6.title,
                            contentID = contentGroupMember3.contentID,
                            order = contentGroupMember3.orderIDX
                        ),
                    )
                )
            )
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture1.id,
            environmentLangID = -1,
            expectedGroups = listOf(
                Group(
                    name = contentGroup1.title,
                    memberList = listOf(
                        GroupMember(
                            name = pictureContent9.title,
                            contentID = contentGroupMember1.contentID,
                            order = contentGroupMember1.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent1.title,
                            contentID = contentGroupMember2.contentID,
                            order = contentGroupMember2.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent6.title,
                            contentID = contentGroupMember3.contentID,
                            order = contentGroupMember3.orderIDX
                        ),
                    )
                )
            )
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture1.id,
            environmentLangID = 0,
            expectedGroups = listOf(
                Group(
                    name = contentGroup1.title,
                    memberList = listOf(
                        GroupMember(
                            name = pictureContent9.title,
                            contentID = contentGroupMember1.contentID,
                            order = contentGroupMember1.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent1.title,
                            contentID = contentGroupMember2.contentID,
                            order = contentGroupMember2.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent6.title,
                            contentID = contentGroupMember3.contentID,
                            order = contentGroupMember3.orderIDX
                        ),
                    )
                )
            )
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture1.id,
            environmentLangID = language1.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup1.title,
                    memberList = listOf(
                        GroupMember(
                            name = pictureContent9.title,
                            contentID = contentGroupMember1.contentID,
                            order = contentGroupMember1.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent1.title,
                            contentID = contentGroupMember2.contentID,
                            order = contentGroupMember2.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent6.title,
                            contentID = contentGroupMember3.contentID,
                            order = contentGroupMember3.orderIDX
                        ),
                    )
                )
            )
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture1.id,
            environmentLangID = language2.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup1.title,
                    memberList = listOf(
                        GroupMember(
                            name = pictureContent9.title,
                            contentID = contentGroupMember1.contentID,
                            order = contentGroupMember1.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent1.title,
                            contentID = contentGroupMember2.contentID,
                            order = contentGroupMember2.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent6.title,
                            contentID = contentGroupMember3.contentID,
                            order = contentGroupMember3.orderIDX
                        ),
                    )
                )
            )
        ),
        GetPictureByIDWithGroupsTestData(
            pictureID = picture1.id,
            environmentLangID = 100000,
            expectedGroups = listOf(
                Group(
                    name = contentGroup1.title,
                    memberList = listOf(
                        GroupMember(
                            name = pictureContent9.title,
                            contentID = contentGroupMember1.contentID,
                            order = contentGroupMember1.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent1.title,
                            contentID = contentGroupMember2.contentID,
                            order = contentGroupMember2.orderIDX
                        ),
                        GroupMember(
                            name = pictureContent6.title,
                            contentID = contentGroupMember3.contentID,
                            order = contentGroupMember3.orderIDX
                        ),
                    )
                )
            )
        ),
    )
}