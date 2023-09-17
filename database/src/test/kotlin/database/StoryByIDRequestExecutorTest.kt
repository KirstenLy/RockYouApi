package database

import common.takeRandomValues
import database.external.ContentType
import database.external.filter.StoryByIDFilter
import database.external.test.TestStoryNode
import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.GetChaptersRequestExecutor
import database.internal.executor.GetPictureListRequestExecutor
import database.internal.executor.GetStoryByIDRequestExecutor
import database.internal.model.StoryNode
import declaration.entity.story.StoryNode as DeclarationStoryNode
import database.internal.test.model.TestModelsStorage
import database.test.selectAvailableLanguages
import database.utils.asDataNotFoundOrFail
import database.utils.extractModelOrFail
import database.utils.findDefaultLanguageID
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Tag
import rockyouapi.DBTest
import kotlin.test.Test
import kotlin.test.fail

/** Test of [GetChaptersRequestExecutor]. */
internal class StoryByIDRequestExecutorTest {

    @Test
    fun execute_with_non_existed_story_id_return_not_found_result() {
        runInEnvironment { _, _, executor ->

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->
                val filter = StoryByIDFilter(
                    storyID = 99999,
                    environmentLangID = envID
                )

                executor.execute(filter).asDataNotFoundOrFail()
            }
        }
    }

    @Test
    fun execute_with_wrong_story_id_return_not_found_result() {
        runInEnvironment { _, storage, executor ->

            val wrongStoryID = storage.contentRegisters
                .first { it.contentType != ContentType.STORY.typeID }
                .id

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->
                val filter = StoryByIDFilter(
                    storyID = wrongStoryID,
                    environmentLangID = envID
                )

                executor.execute(filter).asDataNotFoundOrFail()
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_id() {
        runInEnvironment { _, storage, executor ->
            val randomStoryID = storage.stories.random().id

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->
                val filter = StoryByIDFilter(
                    storyID = randomStoryID,
                    environmentLangID = envID
                )

                val actualStory = executor.execute(filter).extractModelOrFail()

                assert(actualStory.id == randomStoryID) {
                    buildString {
                        append("Incorrect result. Content id mismatch.")
                        appendLine()
                        append("Actual content id: ${actualStory.id}. Expected content id: $randomStoryID")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_title() {
        runInEnvironment { _, storage, executor ->
            val randomStory = storage.stories.random()

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->
                val filter = StoryByIDFilter(
                    storyID = randomStory.id,
                    environmentLangID = envID
                )

                val actualStory = executor.execute(filter).extractModelOrFail()

                assert(actualStory.title == randomStory.title) {
                    buildString {
                        append("Incorrect result. Content title mismatch.")
                        appendLine()
                        append("Actual content title: ${actualStory.title}. Expected content title: ${randomStory.title}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_language() {
        runInEnvironment { _, storage, executor ->
            val randomStory = storage.stories.random()

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            envIDList.forEach { envID ->
                val filter = StoryByIDFilter(
                    storyID = randomStory.id,
                    environmentLangID = envID
                )

                val actualStory = executor.execute(filter).extractModelOrFail()

                assert(actualStory.language?.id == randomStory.languageID) {
                    buildString {
                        append("Incorrect result. Content languageID mismatch.")
                        appendLine()
                        append("Actual content languageID: ${actualStory.language?.id}. Expected content languageID: ${randomStory.languageID}")
                    }
                }

                if (actualStory.language?.id == null) {
                    // No need to check language translation if user not presented
                    return@forEach
                }

                // If envID is invalid or not supported, language name must be translated by default language.
                // If envID valid, language name must be translated with envID.
                // Default language defined by database.
                val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                val actualStoryLanguageTranslation = actualStory.language?.name
                val expectedLanguageTranslation = storage
                    .languages
                    .firstOrNull { it.id == actualStory.language?.id }
                    ?.translations
                    ?.firstOrNull { it.envID == actualEnvID }
                    ?.name
                    ?: fail("Unexpected state, story language or it's translation for env not presented in storage")

                assert(actualStoryLanguageTranslation == expectedLanguageTranslation) {
                    buildString {
                        append("Incorrect result. Content language name mismatch.")
                        appendLine()
                        append("Actual content language name: $actualStoryLanguageTranslation. Expected content language name: $expectedLanguageTranslation")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_available_languages() {
        runInEnvironment { _, storage, executor ->
            val randomStories = storage.stories.takeRandomValues(10)

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            envIDList.forEach { envID ->

                randomStories.forEach checkStoryCycle@{ storedStory ->
                    val filter = StoryByIDFilter(
                        storyID = storedStory.id,
                        environmentLangID = envID
                    )

                    val actualStory = executor.execute(filter).extractModelOrFail()
                    val actualStoryAvailableLanguageIDList = actualStory.availableLanguages?.map(Lang::id)
                    // Sort languages from store because executor returns sorted languages
                    val storedStoryAvailableLanguageIDList = storedStory.availableLanguagesIDs.sorted()

                    assert(actualStoryAvailableLanguageIDList == storedStoryAvailableLanguageIDList) {
                        buildString {
                            append("Incorrect result. Content available languageID list mismatch.")
                            appendLine()
                            append("Actual content available languageID list: $actualStoryAvailableLanguageIDList. Expected content available languageID list: $storedStoryAvailableLanguageIDList")
                        }
                    }

                    if (actualStory.availableLanguages.isNullOrEmpty()) {
                        // No need to check language translations if languages not presented
                        return@checkStoryCycle
                    }

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    actualStory.availableLanguages?.forEach { actualStoryAvailableLanguage ->
                        val actualStoryAvailableLanguageTranslation = actualStoryAvailableLanguage.name
                        val storesStoryAvailableLanguageTranslation = storage
                            .languages
                            .firstOrNull { it.id == actualStoryAvailableLanguage.id }
                            ?.translations
                            ?.firstOrNull { it.envID == actualEnvID }
                            ?.name
                            ?: fail("Unexpected state, story language or it's translation for env not presented in storage")

                        assert(actualStoryAvailableLanguageTranslation == storesStoryAvailableLanguageTranslation) {
                            buildString {
                                append("Incorrect result. Content language name mismatch.")
                                appendLine()
                                append("Actual content available language translation: $actualStoryAvailableLanguageTranslation. Expected content available language translation: $storesStoryAvailableLanguageTranslation")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_authors() {
        runInEnvironment { _, storage, executor ->
            val randomStories = storage.stories.takeRandomValues(10)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                randomStories.forEach checkStoryCycle@{ storedStory ->
                    val filter = StoryByIDFilter(
                        storyID = storedStory.id,
                        environmentLangID = envID
                    )

                    val actualStory = executor.execute(filter).extractModelOrFail()
                    val actualStoryAuthorIDList = actualStory.authors?.map(Author::id)
                    // Sort authors from store because executor returns authors languages
                    val storedStoryAuthorIDList = storedStory.authorsIDs?.sorted()

                    assert(actualStoryAuthorIDList == storedStoryAuthorIDList) {
                        buildString {
                            append("Incorrect result. Content authorID list mismatch.")
                            appendLine()
                            append("Actual content authorID list: $actualStoryAuthorIDList. Expected content authorID list: $storedStoryAuthorIDList")
                        }
                    }

                    if (actualStory.authors.isNullOrEmpty()) {
                        // No need to check authors names if authors not presented
                        return@checkStoryCycle
                    }

                    actualStory.authors?.forEach { actualStoryAuthor ->
                        val actualStoryAuthorName = actualStoryAuthor.name
                        val storedStoryAuthorName = storage
                            .authors
                            .firstOrNull { it.id == actualStoryAuthor.id }
                            ?.name
                            ?: fail("Unexpected state, story author not presented in storage")

                        assert(actualStoryAuthorName == storedStoryAuthorName) {
                            buildString {
                                append("Incorrect result. Content author name mismatch.")
                                appendLine()
                                append("Actual content author name: $actualStoryAuthorName. Expected content author name: $storedStoryAuthorName")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_user() {
        runInEnvironment { _, storage, executor ->
            val randomStories = storage.stories.takeRandomValues(10)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                randomStories.forEach checkStoryCycle@{ storedStory ->
                    val filter = StoryByIDFilter(
                        storyID = storedStory.id,
                        environmentLangID = envID
                    )

                    val actualStory = executor.execute(filter).extractModelOrFail()
                    val actualStoryUserID = actualStory.user?.id
                    val storedStoryUserID = storedStory.userID

                    assert(actualStoryUserID == storedStoryUserID) {
                        buildString {
                            append("Incorrect result. Content userID mismatch.")
                            appendLine()
                            append("Actual content userID: $actualStoryUserID. Expected content userID: $storedStoryUserID")
                        }
                    }

                    if (actualStoryUserID == null) {
                        // No need to check username if user not presented
                        return@checkStoryCycle
                    }

                    val actualStoryUserName = actualStory.user?.name
                    val storedStoryUserName = storage
                        .users
                        .firstOrNull { it.id == actualStoryUserID }
                        ?.name
                        ?: fail("Unexpected state, story user not presented in storage")

                    assert(actualStoryUserName == storedStoryUserName) {
                        buildString {
                            append("Incorrect result. Content user name mismatch.")
                            appendLine()
                            append("Actual content user name: $actualStoryUserName. Expected content author name: $storedStoryUserName")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_tags() {
        runInEnvironment { _, storage, executor ->
            val randomStories = storage.stories.takeRandomValues(10)

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            envIDList.forEach { envID ->

                randomStories.forEach checkStoryCycle@{ storedStory ->
                    val filter = StoryByIDFilter(
                        storyID = storedStory.id,
                        environmentLangID = envID
                    )

                    val actualStory = executor.execute(filter).extractModelOrFail()
                    val actualStoryTagIDList = actualStory.tags?.map(Tag::id)

                    // Sort tags from store because executor returns tags languages
                    val storedStoryTagIDList = storedStory.tagsIDs.sorted()

                    assert(actualStoryTagIDList == storedStoryTagIDList) {
                        buildString {
                            append("Incorrect result. Content tagID list mismatch.")
                            appendLine()
                            append("Actual content tagID list: $actualStoryTagIDList. Expected content tagID list: $storedStoryTagIDList")
                        }
                    }

                    if (actualStory.availableLanguages.isNullOrEmpty()) {
                        // No need to check language translations if languages not presented
                        return@checkStoryCycle
                    }

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    actualStory.availableLanguages?.forEach { actualStoryAvailableLanguage ->
                        val actualStoryAvailableLanguageTranslation = actualStoryAvailableLanguage.name
                        val storesStoryAvailableLanguageTranslation = storage
                            .languages
                            .firstOrNull { it.id == actualStoryAvailableLanguage.id }
                            ?.translations
                            ?.firstOrNull { it.envID == actualEnvID }
                            ?.name
                            ?: fail("Unexpected state, story language or it's translation for env not presented in storage")

                        assert(actualStoryAvailableLanguageTranslation == storesStoryAvailableLanguageTranslation) {
                            buildString {
                                append("Incorrect result. Content language name mismatch.")
                                appendLine()
                                append("Actual content available language translation: $actualStoryAvailableLanguageTranslation. Expected content available language translation: $storesStoryAvailableLanguageTranslation")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_rating() {
        runInEnvironment { _, storage, executor ->
            val randomStories = storage.stories.takeRandomValues(10)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                randomStories.forEach checkStoryCycle@{ storedStory ->
                    val filter = StoryByIDFilter(
                        storyID = storedStory.id,
                        environmentLangID = envID
                    )

                    val actualStory = executor.execute(filter).extractModelOrFail()

                    val actualStoryRating = actualStory.rating
                    val storedStoryRating = storedStory.rating

                    assert(actualStoryRating == storedStoryRating) {
                        buildString {
                            append("Incorrect result. Content rating mismatch.")
                            appendLine()
                            append("Actual content rating: $actualStoryRating. Expected content rating: $storedStoryRating")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_comment_count() {
        runInEnvironment { _, storage, executor ->
            val randomStories = storage.stories.takeRandomValues(10)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                randomStories.forEach checkStoryCycle@{ storedStory ->
                    val filter = StoryByIDFilter(
                        storyID = storedStory.id,
                        environmentLangID = envID
                    )

                    val actualStory = executor.execute(filter).extractModelOrFail()

                    val actualStoryCommentCount = actualStory.commentsCount
                    val storedStoryCommentCount = storage.comments
                        .count { it.contentID == actualStory.id }
                        .toLong()

                    assert(actualStoryCommentCount == storedStoryCommentCount) {
                        buildString {
                            append("Incorrect result. Content comment count mismatch.")
                            appendLine()
                            append("Actual content comment count: $actualStoryCommentCount. Expected content comment count: $storedStoryCommentCount")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_story_id_return_story_with_correct_nodes() {
        runInEnvironment { _, storage, executor ->
            val randomStories = storage.stories.takeRandomValues(10)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                randomStories.forEach checkStoryCycle@{ storedStory ->
                    val filter = StoryByIDFilter(
                        storyID = storedStory.id,
                        environmentLangID = envID
                    )

                    val actualStory = executor.execute(filter).extractModelOrFail()

                    val actualStoryNodes = actualStory.nodes
                    val storedStoryNodes = storage.stories
                        .firstOrNull { it.id == actualStory.id }
                        ?.storyNodes
                        ?: fail("Unexpected state, story not presented in storage")

                    assert(actualStoryNodes.size == storedStoryNodes.size) {
                        buildString {
                            append("Incorrect result. Content story nodes size mismatch.")
                            appendLine()
                            append("Actual content nodes size: ${actualStoryNodes.size}. Expected content nodes size: ${storedStoryNodes.size}")
                        }
                    }

                    actualStoryNodes.forEachIndexed { idx, actualStoryNode ->
                        val storedStoryNode = storedStoryNodes[idx]

                        val isCorrectByType = when (actualStoryNode) {
                            is DeclarationStoryNode.ChapterNode -> storedStoryNode is TestStoryNode.TestChapterNode
                            is DeclarationStoryNode.ForkNode -> storedStoryNode is TestStoryNode.TestForkNode
                        }

                        assert(isCorrectByType) {
                            buildString {
                                append("Incorrect result. Content story node type mismatch.")
                                appendLine()
                                append("Actual content story node: $actualStoryNode. Expected content story node: $storedStoryNode")
                            }
                        }

                        when (actualStoryNode) {
                            is DeclarationStoryNode.ChapterNode -> {
                                val storedStoryNodeAsChapterNode = storedStoryNode as TestStoryNode.TestChapterNode

                                val storedStoryChapterNode = storedStoryNodeAsChapterNode.chapter
                                val actualStoryNodeChapter = actualStoryNode.chapter
                                assert(storedStoryChapterNode.id == actualStoryNodeChapter.id)
                            }
                            is DeclarationStoryNode.ForkNode -> storedStoryNode is TestStoryNode.TestForkNode
                        }
                    }
                }
            }
        }
    }

    private fun runInEnvironment(action: (database: DBTest, storage: TestModelsStorage, executor: GetStoryByIDRequestExecutor) -> Unit) {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val getChaptersRequestExecutor = GetChaptersRequestExecutor(database, availableLanguages)
        val executor = GetStoryByIDRequestExecutor(database, availableLanguages, getChaptersRequestExecutor)
        action(database, storage, executor)
    }
}