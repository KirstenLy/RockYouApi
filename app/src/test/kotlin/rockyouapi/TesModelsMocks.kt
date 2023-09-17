package rockyouapi

import common.takeRandomValues
import database.external.ContentType
import database.external.test.*
import java.util.UUID
import kotlin.random.Random

private const val CONTENT_URL_DEFAILT = "https://google.com"

// TODO: Тут везде пробежаться и сделать единообразно
// TODO: Написать тесты и на эти функции?
// TODO: Поубирать тут и везде "Model"

// TODO: Описать в коде как оно работает
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

// 0 -> 0, 1, 2 ,3
// 1 -> 4, 5, 6, 7
// 2 -> 8, 9, 10, 11
// 3 -> 12, 13, 14, 15
internal fun getDefaultLanguages(): List<TestLanguage> = listOf(
    TestLanguage(
        id = 0,
        isDefault = false,
        translations = listOf(
            TestLanguageTranslation(
                id = 0,
                langID = 0,
                envID = 0,
                name = "Язык 0 перевод для 0",
            ),
            TestLanguageTranslation(
                id = 1,
                langID = 1,
                envID = 1,
                name = "Язык 0 перевод для 1",
            ),
            TestLanguageTranslation(
                id = 2,
                langID = 2,
                envID = 2,
                name = "Язык 0 перевод для 2",

                ),
            TestLanguageTranslation(
                id = 3,
                langID = 3,
                envID = 3,
                name = "Язык 0 перевод для 3",
            ),
        )
    ),
    TestLanguage(
        id = 1,
        isDefault = true,
        translations = listOf(
            TestLanguageTranslation(
                id = 4,
                langID = 0,
                envID = 0,
                name = "Язык 1 перевод для 0",
            ),
            TestLanguageTranslation(
                id = 5,
                langID = 1,
                envID = 1,
                name = "Язык 1 перевод для 1",
            ),
            TestLanguageTranslation(
                id = 6,
                langID = 2,
                envID = 2,
                name = "Язык 1 перевод для 2",

                ),
            TestLanguageTranslation(
                id = 7,
                langID = 3,
                envID = 3,
                name = "Язык 1 перевод для 3",
            ),
        )
    ),
    TestLanguage(
        id = 2,
        isDefault = false,
        translations = listOf(
            TestLanguageTranslation(
                id = 8,
                langID = 0,
                envID = 0,
                name = "Язык 2 перевод для 0",
            ),
            TestLanguageTranslation(
                id = 9,
                langID = 1,
                envID = 1,
                name = "Язык 2 перевод для 1",

                ),
            TestLanguageTranslation(
                id = 10,
                langID = 2,
                envID = 2,
                name = "Язык 2 перевод для 2",
            ),
            TestLanguageTranslation(
                id = 11,
                langID = 3,
                envID = 3,
                name = "Язык 2 перевод для 3",
            ),
        )
    ),
    TestLanguage(
        id = 3,
        isDefault = false,
        translations = listOf(
            TestLanguageTranslation(
                id = 12,
                langID = 0,
                envID = 0,
                name = "Язык 3 перевод для 0",
            ),
            TestLanguageTranslation(
                id = 13,
                langID = 1,
                envID = 1,
                name = "Язык 3 перевод для 1",
            ),
            TestLanguageTranslation(
                id = 14,
                langID = 2,
                envID = 2,
                name = "Язык 3 перевод для 2",
            ),
            TestLanguageTranslation(
                id = 15,
                langID = 3,
                envID = 3,
                name = "Язык 3 перевод для 3",
            ),
        )
    ),
)

internal fun getDefaultUsers() = listOf(
    TestUser(0, "Ivan"),
    TestUser(1, "Olga"),
    TestUser(2, "Natalia"),
    TestUser(3, "Egor"),
    TestUser(4, "Oleg"),
    TestUser(5, "Viktor"),
)

