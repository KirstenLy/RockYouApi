package database.data

import database.external.model.*
import database.external.model.group.Group
import database.external.model.group.GroupMember
import database.external.model.language.LanguageSimple
import database.external.model.story.StoryNode
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple
import database.internal.executor.ReadStoryByIDExecutor
import database.internal.indexToUserRole
import database.internal.mock.*
import database.utils.*
import java.util.stream.Stream

/** Model of data to test [ReadStoryByIDExecutor] where no data result expected. */
internal data class GetStoryByIDTSimpleTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.id]. */
internal data class GetStoryByIDWithExpectedPictureIDTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedID: Int
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.title]. */
internal data class GetStoryByIDWithExpectedTitleTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedTitle: String
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.language]. */
internal data class GetStoryByIDWithExpectedLanguageTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedLanguageSimple: LanguageSimple?
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.availableLanguages]. */
internal data class GetStoryByIDWithExpectedAvailableLanguageTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedAvailableLanguageListSimple: List<LanguageSimple>
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.authors]. */
internal data class GetStoryByIDWithExpectedAuthorListTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedAuthorList: List<Author>
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.user]. */
internal data class GetStoryByIDWithExpectedUserTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedUserSimple: UserSimple
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.tags]. */
internal data class GetStoryByIDWithExpectedTagsTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedTagSimpleList: List<TagSimple>
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.rating]. */
internal data class GetStoryByIDWithExpectedRatingTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedRating: Int
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.commentsCount]. */
internal data class GetStoryByIDWithCommentCountTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedCommentCount: Int
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.nodes]. */
internal data class GetStoryByIDWithExpectedStoryNodesTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedStoryNodes: List<StoryNode>
)

/** Model of data to test [ReadStoryByIDExecutor] and check [Story.groups]. */
internal data class GetStoryByIDWithGroupsTestData(
    val storyID: Int,
    val environmentLangID: Int? = null,
    val expectedGroups: List<Group>
)

/** Data creator to test [ReadStoryByIDExecutor].*/
internal object GetStoryByIDTestDataStreamCreator {

