package database.data

import database.external.model.Author
import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple
import database.internal.executor.ReadChapterByIDExecutor
import database.internal.indexToUserRole
import database.internal.mock.*
import database.utils.*
import java.util.stream.Stream

/** Model of data to test [ReadChapterByIDExecutor] where no data result expected. */
internal data class GetChapterByIDTSimpleTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.id]. */
internal data class GetChapterByIDWithExpectedChapterIDTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedID: Int
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.title]. */
internal data class GetChapterByIDWithExpectedTitleTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedTitle: String
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.storyID]. */
internal data class GetChapterByIDWithExpectedStoryIDTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedStoryID: Int
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.language]. */
internal data class GetChapterByIDWithExpectedLanguageTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedLanguageSimple: LanguageSimple?
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.availableLanguages]. */
internal data class GetChapterByIDWithExpectedAvailableLanguageTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedAvailableLanguageListSimple: List<LanguageSimple>
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.authors]. */
internal data class GetChapterByIDWithExpectedAuthorsTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedAuthorList: List<Author>
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.tags]. */
internal data class GetChapterByIDWithExpectedTagsTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedTagSimpleList: List<TagSimple>
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.userBasicInfo]. */
internal data class GetChapterByIDWithExpectedUserTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedUserSimple: UserSimple
)


/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.rating]. */
internal data class GetChapterByIDWithExpectedRatingTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedRating: Int
)

/** Model of data to test [ReadChapterByIDExecutor] and check [Chapter.rating]. */
internal data class GetChapterByIDWithCommentCountTestData(
    val chapterID: Int,
    val environmentLangID: Int? = null,
    val expectedCommentCount: Int
)

/** Data creator to test [ReadChapterByIDExecutor].*/
internal object GetChapterByIDTestDataStreamCreator {

