package database.data

import database.external.model.Author
import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple
import database.external.model.user.UserSimple
import database.internal.executor.ReadChaptersExecutor
import database.internal.mock.*
import database.utils.*
import java.util.stream.Stream

/** Model of data to test [ReadChaptersExecutor] in simple scenarios where no data expected. */
internal data class GetChapterListWithoutExpectedValueTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.id] argument. */
internal data class GetChapterListChapterIDTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterIDList: List<Int>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.id] argument. */
internal data class GetChapterListChapterTitleTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterTitleList: List<String>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.storyID] argument. */
internal data class GetChapterListChapterStoryIDTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterStoryIDList: List<Int>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.storyID] argument. */
internal data class GetChapterListChapterLanguageTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterLanguageListSimple: List<LanguageSimple>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.storyID] argument. */
internal data class GetChapterListChapterAvailableLanguageTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterAvailableLanguageListSimple: List<List<LanguageSimple>>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.authors] argument. */
internal data class GetChapterListChapterAuthorTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterAuthorList: List<List<Author>>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.tags] argument. */
internal data class GetChapterListChapterTagTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterTagListSimple: List<List<TagSimple>>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.userBasicInfo] argument. */
internal data class GetChapterListChapterUserTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterUserListBasicInfo: List<UserSimple>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.rating] argument. */
internal data class GetChapterListChapterRatingTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterRating: List<Int>
)

/** Model of data to test [ReadChaptersExecutor] to check [Chapter.commentsCount] argument. */
internal data class GetChapterListChapterCommentCountTestData(
    val chapterIDList: List<Int>,
    val environmentID: Int?,
    val expectedChapterCommentCount: List<Int>
)

/** Data creator to test [ReadChaptersExecutor].*/
internal object GetChapterListTestDataStreamCreator {

    /** Data to test [ReadChaptersExecutor] to check if any will be returned. */
    @JvmStatic
    fun checkEmptyChapterIDListScenarioTestData(): Stream<GetChapterListWithoutExpectedValueTestData> = Stream.of(
        GetChapterListWithoutExpectedValueTestData(
            chapterIDList = emptyList(),
            environmentID = -100000,
        ),
        GetChapterListWithoutExpectedValueTestData(
            chapterIDList = emptyList(),
            environmentID = -2,
        ),
        GetChapterListWithoutExpectedValueTestData(
            chapterIDList = emptyList(),
            environmentID = -1,
        ),
        GetChapterListWithoutExpectedValueTestData(
            chapterIDList = emptyList(),
            environmentID = 0,
        ),
        GetChapterListWithoutExpectedValueTestData(
            chapterIDList = emptyList(),
            environmentID = 1,
        ),
        GetChapterListWithoutExpectedValueTestData(
            chapterIDList = emptyList(),
            environmentID = 2,
        ),
        GetChapterListWithoutExpectedValueTestData(
            chapterIDList = emptyList(),
            environmentID = 100000,
        ),
    )

