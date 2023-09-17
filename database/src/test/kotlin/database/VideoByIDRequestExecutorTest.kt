package database

import common.takeRandomValues
import database.external.ContentType
import database.external.filter.VideoByIDFilter
import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.GetChaptersRequestExecutor
import database.internal.executor.GetVideoByIDRequestExecutor
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

/** Test of [GetVideoByIDRequestExecutor]. */
internal class VideoByIDRequestExecutorTest {

    @Test
    fun execute_with_non_existed_video_id_return_not_found_result() {
        runInEnvironment { _, _, executor ->
            val nonExistedVideoID = 99999
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            envIDList.forEach { envID ->
                val filter = VideoByIDFilter(
                    videoID = nonExistedVideoID,
                    environmentLangID = envID
                )
                executor.execute(filter).asDataNotFoundOrFail()
            }
        }
    }

    @Test
    fun execute_with_wrong_video_id_return_not_found_result() {
        runInEnvironment { _, storage, executor ->
            val wrongVideoID = storage.contentRegisters
                .first { it.contentType != ContentType.VIDEO.typeID }
                .id

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            envIDList.forEach { envID ->
                val filter = VideoByIDFilter(
                    videoID = wrongVideoID,
                    environmentLangID = envID
                )

                executor.execute(filter).asDataNotFoundOrFail()
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_id() {
        runInEnvironment { _, storage, executor ->
            val randomVideo = storage.videos.random()
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            envIDList.forEach { envID ->
                val filter = VideoByIDFilter(
                    videoID = randomVideo.id,
                    environmentLangID = envID
                )

                val actualVideo = executor.execute(filter).extractModelOrFail()

                assert(actualVideo.id == randomVideo.id) {
                    buildString {
                        append("Incorrect result. Content id mismatch.")
                        appendLine()
                        append("Actual content id: ${actualVideo.id}. Expected content id: ${randomVideo.id}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_title() {
        runInEnvironment { _, storage, executor ->
            val randomVideo = storage.videos.random()
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            envIDList.forEach { envID ->
                val filter = VideoByIDFilter(
                    videoID = randomVideo.id,
                    environmentLangID = envID
                )

                val actualVideo = executor.execute(filter).extractModelOrFail()

                assert(actualVideo.title == randomVideo.title) {
                    buildString {
                        append("Incorrect result. Content title mismatch.")
                        appendLine()
                        append("Actual content title: ${actualVideo.title}. Expected content title: ${randomVideo.title}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_url() {
        runInEnvironment { _, storage, executor ->
            val randomVideo = storage.videos.random()
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            envIDList.forEach { envID ->
                val filter = VideoByIDFilter(
                    videoID = randomVideo.id,
                    environmentLangID = envID
                )

                val actualVideo = executor.execute(filter).extractModelOrFail()

                assert(actualVideo.url == randomVideo.url) {
                    buildString {
                        append("Incorrect result. Content url mismatch.")
                        appendLine()
                        append("Actual content url: ${actualVideo.url}. Expected content url: ${randomVideo.url}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_lang() {
        runInEnvironment { _, storage, executor ->
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            storage.videos.takeRandomValues(10).forEach { randomVideo ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = VideoByIDFilter(
                        videoID = randomVideo.id,
                        environmentLangID = envID
                    )

                    val actualVideo = executor.execute(filter).extractModelOrFail()

                    assert(actualVideo.language?.id == randomVideo.languageID) {
                        buildString {
                            append("Incorrect result. Content languageID mismatch.")
                            appendLine()
                            append("Actual content languageID: ${actualVideo.url}. Expected content languageID: ${randomVideo.url}")
                        }
                    }

                    if (actualVideo.language == null) {
                        // No need to check language translation if language not presented
                        return@envIDCycle
                    }

                    val actualVideoLanguageID = actualVideo.language?.id ?: fail(
                        "Unexpected state, picture language ID must be presented at this time"
                    )

                    val actualVideoLanguageName = actualVideo.language?.name ?: fail(
                        "Unexpected state, picture language name must be presented at this time"
                    )

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    val expectedLanguageTranslation = storage
                        .languages
                        .firstOrNull { it.id == actualVideoLanguageID }
                        ?.translations
                        ?.firstOrNull { it.envID == actualEnvID }
                        ?.name
                        ?: fail("Unexpected state, video language or it's translation for env not presented in storage")

                    assert(actualVideoLanguageName == expectedLanguageTranslation) {
                        buildString {
                            append("Incorrect result. Content language name mismatch.")
                            appendLine()
                            append("Actual content language name: $actualVideoLanguageName. Expected content language name: $expectedLanguageTranslation")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_available_languages() {
        runInEnvironment { _, storage, executor ->
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            storage.videos.takeRandomValues(10).forEach { randomVideo ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = VideoByIDFilter(
                        videoID = randomVideo.id,
                        environmentLangID = envID
                    )

                    val actualVideo = executor.execute(filter).extractModelOrFail()

                    val storageVideo = storage.videos
                        .firstOrNull { it.id == actualVideo.id }
                        ?: fail("Unexpected state, no video model found in storage")

                    val actualVideoAvailableLanguagesIDList = actualVideo.availableLanguages?.map(Lang::id)
                    val storageVideoAvailableLanguageIDList = storageVideo.availableLanguagesIDs

                    assert(actualVideoAvailableLanguagesIDList == storageVideoAvailableLanguageIDList) {
                        fail(
                            buildString {
                                append("Incorrect result. Content available language mismatch.")
                                appendLine()
                                append("Actual content available languages IDs: $actualVideoAvailableLanguagesIDList. Expected content available languages IDs: $storageVideoAvailableLanguageIDList")
                            }
                        )
                    }

                    if (actualVideoAvailableLanguagesIDList == null && storageVideoAvailableLanguageIDList == null) {
                        // No need to check available languages and their translations if available languages not presented
                        return@envIDCycle
                    }

                    if (actualVideoAvailableLanguagesIDList!!.isEmpty() && storageVideoAvailableLanguageIDList!!.isEmpty()) {
                        // No need to check available languages and their translations if available languages not presented
                        return@envIDCycle
                    }

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    actualVideo.availableLanguages?.forEach { actualVideoLanguageModel ->
                        val actualVideoLanguageTranslation = actualVideoLanguageModel.name
                        val expectedVideoLanguageTranslation = storage.languages
                            .firstOrNull { it.id == actualVideoLanguageModel.id }
                            ?.translations
                            ?.firstOrNull { it.envID == actualEnvID }
                            ?.name
                            ?: fail("Unexpected state, can't find translation for default language")

                        assert(actualVideoLanguageTranslation == expectedVideoLanguageTranslation) {
                            buildString {
                                append("Incorrect result. Content available language name mismatch.")
                                appendLine()
                                append("Actual content available language name: $actualVideoLanguageTranslation. Expected content available language name: $expectedVideoLanguageTranslation")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_authors() {
        runInEnvironment { _, storage, executor ->
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            storage.videos.takeRandomValues(10).forEach { randomVideo ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = VideoByIDFilter(
                        videoID = randomVideo.id,
                        environmentLangID = envID
                    )

                    val actualVideo = executor.execute(filter).extractModelOrFail()

                    val storageVideo = storage.videos
                        .firstOrNull { it.id == actualVideo.id }
                        ?: fail("Unexpected state, no video model found in storage")

                    val actualVideoAuthorList = actualVideo.authors?.map(Author::id)
                    val storedVideoAuthorIDList = storageVideo.authorsIDs

                    assert(actualVideoAuthorList == storedVideoAuthorIDList) {
                        fail(
                            buildString {
                                append("Incorrect result. Content author ID list mismatch.")
                                appendLine()
                                append("Actual content author ID list: $actualVideoAuthorList. Expected content author ID list: $storedVideoAuthorIDList")
                            }
                        )
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_tags() {
        runInEnvironment { _, storage, executor ->
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            storage.videos.takeRandomValues(10).forEach { randomVideo ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = VideoByIDFilter(
                        videoID = randomVideo.id,
                        environmentLangID = envID
                    )

                    val actualVideo = executor.execute(filter).extractModelOrFail()

                    val storageVideo = storage.videos
                        .firstOrNull { it.id == actualVideo.id }
                        ?: fail("Unexpected state, no video model found in storage")

                    val actualVideoTagIDList = actualVideo.tags?.map(Tag::id)
                    val storedVideoTagIDList = storageVideo.tagsIDs

                    // Sort IDs from store because executor return sorted tags
                    assert(actualVideoTagIDList == storedVideoTagIDList?.sorted()) {
                        fail(
                            buildString {
                                append("Incorrect result. Content tags IDs mismatch.")
                                appendLine()
                                append("Actual content tags IDs: $actualVideoTagIDList. Expected content tags IDs: $storedVideoTagIDList")
                            }
                        )
                    }

                    if (actualVideoTagIDList == null && storedVideoTagIDList == null) {
                        // No need to check tags translations if tags not presented
                        return@envIDCycle
                    }

                    if (actualVideoTagIDList!!.isEmpty() && storedVideoTagIDList!!.isEmpty()) {
                        // No need to check tags translations if tags not presented
                        return@envIDCycle
                    }

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    actualVideo.tags?.forEach { actualVideoTag ->
                        val actualVideoTagID = actualVideoTag.id
                        val actualVideoTagTranslation = actualVideoTag.name

                        val expectedTagTranslation = storage.tags
                            .firstOrNull { it.id == actualVideoTagID }
                            ?.translations
                            ?.firstOrNull { it.envID == actualEnvID }
                            ?.name
                            ?: fail("Unexpected state, video tag or it's translation not presented in test models")

                        assert(actualVideoTagTranslation == expectedTagTranslation) {
                            buildString {
                                append("Incorrect result. Content tag translation mismatch.")
                                appendLine()
                                append("Actual content tag translation: $actualVideoTagTranslation. Expected content tag translation: $expectedTagTranslation")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_user() {
        runInEnvironment { _, storage, executor ->
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            storage.videos.takeRandomValues(10).forEach { randomVideo ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = VideoByIDFilter(
                        videoID = randomVideo.id,
                        environmentLangID = envID
                    )

                    val actualVideo = executor.execute(filter).extractModelOrFail()

                    val storageVideo = storage.videos
                        .firstOrNull { it.id == actualVideo.id }
                        ?: fail("Unexpected state, no video model found in storage")

                    val actualVideoUserID = actualVideo.user?.id
                    val storedVideoUserID = storageVideo.userID

                    assert(actualVideoUserID == storedVideoUserID) {
                        buildString {
                            append("Incorrect result. Content userID mismatch.")
                            appendLine()
                            append("Actual content userID: $actualVideoUserID. Expected content userID: $storedVideoUserID")
                        }
                    }

                    if (actualVideoUserID == null) {
                        // No need to check username if user not presented
                        return@envIDCycle
                    }

                    val actualVideoUserName = actualVideo.user?.name
                    val storedVideoUserName = storage.users
                        .firstOrNull { it.id == actualVideoUserID }
                        ?.name
                        ?: fail("Unexpected state, user model not found in storage")

                    assert(actualVideoUserName == storedVideoUserName) {
                        buildString {
                            append("Incorrect result. Content userName mismatch.")
                            appendLine()
                            append("Actual content userName: $actualVideoUserName. Expected content userName: $storedVideoUserName")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_rating() {
        runInEnvironment { _, storage, executor ->

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            storage.videos.takeRandomValues(10).forEach { randomVideo ->
                envIDList.forEach envIDCycle@{ envID ->

                    val filter = VideoByIDFilter(
                        videoID = randomVideo.id,
                        environmentLangID = envID
                    )

                    val actualVideo = executor.execute(filter).extractModelOrFail()

                    val storageVideo = storage.videos
                        .firstOrNull { it.id == actualVideo.id }
                        ?: fail("Unexpected state, no video model found in storage")

                    val actualVideoRating = actualVideo.rating
                    val storedVideoRating = storageVideo.rating

                    assert(actualVideoRating == storedVideoRating) {
                        buildString {
                            append("Incorrect result. Content rating mismatch.")
                            appendLine()
                            append("Actual content rating: $actualVideoRating. Expected content rating: $storedVideoRating")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_video_id_return_video_with_correct_comments_count() {
        runInEnvironment { _, storage, executor ->

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            storage.videos.takeRandomValues(10).forEach { randomVideo ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = VideoByIDFilter(
                        videoID = randomVideo.id,
                        environmentLangID = envID
                    )

                    val actualVideo = executor.execute(filter).extractModelOrFail()

                    val actualVideoCommentsCount = actualVideo.commentsCount
                    val storedVideoCommentsCount = storage.comments
                        .count { it.contentID == actualVideo.id }
                        .toLong()

                    assert(actualVideoCommentsCount == storedVideoCommentsCount) {
                        buildString {
                            append("Incorrect result. Content comment count mismatch.")
                            appendLine()
                            append("Actual content comment count: $actualVideoCommentsCount. Expected content comment count: $storedVideoCommentsCount")
                        }
                    }
                }
            }
        }
    }

    private fun runInEnvironment(action: (database: DBTest, storage: TestModelsStorage, executor: GetVideoByIDRequestExecutor) -> Unit) {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetVideoByIDRequestExecutor(database, availableLanguages)
        action(database, storage, executor)
    }
}