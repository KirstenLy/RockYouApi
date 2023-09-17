package database

import common.takeRandomValues
import database.external.test.TestStoryChapter
import database.external.test.TestStoryNode
import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.AddOrRemoveFavoriteRequestExecutor
import database.internal.executor.GetChaptersRequestExecutor
import database.internal.test.model.TestModelsStorage
import database.test.selectAvailableLanguages
import database.utils.extractDataOrFail
import database.utils.findDefaultLanguageID
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Tag
import declaration.entity.story.Chapter
import rockyouapi.DBTest
import kotlin.test.Test
import kotlin.test.fail

/** Test of [GetChaptersRequestExecutor]. */
internal class ChapterListRequestExecutorTest {

    @Test
    fun execute_without_chapters_return_empty_results() {
        runInEnvironment { _, _, executor ->

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                val actualChapters = executor.execute(
                    chaptersRegistersIDs = emptyList(),
                    envID = envID
                ).extractDataOrFail()

                assert(actualChapters.isEmpty()) {
                    buildString {
                        append("Incorrect result. Content size mismatch.")
                        appendLine()
                        append("Actual content size: ${actualChapters.size}. Expected content size: 0")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_non_existed_chapters_return_empty_results() {
        runInEnvironment { _, _, executor ->

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                val actualChapters = executor.execute(
                    chaptersRegistersIDs = List(10) { (10000..100000).random() },
                    envID = envID
                ).extractDataOrFail()

                assert(actualChapters.isEmpty()) {
                    buildString {
                        append("Incorrect result. Content size mismatch.")
                        appendLine()
                        append("Actual content size: ${actualChapters.size}. Expected content size: 0")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_size() {
        runInEnvironment { _, storage, executor ->

            val randomChaptersIDs = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)
                .map(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                val actualChapters = executor.execute(
                    chaptersRegistersIDs = randomChaptersIDs,
                    envID = envID
                ).extractDataOrFail()

                assert(actualChapters.size == randomChaptersIDs.size) {
                    buildString {
                        append("Incorrect result. Content size mismatch.")
                        appendLine()
                        append("Actual content size: ${actualChapters.size}. Expected content size: ${randomChaptersIDs.size}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_ids() {
        runInEnvironment { _, storage, executor ->

            val randomChaptersIDs = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)
                .map(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                val actualChaptersIDs = executor
                    .execute(randomChaptersIDs, envID)
                    .extractDataOrFail()
                    .map(Chapter::id)

                // Sort IDs from store because executor return sorted chapters
                assert(actualChaptersIDs == randomChaptersIDs.sorted()) {
                    buildString {
                        append("Incorrect result. Content id list mismatch.")
                        appendLine()
                        append("Actual content id list: $actualChaptersIDs. Expected content id list: $randomChaptersIDs")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_titles() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed { idx, actualChapter ->
                        val actualChapterTitle = actualChapter.title
                        val expectedChapterTitle = sortedRandomChapters[idx].title

                        assert(actualChapterTitle == expectedChapterTitle) {
                            buildString {
                                append("Incorrect result. Content title mismatch.")
                                appendLine()
                                append("Actual title: $actualChapterTitle. Expected title: $expectedChapterTitle")
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_story_id() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed { idx, actualChapter ->
                        val actualChapterStoryID = actualChapter.storyID
                        val expectedChapterStoryID = sortedRandomChapters[idx].storyID

                        assert(actualChapterStoryID == expectedChapterStoryID) {
                            buildString {
                                append("Incorrect result. Content storyID mismatch.")
                                appendLine()
                                append("Actual storyID: $actualChapterStoryID. Expected storyID: $expectedChapterStoryID")
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_language() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            envIDList.forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed checkLanguageCycle@{ idx, actualChapter ->
                        val actualChapterLanguageID = actualChapter.language?.id
                        val storedChapterLanguageID = sortedRandomChapters[idx].languageID

                        assert(actualChapterLanguageID == storedChapterLanguageID) {
                            buildString {
                                append("Incorrect result. Content languageID mismatch.")
                                appendLine()
                                append("Actual languageID: $actualChapterLanguageID. Expected languageID: $storedChapterLanguageID")
                            }
                        }

                        if (actualChapterLanguageID == null) {
                            // No need to check language translation if language not presented
                            return@checkLanguageCycle
                        }

                        // If envID is invalid or not supported, language name must be translated by default language.
                        // If envID valid, language name must be translated with envID.
                        // Default language defined by database.
                        val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                        val actualChapterLanguageTranslation = actualChapter.language?.name
                        val storedChapterLanguageTranslation = storage.languages
                            .firstOrNull { it.id == actualChapterLanguageID }
                            ?.translations
                            ?.firstOrNull { it.envID == actualEnvID }
                            ?.name
                            ?: fail("Unexpected state, no picture model found in storage")

                        assert(actualChapterLanguageTranslation == storedChapterLanguageTranslation) {
                            buildString {
                                append("Incorrect result. Content language translation mismatch.")
                                appendLine()
                                append("Actual content language translation: $actualChapterLanguageTranslation. Expected content language translation: $storedChapterLanguageTranslation")
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_available_languages() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            envIDList.forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed checkLanguageCycle@{ idx, actualChapter ->
                        val actualChapterAvailableLanguageList = actualChapter.availableLanguages?.map(Lang::id)
                        val storedChapterAvailableLanguageList = sortedRandomChapters[idx].availableLanguagesIDs

                        assert(actualChapterAvailableLanguageList == storedChapterAvailableLanguageList) {
                            buildString {
                                append("Incorrect result. Content available language list mismatch.")
                                appendLine()
                                append("Actual available language list: $actualChapterAvailableLanguageList. Expected available language list: $storedChapterAvailableLanguageList")
                            }
                        }

                        if (actualChapterAvailableLanguageList.isNullOrEmpty()) {
                            // No need to check available language translations if available language snot presented
                            return@checkLanguageCycle
                        }

                        // If envID is invalid or not supported, language name must be translated by default language.
                        // If envID valid, language name must be translated with envID.
                        // Default language defined by database.
                        val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                        actualChapter.availableLanguages?.forEach { actualAvailableLanguage ->
                            val actualChapterAvailableLanguageTranslation = actualAvailableLanguage.name
                            val storedChapterAvailableLanguageTranslation = storage.languages
                                .firstOrNull { it.id == actualAvailableLanguage.id }
                                ?.translations
                                ?.firstOrNull { it.envID == actualEnvID }
                                ?.name
                                ?: fail("Unexpected state, no picture model found in storage")

                            assert(actualChapterAvailableLanguageTranslation == storedChapterAvailableLanguageTranslation) {
                                buildString {
                                    append("Incorrect result. Content available language translation mismatch.")
                                    appendLine()
                                    append("Actual content available language translation: $actualChapterAvailableLanguageTranslation. Expected content available language translation: $storedChapterAvailableLanguageTranslation")
                                }
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_authors() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed checkLanguageCycle@{ idx, actualChapter ->
                        val actualChapterAuthorIDList = actualChapter.authors?.map(Author::id)
                        val storedChapterAuthorIDList = sortedRandomChapters[idx].authorsIDs

                        // Sort authors from store because executor returns sorted authors
                        assert(actualChapterAuthorIDList == storedChapterAuthorIDList?.sorted()) {
                            buildString {
                                append("Incorrect result. Content authorID list mismatch.")
                                appendLine()
                                append("Actual authorID list: $actualChapterAuthorIDList. Expected authorID list: $storedChapterAuthorIDList")
                            }
                        }

                        if (actualChapterAuthorIDList.isNullOrEmpty()) {
                            // No need to check author names if authors not presented
                            return@checkLanguageCycle
                        }

                        actualChapter.authors?.forEach { actualChapterAuthor ->
                            val actualChapterAuthorName = actualChapterAuthor.name
                            val storedChapterAuthorName = storage.authors
                                .firstOrNull { it.id == actualChapterAuthor.id }
                                ?.name
                                ?: fail("Unexpected state, no author model found in storage")

                            assert(actualChapterAuthorName == storedChapterAuthorName) {
                                buildString {
                                    append("Incorrect result. Content author name mismatch.")
                                    appendLine()
                                    append("Actual content author name: $actualChapterAuthorName. Expected content author name: $storedChapterAuthorName")
                                }
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_tags() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            envIDList.forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed checkLanguageCycle@{ idx, actualChapter ->
                        val actualChapterTagIDList = actualChapter.tags?.map(Tag::id)
                        val storedChapterTagIDList = sortedRandomChapters[idx].tagsIDs

                        // Sort tags from store because executor returns sorted authors
                        assert(actualChapterTagIDList == storedChapterTagIDList.sorted()) {
                            buildString {
                                append("Incorrect result. Content tagID list mismatch.")
                                appendLine()
                                append("Actual tagID list: $actualChapterTagIDList. Expected tagID list: $storedChapterTagIDList")
                            }
                        }

                        if (actualChapterTagIDList.isNullOrEmpty()) {
                            // No need to check tag translations if tags not presented
                            return@checkLanguageCycle
                        }


                        // If envID is invalid or not supported, language name must be translated by default language.
                        // If envID valid, language name must be translated with envID.
                        // Default language defined by database.
                        val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                        actualChapter.tags?.forEach { actualChapterTag ->
                            val actualChapterTagTranslation = actualChapterTag.name
                            val storedChapterTagTranslation = storage.tags
                                .firstOrNull { it.id == actualChapterTag.id }
                                ?.translations
                                ?.firstOrNull { it.envID == actualEnvID }
                                ?.name
                                ?: fail("Unexpected state, no tag translation found in storage")

                            assert(actualChapterTagTranslation == storedChapterTagTranslation) {
                                buildString {
                                    append("Incorrect result. Content tag translation mismatch.")
                                    appendLine()
                                    append("Actual content tag translation: $actualChapterTagTranslation. Expected content tag translation: $storedChapterTagTranslation")
                                }
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_users() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed checkLanguageCycle@{ idx, actualChapter ->
                        val actualChapterUserID = actualChapter.user?.id
                        val storedChapterUserID = sortedRandomChapters[idx].userID

                        assert(actualChapterUserID == storedChapterUserID) {
                            buildString {
                                append("Incorrect result. Content userID mismatch.")
                                appendLine()
                                append("Actual userID: $actualChapterUserID. Expected userID: $storedChapterUserID")
                            }
                        }

                        if (actualChapterUserID == null) {
                            // No need to check username if user not presented
                            return@checkLanguageCycle
                        }

                        val actualChapterUserName = actualChapter.user?.name
                        val storedChapterUserName = storage.users
                            .firstOrNull { it.id == actualChapterUserID }
                            ?.name
                            ?: fail("Unexpected state, no user model found in storage")

                        assert(actualChapterUserName == storedChapterUserName) {
                            buildString {
                                append("Incorrect result. Content user name mismatch.")
                                appendLine()
                                append("Actual content user name: $actualChapterUserName. Expected content user name: $storedChapterUserName")
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_rating() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEachIndexed checkLanguageCycle@{ idx, actualChapter ->
                        val actualChapterRating = actualChapter.rating
                        val storedChapterRating = sortedRandomChapters[idx].rating

                        assert(actualChapterRating == storedChapterRating) {
                            buildString {
                                append("Incorrect result. Content rating mismatch.")
                                appendLine()
                                append("Actual rating: $actualChapterRating. Expected rating: $storedChapterRating")
                            }
                        }
                    }
            }
        }
    }

    @Test
    fun execute_with_valid_args_return_results_with_correct_comments_count() {
        runInEnvironment { _, storage, executor ->

            val randomChapters = storage.stories
                .flatMap { it.storyNodes.extractAllChapters() }
                .takeRandomValues(10)

            // Sort chapters from store because executor returns sorted chapters
            val sortedRandomChapters = randomChapters.sortedBy(TestStoryChapter::id)

            listOf<Byte?>(null, -1, 0, 1, 100).forEach { envID ->

                executor.execute(randomChapters.map(TestStoryChapter::id), envID)
                    .extractDataOrFail()
                    .forEach { actualChapter ->
                        val actualChapterCommentsCount = actualChapter.commentsCount
                        val storedChapterCommentsCount = storage.comments
                            .count { it.contentID == actualChapter.id }
                            .toLong()

                        assert(actualChapterCommentsCount == storedChapterCommentsCount) {
                            buildString {
                                append("Incorrect result. Content comments count mismatch.")
                                appendLine()
                                append("Actual comments count: $actualChapterCommentsCount. Expected comments count: $storedChapterCommentsCount")
                            }
                        }
                    }
            }
        }
    }

    private fun runInEnvironment(action: (database: DBTest, storage: TestModelsStorage, executor: GetChaptersRequestExecutor) -> Unit) {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetChaptersRequestExecutor(database, availableLanguages)
        action(database, storage, executor)
    }

    private fun List<TestStoryNode>.extractAllChapters(): List<TestStoryChapter> {
        val result = mutableListOf<TestStoryChapter>()
        forEach { node ->
            when (node) {
                is TestStoryNode.TestChapterNode -> result.add(node.chapter)
                is TestStoryNode.TestForkNode -> {
                    node.variants
                        .map { it.nodes.extractAllChapters() }
                        .flatten()
                        .distinct()
                        .forEach(result::add)
                }
            }
        }
        return result
    }
}