    /** Data to test [ReadStoryByIDExecutor] when [GetStoryByIDTSimpleTestData.storyID] link to not existed content. */
    @JvmStatic
    fun notExistedContentScenarioTestData(): Stream<GetStoryByIDTSimpleTestData> = Stream.of(
        GetStoryByIDTSimpleTestData(
            storyID = -1,
            environmentLangID = null,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = -1,
            environmentLangID = -1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = -1,
            environmentLangID = 0,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = -1,
            environmentLangID = 1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = -1,
            environmentLangID = 2,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = -1,
            environmentLangID = 100000,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = 100000,
            environmentLangID = null,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = 100000,
            environmentLangID = -1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = 100000,
            environmentLangID = 0,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = 100000,
            environmentLangID = 1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = 100000,
            environmentLangID = 2,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = 100000,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] when [GetStoryByIDTSimpleTestData.storyID] link to not picture content type. */
    @JvmStatic
    fun wrongContentScenarioTestData(): Stream<GetStoryByIDTSimpleTestData> = Stream.of(
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = null,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = -1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 0,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 2,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 100000,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = null,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = -1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 0,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 1,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 2,
        ),
        GetStoryByIDTSimpleTestData(
            storyID = videoContent12.id,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.id]. */
    @JvmStatic
    fun checkStoryIDScenarioTestData(): Stream<GetStoryByIDWithExpectedPictureIDTestData> = Stream.of(
        GetStoryByIDWithExpectedPictureIDTestData(
            storyID = story12.id,
            environmentLangID = null,
            expectedID = story12.id
        ),
        GetStoryByIDWithExpectedPictureIDTestData(
            storyID = story12.id,
            environmentLangID = -1,
            expectedID = story12.id
        ),
        GetStoryByIDWithExpectedPictureIDTestData(
            storyID = story12.id,
            environmentLangID = 0,
            expectedID = story12.id
        ),
        GetStoryByIDWithExpectedPictureIDTestData(
            storyID = story12.id,
            environmentLangID = 1,
            expectedID = story12.id
        ),
        GetStoryByIDWithExpectedPictureIDTestData(
            storyID = story12.id,
            environmentLangID = 100000,
            expectedID = story12.id
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.title]. */
    @JvmStatic
    fun checkStoryTitleScenarioTestData(): Stream<GetStoryByIDWithExpectedTitleTestData> = Stream.of(
        GetStoryByIDWithExpectedTitleTestData(
            storyID = story2.id,
            environmentLangID = null,
            expectedTitle = storyContent2.title
        ),
        GetStoryByIDWithExpectedTitleTestData(
            storyID = story2.id,
            environmentLangID = -1,
            expectedTitle = storyContent2.title
        ),
        GetStoryByIDWithExpectedTitleTestData(
            storyID = story2.id,
            environmentLangID = 0,
            expectedTitle = storyContent2.title
        ),
        GetStoryByIDWithExpectedTitleTestData(
            storyID = story2.id,
            environmentLangID = 1,
            expectedTitle = storyContent2.title
        ),
        GetStoryByIDWithExpectedTitleTestData(
            storyID = story2.id,
            environmentLangID = 100000,
            expectedTitle = storyContent2.title
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.language]. */
    @JvmStatic
    fun checkStoryLanguageScenarioTestData(): Stream<GetStoryByIDWithExpectedLanguageTestData> = Stream.of(
        GetStoryByIDWithExpectedLanguageTestData(
            storyID = story11.id,
            environmentLangID = null,
            expectedLanguageSimple = LanguageSimple(
                id = language3.id,
                name = getLanguageByID(language3.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetStoryByIDWithExpectedLanguageTestData(
            storyID = story11.id,
            environmentLangID = -1,
            expectedLanguageSimple = LanguageSimple(
                id = language3.id,
                name = getLanguageByID(language3.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetStoryByIDWithExpectedLanguageTestData(
            storyID = story11.id,
            environmentLangID = 0,
            expectedLanguageSimple = LanguageSimple(
                id = language3.id,
                name = getLanguageByID(language3.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetStoryByIDWithExpectedLanguageTestData(
            storyID = story11.id,
            environmentLangID = language1.id,
            expectedLanguageSimple = LanguageSimple(
                id = language3.id,
                name = getLanguageByID(language3.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language1.id)
            )
        ),
        GetStoryByIDWithExpectedLanguageTestData(
            storyID = story11.id,
            environmentLangID = language2.id,
            expectedLanguageSimple = LanguageSimple(
                id = language3.id,
                name = getLanguageByID(language3.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language2.id)
            )
        ),
        GetStoryByIDWithExpectedLanguageTestData(
            storyID = story11.id,
            environmentLangID = 100000,
            expectedLanguageSimple = LanguageSimple(
                id = language3.id,
                name = getLanguageByID(language3.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.availableLanguages]. */
    @JvmStatic
    fun checkStoryAvailableLanguageScenarioTestData(): Stream<GetStoryByIDWithExpectedAvailableLanguageTestData> =
        Stream.of(
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story4.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story4.id,
                environmentLangID = -1,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story4.id,
                environmentLangID = 0,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story4.id,
                environmentLangID = language1.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story4.id,
                environmentLangID = language2.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story4.id,
                environmentLangID = 100000,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story3.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language2.id,
                        name = getLanguageByID(language2.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story3.id,
                environmentLangID = -1,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language2.id,
                        name = getLanguageByID(language2.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story3.id,
                environmentLangID = 0,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language2.id,
                        name = getLanguageByID(language2.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story3.id,
                environmentLangID = language1.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                    LanguageSimple(
                        id = language2.id,
                        name = getLanguageByID(language2.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story3.id,
                environmentLangID = language2.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                    LanguageSimple(
                        id = language2.id,
                        name = getLanguageByID(language2.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                )
            ),
            GetStoryByIDWithExpectedAvailableLanguageTestData(
                storyID = story3.id,
                environmentLangID = 100000,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                    LanguageSimple(
                        id = language2.id,
                        name = getLanguageByID(language2.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    ),
                )
            ),
        )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.availableLanguages]. */
    @JvmStatic
    fun checkStoryAuthorsScenarioTestData(): Stream<GetStoryByIDWithExpectedAuthorListTestData> = Stream.of(
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story7.id,
            environmentLangID = null,
            expectedAuthorList = emptyList()
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story7.id,
            environmentLangID = -1,
            expectedAuthorList = emptyList()
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story7.id,
            environmentLangID = 0,
            expectedAuthorList = emptyList()
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story7.id,
            environmentLangID = language1.id,
            expectedAuthorList = emptyList()
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story7.id,
            environmentLangID = language2.id,
            expectedAuthorList = emptyList()
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story7.id,
            environmentLangID = 100000,
            expectedAuthorList = emptyList()
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story5.id,
            environmentLangID = null,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story5.id,
            environmentLangID = -1,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story5.id,
            environmentLangID = 0,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story5.id,
            environmentLangID = language1.id,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story5.id,
            environmentLangID = language2.id,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story5.id,
            environmentLangID = 100000,
            expectedAuthorList = listOf(
                Author(
                    id = author4.id,
                    name = author4.name
                )
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story10.id,
            environmentLangID = null,
            expectedAuthorList = listOf(
                Author(
                    id = author8.id,
                    name = author8.name
                ),
                Author(
                    id = author7.id,
                    name = author7.name
                ),
                Author(
                    id = author6.id,
                    name = author6.name
                ),
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story10.id,
            environmentLangID = -1,
            expectedAuthorList = listOf(
                Author(
                    id = author8.id,
                    name = author8.name
                ),
                Author(
                    id = author7.id,
                    name = author7.name
                ),
                Author(
                    id = author6.id,
                    name = author6.name
                ),
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story10.id,
            environmentLangID = 0,
            expectedAuthorList = listOf(
                Author(
                    id = author8.id,
                    name = author8.name
                ),
                Author(
                    id = author7.id,
                    name = author7.name
                ),
                Author(
                    id = author6.id,
                    name = author6.name
                ),
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story10.id,
            environmentLangID = language1.id,
            expectedAuthorList = listOf(
                Author(
                    id = author8.id,
                    name = author8.name
                ),
                Author(
                    id = author7.id,
                    name = author7.name
                ),
                Author(
                    id = author6.id,
                    name = author6.name
                ),
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story10.id,
            environmentLangID = language2.id,
            expectedAuthorList = listOf(
                Author(
                    id = author8.id,
                    name = author8.name
                ),
                Author(
                    id = author7.id,
                    name = author7.name
                ),
                Author(
                    id = author6.id,
                    name = author6.name
                ),
            )
        ),
        GetStoryByIDWithExpectedAuthorListTestData(
            storyID = story10.id,
            environmentLangID = 100000,
            expectedAuthorList = listOf(
                Author(
                    id = author8.id,
                    name = author8.name
                ),
                Author(
                    id = author7.id,
                    name = author7.name
                ),
                Author(
                    id = author6.id,
                    name = author6.name
                ),
            )
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.tags]. */
    @JvmStatic
    fun checkStoryTagsScenarioTestData(): Stream<GetStoryByIDWithExpectedTagsTestData> = Stream.of(
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story13.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story13.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story13.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story13.id,
            environmentLangID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story13.id,
            environmentLangID = language3.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForEnvironment(language3.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story13.id,
            environmentLangID = 100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story14.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag3.id,
                    name = getTagByID(tag3.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story14.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag3.id,
                    name = getTagByID(tag3.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story14.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag3.id,
                    name = getTagByID(tag3.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story14.id,
            environmentLangID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag3.id,
                    name = getTagByID(tag3.id)
                        .translations
                        .getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story14.id,
            environmentLangID = language2.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForEnvironment(language2.id),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag3.id,
                    name = getTagByID(tag3.id)
                        .translations
                        .getTagTranslationForEnvironment(language2.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetStoryByIDWithExpectedTagsTestData(
            storyID = story14.id,
            environmentLangID = 100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag7.id,
                    name = getTagByID(tag7.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
                TagSimple(
                    id = tag3.id,
                    name = getTagByID(tag3.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.user]. */
    @JvmStatic
    fun checkStoryUserScenarioTestData(): Stream<GetStoryByIDWithExpectedUserTestData> = Stream.of(
        GetStoryByIDWithExpectedUserTestData(
            storyID = story13.id,
            environmentLangID = null,
            expectedUserSimple = UserSimple(
                id = user1.id,
                name = user1.name,
                role = indexToUserRole(user1.role)
            )
        ),
        GetStoryByIDWithExpectedUserTestData(
            storyID = story13.id,
            environmentLangID = -1,
            expectedUserSimple = UserSimple(
                id = user1.id,
                name = user1.name,
                role = indexToUserRole(user1.role)
            )
        ),
        GetStoryByIDWithExpectedUserTestData(
            storyID = story13.id,
            environmentLangID = 0,
            expectedUserSimple = UserSimple(
                id = user1.id,
                name = user1.name,
                role = indexToUserRole(user1.role)
            )
        ),
        GetStoryByIDWithExpectedUserTestData(
            storyID = story13.id,
            environmentLangID = language1.id,
            expectedUserSimple = UserSimple(
                id = user1.id,
                name = user1.name,
                role = indexToUserRole(user1.role)
            )
        ),
        GetStoryByIDWithExpectedUserTestData(
            storyID = story13.id,
            environmentLangID = language2.id,
            expectedUserSimple = UserSimple(
                id = user1.id,
                name = user1.name,
                role = indexToUserRole(user1.role)
            )
        ),
        GetStoryByIDWithExpectedUserTestData(
            storyID = story13.id,
            environmentLangID = 100000,
            expectedUserSimple = UserSimple(
                id = user1.id,
                name = user1.name,
                role = indexToUserRole(user1.role)
            )
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.rating]. */
    @JvmStatic
    fun checkStoryRatingScenarioTestData(): Stream<GetStoryByIDWithExpectedRatingTestData> = Stream.of(
        GetStoryByIDWithExpectedRatingTestData(
            storyID = story6.id,
            environmentLangID = null,
            expectedRating = storyContent6.rating
        ),
        GetStoryByIDWithExpectedRatingTestData(
            storyID = story6.id,
            environmentLangID = -1,
            expectedRating = storyContent6.rating
        ),
        GetStoryByIDWithExpectedRatingTestData(
            storyID = story6.id,
            environmentLangID = 0,
            expectedRating = storyContent6.rating
        ),
        GetStoryByIDWithExpectedRatingTestData(
            storyID = story6.id,
            environmentLangID = language1.id,
            expectedRating = storyContent6.rating
        ),
        GetStoryByIDWithExpectedRatingTestData(
            storyID = story6.id,
            environmentLangID = language3.id,
            expectedRating = storyContent6.rating
        ),
        GetStoryByIDWithExpectedRatingTestData(
            storyID = story6.id,
            environmentLangID = 100000,
            expectedRating = storyContent6.rating
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.commentsCount]. */
    @JvmStatic
    fun checkStoryCommentCountScenarioTestData(): Stream<GetStoryByIDWithCommentCountTestData> = Stream.of(
        GetStoryByIDWithCommentCountTestData(
            storyID = story5.id,
            environmentLangID = null,
            expectedCommentCount = allComment.count { it.contentID == story5.id }
        ),
        GetStoryByIDWithCommentCountTestData(
            storyID = story5.id,
            environmentLangID = -1,
            expectedCommentCount = allComment.count { it.contentID == story5.id }
        ),
        GetStoryByIDWithCommentCountTestData(
            storyID = story5.id,
            environmentLangID = 0,
            expectedCommentCount = allComment.count { it.contentID == story5.id }
        ),
        GetStoryByIDWithCommentCountTestData(
            storyID = story5.id,
            environmentLangID = language1.id,
            expectedCommentCount = allComment.count { it.contentID == story5.id }
        ),
        GetStoryByIDWithCommentCountTestData(
            storyID = story5.id,
            environmentLangID = language4.id,
            expectedCommentCount = allComment.count { it.contentID == story5.id }
        ),
        GetStoryByIDWithCommentCountTestData(
            storyID = story5.id,
            environmentLangID = 100000,
            expectedCommentCount = allComment.count { it.contentID == story5.id }
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.nodes]. */
    @JvmStatic
    fun checkStoryNodesScenarioTestData(): Stream<GetStoryByIDWithExpectedStoryNodesTestData> = Stream.of(
        GetStoryByIDWithExpectedStoryNodesTestData(
            storyID = story1.id,
            environmentLangID = null,
            expectedStoryNodes = listOf(
                StoryNode.ChapterNode(
                    chapter = Chapter(
                        id = storyChapter1.id,
                        storyID = storyChapter1.storyID,
                        title = storyChapterContent1.title,
                        language = LanguageSimple(
                            id = language1.id,
                            name = language1.translations.getLanguageTranslationForDefaultEnvironment()
                        ),
                        availableLanguages = listOf(
                            LanguageSimple(
                                id = language1.id,
                                name = language1.translations.getLanguageTranslationForDefaultEnvironment()
                            ),
                        ),
                        authors = listOf(
                            Author(
                                id = author1.id,
                                name = author1.name
                            )
                        ),
                        user = UserSimple(
                            id = user6.id,
                            name = user6.name
                        ),
                        tags = listOf(
                            TagSimple(
                                id = tag8.id,
                                name = tag8.translations.getTagTranslationForDefaultEnvironment(),
                                isOneOfMainTag = false
                            )
                        ),
                        rating = storyChapterContent1.rating,
                        commentsCount = allComment.count { it.contentID == storyChapter1.id }.toLong()
                    )
                ),
                StoryNode.ChapterNode(
                    chapter = Chapter(
                        id = storyChapter2.id,
                        storyID = storyChapter2.storyID,
                        title = storyChapterContent2.title,
                        language = LanguageSimple(
                            id = language2.id,
                            name = language2.translations.getLanguageTranslationForDefaultEnvironment()
                        ),
                        availableLanguages = listOf(
                            LanguageSimple(
                                id = language2.id,
                                name = language2.translations.getLanguageTranslationForDefaultEnvironment()
                            ),
                        ),
                        authors = listOf(
                            Author(
                                id = author1.id,
                                name = author1.name
                            )
                        ),
                        user = UserSimple(
                            id = user8.id,
                            name = user8.name
                        ),
                        tags = listOf(
                            TagSimple(
                                id = tag8.id,
                                name = tag8.translations.getTagTranslationForDefaultEnvironment(),
                                isOneOfMainTag = false
                            ),
                            TagSimple(
                                id = tag1.id,
                                name = tag1.translations.getTagTranslationForDefaultEnvironment(),
                                isOneOfMainTag = false
                            )
                        ),
                        rating = storyChapterContent2.rating,
                        commentsCount = allComment.count { it.contentID == storyChapterContent2.id }.toLong()
                    )
                )
            )
        ),
    )

    /** Data to test [ReadStoryByIDExecutor] to check [Story.groups]. */
    @JvmStatic
    fun checkStoryGroupsTestData(): Stream<GetStoryByIDWithGroupsTestData> = Stream.of(
        GetStoryByIDWithGroupsTestData(
            storyID = story12.id,
            environmentLangID = null,
            expectedGroups = emptyList()
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story12.id,
            environmentLangID = -1,
            expectedGroups = emptyList()
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story12.id,
            environmentLangID = 0,
            expectedGroups = emptyList()
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story12.id,
            environmentLangID = language1.id,
            expectedGroups = emptyList()
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story12.id,
            environmentLangID = language2.id,
            expectedGroups = emptyList()
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story12.id,
            environmentLangID = 1000,
            expectedGroups = emptyList()
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story2.id,
            environmentLangID = null,
            expectedGroups = listOf(
                Group(
                    name = contentGroup5.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = storyContent6.id,
                            name = storyContent6.title,
                            order = contentGroupMember11.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent4.id,
                            name = storyContent4.title,
                            order = contentGroupMember12.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent2.id,
                            name = storyContent2.title,
                            order = contentGroupMember13.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent1.id,
                            name = storyContent1.title,
                            order = contentGroupMember14.orderIDX
                        ),
                    )
                )
            )
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story2.id,
            environmentLangID = -1,
            expectedGroups = listOf(
                Group(
                    name = contentGroup5.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = storyContent6.id,
                            name = storyContent6.title,
                            order = contentGroupMember11.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent4.id,
                            name = storyContent4.title,
                            order = contentGroupMember12.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent2.id,
                            name = storyContent2.title,
                            order = contentGroupMember13.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent1.id,
                            name = storyContent1.title,
                            order = contentGroupMember14.orderIDX
                        ),
                    )
                )
            )
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story2.id,
            environmentLangID = 0,
            expectedGroups = listOf(
                Group(
                    name = contentGroup5.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = storyContent6.id,
                            name = storyContent6.title,
                            order = contentGroupMember11.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent4.id,
                            name = storyContent4.title,
                            order = contentGroupMember12.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent2.id,
                            name = storyContent2.title,
                            order = contentGroupMember13.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent1.id,
                            name = storyContent1.title,
                            order = contentGroupMember14.orderIDX
                        ),
                    )
                )
            )
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story2.id,
            environmentLangID = language1.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup5.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = storyContent6.id,
                            name = storyContent6.title,
                            order = contentGroupMember11.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent4.id,
                            name = storyContent4.title,
                            order = contentGroupMember12.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent2.id,
                            name = storyContent2.title,
                            order = contentGroupMember13.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent1.id,
                            name = storyContent1.title,
                            order = contentGroupMember14.orderIDX
                        ),
                    )
                )
            )
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story2.id,
            environmentLangID = language3.id,
            expectedGroups = listOf(
                Group(
                    name = contentGroup5.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = storyContent6.id,
                            name = storyContent6.title,
                            order = contentGroupMember11.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent4.id,
                            name = storyContent4.title,
                            order = contentGroupMember12.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent2.id,
                            name = storyContent2.title,
                            order = contentGroupMember13.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent1.id,
                            name = storyContent1.title,
                            order = contentGroupMember14.orderIDX
                        ),
                    )
                )
            )
        ),
        GetStoryByIDWithGroupsTestData(
            storyID = story2.id,
            environmentLangID = 100000,
            expectedGroups = listOf(
                Group(
                    name = contentGroup5.title,
                    memberList = listOf(
                        GroupMember(
                            contentID = storyContent6.id,
                            name = storyContent6.title,
                            order = contentGroupMember11.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent4.id,
                            name = storyContent4.title,
                            order = contentGroupMember12.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent2.id,
                            name = storyContent2.title,
                            order = contentGroupMember13.orderIDX
                        ),
                        GroupMember(
                            contentID = storyContent1.id,
                            name = storyContent1.title,
                            order = contentGroupMember14.orderIDX
                        ),
                    )
                )
            )
        ),
    )
}