internal fun getUsersDefaultAuthData() = listOf(
    TestUserAuthData(0, 0, UUID.randomUUID().toString()),
    TestUserAuthData(1, 1, UUID.randomUUID().toString()),
    TestUserAuthData(2, 2, UUID.randomUUID().toString()),
    TestUserAuthData(3, 3, UUID.randomUUID().toString()),
    TestUserAuthData(4, 4, UUID.randomUUID().toString()),
    TestUserAuthData(5, 5, UUID.randomUUID().toString()),
)

internal fun getDefaultAuthors() = listOf(
    TestAuthor(0, "Thomas Wyatt"),
    TestAuthor(1, "William Shakespeare"),
    TestAuthor(2, "Thomas Hardy"),
    TestAuthor(3, "Arthur Conan Doyle"),
    TestAuthor(4, "John Tolkien"),
    TestAuthor(5, "Joanne Rowling"),
)


// TODO: Убрать =emptyList, сделано чтобы не сломались все тесты разом
// TODO: Написать, что метод внутри себя сам решает, добавлять языки, авторов, юзеров и прочее
internal fun generateTestPictures(
    number: Int = 40,
    availableUsersIDs: List<Int> = emptyList(),
    availableAuthorsIDs: List<Int> = emptyList(),
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
    namePart: String? = null
) = List(number) { idx ->
    generateTestPictureModel(
        idx,
        availableUsersIDs,
        availableAuthorsIDs,
        availableLanguagesIDs,
        availableTagsIDs,
        namePart
    )
}


// TODO: Убрать =emptyList, сделано чтобы не сломались все тесты разом
// TODO: Написать, что метод внутри себя сам решает, добавлять языки, авторов, юзеров и прочее
internal fun generateTestVideos(
    number: Int = 40,
    availableUsersIDs: List<Int> = emptyList(),
    availableAuthorsIDs: List<Int> = emptyList(),
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
    namePart: String? = null
) = List(number) { idx ->
    generateTestVideoModel(
        idx,
        availableUsersIDs,
        availableAuthorsIDs,
        availableLanguagesIDs,
        availableTagsIDs,
        namePart
    )
}

internal fun generateTestStories(
    storiesRegisterRecords: List<TestContentRegister>,
    reservedChaptersCountList: List<Int>,
    availableUsersIDs: List<Int> = emptyList(),
    availableAuthorsIDs: List<Int> = emptyList(),
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
): List<TestStory> {
    val result = mutableListOf<TestStory>()

    var insertedChaptersCounter = 0

    storiesRegisterRecords.forEachIndexed { index, registerRecord ->
        val chaptersToInsertNumber = reservedChaptersCountList[index]
        val left = insertedChaptersCounter
        val right = left + chaptersToInsertNumber
        generateTestStoryModel1(
            registerID = registerRecord.id,
            entityID = registerRecord.contentID,
            chaptersAvailableIDs = (left..<right),
            availableUsersIDs = availableUsersIDs,
            availableAuthorsIDs = availableAuthorsIDs,
            availableLanguagesIDs = availableLanguagesIDs,
            availableTagsIDs = availableTagsIDs
        ).also(result::add)
        insertedChaptersCounter += chaptersToInsertNumber
    }
    return result
}

internal fun generateTestStoriesChapters(
    chaptersCountForEveryStory: Map<Int, Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
): List<TestStoryChapter> {
    val result = mutableListOf<TestStoryChapter>()
    var chapterIDX = 0
    chaptersCountForEveryStory.forEach { storyRegisterID, chaptersCount ->
        repeat(chaptersCount) {
            generateTestStoryChapter(
                number = chapterIDX,
                storyID = storyRegisterID,
                availableUsersIDs = availableUsersIDs,
                availableAuthorsIDs = availableAuthorsIDs,
                availableLanguagesIDs = availableLanguagesIDs,
                availableTagsIDs = availableTagsIDs,
            ).also(result::add)
            chapterIDX++
        }
    }
    return result
}

