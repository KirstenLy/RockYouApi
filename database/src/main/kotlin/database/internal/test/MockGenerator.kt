package database.internal.test

import common.getRandomAndRemove
import common.takeRandomValues
import database.external.ContentType
import database.external.test.*
import database.internal.test.model.TestCommentCreationData
import java.util.UUID
import kotlin.random.Random

internal fun generateContentRegisters(
    picturesSize: Int,
    videosSize: Int,
    storiesSize: Int,
    chaptersSize: Int,
): List<TestContentRegister> {
    val allContentSize = picturesSize + videosSize + storiesSize + chaptersSize
    val result = mutableListOf<TestContentRegister>()

    val picturesIDs = MutableList(picturesSize) { it }
    val videosIDs = MutableList(videosSize) { it }
    val storiesIDs = MutableList(storiesSize) { it }
    val chaptersIDs = MutableList(chaptersSize) { it }

    (0..<allContentSize).map {
        val randomizeContentTypes = buildList {
            if (picturesIDs.isNotEmpty()) add(ContentType.PICTURE)
            if (videosIDs.isNotEmpty()) add(ContentType.VIDEO)
            if (storiesIDs.isNotEmpty()) add(ContentType.STORY)
            if (chaptersIDs.isNotEmpty()) add(ContentType.STORY_CHAPTER)
        }
        val contentType = randomizeContentTypes.random()
        val contentID = when (contentType) {
            ContentType.PICTURE -> picturesIDs.random()
            ContentType.VIDEO -> videosIDs.random()
            ContentType.STORY -> storiesIDs.random()
            ContentType.STORY_CHAPTER -> chaptersIDs.random()
        }
        when (contentType) {
            ContentType.PICTURE -> picturesIDs.remove(contentID)
            ContentType.VIDEO -> videosIDs.remove(contentID)
            ContentType.STORY -> storiesIDs.remove(contentID)
            ContentType.STORY_CHAPTER -> chaptersIDs.remove(contentID)
        }
        TestContentRegister(
            id = it,
            contentType = contentType.typeID,
            contentID = contentID
        ).also(result::add)
    }
    return result
}

internal fun generateTestPictures(
    availableRegisterIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>
) = availableRegisterIDs.map { registerID ->
    generateTestPicture(
        registerID,
        availableUsersIDs,
        availableAuthorsIDs,
        availableLanguagesIDs,
        availableTagsIDs
    )
}

internal fun generateTestVideos(
    availableRegisterIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>
) = availableRegisterIDs.map { registerID ->
    generateTestVideo(
        registerID,
        availableUsersIDs,
        availableAuthorsIDs,
        availableLanguagesIDs,
        availableTagsIDs
    )
}

internal fun generateTestStories(
    availableStoriesIDs: List<Int>,
    availableChaptersIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>
): List<TestStory> {
    val mutableAvailableChaptersIDs = availableChaptersIDs.toMutableList()

    // It's a map [StoryID -> List<StoryChapterID>].
    // Every story must have minimum one chapter, so the map is initiated with a not empty list.
    // Every chapter has a unique ID, so when chapter associated with story by this map, chapterID removed from available IDs.
    val storyIDWithChapterIDListMap = availableStoriesIDs.associateWith {
        val initialChapterID = mutableAvailableChaptersIDs.getRandomAndRemove()
        mutableListOf(initialChapterID)
    }

    // Free chapters IDs randomly distributed between stories.
    val freeChapterIDCount = mutableAvailableChaptersIDs.size
    repeat(freeChapterIDCount) {
        val chapterID = mutableAvailableChaptersIDs.getRandomAndRemove()
        storyIDWithChapterIDListMap.values.random().add(chapterID)
    }

    val stories = availableStoriesIDs.map { storyID ->
        val storyChaptersIDs = storyIDWithChapterIDListMap[storyID] ?: throw IllegalStateException(
            "Test models initialization error, No chapters for story"
        )
        generateTestStory(
            storyID = storyID,
            availableChaptersIDs = storyChaptersIDs,
            availableUsersIDs = availableUsersIDs,
            availableAuthorsIDs = availableAuthorsIDs,
            availableLanguagesIDs = availableLanguagesIDs,
            availableTagsIDs = availableTagsIDs
        )
    }
    return stories
}

private fun generateTestPicture(
    id: Int,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>,
): TestPicture {
    val userID = availableUsersIDs.random()
    val languageID = if (Random.nextBoolean()) availableLanguagesIDs.random() else null
    val languagesIDs = if (Random.nextBoolean()) availableLanguagesIDs.takeRandomValues() else null
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = if (Random.nextBoolean()) availableTagsIDs.takeRandomValues() else null
    return TestPicture(
        id = id,
        title = "Picture with id №$id",
        url = UUID.randomUUID().toString(),
        languageID = languageID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        userID = userID,
        tagsIDs = tagsIDs,
        rating = generateRating(),
    )
}

