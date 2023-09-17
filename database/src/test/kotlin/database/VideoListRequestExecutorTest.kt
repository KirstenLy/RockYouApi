package database

import database.external.filter.VideoListFilter
import database.external.result.SimpleListResult
import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.GetVideoByIDRequestExecutor
import database.internal.executor.GetVideosListRequestExecutor
import database.internal.test.model.TestModelsStorage
import database.test.selectAvailableLanguages
import database.utils.extractDataOrFail
import database.utils.findDefaultLanguageID
import database.utils.getVideosByImitateListRequest
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Tag
import rockyouapi.DBTest
import kotlin.math.max
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.fail

/** Test of [GetVideosListRequestExecutor]. */
internal class VideoListRequestExecutorTest {

    @Test
    fun execute_with_zero_limit_return_empty_results() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = 0,
                offset = if (Random.nextBoolean()) (0..10).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..10).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..10).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                VideoListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executeVideoListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Incorrect result. Actual result: $result. Expected result: Data"
            )

            assert(executeVideoListResult.isEmpty()) {
                "Incorrect result. Empty data expected but something returned."
            }
        }
    }

    @Test
    fun execute_with_negative_limit_return_error() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = -1,
                offset = if (Random.nextBoolean()) (0..10).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..10).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..10).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                VideoListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            assert(result is SimpleListResult.Error) {
                "Incorrect result. Actual result: $result. Expected result: Data"
            }
        }
    }

    @Test
    fun execute_with_negative_offset_return_error() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = Random.nextLong(),
                offset = -1,
                environmentLangID = if (Random.nextBoolean()) (0..10).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..10).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                VideoListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            assert(result is SimpleListResult.Error) {
                "Incorrect result. Actual result: $result. Expected result: Data"
            }
        }
    }

    @Test
    fun execute_with_invalid_lang_id_return_empty_results() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = (1..10).random().toLong(),
                offset = (1..5).random().toLong(),
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = -1,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                VideoListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executeVideoListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Incorrect result. Actual result: $result. Expected result: Data"
            )

            assert(executeVideoListResult.isEmpty()) {
                "Incorrect result. Empty data expected but something returned."
            }
        }
    }

    @Test
    fun execute_with_non_existed_lang_id_return_empty_results_test() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = (1..10).random().toLong(),
                offset = (1..5).random().toLong(),
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = 999,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                VideoListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executeVideoListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Incorrect result. Actual result: $result. Expected result: Data"
            )

            assert(executeVideoListResult.isEmpty()) {
                "Incorrect result. Empty data expected but something returned."
            }
        }
    }

    @Test
    fun execute_with_valid_limit_and_offset_return_result_with_correct_size() {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = (0..50).random().toLong(),
                offset = if (Random.nextBoolean()) (0..50).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..3).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                VideoListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executeVideoListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Incorrect result. Actual result: $result. Expected result: Data"
            )

            val requiredOffset = filter.offset ?: 0L
            val requiredLimit = filter.limit
            val requiredVideosLanguageID = filter.langID
            val requiredEnvironmentID = filter.environmentLangID

            // Count all videos that confirm "langID" condition
            val totalAvailableVideosCount = if (requiredVideosLanguageID == null) {
                storage.videos.count()
            } else {
                storage.videos.count { it.languageID == requiredVideosLanguageID.toByte() }
            }

            // If offset is too big, it's okay that executor return an empty result, especially if language filter set.
            // Check it and finish with this filter if so.
            val isAnyVideosExpected = totalAvailableVideosCount >= requiredOffset
            if (!isAnyVideosExpected) {
                assert(executeVideoListResult.isEmpty()) { "Incorrect result. Empty data expected but something returned." }
                return@forEach
            }

            val availableVideoCountAfterOffset = max(totalAvailableVideosCount - requiredOffset, 0)
            val expectedVideosCount = when {
                availableVideoCountAfterOffset >= requiredLimit -> requiredLimit
                else -> availableVideoCountAfterOffset
            }

            assert(executeVideoListResult.size == expectedVideosCount.toInt()) {
                buildString {
                    append("Incorrect result. Content size mismatch.")
                    appendLine()
                    append("Actual content size: ${executeVideoListResult.size}. Expected content size: $expectedVideosCount")
                    appendLine()
                    append("Required offset: $requiredOffset. Required limit: $requiredLimit. Required langID: $requiredVideosLanguageID. Required envID: $requiredEnvironmentID")
                }
            }
            return@forEach
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_ids() {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = (0..50).random().toLong(),
                offset = if (Random.nextBoolean()) (0..50).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..3).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                VideoListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executeVideoListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Incorrect result. Actual result: $result. Expected result: Data"
            )

            val requiredOffset = filter.offset ?: 0L
            val requiredLimit = filter.limit
            val requiredVideosLanguageID = filter.langID?.toByte()
            val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                limit = requiredLimit,
                offset = requiredOffset,
                langID = requiredVideosLanguageID
            )

            executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                assert(actualVideoModel.id == expectedExecuteResult[index].id) {
                    buildString {
                        append("Incorrect result. Content id mismatch.")
                        appendLine()
                        append("Actual content ID: ${actualVideoModel.id}. Expected content ID: ${expectedExecuteResult[index].id}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_titles() {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            VideoListFilter(
                limit = (0..50).random().toLong(),
                offset = if (Random.nextBoolean()) (0..50).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..3).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(filter)

            val executeVideoListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Incorrect result. Actual result: $result. Expected result: Data"
            )

            val requiredOffset = filter.offset ?: 0L
            val requiredLimit = filter.limit
            val requiredVideosLanguageID = filter.langID?.toByte()
            val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                limit = requiredLimit,
                offset = requiredOffset,
                langID = requiredVideosLanguageID
            )

            executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                assert(actualVideoModel.title == expectedExecuteResult[index].title) {
                    buildString {
                        append("Incorrect result. Content title mismatch.")
                        appendLine()
                        append("Actual content title: ${actualVideoModel.title}. Expected content title: ${expectedExecuteResult[index].title}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_urls() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    assert(actualVideoModel.url == expectedExecuteResult[index].url) {
                        buildString {
                            append("Incorrect result. Content url mismatch.")
                            appendLine()
                            append("Actual content url: ${actualVideoModel.url}. Expected content url: ${expectedExecuteResult[index].url}")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_language() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val requiredEnvironmentID = filter.environmentLangID ?: storage.findDefaultLanguageID()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    val videoLanguageID = actualVideoModel.language?.id
                    assert(videoLanguageID == expectedExecuteResult[index].languageID) {
                        buildString {
                            append("Incorrect result. Content languageID mismatch.")
                            appendLine()
                            append("Actual content languageID: $videoLanguageID. Expected content languageID: ${expectedExecuteResult[index].languageID}")
                        }
                    }

                    if (videoLanguageID == null) return@forEachIndexed

                    val videoLanguageActualTranslation = actualVideoModel.language?.name
                    val videoLanguageExpectedTranslation = storage.languages
                        .firstOrNull { it.id == videoLanguageID }
                        ?.translations
                        ?.firstOrNull { it.envID == requiredEnvironmentID }
                        ?.name
                        ?: fail("Unexpected state, can't find language translation for langID: $videoLanguageID and envID: $requiredEnvironmentID")

                    assert(videoLanguageActualTranslation == videoLanguageExpectedTranslation) {
                        buildString {
                            append("Incorrect result. Content language name translation mismatch.")
                            appendLine()
                            append("Actual content language name translation: $videoLanguageActualTranslation. Expected content language name translation: $videoLanguageExpectedTranslation")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_available_language_list() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val requiredEnvironmentID = filter.environmentLangID ?: storage.findDefaultLanguageID()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    val actualVideoAvailableLanguagesIDs = actualVideoModel.availableLanguages?.map(Lang::id)
                    assert(actualVideoAvailableLanguagesIDs == expectedExecuteResult[index].availableLanguagesIDs) {
                        buildString {
                            append("Incorrect result. Content available language ID list mismatch.")
                            appendLine()
                            append("Actual content available language ID list: $actualVideoAvailableLanguagesIDs. Expected content languageID: ${expectedExecuteResult[index].languageID}")
                        }
                    }

                    val videoAvailableLanguages = actualVideoModel.availableLanguages
                    if (videoAvailableLanguages?.isEmpty() == true) {
                        // No need to check available language translations if languages are not presented
                        return@forEachIndexed
                    }

                    videoAvailableLanguages?.forEach checkAvailableLanguageTranslationCycle@{ videoAvailableLanguage ->
                        val videoAvailableLanguageActualID = videoAvailableLanguage.id
                        val videoAvailableLanguageActualTranslation = videoAvailableLanguage.name
                        val videoLanguageExpectedTranslation = storage.languages
                            .firstOrNull { it.id == videoAvailableLanguageActualID }
                            ?.translations
                            ?.firstOrNull { it.envID == requiredEnvironmentID }
                            ?.name
                            ?: fail("Unexpected state, can't find language translation for langID: $videoAvailableLanguageActualID and envID: $requiredEnvironmentID")

                        assert(videoAvailableLanguageActualTranslation == videoLanguageExpectedTranslation) {
                            buildString {
                                append("Incorrect result. Content available language name translation mismatch.")
                                appendLine()
                                append("Actual content available language name translation: $videoAvailableLanguageActualTranslation. Expected content available language name translation: $videoLanguageExpectedTranslation")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_authors() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    val actualVideoAuthorIDList = actualVideoModel.authors?.map(Author::id)
                    val expectedVideoAuthorIDList = expectedExecuteResult[index].authorsIDs?.sorted()
                    assert(actualVideoAuthorIDList == expectedVideoAuthorIDList) {
                        buildString {
                            append("Incorrect result. Content authors ID list mismatch.")
                            appendLine()
                            append("Actual content authors ID list: $actualVideoAuthorIDList. Expected content authors ID list: $expectedVideoAuthorIDList")
                        }
                    }

                    val actualVideoAuthors = actualVideoModel.authors
                    if (actualVideoAuthors?.isEmpty() == true) {
                        // No need to check author names if authors are not presented
                        return@forEachIndexed
                    }

                    actualVideoAuthors?.forEach checkActualAuthorNameCycle@{ actualVideoAuthor ->
                        val actualVideoAuthorID = actualVideoAuthor.id
                        val actualVideoAuthorName = actualVideoAuthor.name
                        val expectedVideoAuthorName = storage.authors
                            .firstOrNull { it.id == actualVideoAuthorID }
                            ?.name
                            ?: fail("Unexpected state, can't find author by ID in storage. Author ID: $actualVideoAuthorID")

                        assert(actualVideoAuthorName == expectedVideoAuthorName) {
                            buildString {
                                append("Incorrect result. Content author name mismatch.")
                                appendLine()
                                append("Actual content author name: $actualVideoAuthorName. Expected content author name : $expectedVideoAuthorName")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_users() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    val actualVideoUserID = actualVideoModel.user?.id
                    val expectedVideoUserID = expectedExecuteResult[index].userID
                    assert(actualVideoUserID == expectedVideoUserID) {
                        buildString {
                            append("Incorrect result. Content userID mismatch.")
                            appendLine()
                            append("Actual content userID: $actualVideoUserID. Expected content userID: $expectedVideoUserID")
                        }
                    }

                    val actualVideoUser = actualVideoModel.user
                    if (actualVideoUser == null) {
                        // No need to check username if user not presented
                        return@forEachIndexed
                    }

                    val actualVideoUserName = actualVideoUser.name
                    val expectedVideoUserName =  storage.users
                        .firstOrNull { it.id == actualVideoUser.id }
                        ?.name
                        ?: fail("Unexpected state, can't find user by ID in storage. User ID: ${actualVideoUser.id}")

                    assert(actualVideoUserName == expectedVideoUserName) {
                        buildString {
                            append("Incorrect result. Content user name mismatch.")
                            appendLine()
                            append("Actual content user name: $actualVideoUserName. Expected content user name : $expectedVideoUserName")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_tags() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val requiredEnvironmentID = filter.environmentLangID ?: storage.findDefaultLanguageID()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    val actualVideoTagIDList = actualVideoModel.tags?.map(Tag::id)
                    val expectedVideoTagIDList = expectedExecuteResult[index].tagsIDs?.sorted()
                    assert(actualVideoTagIDList == expectedVideoTagIDList) {
                        buildString {
                            append("Incorrect result. Content tagID list mismatch.")
                            appendLine()
                            append("Actual content tagID list: $actualVideoTagIDList. Expected content tagID: $expectedVideoTagIDList")
                        }
                    }

                    val actualVideoTags = actualVideoModel.tags
                    if (actualVideoTags?.isEmpty() == true) {
                        // No need to check tags translations if tags are not presented
                        return@forEachIndexed
                    }

                    actualVideoTags?.forEach checkTagTranslationCycle@{ actualVideoTag ->
                        val actualVideoTagID = actualVideoTag.id
                        val actualVideoTranslation = actualVideoTag.name
                        val expectedVideoTagTranslation = storage.tags
                            .firstOrNull { it.id == actualVideoTagID }
                            ?.translations
                            ?.firstOrNull { it.envID == requiredEnvironmentID }
                            ?.name
                            ?: fail("Unexpected state, can't find tag translation for tagID: $actualVideoTagID and envID: $requiredEnvironmentID")

                        assert(actualVideoTranslation == expectedVideoTagTranslation) {
                            buildString {
                                append("Incorrect result. Content tag name translation mismatch.")
                                appendLine()
                                append("Actual content tag translation: $actualVideoTranslation. Expected content tag translation: $expectedVideoTagTranslation")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_rating() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    val actualVideoRating = actualVideoModel.rating
                    val expectedVideoRating = expectedExecuteResult[index].rating
                    assert(actualVideoRating == expectedVideoRating) {
                        buildString {
                            append("Incorrect result. Content rating mismatch.")
                            appendLine()
                            append("Actual content rating: $actualVideoRating. Expected content rating: $expectedVideoRating")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_valid_filter_return_videos_with_correct_comment_count() {
        runInEnvironment { _, storage, executor ->

            generateValidFilters().forEach { filter ->
                val executeVideoListResult = executor.execute(filter).extractDataOrFail()

                val requiredOffset = filter.offset ?: 0L
                val requiredLimit = filter.limit
                val requiredVideosLanguageID = filter.langID?.toByte()
                val expectedExecuteResult = storage.videos.getVideosByImitateListRequest(
                    limit = requiredLimit,
                    offset = requiredOffset,
                    langID = requiredVideosLanguageID
                )

                executeVideoListResult.forEachIndexed { index, actualVideoModel ->
                    val actualVideoCommentsCount = actualVideoModel.commentsCount.toInt()
                    val expectedVideoCommentsCount = storage.comments.count {
                        it.contentID == expectedExecuteResult[index].id
                    }
                    assert(actualVideoCommentsCount == expectedVideoCommentsCount) {
                        buildString {
                            append("Incorrect result. Content comments count mismatch.")
                            appendLine()
                            append("Actual content comments count: $actualVideoCommentsCount. Expected content comments count: $expectedVideoCommentsCount")
                        }
                    }
                }
            }
        }
    }

    private fun runInEnvironment(action: (database: DBTest, storage: TestModelsStorage, executor: GetVideosListRequestExecutor) -> Unit) {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideosListRequestExecutor(database, availableLanguages)
        action(database, storage, executor)
    }

    private fun generateValidFilters() = List(100) {
        VideoListFilter(
            limit = (0..50).random().toLong(),
            offset = if (Random.nextBoolean()) (0..50).random().toLong() else null,
            environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
            langID = if (Random.nextBoolean()) (0..3).random() else null,
        )
    }
}