internal fun generateTestStoriesChapters1(
    storyID: Int,
    chaptersAvailableIDs: IntRange,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
): List<TestStoryChapter> {
    val result = mutableListOf<TestStoryChapter>()
    chaptersAvailableIDs.forEach { chapterEntityID ->
        generateTestStoryChapter(
            number = chapterEntityID,
            storyID = storyID,
            availableUsersIDs = availableUsersIDs,
            availableAuthorsIDs = availableAuthorsIDs,
            availableLanguagesIDs = availableLanguagesIDs,
            availableTagsIDs = availableTagsIDs,
        ).also(result::add)
    }
    return result
}

internal fun generateTestStoriesChapters(
    storiesRegistersIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
): List<TestStoryChapter> {
    val result = mutableListOf<TestStoryChapter>()
    var chapterIDX = 0
    storiesRegistersIDs.forEach { storyRegisterID ->
        repeat((1..10).random()) {
            generateTestStoryChapter(
                number = chapterIDX,
                storyID = storyRegisterID,
                availableUsersIDs = availableUsersIDs,
                availableAuthorsIDs = availableAuthorsIDs,
                availableLanguagesIDs = availableLanguagesIDs,
                availableTagsIDs = availableTagsIDs,
            ).also(result::add)
            chapterIDX++
        }
    }
    return result
}