private fun generateTestVideo(
    id: Int,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>,
): TestVideo {
    val userID = availableUsersIDs.random()
    val languageID = if (Random.nextBoolean()) availableLanguagesIDs.random() else null
    val languagesIDs = if (Random.nextBoolean()) availableLanguagesIDs.takeRandomValues() else null
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = if (Random.nextBoolean()) availableTagsIDs.takeRandomValues() else null
    return TestVideo(
        id = id,
        title = "Video with id №$id",
        url = UUID.randomUUID().toString(),
        languageID = languageID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        userID = userID,
        tagsIDs = tagsIDs,
        rating = generateRating(),
    )
}

private fun generateTestStory(
    storyID: Int,
    availableChaptersIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>,
): TestStory {
    val userID = availableUsersIDs.random()
    val languageID = availableLanguagesIDs.random()
    val languagesIDs = availableLanguagesIDs.takeRandomValues()
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = availableTagsIDs.takeRandomValues()
    val storyChapters = availableChaptersIDs.map { chapterID ->
        generateTestStoryChapter(
            chapterID = chapterID,
            storyID = storyID,
            availableUsersIDs = availableUsersIDs,
            availableAuthorsIDs = availableAuthorsIDs,
            availableLanguagesIDs = availableLanguagesIDs,
            availableTagsIDs = availableTagsIDs
        )
    }
    val storyNodes = generateTestStoriesNodes(storyChapters)

    return TestStory(
        id = storyID,
        title = "Story with ID: $storyID",
        languageID = languageID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        userID = userID,
        tagsIDs = tagsIDs,
        rating = generateRating(),
        storyNodes = storyNodes
    )
}

private fun generateTestStoryChapter(
    chapterID: Int,
    storyID: Int,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>,
): TestStoryChapter {
    val userID = availableUsersIDs.random()
    val languageID = availableLanguagesIDs.random()
    val languagesIDs = availableLanguagesIDs.takeRandomValues()
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = availableTagsIDs.takeRandomValues()

    return TestStoryChapter(
        id = chapterID,
        storyID = storyID,
        title = "Story chapter with ID: $chapterID and storyID: $storyID",
        languageID = languageID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        userID = userID,
        tagsIDs = tagsIDs,
        text = UUID.randomUUID().toString(),
        rating = generateRating(),
    )
}

// It's not good, but straightforward and stable.
internal fun generateTestStoriesNodes(storyChapters: List<TestStoryChapter>): List<TestStoryNode> {
    val localResult = mutableListOf<TestStoryNode>()
    when (storyChapters.size) {
        0 -> throw IllegalStateException("Minimum one chapter required for story")
        1 -> {
            val chapterStoryNode = TestStoryNode.TestChapterNode(storyChapters.first())
            localResult.add(chapterStoryNode)
        }

        2 -> {
            val firstChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters.first())
            val secondChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters[1])
            localResult.add(firstChapterStoryNode)
            localResult.add(secondChapterStoryNode)
        }

        3 -> {
            val firstChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters.first())
            val forkNode = TestStoryNode.TestForkNode(
                variants = listOf(
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(TestStoryNode.TestChapterNode(storyChapters[1]))
                    ),
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(TestStoryNode.TestChapterNode(storyChapters[2]))
                    )
                )
            )
            localResult.add(firstChapterStoryNode)
            localResult.add(forkNode)
        }

        4 -> {
            val firstChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters.first())
            val secondChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters[1])
            val forkNode = TestStoryNode.TestForkNode(
                variants = listOf(
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(TestStoryNode.TestChapterNode(storyChapters[2]))
                    ),
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(TestStoryNode.TestChapterNode(storyChapters[3]))
                    )
                )
            )
            localResult.add(firstChapterStoryNode)
            localResult.add(secondChapterStoryNode)
            localResult.add(forkNode)
        }

        5 -> {
            val firstChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters.first())
            val secondChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters[1])
            val forkNode = TestStoryNode.TestForkNode(
                variants = listOf(
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(TestStoryNode.TestChapterNode(storyChapters[2]))
                    ),
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(
                            TestStoryNode.TestChapterNode(storyChapters[3]),
                            TestStoryNode.TestChapterNode(storyChapters[4]),
                        )
                    )
                )
            )
            localResult.add(firstChapterStoryNode)
            localResult.add(secondChapterStoryNode)
            localResult.add(forkNode)
        }

        else -> {
            val firstChapters = storyChapters.take(5)
            val otherChapters = storyChapters - firstChapters

            val firstChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters.first())
            val secondChapterStoryNode = TestStoryNode.TestChapterNode(storyChapters[1])
            val forkNode = TestStoryNode.TestForkNode(
                variants = listOf(
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(
                            TestStoryNode.TestChapterNode(storyChapters[2])
                        )
                    ),
                    TestStoryForkVariant(
                        variantText = UUID.randomUUID().toString(),
                        nodes = listOf(
                            TestStoryNode.TestChapterNode(storyChapters[3]),
                            TestStoryNode.TestForkNode(
                                variants = listOf(
                                    TestStoryForkVariant(
                                        variantText = UUID.randomUUID().toString(),
                                        nodes = listOf(
                                            TestStoryNode.TestChapterNode(storyChapters[4]),
                                        )
                                    ),
                                    TestStoryForkVariant(
                                        variantText = UUID.randomUUID().toString(),
                                        nodes = listOf(
                                            TestStoryNode.TestChapterNode(storyChapters[5]),
                                        ).plus(otherChapters.map { TestStoryNode.TestChapterNode(it) })
                                    ),
                                )
                            )
                        )
                    )
                )
            )
            localResult.add(firstChapterStoryNode)
            localResult.add(secondChapterStoryNode)
            localResult.add(forkNode)
        }
    }
    return localResult
}