    /** Data to test [ReadChapterByIDExecutor] when [GetChapterByIDTSimpleTestData.chapterID] link to not existed content. */
    @JvmStatic
    fun notExistedContentScenarioTestData(): Stream<GetChapterByIDTSimpleTestData> = Stream.of(
        GetChapterByIDTSimpleTestData(
            chapterID = -1,
            environmentLangID = null,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = -1,
            environmentLangID = -1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = -1,
            environmentLangID = 0,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = -1,
            environmentLangID = 1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = -1,
            environmentLangID = 2,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = -1,
            environmentLangID = 100000,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = 100000,
            environmentLangID = null,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = 100000,
            environmentLangID = -1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = 100000,
            environmentLangID = 0,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = 100000,
            environmentLangID = 1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = 100000,
            environmentLangID = 2,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = 100000,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] when [GetChapterByIDTSimpleTestData.chapterID] link to not chapter content type. */
    @JvmStatic
    fun wrongContentScenarioTestData(): Stream<GetChapterByIDTSimpleTestData> = Stream.of(
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = null,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = -1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 0,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 2,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 100000,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = null,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = -1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 0,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 1,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 2,
        ),
        GetChapterByIDTSimpleTestData(
            chapterID = videoContent12.id,
            environmentLangID = 100000,
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.id]. */
    @JvmStatic
    fun checkChapterIDScenarioTestData(): Stream<GetChapterByIDWithExpectedChapterIDTestData> = Stream.of(
        GetChapterByIDWithExpectedChapterIDTestData(
            chapterID = storyChapter28.id,
            environmentLangID = null,
            expectedID = storyChapter28.id
        ),
        GetChapterByIDWithExpectedChapterIDTestData(
            chapterID = storyChapter28.id,
            environmentLangID = -1,
            expectedID = storyChapter28.id
        ),
        GetChapterByIDWithExpectedChapterIDTestData(
            chapterID = storyChapter28.id,
            environmentLangID = 0,
            expectedID = storyChapter28.id
        ),
        GetChapterByIDWithExpectedChapterIDTestData(
            chapterID = storyChapter28.id,
            environmentLangID = 1,
            expectedID = storyChapter28.id
        ),
        GetChapterByIDWithExpectedChapterIDTestData(
            chapterID = storyChapter28.id,
            environmentLangID = 100000,
            expectedID = storyChapter28.id
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.title]. */
    @JvmStatic
    fun checkChapterTitleScenarioTestData(): Stream<GetChapterByIDWithExpectedTitleTestData> = Stream.of(
        GetChapterByIDWithExpectedTitleTestData(
            chapterID = storyChapter12.id,
            environmentLangID = null,
            expectedTitle = storyChapterContent12.title
        ),
        GetChapterByIDWithExpectedTitleTestData(
            chapterID = storyChapter12.id,
            environmentLangID = -1,
            expectedTitle = storyChapterContent12.title
        ),
        GetChapterByIDWithExpectedTitleTestData(
            chapterID = storyChapter12.id,
            environmentLangID = 0,
            expectedTitle = storyChapterContent12.title
        ),
        GetChapterByIDWithExpectedTitleTestData(
            chapterID = storyChapter12.id,
            environmentLangID = 1,
            expectedTitle = storyChapterContent12.title
        ),
        GetChapterByIDWithExpectedTitleTestData(
            chapterID = storyChapter12.id,
            environmentLangID = 100000,
            expectedTitle = storyChapterContent12.title
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.storyID]. */
    @JvmStatic
    fun checkChapterStoryIDScenarioTestData(): Stream<GetChapterByIDWithExpectedStoryIDTestData> = Stream.of(
        GetChapterByIDWithExpectedStoryIDTestData(
            chapterID = storyChapter12.id,
            environmentLangID = null,
            expectedStoryID = storyChapter12.storyID
        ),
        GetChapterByIDWithExpectedStoryIDTestData(
            chapterID = storyChapter11.id,
            environmentLangID = -1,
            expectedStoryID = storyChapter11.storyID
        ),
        GetChapterByIDWithExpectedStoryIDTestData(
            chapterID = storyChapter3.id,
            environmentLangID = 0,
            expectedStoryID = storyChapter3.storyID
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.language]. */
    @JvmStatic
    fun checkChapterLanguageScenarioTestData(): Stream<GetChapterByIDWithExpectedLanguageTestData> = Stream.of(
        GetChapterByIDWithExpectedLanguageTestData(
            chapterID = storyChapter28.id,
            environmentLangID = null,
            expectedLanguageSimple = LanguageSimple(
                id = language4.id,
                name = getLanguageByID(language4.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetChapterByIDWithExpectedLanguageTestData(
            chapterID = storyChapter28.id,
            environmentLangID = -1,
            expectedLanguageSimple = LanguageSimple(
                id = language4.id,
                name = getLanguageByID(language4.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetChapterByIDWithExpectedLanguageTestData(
            chapterID = storyChapter28.id,
            environmentLangID = 0,
            expectedLanguageSimple = LanguageSimple(
                id = language4.id,
                name = getLanguageByID(language4.id)
                    .translations
                    .getLanguageTranslationForDefaultEnvironment()
            )
        ),
        GetChapterByIDWithExpectedLanguageTestData(
            chapterID = storyChapter28.id,
            environmentLangID = language1.id,
            expectedLanguageSimple = LanguageSimple(
                id = language4.id,
                name = getLanguageByID(language4.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language1.id)
            )
        ),
        GetChapterByIDWithExpectedLanguageTestData(
            chapterID = storyChapter28.id,
            environmentLangID = language2.id,
            expectedLanguageSimple = LanguageSimple(
                id = language4.id,
                name = getLanguageByID(language4.id)
                    .translations
                    .getLanguageTranslationForEnvironment(language2.id)
            )
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.availableLanguages]. */
    @JvmStatic
    fun checkChapterAvailableLanguageScenarioTestData(): Stream<GetChapterByIDWithExpectedAvailableLanguageTestData> =
        Stream.of(
            GetChapterByIDWithExpectedAvailableLanguageTestData(
                chapterID = storyChapter7.id,
                environmentLangID = null,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
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
            GetChapterByIDWithExpectedAvailableLanguageTestData(
                chapterID = storyChapter7.id,
                environmentLangID = -1,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
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
            GetChapterByIDWithExpectedAvailableLanguageTestData(
                chapterID = storyChapter7.id,
                environmentLangID = 0,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
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
            GetChapterByIDWithExpectedAvailableLanguageTestData(
                chapterID = storyChapter7.id,
                environmentLangID = language1.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    ),
                )
            ),
            GetChapterByIDWithExpectedAvailableLanguageTestData(
                chapterID = storyChapter7.id,
                environmentLangID = language2.id,
                expectedAvailableLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language3.id,
                        name = getLanguageByID(language3.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                    LanguageSimple(
                        id = language4.id,
                        name = getLanguageByID(language4.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    ),
                )
            ),
        )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.authors]. */
    @JvmStatic
    fun checkChapterAuthorsScenarioTestData(): Stream<GetChapterByIDWithExpectedAuthorsTestData> = Stream.of(
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter37.id,
            environmentLangID = null,
            expectedAuthorList = emptyList()
        ),
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter37.id,
            environmentLangID = -1,
            expectedAuthorList = emptyList()
        ),
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter37.id,
            environmentLangID = 0,
            expectedAuthorList = emptyList()
        ),
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter37.id,
            environmentLangID = language1.id,
            expectedAuthorList = emptyList()
        ),
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter37.id,
            environmentLangID = language2.id,
            expectedAuthorList = emptyList()
        ),
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter37.id,
            environmentLangID = 100000,
            expectedAuthorList = emptyList()
        ),
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter9.id,
            environmentLangID = null,
            expectedAuthorList = listOf(
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
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter9.id,
            environmentLangID = -1,
            expectedAuthorList = listOf(
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
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter9.id,
            environmentLangID = 0,
            expectedAuthorList = listOf(
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
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter9.id,
            environmentLangID = language1.id,
            expectedAuthorList = listOf(
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
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter9.id,
            environmentLangID = language2.id,
            expectedAuthorList = listOf(
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
        GetChapterByIDWithExpectedAuthorsTestData(
            chapterID = storyChapter9.id,
            environmentLangID = 100000,
            expectedAuthorList = listOf(
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

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.tags]. */
    @JvmStatic
    fun checkChapterTagsScenarioTestData(): Stream<GetChapterByIDWithExpectedTagsTestData> = Stream.of(
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter17.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter17.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter17.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter17.id,
            environmentLangID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
                        .translations
                        .getTagTranslationForEnvironment(language1.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter17.id,
            environmentLangID = language2.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
                        .translations
                        .getTagTranslationForEnvironment(language2.id),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter17.id,
            environmentLangID = 100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
                        .translations
                        .getTagTranslationForDefaultEnvironment(),
                    isOneOfMainTag = false
                ),
            )
        ),
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter19.id,
            environmentLangID = null,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
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
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter19.id,
            environmentLangID = -1,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
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
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter19.id,
            environmentLangID = 0,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
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
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter19.id,
            environmentLangID = language1.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
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
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter19.id,
            environmentLangID = language2.id,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
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
        GetChapterByIDWithExpectedTagsTestData(
            chapterID = storyChapter19.id,
            environmentLangID = 100000,
            expectedTagSimpleList = listOf(
                TagSimple(
                    id = tag9.id,
                    name = getTagByID(tag9.id)
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

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.userBasicInfo]. */
    @JvmStatic
    fun checkChapterUserScenarioTestData(): Stream<GetChapterByIDWithExpectedUserTestData> = Stream.of(
        GetChapterByIDWithExpectedUserTestData(
            chapterID = storyChapter18.id,
            environmentLangID = null,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetChapterByIDWithExpectedUserTestData(
            chapterID = storyChapter18.id,
            environmentLangID = -1,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetChapterByIDWithExpectedUserTestData(
            chapterID = storyChapter18.id,
            environmentLangID = 0,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetChapterByIDWithExpectedUserTestData(
            chapterID = storyChapter18.id,
            environmentLangID = language1.id,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetChapterByIDWithExpectedUserTestData(
            chapterID = storyChapter18.id,
            environmentLangID = language2.id,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
        GetChapterByIDWithExpectedUserTestData(
            chapterID = storyChapter18.id,
            environmentLangID = 100000,
            expectedUserSimple = UserSimple(
                id = user6.id,
                name = user6.name,
                role = indexToUserRole(user6.role)
            )
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.rating]. */
    @JvmStatic
    fun checkChapterRatingScenarioTestData(): Stream<GetChapterByIDWithExpectedRatingTestData> = Stream.of(
        GetChapterByIDWithExpectedRatingTestData(
            chapterID = storyChapterContent16.id,
            environmentLangID = null,
            expectedRating = storyChapterContent16.rating
        ),
        GetChapterByIDWithExpectedRatingTestData(
            chapterID = storyChapterContent16.id,
            environmentLangID = -1,
            expectedRating = storyChapterContent16.rating
        ),
        GetChapterByIDWithExpectedRatingTestData(
            chapterID = storyChapterContent16.id,
            environmentLangID = 0,
            expectedRating = storyChapterContent16.rating
        ),
        GetChapterByIDWithExpectedRatingTestData(
            chapterID = storyChapterContent16.id,
            environmentLangID = language1.id,
            expectedRating = storyChapterContent16.rating
        ),
        GetChapterByIDWithExpectedRatingTestData(
            chapterID = storyChapterContent16.id,
            environmentLangID = language2.id,
            expectedRating = storyChapterContent16.rating
        ),
        GetChapterByIDWithExpectedRatingTestData(
            chapterID = storyChapterContent16.id,
            environmentLangID = 100000,
            expectedRating = storyChapterContent16.rating
        ),
    )

    /** Data to test [ReadChapterByIDExecutor] to check [Chapter.commentsCount]. */
    @JvmStatic
    fun checkChapterCommentCountScenarioTestData(): Stream<GetChapterByIDWithCommentCountTestData> = Stream.of(
        GetChapterByIDWithCommentCountTestData(
            chapterID = storyChapter3.id,
            environmentLangID = null,
            expectedCommentCount = allComment.count { it.contentID == storyChapter3.id }
        ),
        GetChapterByIDWithCommentCountTestData(
            chapterID = storyChapter3.id,
            environmentLangID = -1,
            expectedCommentCount = allComment.count { it.contentID == storyChapter3.id }
        ),
        GetChapterByIDWithCommentCountTestData(
            chapterID = storyChapter3.id,
            environmentLangID = 0,
            expectedCommentCount = allComment.count { it.contentID == storyChapter3.id }
        ),
        GetChapterByIDWithCommentCountTestData(
            chapterID = storyChapter3.id,
            environmentLangID = language1.id,
            expectedCommentCount = allComment.count { it.contentID == storyChapter3.id }
        ),
        GetChapterByIDWithCommentCountTestData(
            chapterID = storyChapter3.id,
            environmentLangID = language2.id,
            expectedCommentCount = allComment.count { it.contentID == storyChapter3.id }
        ),
        GetChapterByIDWithCommentCountTestData(
            chapterID = storyChapter3.id,
            environmentLangID = 100000,
            expectedCommentCount = allComment.count { it.contentID == storyChapter3.id }
        ),
    )
}