internal fun generateTestStoriesNodes1(storyChapters: List<TestStoryChapter>): List<TestStoryNode> {
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

internal fun generateTestStoriesNodes(storiesWithChapters: HashMap<Int, List<Int>>): List<Pair<Int, List<TestStoryNode>>> {
    val result = mutableListOf<Pair<Int, List<TestStoryNode>>>()

//    storiesWithChapters.forEach { (storyRegisterID, chaptersRegistersIDs) ->
//        val localResult = mutableListOf<TestStoryNode>()
//        when (chaptersRegistersIDs.size) {
//            0 -> throw IllegalStateException("Minimum one chapter required for story")
//            1 -> {
//                val chapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs.first())
//                localResult.add(chapterStoryNode)
//            }
//
//            2 -> {
//                val firstChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs.first())
//                val secondChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs[1])
//                localResult.add(firstChapterStoryNode)
//                localResult.add(secondChapterStoryNode)
//            }
//
//            3 -> {
//                val firstChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs.first())
//                val forkNode = TestStoryNode.TestForkNode(
//                    variants = listOf(
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(TestStoryNode.TestChapterNode(chaptersRegistersIDs[1]))
//                        ),
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(TestStoryNode.TestChapterNode(chaptersRegistersIDs[2]))
//                        )
//                    )
//                )
//                localResult.add(firstChapterStoryNode)
//                localResult.add(forkNode)
//            }
//
//            4 -> {
//                val firstChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs.first())
//                val secondChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs[1])
//                val forkNode = TestStoryNode.TestForkNode(
//                    variants = listOf(
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(TestStoryNode.TestChapterNode(chaptersRegistersIDs[2]))
//                        ),
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(TestStoryNode.TestChapterNode(chaptersRegistersIDs[3]))
//                        )
//                    )
//                )
//                localResult.add(firstChapterStoryNode)
//                localResult.add(secondChapterStoryNode)
//                localResult.add(forkNode)
//            }
//
//            5 -> {
//                val firstChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs.first())
//                val secondChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs[1])
//                val forkNode = TestStoryNode.TestForkNode(
//                    variants = listOf(
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(TestStoryNode.TestChapterNode(chaptersRegistersIDs[2]))
//                        ),
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(
//                                TestStoryNode.TestChapterNode(chaptersRegistersIDs[3]),
//                                TestStoryNode.TestChapterNode(chaptersRegistersIDs[4]),
//                            )
//                        )
//                    )
//                )
//                localResult.add(firstChapterStoryNode)
//                localResult.add(secondChapterStoryNode)
//                localResult.add(forkNode)
//            }
//
//            else -> {
//                val firstChapters = chaptersRegistersIDs.take(5)
//                val otherChapters = chaptersRegistersIDs - firstChapters
//
//                val firstChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs.first())
//                val secondChapterStoryNode = TestStoryNode.TestChapterNode(chaptersRegistersIDs[1])
//                val forkNode = TestStoryNode.TestForkNode(
//                    variants = listOf(
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(
//                                TestStoryNode.TestChapterNode(chaptersRegistersIDs[2])
//                            )
//                        ),
//                        TestStoryForkVariant(
//                            variantText = UUID.randomUUID().toString(),
//                            nodes = listOf(
//                                TestStoryNode.TestChapterNode(chaptersRegistersIDs[3]),
//                                TestStoryNode.TestForkNode(
//                                    variants = listOf(
//                                        TestStoryForkVariant(
//                                            variantText = UUID.randomUUID().toString(),
//                                            nodes = listOf(
//                                                TestStoryNode.TestChapterNode(chaptersRegistersIDs[4]),
//                                            )
//                                        ),
//                                        TestStoryForkVariant(
//                                            variantText = UUID.randomUUID().toString(),
//                                            nodes = listOf(
//                                                TestStoryNode.TestChapterNode(chaptersRegistersIDs[5]),
//                                            ).plus(otherChapters.map { TestStoryNode.TestChapterNode(it) })
//                                        ),
//                                    )
//                                )
//                            )
//                        )
//                    )
//                )
//                localResult.add(firstChapterStoryNode)
//                localResult.add(secondChapterStoryNode)
//                localResult.add(forkNode)
//            }
//        }
//        result.add(storyRegisterID to localResult)
//    }
    return result
}


//List(number) { idx ->
//    generateTestStoryChapterModel(idx, namePart)
//}

internal fun generateTestComments(commentData: List<TestCommentCreationData>) = List(commentData.size) { idx ->
    TestComment(
        id = idx,
        contentID = commentData[idx].contentID,
        userID = commentData[idx].userID,
        userName = commentData[idx].userName,
        text = "Comment: №$idx",
    )
}

internal fun generateTestFavorites(
    availableContentIDs: List<Int> = emptyList(),
    availableUsersIDs: List<Int> = emptyList()
): List<TestFavorite> {
    val result = mutableListOf<TestFavorite>()
    var idx = 0
    availableUsersIDs.forEach { userID ->
        val shouldAddFavoriteRecordsForUser = Random.nextBoolean()
        if (!shouldAddFavoriteRecordsForUser) return@forEach

        availableContentIDs.takeRandomValues().forEach { contentID ->
            result.add(TestFavorite(idx, userID, contentID))
            idx++
        }
    }
    return result
}

// 0 -> 0, 1, 2 ,3
// 1 -> 4, 5, 6, 7
// 2 -> 8, 9, 10, 11
// 3 -> 12, 13, 14, 15
internal fun generateTestTags(number: Int = 40, availableLanguagesIDs: List<Byte> = emptyList()) = List(number) {
    val tagTranslations = listOf(
        TestTagTranslation(
            id = if (it == 0) 0 else it * 4,
            envID = availableLanguagesIDs[0],
            tagID = it.toByte(),
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 1 else (it * 4) + 1,
            envID = availableLanguagesIDs[1],
            tagID = it.toByte(),
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 2 else (it * 4) + 2,
            envID = availableLanguagesIDs[2],
            tagID = it.toByte(),
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 3 else (it * 4) + 3,
            envID = availableLanguagesIDs[3],
            tagID = it.toByte(),
            name = UUID.randomUUID().toString()
        ),
    )
    TestTag(
        id = it.toShort(),
        translations = tagTranslations
    )
}

internal fun generateTestTagsTranslations(number: Int = 40, namePart: String? = null) = List(number) {
    generateTagTranslationModel(it, namePart)
}

internal fun generateRelationsFromTagToTranslates(count: Int = 40) = List(count) {
    TestRelationBetweenTagAndTagTranslation(it, it)
}

internal fun generateRelationsFromPictureToTag(count: Int = 40): List<TestRelationBetweenPictureAndTag> {
    val relations = mutableListOf<TestRelationBetweenPictureAndTag>()
    repeat(count) { pictureID ->
        if (Random.nextBoolean()) return@repeat
        repeat((0..10).random()) { tagID ->
            relations.add(TestRelationBetweenPictureAndTag(pictureID, tagID))
        }
    }
    return relations
}

internal fun generateEnvIDs(count: Int = 4) = List(count) { it }

// TODO: Тут везде заменить генерейт на креейт
private fun generateContentRegisterModel(contentType: Int, intRange: IntRange): List<TestContentRegister> {
    val shuffledRegistersIDs = intRange.shuffled()
    val shuffledContentIDs = (0..39).shuffled()

    return shuffledRegistersIDs.mapIndexed { idx, id ->
        TestContentRegister(
            id = id,
            contentType = contentType,
            contentID = shuffledContentIDs[idx]
        )
    }
}

// TODO: Убрать = emptyList
private fun generateTestPictureModel(
    number: Int,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
    namePart: String? = null
): TestPicture {
    val userID = availableUsersIDs.random()
    val languageID = if (Random.nextBoolean()) availableLanguagesIDs.random() else null
    val languagesIDs = if (Random.nextBoolean()) availableLanguagesIDs.takeRandomValues() else null
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = if (Random.nextBoolean()) availableTagsIDs.takeRandomValues() else null
    return TestPicture(
        id = number,
        title = namePart?.let { "$it $number" } ?: "Картинка №$number",
        url = CONTENT_URL_DEFAILT,
        languageID = languageID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        userID = userID,
        tagsIDs = tagsIDs,
        rating = generateRating(),
    )
}

// TODO: Убрать = emptyList
private fun generateTestVideoModel(
    number: Int,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList(),
    namePart: String? = null
): TestVideo {
    val userID = availableUsersIDs.random()
    val languageID = if (Random.nextBoolean()) availableLanguagesIDs.random() else null
    val languagesIDs = if (Random.nextBoolean()) availableLanguagesIDs.takeRandomValues() else null
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = if (Random.nextBoolean()) availableTagsIDs.takeRandomValues() else null
    return TestVideo(
        id = number,
        title = namePart?.let { "$it $number" } ?: "Видео №$number",
        url = "https://google.com",
        languageID = languageID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        userID = userID,
        tagsIDs = tagsIDs,
        rating = (-10000..10000).random(),
    )
}

private fun generateTestStoryModel(
    number: Int,
    availableUsersIDs: List<Int> = emptyList(),
    availableAuthorsIDs: List<Int> = emptyList(),
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList()
): TestStory {
    val userID = availableUsersIDs.random()
    val languageID = availableLanguagesIDs.random()
    val languagesIDs = availableLanguagesIDs.takeRandomValues()
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = availableTagsIDs.takeRandomValues()
    return TestStory(
        id = number,
        title = "Story №$number",
        languageID = languageID,
        userID = userID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        tagsIDs = tagsIDs,
        rating = (-10000..10000).random(),
        storyNodes = emptyList()
    )
}

private fun generateTestStoryModel1(
    registerID: Int,
    entityID: Int,
    chaptersAvailableIDs: IntRange,
    availableUsersIDs: List<Int> = emptyList(),
    availableAuthorsIDs: List<Int> = emptyList(),
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList()
): TestStory {
    val userID = availableUsersIDs.random()
    val languageID = availableLanguagesIDs.random()
    val languagesIDs = availableLanguagesIDs.takeRandomValues()
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = availableTagsIDs.takeRandomValues()
    val chapters = generateTestStoriesChapters1(
        storyID = registerID,
        chaptersAvailableIDs = chaptersAvailableIDs,
        availableUsersIDs = availableUsersIDs,
        availableAuthorsIDs = availableAuthorsIDs,
        availableLanguagesIDs = availableLanguagesIDs,
        availableTagsIDs = availableTagsIDs
    )
    val storyNodes = generateTestStoriesNodes1(chapters)
    return TestStory(
        id = entityID,
        title = "Story with entityID: $entityID",
        languageID = languageID,
        userID = userID,
        availableLanguagesIDs = languagesIDs,
        authorsIDs = authorsIDs,
        tagsIDs = tagsIDs,
        rating = (-10000..10000).random(),
        storyNodes = storyNodes
    )
}

private fun generateTestStoryChapter(
    number: Int,
    storyID: Int,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte> = emptyList(),
    availableTagsIDs: List<Short> = emptyList()
): TestStoryChapter {
    val userID = availableUsersIDs.random()
    val languageID = availableLanguagesIDs.random()
    val languagesIDs = availableLanguagesIDs.takeRandomValues()
    val authorsIDs = if (Random.nextBoolean()) availableAuthorsIDs.takeRandomValues() else null
    val tagsIDs = availableTagsIDs.takeRandomValues()
    return TestStoryChapter(
        id = number,
        title = "Part №$number",
        languageID = languageID,
        availableLanguagesIDs = languagesIDs,
        userID = userID,
        authorsIDs = authorsIDs,
        tagsIDs = tagsIDs,
        rating = (-10000..10000).random(),
        storyID = storyID,
        text = UUID.randomUUID().toString()
    )
}

private fun generateTagTranslationModel(number: Int, namePart: String? = null) = TestTagTranslation(
    id = number,
    envID = 0,
    tagID = 0,
    name = namePart?.let { "$it $number" } ?: "Перевод тега. Номер перевода: №$number",
)

/**
 * Generate language id. Purpose for generate "langID" argument of contents entities.
 * Can return null. It means entity has no language.
 * */
private fun generateNullableLanguageID(): Int? {
    return if (Random.nextBoolean()) null else (0..3).random()
}

/**
 * Generate language id. Purpose for generate "langID" argument of contents entities.
 * Cannot return null. Used for entity that has no sense without languages, like a story.
 * */
private fun generateLanguageID() = (0..3).random()

/**
 * Generate user id. Purpose for generate "userID" argument of contents entities.
 * Cannot return null. All content entities must have user.
 * */
private fun generateUserID() = (0..3).random()

/**
 * Generate user id. Purpose for generate "userID" argument of contents entities.
 * Cannot return null. All content entities must have user.
 * */
// TODO: Change comment
private fun generateAvailableLanguagesIDs(): List<Int>? {
    return if (Random.nextBoolean()) null else List((1..4).random()) { it }
}

/**
 * Generate authors IDs list. Purpose for generate "authors" argument of contents entities.
 * Can return null. It means an entity has no authors, or authors are unknown.
 * */
private fun generateAuthorsIDs(): List<Int>? {
    return if (Random.nextBoolean()) null else List((1..4).random()) { it }
}

/**
 * Generate a tags IDs list. Purpose for generate "tags" argument of contents entities.
 * Can return null. It means an entity has no authors. It's strange and seems like an error, but possible.
 * */
private fun generateTagsIDs(): List<Int>? {
    return if (Random.nextBoolean()) null else List((0..39).random()) { it }
}

private fun generateRating(minValue: Int = -10000, maxValue: Int = 1000): Int {
    if (minValue > maxValue) throw IllegalStateException("minValue > maxValue")
    if (minValue == maxValue) return minValue
    return (minValue..maxValue).random()
}


internal class TestCommentCreationData(val contentID: Int, val userID: Int, val userName: String)