private fun generateRating(minValue: Int = -10000, maxValue: Int = 1000): Int {
    if (minValue > maxValue) throw IllegalStateException("minValue > maxValue")
    if (minValue == maxValue) return minValue
    return (minValue..maxValue).random()
}

internal fun generateUsers() = listOf(
    TestUser(0, "Ivan"),
    TestUser(1, "Olga"),
    TestUser(2, "Natalia"),
    TestUser(3, "Egor"),
    TestUser(4, "Oleg"),
    TestUser(5, "Viktor"),
)

internal fun generateUsersDefaultAuthData() = listOf(
    TestUserAuthData(0, 0, UUID.randomUUID().toString()),
    TestUserAuthData(1, 1, UUID.randomUUID().toString()),
    TestUserAuthData(2, 2, UUID.randomUUID().toString()),
    TestUserAuthData(3, 3, UUID.randomUUID().toString()),
    TestUserAuthData(4, 4, UUID.randomUUID().toString()),
    TestUserAuthData(5, 5, UUID.randomUUID().toString()),
)

internal fun getAuthors() = listOf(
    TestAuthor(0, "Thomas Wyatt"),
    TestAuthor(1, "William Shakespeare"),
    TestAuthor(2, "Thomas Hardy"),
    TestAuthor(3, "Arthur Conan Doyle"),
    TestAuthor(4, "John Tolkien"),
    TestAuthor(5, "Joanne Rowling"),
)

internal fun getLanguages(): List<TestLanguage> {
    val result = mutableListOf<TestLanguage>()

    var languageTranslationRecordIDX: Short = 0
    (0..3).forEach { languageID ->
        val languageTranslations = List(4) {
            TestLanguageTranslation(
                id = languageTranslationRecordIDX,
                langID = languageID.toByte(),
                envID = it.toByte(),
                name = UUID.randomUUID().toString()
            ).also { languageTranslationRecordIDX++ }
        }
        val language = TestLanguage(
            id = languageID.toByte(),
            isDefault = languageID == 0,
            translations = languageTranslations
        )
        result.add(language)
    }

    return result
}

internal fun getTags(availableLanguagesIDs: List<Byte>): List<TestTag> {
    return List(40) {
        val tagTranslations = listOf(
            TestTagTranslation(
                id = if (it == 0) 0 else it * 4,
                tagID = it.toByte(),
                envID = availableLanguagesIDs[0],
                name = UUID.randomUUID().toString()
            ),
            TestTagTranslation(
                id = if (it == 0) 1 else (it * 4) + 1,
                tagID = it.toByte(),
                envID = availableLanguagesIDs[1],
                name = UUID.randomUUID().toString()
            ),
            TestTagTranslation(
                id = if (it == 0) 2 else (it * 4) + 2,
                tagID = it.toByte(),
                envID = availableLanguagesIDs[2],
                name = UUID.randomUUID().toString()
            ),
            TestTagTranslation(
                id = if (it == 0) 3 else (it * 4) + 3,
                tagID = it.toByte(),
                envID = availableLanguagesIDs[3],
                name = UUID.randomUUID().toString()
            ),
        )
        TestTag(
            id = it.toShort(),
            translations = tagTranslations
        )
    }
}

internal fun generateTestComments(commentData: List<TestCommentCreationData>) = List(commentData.size) { idx ->
    TestComment(
        id = idx + 1,
        contentID = commentData[idx].contentID,
        userID = commentData[idx].userID,
        userName = commentData[idx].userName,
        text = "Comment: №$idx",
    )
}