    /** Data to test [ReadChaptersExecutor] to check if any will be returned. */
    @JvmStatic
    fun checkNotExistedChapterIDListScenarioTestData(): Stream<GetChapterListWithoutExpectedValueTestData> {
        return Stream.of(
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(-1),
                environmentID = -100000,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(-1),
                environmentID = -2,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(-1),
                environmentID = -1,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(-1),
                environmentID = 0,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(-1),
                environmentID = 1,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(-1),
                environmentID = 2,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(-1),
                environmentID = 100000,
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check if any will be returned. */
    @JvmStatic
    fun checkWrongChapterIDListScenarioTestData(): Stream<GetChapterListWithoutExpectedValueTestData> {
        return Stream.of(
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(videoContent13.id),
                environmentID = -100000,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(videoContent13.id),
                environmentID = -2,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(videoContent13.id),
                environmentID = -1,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(videoContent13.id),
                environmentID = 0,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(videoContent13.id),
                environmentID = 1,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(videoContent13.id),
                environmentID = 2,
            ),
            GetChapterListWithoutExpectedValueTestData(
                chapterIDList = listOf(videoContent13.id),
                environmentID = 100000,
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.id] argument. */
    @JvmStatic
    fun checkChapterIDListScenarioTestData(): Stream<GetChapterListChapterIDTestData> {
        return Stream.of(
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -100000,
                expectedChapterIDList = listOf(storyChapter5.id)
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -2,
                expectedChapterIDList = listOf(storyChapter5.id)
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -1,
                expectedChapterIDList = listOf(storyChapter5.id)
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 0,
                expectedChapterIDList = listOf(storyChapter5.id)
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 1,
                expectedChapterIDList = listOf(storyChapter5.id)
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 2,
                expectedChapterIDList = listOf(storyChapter5.id)
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 100000,
                expectedChapterIDList = listOf(storyChapter5.id)
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -100000,
                expectedChapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -2,
                expectedChapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -1,
                expectedChapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = 0,
                expectedChapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = language1.id,
                expectedChapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = language2.id,
                expectedChapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
            ),
            GetChapterListChapterIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = 100000,
                expectedChapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.title] argument. */
    @JvmStatic
    fun checkTitleScenarioTestData(): Stream<GetChapterListChapterTitleTestData> {
        return Stream.of(
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -100000,
                expectedChapterTitleList = listOf(storyChapterContent5.title)
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -2,
                expectedChapterTitleList = listOf(storyChapterContent5.title)
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -1,
                expectedChapterTitleList = listOf(storyChapterContent5.title)
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 0,
                expectedChapterTitleList = listOf(storyChapterContent5.title)
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language1.id,
                expectedChapterTitleList = listOf(storyChapterContent5.title)
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language2.id,
                expectedChapterTitleList = listOf(storyChapterContent5.title)
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 100000,
                expectedChapterTitleList = listOf(storyChapterContent5.title)
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -100000,
                expectedChapterTitleList = listOf(
                    storyChapterContent4.title,
                    storyChapterContent5.title,
                    storyChapterContent44.title,
                )
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -2,
                expectedChapterTitleList = listOf(
                    storyChapterContent4.title,
                    storyChapterContent5.title,
                    storyChapterContent44.title,
                )
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -1,
                expectedChapterTitleList = listOf(
                    storyChapterContent4.title,
                    storyChapterContent5.title,
                    storyChapterContent44.title,
                )
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = 0,
                expectedChapterTitleList = listOf(
                    storyChapterContent4.title,
                    storyChapterContent5.title,
                    storyChapterContent44.title,
                )
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = language1.id,
                expectedChapterTitleList = listOf(
                    storyChapterContent4.title,
                    storyChapterContent5.title,
                    storyChapterContent44.title,
                )
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = language2.id,
                expectedChapterTitleList = listOf(
                    storyChapterContent4.title,
                    storyChapterContent5.title,
                    storyChapterContent44.title,
                )
            ),
            GetChapterListChapterTitleTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = 100000,
                expectedChapterTitleList = listOf(
                    storyChapterContent4.title,
                    storyChapterContent5.title,
                    storyChapterContent44.title,
                )
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.storyID] argument. */
    @JvmStatic
    fun checkStoryIDScenarioTestData(): Stream<GetChapterListChapterStoryIDTestData> {
        return Stream.of(
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -100000,
                expectedChapterStoryIDList = listOf(storyChapter5.storyID)
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -2,
                expectedChapterStoryIDList = listOf(storyChapter5.storyID)
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -1,
                expectedChapterStoryIDList = listOf(storyChapter5.storyID)
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 0,
                expectedChapterStoryIDList = listOf(storyChapter5.storyID)
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language1.id,
                expectedChapterStoryIDList = listOf(storyChapter5.storyID)
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language2.id,
                expectedChapterStoryIDList = listOf(storyChapter5.storyID)
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 100000,
                expectedChapterStoryIDList = listOf(storyChapter5.storyID)
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -100000,
                expectedChapterStoryIDList = listOf(
                    storyChapter4.storyID,
                    storyChapter5.storyID,
                    storyChapter44.storyID,
                )
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -2,
                expectedChapterStoryIDList = listOf(
                    storyChapter4.storyID,
                    storyChapter5.storyID,
                    storyChapter44.storyID,
                )
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = -1,
                expectedChapterStoryIDList = listOf(
                    storyChapter4.storyID,
                    storyChapter5.storyID,
                    storyChapter44.storyID,
                )
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = 0,
                expectedChapterStoryIDList = listOf(
                    storyChapter4.storyID,
                    storyChapter5.storyID,
                    storyChapter44.storyID,
                )
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = language1.id,
                expectedChapterStoryIDList = listOf(
                    storyChapter4.storyID,
                    storyChapter5.storyID,
                    storyChapter44.storyID,
                )
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = language2.id,
                expectedChapterStoryIDList = listOf(
                    storyChapter4.storyID,
                    storyChapter5.storyID,
                    storyChapter44.storyID,
                )
            ),
            GetChapterListChapterStoryIDTestData(
                chapterIDList = listOf(storyChapter4.id, storyChapter5.id, storyChapter44.id),
                environmentID = 100000,
                expectedChapterStoryIDList = listOf(
                    storyChapter4.storyID,
                    storyChapter5.storyID,
                    storyChapter44.storyID,
                )
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.language] argument. */
    @JvmStatic
    fun checkChapterLanguageScenarioTestData(): Stream<GetChapterListChapterLanguageTestData> {
        return Stream.of(
            GetChapterListChapterLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -100000,
                expectedChapterLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    )
                )
            ),
            GetChapterListChapterLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -1,
                expectedChapterLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    )
                )
            ),
            GetChapterListChapterLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 0,
                expectedChapterLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    )
                )
            ),
            GetChapterListChapterLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language1.id,
                expectedChapterLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language1.id)
                    )
                )
            ),
            GetChapterListChapterLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language2.id,
                expectedChapterLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForEnvironment(language2.id)
                    )
                )
            ),
            GetChapterListChapterLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 100000,
                expectedChapterLanguageListSimple = listOf(
                    LanguageSimple(
                        id = language1.id,
                        name = getLanguageByID(language1.id)
                            .translations
                            .getLanguageTranslationForDefaultEnvironment()
                    )
                )
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.availableLanguages] argument. */
    @JvmStatic
    fun checkChapterAvailableLanguageScenarioTestData(): Stream<GetChapterListChapterAvailableLanguageTestData> {
        return Stream.of(
            GetChapterListChapterAvailableLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -100000,
                expectedChapterAvailableLanguageListSimple = listOf(
                    listOf(
                        LanguageSimple(
                            id = language1.id,
                            name = getLanguageByID(language1.id)
                                .translations
                                .getLanguageTranslationForDefaultEnvironment()
                        )
                    )
                )
            ),
            GetChapterListChapterAvailableLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -1,
                expectedChapterAvailableLanguageListSimple = listOf(
                    listOf(
                        LanguageSimple(
                            id = language1.id,
                            name = getLanguageByID(language1.id)
                                .translations
                                .getLanguageTranslationForDefaultEnvironment()
                        )
                    )
                )
            ),
            GetChapterListChapterAvailableLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 0,
                expectedChapterAvailableLanguageListSimple = listOf(
                    listOf(
                        LanguageSimple(
                            id = language1.id,
                            name = getLanguageByID(language1.id)
                                .translations
                                .getLanguageTranslationForDefaultEnvironment()
                        )
                    )
                )
            ),
            GetChapterListChapterAvailableLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language1.id,
                expectedChapterAvailableLanguageListSimple = listOf(
                    listOf(
                        LanguageSimple(
                            id = language1.id,
                            name = getLanguageByID(language1.id)
                                .translations
                                .getLanguageTranslationForEnvironment(language1.id)
                        )
                    )
                )
            ),
            GetChapterListChapterAvailableLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language2.id,
                expectedChapterAvailableLanguageListSimple = listOf(
                    listOf(
                        LanguageSimple(
                            id = language1.id,
                            name = getLanguageByID(language1.id)
                                .translations
                                .getLanguageTranslationForEnvironment(language2.id)
                        )
                    )
                )
            ),
            GetChapterListChapterAvailableLanguageTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 100000,
                expectedChapterAvailableLanguageListSimple = listOf(
                    listOf(
                        LanguageSimple(
                            id = language1.id,
                            name = getLanguageByID(language1.id)
                                .translations
                                .getLanguageTranslationForDefaultEnvironment()
                        )
                    )
                )
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.authors] argument. */
    @JvmStatic
    fun checkChapterAuthorScenarioTestData(): Stream<GetChapterListChapterAuthorTestData> {
        return Stream.of(
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter4.id),
                environmentID = -100000,
                expectedChapterAuthorList = listOf(emptyList())
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter4.id),
                environmentID = -1,
                expectedChapterAuthorList = listOf(emptyList())
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter4.id),
                environmentID = 0,
                expectedChapterAuthorList = listOf(emptyList())
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter4.id),
                environmentID = language1.id,
                expectedChapterAuthorList = listOf(emptyList())
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter4.id),
                environmentID = language2.id,
                expectedChapterAuthorList = listOf(emptyList())
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter4.id),
                environmentID = 100000,
                expectedChapterAuthorList = listOf(emptyList())
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -100000,
                expectedChapterAuthorList = listOf(
                    listOf(
                        Author(
                            id = author4.id,
                            name = author4.name
                        )
                    )
                )
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -1,
                expectedChapterAuthorList = listOf(
                    listOf(
                        Author(
                            id = author4.id,
                            name = author4.name
                        )
                    )
                )
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 0,
                expectedChapterAuthorList = listOf(
                    listOf(
                        Author(
                            id = author4.id,
                            name = author4.name
                        )
                    )
                )
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language1.id,
                expectedChapterAuthorList = listOf(
                    listOf(
                        Author(
                            id = author4.id,
                            name = author4.name
                        )
                    )
                )
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language2.id,
                expectedChapterAuthorList = listOf(
                    listOf(
                        Author(
                            id = author4.id,
                            name = author4.name
                        )
                    )
                )
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 100000,
                expectedChapterAuthorList = listOf(
                    listOf(
                        Author(
                            id = author4.id,
                            name = author4.name
                        )
                    )
                )
            ),
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter3.id),
                environmentID = -100000,
                expectedChapterAuthorList = listOf(
                    listOf(
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
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter3.id),
                environmentID = -1,
                expectedChapterAuthorList = listOf(
                    listOf(
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
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter3.id),
                environmentID = 0,
                expectedChapterAuthorList = listOf(
                    listOf(
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
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter3.id),
                environmentID = language1.id,
                expectedChapterAuthorList = listOf(
                    listOf(
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
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter3.id),
                environmentID = language2.id,
                expectedChapterAuthorList = listOf(
                    listOf(
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
            GetChapterListChapterAuthorTestData(
                chapterIDList = listOf(storyChapter3.id),
                environmentID = 100000,
                expectedChapterAuthorList = listOf(
                    listOf(
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
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.tags] argument. */
    @JvmStatic
    fun checkChapterTagScenarioTestData(): Stream<GetChapterListChapterTagTestData> {
        return Stream.of(
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -100000,
                expectedChapterTagListSimple = listOf(emptyList())
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -1,
                expectedChapterTagListSimple = listOf(emptyList())
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 0,
                expectedChapterTagListSimple = listOf(emptyList())
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language1.id,
                expectedChapterTagListSimple = listOf(emptyList())
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language2.id,
                expectedChapterTagListSimple = listOf(emptyList())
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 100000,
                expectedChapterTagListSimple = listOf(emptyList())
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -100000,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag3.id,
                            name = getTagByID(tag3.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        )
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = -1,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag3.id,
                            name = getTagByID(tag3.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        )
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 0,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag3.id,
                            name = getTagByID(tag3.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        )
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language1.id,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag3.id,
                            name = getTagByID(tag3.id)
                                .translations
                                .getTagTranslationForEnvironment(language1.id),
                            isOneOfMainTag = false
                        )
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = language2.id,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag3.id,
                            name = getTagByID(tag3.id)
                                .translations
                                .getTagTranslationForEnvironment(language2.id),
                            isOneOfMainTag = false
                        )
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter5.id),
                environmentID = 100000,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag3.id,
                            name = getTagByID(tag3.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        )
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter2.id),
                environmentID = -100000,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag8.id,
                            name = getTagByID(tag8.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                        TagSimple(
                            id = tag1.id,
                            name = getTagByID(tag1.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter2.id),
                environmentID = -1,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag8.id,
                            name = getTagByID(tag8.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                        TagSimple(
                            id = tag1.id,
                            name = getTagByID(tag1.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter2.id),
                environmentID = 0,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag8.id,
                            name = getTagByID(tag8.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                        TagSimple(
                            id = tag1.id,
                            name = getTagByID(tag1.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter2.id),
                environmentID = language1.id,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag8.id,
                            name = getTagByID(tag8.id)
                                .translations
                                .getTagTranslationForEnvironment(language1.id),
                            isOneOfMainTag = false
                        ),
                        TagSimple(
                            id = tag1.id,
                            name = getTagByID(tag1.id)
                                .translations
                                .getTagTranslationForEnvironment(language1.id),
                            isOneOfMainTag = false
                        ),
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter2.id),
                environmentID = language2.id,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag8.id,
                            name = getTagByID(tag8.id)
                                .translations
                                .getTagTranslationForEnvironment(language2.id),
                            isOneOfMainTag = false
                        ),
                        TagSimple(
                            id = tag1.id,
                            name = getTagByID(tag1.id)
                                .translations
                                .getTagTranslationForEnvironment(language2.id),
                            isOneOfMainTag = false
                        ),
                    )
                )
            ),
            GetChapterListChapterTagTestData(
                chapterIDList = listOf(storyChapter2.id),
                environmentID = language2.id,
                expectedChapterTagListSimple = listOf(
                    listOf(
                        TagSimple(
                            id = tag8.id,
                            name = getTagByID(tag8.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                        TagSimple(
                            id = tag1.id,
                            name = getTagByID(tag1.id)
                                .translations
                                .getTagTranslationForDefaultEnvironment(),
                            isOneOfMainTag = false
                        ),
                    )
                )
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.userBasicInfo] argument. */
    @JvmStatic
    fun checkChapterUserScenarioTestData(): Stream<GetChapterListChapterUserTestData> {
        return Stream.of(
            GetChapterListChapterUserTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -100000,
                expectedChapterUserListBasicInfo = listOf(
                    UserSimple(
                        id = user3.id,
                        name = user3.name
                    )
                )
            ),
            GetChapterListChapterUserTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -1,
                expectedChapterUserListBasicInfo = listOf(
                    UserSimple(
                        id = user3.id,
                        name = user3.name
                    )
                )
            ),
            GetChapterListChapterUserTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 0,
                expectedChapterUserListBasicInfo = listOf(
                    UserSimple(
                        id = user3.id,
                        name = user3.name
                    )
                )
            ),
            GetChapterListChapterUserTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language1.id,
                expectedChapterUserListBasicInfo = listOf(
                    UserSimple(
                        id = user3.id,
                        name = user3.name
                    )
                )
            ),
            GetChapterListChapterUserTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language2.id,
                expectedChapterUserListBasicInfo = listOf(
                    UserSimple(
                        id = user3.id,
                        name = user3.name
                    )
                )
            ),
            GetChapterListChapterUserTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 100000,
                expectedChapterUserListBasicInfo = listOf(
                    UserSimple(
                        id = user3.id,
                        name = user3.name
                    )
                )
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.rating] argument. */
    @JvmStatic
    fun checkChapterRatingScenarioTestData(): Stream<GetChapterListChapterRatingTestData> {
        return Stream.of(
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -100000,
                expectedChapterRating = listOf(storyChapterContent8.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -1,
                expectedChapterRating = listOf(storyChapterContent8.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 0,
                expectedChapterRating = listOf(storyChapterContent8.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language1.id,
                expectedChapterRating = listOf(storyChapterContent8.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language2.id,
                expectedChapterRating = listOf(storyChapterContent8.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 100000,
                expectedChapterRating = listOf(storyChapterContent8.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = -100000,
                expectedChapterRating = listOf(storyChapterContent8.rating, storyChapterContent9.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = -1,
                expectedChapterRating = listOf(storyChapterContent8.rating, storyChapterContent9.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = 0,
                expectedChapterRating = listOf(storyChapterContent8.rating, storyChapterContent9.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = language1.id,
                expectedChapterRating = listOf(storyChapterContent8.rating, storyChapterContent9.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = language2.id,
                expectedChapterRating = listOf(storyChapterContent8.rating, storyChapterContent9.rating)
            ),
            GetChapterListChapterRatingTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = 100000,
                expectedChapterRating = listOf(storyChapterContent8.rating, storyChapterContent9.rating)
            ),
        )
    }

    /** Data to test [ReadChaptersExecutor] to check [Chapter.rating] argument. */
    @JvmStatic
    fun checkChapterCommentCountScenarioTestData(): Stream<GetChapterListChapterCommentCountTestData> {
        return Stream.of(
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -100000,
                expectedChapterCommentCount = listOf(allComment.count { it.contentID == storyChapterContent8.id })
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = -1,
                expectedChapterCommentCount = listOf(allComment.count { it.contentID == storyChapterContent8.id })
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 0,
                expectedChapterCommentCount = listOf(allComment.count { it.contentID == storyChapterContent8.id })
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language1.id,
                expectedChapterCommentCount = listOf(allComment.count { it.contentID == storyChapterContent8.id })
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = language2.id,
                expectedChapterCommentCount = listOf(allComment.count { it.contentID == storyChapterContent8.id })
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id),
                environmentID = 100000,
                expectedChapterCommentCount = listOf(allComment.count { it.contentID == storyChapterContent8.id })
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = -100000,
                expectedChapterCommentCount = listOf(
                    allComment.count { it.contentID == storyChapterContent8.id },
                    allComment.count { it.contentID == storyChapterContent9.id }
                )
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = -1,
                expectedChapterCommentCount = listOf(
                    allComment.count { it.contentID == storyChapterContent8.id },
                    allComment.count { it.contentID == storyChapterContent9.id }
                )
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = 0,
                expectedChapterCommentCount = listOf(
                    allComment.count { it.contentID == storyChapterContent8.id },
                    allComment.count { it.contentID == storyChapterContent9.id }
                )
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = language1.id,
                expectedChapterCommentCount = listOf(
                    allComment.count { it.contentID == storyChapterContent8.id },
                    allComment.count { it.contentID == storyChapterContent9.id }
                )
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = language2.id,
                expectedChapterCommentCount = listOf(
                    allComment.count { it.contentID == storyChapterContent8.id },
                    allComment.count { it.contentID == storyChapterContent9.id }
                )
            ),
            GetChapterListChapterCommentCountTestData(
                chapterIDList = listOf(storyChapter8.id, storyChapter9.id),
                environmentID = 100000,
                expectedChapterCommentCount = listOf(
                    allComment.count { it.contentID == storyChapterContent8.id },
                    allComment.count { it.contentID == storyChapterContent9.id }
                )
            ),
        )
    }
}