package rockyouapi

import common.takeRandomValues
import database.external.model.*
import java.util.UUID
import kotlin.random.Random

private const val CONTENT_URL_DEFAILT = "https://google.com"

// TODO: Тут везде пробежаться и сделать единообразно
// TODO: Написать тесты и на эти функции?
internal fun generateContentRegisters(batchSize: Int = 40): List<TestContentRegister> {
    val picturesContentRegister = generateContentRegisterModel(1, 0..39)
    val videosContentRegister = generateContentRegisterModel(2, 40..79)
    val storiesContentRegister = generateContentRegisterModel(3, 80..119)
    val storiesChaptersContentRegister = generateContentRegisterModel(4, 120..159)
    return buildList {
        addAll(picturesContentRegister)
        addAll(videosContentRegister)
        addAll(storiesContentRegister)
        addAll(storiesChaptersContentRegister)
    }
//        .shuffled() // TODO: Это нужно восстановить. Для этого в методах list нужно явно установить ORDER BY
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
                name = "Язык 0 перевод для 0",
            ),
            TestLanguageTranslation(
                id = 1,
                langID = 1,
                name = "Язык 0 перевод для 1",
            ),
            TestLanguageTranslation(
                id = 2,
                langID = 2,
                name = "Язык 0 перевод для 2",

                ),
            TestLanguageTranslation(
                id = 3,
                langID = 3,
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
                name = "Язык 1 перевод для 0",
            ),
            TestLanguageTranslation(
                id = 5,
                langID = 1,
                name = "Язык 1 перевод для 1",
            ),
            TestLanguageTranslation(
                id = 6,
                langID = 2,
                name = "Язык 1 перевод для 2",

                ),
            TestLanguageTranslation(
                id = 7,
                langID = 3,
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
                name = "Язык 2 перевод для 0",
                ),
            TestLanguageTranslation(
                id = 9,
                langID = 1,
                name = "Язык 2 перевод для 1",

                ),
            TestLanguageTranslation(
                id = 10,
                langID = 2,
                name = "Язык 2 перевод для 2",


                ),
            TestLanguageTranslation(
                id = 11,
                langID = 3,
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
                name = "Язык 3 перевод для 0",

                ),
            TestLanguageTranslation(
                id = 13,
                langID = 1,
                name = "Язык 3 перевод для 1",

                ),
            TestLanguageTranslation(
                id = 14,
                langID = 2,
                name = "Язык 3 перевод для 2",

                ),
            TestLanguageTranslation(
                id = 15,
                langID = 3,
                name = "Язык 3 перевод для 3",

                ),
        )
    ),
)

// 0 -> 0, 1, 2 ,3
// 1 -> 4, 5, 6, 7
// 2 -> 8, 9, 10, 11
// 3 -> 12, 13, 14, 15
internal fun generateTes1tTags1(number: Int = 40, availableLanguagesIDs: List<Int> = emptyList()) = List(number) {
    val tagTranslations = listOf(
        TestTagTranslation(
            id = if (it == 0) 0 else it * 4,
            langID = availableLanguagesIDs[0],
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 1 else (it * 4) + 1,
            langID = availableLanguagesIDs[1],
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 2 else (it * 4) + 2,
            langID = availableLanguagesIDs[2],
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 3 else (it * 4) + 3,
            langID = availableLanguagesIDs[3],
            name = UUID.randomUUID().toString()
        ),
    )
    TestTag(
        id = it,
        translations = tagTranslations
    )
}


internal fun getDefaultUsers() = listOf(
    TestUser(0, "Ivan"),
    TestUser(1, "Olga"),
    TestUser(2, "Natalia"),
    TestUser(3, "Egor"),
    TestUser(4, "Oleg"),
    TestUser(5, "Viktor"),
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
    availableLanguagesIDs: List<Int> = emptyList(),
    availableTagsIDs: List<Int> = emptyList(),
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

internal fun generateTestVideos(number: Int = 40, namePart: String? = null) = List(number) { idx ->
    generateTestVideoModel(idx, namePart)
}

internal fun generateTestStories(number: Int = 40, namePart: String? = null) = List(number) { idx ->
    generateTestStoryModel(idx, namePart)
}

internal fun generateTestStoriesChapters(number: Int = 40, namePart: String? = null) = List(number) { idx ->
    generateTestStoryChapterModel(idx, namePart)
}

// TODO: Data заменить на структуру
internal fun generateTestComments(commentData: List<Pair<Int, Int>>, commentPattern: String? = null) =
    List(commentData.size) { idx ->
        val (contentID, userID) = commentData[idx]
        TestComment(
            contentID = contentID,
            userID = userID,
            text = commentPattern?.let { "$it $idx" } ?: "Комментарий: №$idx",
        )
    }

// 0 -> 0, 1, 2 ,3
// 1 -> 4, 5, 6, 7
// 2 -> 8, 9, 10, 11
// 3 -> 12, 13, 14, 15
internal fun generateTestTags(number: Int = 40, availableLanguagesIDs: List<Int> = emptyList()) = List(number) {
    val tagTranslations = listOf(
        TestTagTranslation(
            id = if (it == 0) 0 else it * 4,
            langID = availableLanguagesIDs[0],
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 1 else (it * 4) + 1,
            langID = availableLanguagesIDs[1],
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 2 else (it * 4) + 2,
            langID = availableLanguagesIDs[2],
            name = UUID.randomUUID().toString()
        ),
        TestTagTranslation(
            id = if (it == 0) 3 else (it * 4) + 3,
            langID = availableLanguagesIDs[3],
            name = UUID.randomUUID().toString()
        ),
    )
    TestTag(
        id = it,
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
    availableLanguagesIDs: List<Int> = emptyList(),
    availableTagsIDs: List<Int> = emptyList(),
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

private fun generateTestVideoModel(number: Int, namePart: String? = null) = TestVideo(
    id = number,
    title = namePart?.let { "$it $number" } ?: "Видео №$number",
    url = "https://google.com",
    languageID = generateNullableLanguageID(),
    userID = generateUserID(),
    rating = (-10000..10000).random(),
)

private fun generateTestStoryModel(number: Int, namePart: String? = null) = TestStory(
    id = number,
    title = namePart?.let { "$it $number" } ?: "Рассказ №$number",
    languageID = generateLanguageID(),
    userID = generateUserID(),
    rating = (-10000..10000).random(),
)

private fun generateTestStoryChapterModel(number: Int, namePart: String? = null) = TestStoryChapter(
    id = number,
    title = namePart?.let { "$it $number" } ?: "Глава №$number",
    languageID = generateLanguageID(),
    userID = generateUserID(),
    rating = (-10000..10000).random(),
)

private fun generateTagTranslationModel(number: Int, namePart: String? = null) = TestTagTranslation(
    id = number,
    langID = 1,
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
