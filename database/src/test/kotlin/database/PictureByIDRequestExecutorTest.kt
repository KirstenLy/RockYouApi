package database

import common.takeRandomValues
import database.external.ContentType
import database.external.filter.PictureByIDFilter
import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.GetChaptersRequestExecutor
import database.internal.executor.GetPictureByIDRequestExecutor
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

/** Test of [GetPictureByIDRequestExecutor]. */
internal class PictureByIDRequestExecutorTest {

    @Test
    fun execute_with_non_existed_picture_id_return_not_found_result() {
        runInEnvironment { _, _, executor ->
            val nonExistedPictureID = 99999
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            envIDList.forEach { envID ->
                val filter = PictureByIDFilter(
                    pictureID = nonExistedPictureID,
                    environmentLangID = envID
                )
                executor.execute(filter).asDataNotFoundOrFail()
            }
        }
    }

    @Test
    fun execute_with_wrong_picture_id_return_not_found_result() {
        runInEnvironment { _, storage, executor ->
            val wrongPictureID = storage.contentRegisters
                .first { it.contentType != ContentType.PICTURE.typeID }
                .id

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            envIDList.forEach { envID ->
                val filter = PictureByIDFilter(
                    pictureID = wrongPictureID,
                    environmentLangID = envID
                )

                executor.execute(filter).asDataNotFoundOrFail()
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_id() {
        runInEnvironment { _, storage, executor ->
            val randomPicture = storage.pictures.random()
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            envIDList.forEach { envID ->
                val filter = PictureByIDFilter(
                    pictureID = randomPicture.id,
                    environmentLangID = envID
                )
                val actualPicture = executor.execute(filter).extractModelOrFail()

                assert(actualPicture.id == randomPicture.id) {
                    buildString {
                        append("Incorrect result. Content id mismatch.")
                        appendLine()
                        append("Actual content id: ${actualPicture.id}. Expected content id: ${randomPicture.id}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_title() {
        runInEnvironment { _, storage, executor ->
            val randomPicture = storage.pictures.random()
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            envIDList.forEach { envID ->
                val filter = PictureByIDFilter(
                    pictureID = randomPicture.id,
                    environmentLangID = envID
                )

                val actualPicture = executor.execute(filter).extractModelOrFail()

                assert(actualPicture.title == randomPicture.title) {
                    buildString {
                        append("Incorrect result. Content title mismatch.")
                        appendLine()
                        append("Actual content title: ${actualPicture.title}. Expected content title: ${randomPicture.title}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_url() {
        runInEnvironment { _, storage, executor ->
            val randomPicture = storage.pictures.random()
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            envIDList.forEach { envID ->
                val filter = PictureByIDFilter(
                    pictureID = randomPicture.id,
                    environmentLangID = envID
                )

                val actualPicture = executor.execute(filter).extractModelOrFail()

                assert(actualPicture.url == randomPicture.url) {
                    buildString {
                        append("Incorrect result. Content url mismatch.")
                        appendLine()
                        append("Actual content url: ${actualPicture.url}. Expected content url: ${randomPicture.url}")
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_lang() {
        runInEnvironment { _, storage, executor ->
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)
            val invalidEnvIDList = listOf<Byte?>(null, -1, 100)

            storage.pictures.takeRandomValues(10).forEach { randomPicture ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = PictureByIDFilter(
                        pictureID = randomPicture.id,
                        environmentLangID = envID
                    )

                    val actualPicture = executor.execute(filter).extractModelOrFail()

                    assert(actualPicture.language?.id == randomPicture.languageID) {
                        buildString {
                            append("Incorrect result. Content languageID mismatch.")
                            appendLine()
                            append("Actual content languageID: ${actualPicture.url}. Expected content languageID: ${randomPicture.url}")
                        }
                    }

                    if (actualPicture.language == null) {
                        // No need to check language translation if language not presented
                        return@envIDCycle
                    }

                    val actualPictureLanguageID = actualPicture.language?.id ?: fail(
                        "Unexpected state, picture language ID must be presented at this time"
                    )

                    val actualPictureLanguageName = actualPicture.language?.name ?: fail(
                        "Unexpected state, picture language name must be presented at this time"
                    )

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    val expectedLanguageTranslation = storage
                        .languages
                        .firstOrNull { it.id == actualPictureLanguageID }
                        ?.translations
                        ?.firstOrNull { it.envID == actualEnvID }
                        ?.name
                        ?: fail("Unexpected state, picture language or it's translation for env not presented in storage")

                    assert(actualPictureLanguageName == expectedLanguageTranslation) {
                        buildString {
                            append("Incorrect result. Content language name mismatch.")
                            appendLine()
                            append("Actual content language name: $actualPictureLanguageName. Expected content language name: $expectedLanguageTranslation")
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

            storage.pictures.takeRandomValues(10).forEach { randomPicture ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = PictureByIDFilter(
                        pictureID = randomPicture.id,
                        environmentLangID = envID
                    )

                    val actualPicture = executor.execute(filter).extractModelOrFail()

                    val storagePicture = storage.pictures
                        .firstOrNull { it.id == actualPicture.id }
                        ?: fail("Unexpected state, no picture model found in storage")

                    val actualPictureAvailableLanguagesIDList = actualPicture.availableLanguages?.map(Lang::id)
                    val storagePictureAvailableLanguageIDList = storagePicture.availableLanguagesIDs

                    assert(actualPictureAvailableLanguagesIDList == storagePictureAvailableLanguageIDList) {
                        fail(
                            buildString {
                                append("Incorrect result. Content available language mismatch.")
                                appendLine()
                                append("Actual content available languages IDs: $actualPictureAvailableLanguagesIDList. Expected content available languages IDs: $storagePictureAvailableLanguageIDList")
                            }
                        )
                    }

                    if (actualPictureAvailableLanguagesIDList == null && storagePictureAvailableLanguageIDList == null) {
                        // No need to check available languages and their translations if available languages not presented
                        return@envIDCycle
                    }

                    if (actualPictureAvailableLanguagesIDList!!.isEmpty() && storagePictureAvailableLanguageIDList!!.isEmpty()) {
                        // No need to check available languages and their translations if available languages not presented
                        return@envIDCycle
                    }

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    actualPicture.availableLanguages?.forEach { actualPictureLanguageModel ->
                        val actualPictureLanguageTranslation = actualPictureLanguageModel.name
                        val expectedPictureLanguageTranslation = storage.languages
                            .firstOrNull { it.id == actualPictureLanguageModel.id }
                            ?.translations
                            ?.firstOrNull { it.envID == actualEnvID }
                            ?.name
                            ?: fail("Unexpected state, can't find translation for default language")

                        assert(actualPictureLanguageTranslation == expectedPictureLanguageTranslation) {
                            buildString {
                                append("Incorrect result. Content available language name mismatch.")
                                appendLine()
                                append("Actual content available language name: $actualPictureLanguageTranslation. Expected content available language name: $expectedPictureLanguageTranslation")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_authors() {
        runInEnvironment { _, storage, executor ->
            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            storage.pictures.takeRandomValues(10).forEach { randomPicture ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = PictureByIDFilter(
                        pictureID = randomPicture.id,
                        environmentLangID = envID
                    )

                    val actualPicture = executor.execute(filter).extractModelOrFail()

                    val storagePicture = storage.pictures
                        .firstOrNull { it.id == actualPicture.id }
                        ?: fail("Unexpected state, no picture model found in storage")

                    val actualPictureAuthorIDList = actualPicture.authors?.map(Author::id)
                    val storedPictureAuthorIDList = storagePicture.authorsIDs

                    assert(actualPictureAuthorIDList == storedPictureAuthorIDList) {
                        fail(
                            buildString {
                                append("Incorrect result. Content author ID list mismatch.")
                                appendLine()
                                append("Actual content author ID list: $actualPictureAuthorIDList. Expected content author ID list: $storedPictureAuthorIDList")
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

            storage.pictures.takeRandomValues(10).forEach { randomPicture ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = PictureByIDFilter(
                        pictureID = randomPicture.id,
                        environmentLangID = envID
                    )

                    val actualPicture = executor.execute(filter).extractModelOrFail()

                    val storagePicture = storage.pictures
                        .firstOrNull { it.id == actualPicture.id }
                        ?: fail("Unexpected state, no picture model found in storage")

                    val actualPictureTagIDList = actualPicture.tags?.map(Tag::id)
                    val storedPictureTagIDList = storagePicture.tagsIDs

                    // Sort IDs from store because executor return sorted tags
                    assert(actualPictureTagIDList == storedPictureTagIDList?.sorted()) {
                        fail(
                            buildString {
                                append("Incorrect result. Content tags IDs mismatch.")
                                appendLine()
                                append("Actual content tags IDs: $actualPictureTagIDList. Expected content tags IDs: $storedPictureTagIDList")
                            }
                        )
                    }

                    if (actualPictureTagIDList == null && storedPictureTagIDList == null) {
                        // No need to check tags translations if tags not presented
                        return@envIDCycle
                    }

                    if (actualPictureTagIDList!!.isEmpty() && storedPictureTagIDList!!.isEmpty()) {
                        // No need to check tags translations if tags not presented
                        return@envIDCycle
                    }

                    // If envID is invalid or not supported, language name must be translated by default language.
                    // If envID valid, language name must be translated with envID.
                    // Default language defined by database.
                    val actualEnvID = if (envID in invalidEnvIDList) storage.findDefaultLanguageID() else envID

                    actualPicture.tags?.forEach { actualPictureTag ->
                        val actualPictureTagID = actualPictureTag.id
                        val actualPictureTagTranslation = actualPictureTag.name

                        val expectedTagTranslation = storage.tags
                            .firstOrNull { it.id == actualPictureTagID }
                            ?.translations
                            ?.firstOrNull { it.envID == actualEnvID }
                            ?.name
                            ?: fail("Unexpected state, video tag or it's translation not presented in test models")

                        assert(actualPictureTagTranslation == expectedTagTranslation) {
                            buildString {
                                append("Incorrect result. Content tag translation mismatch.")
                                appendLine()
                                append("Actual content tag translation: $actualPictureTagTranslation. Expected content tag translation: $expectedTagTranslation")
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

            storage.pictures.takeRandomValues(10).forEach { randomPicture ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = PictureByIDFilter(
                        pictureID = randomPicture.id,
                        environmentLangID = envID
                    )

                    val actualPicture = executor.execute(filter).extractModelOrFail()

                    val storagePicture = storage.pictures
                        .firstOrNull { it.id == actualPicture.id }
                        ?: fail("Unexpected state, no picture model found in storage")

                    val actualPictureUserID = actualPicture.user?.id
                    val storedPictureUserID = storagePicture.userID

                    assert(actualPictureUserID == storedPictureUserID) {
                        buildString {
                            append("Incorrect result. Content userID mismatch.")
                            appendLine()
                            append("Actual content userID: $actualPictureUserID. Expected content userID: $storedPictureUserID")
                        }
                    }

                    if (actualPictureUserID == null) {
                        // No need to check username if user not presented
                        return@envIDCycle
                    }

                    val actualPictureUserName = actualPicture.user?.name
                    val storedPictureUserName = storage.users
                        .firstOrNull { it.id == actualPictureUserID }
                        ?.name
                        ?: fail("Unexpected state, user model not found in storage")

                    assert(actualPictureUserName == storedPictureUserName) {
                        buildString {
                            append("Incorrect result. Content userName mismatch.")
                            appendLine()
                            append("Actual content userName: $actualPictureUserName. Expected content userName: $storedPictureUserName")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_rating() {
        runInEnvironment { _, storage, executor ->

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            storage.pictures.takeRandomValues(10).forEach { randomPicture ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = PictureByIDFilter(
                        pictureID = randomPicture.id,
                        environmentLangID = envID
                    )

                    val actualPicture = executor.execute(filter).extractModelOrFail()

                    val storagePicture = storage.pictures
                        .firstOrNull { it.id == actualPicture.id }
                        ?: fail("Unexpected state, no picture model found in storage")

                    val actualPictureRating = actualPicture.rating
                    val storedPictureRating = storagePicture.rating

                    assert(actualPictureRating == storedPictureRating) {
                        buildString {
                            append("Incorrect result. Content rating mismatch.")
                            appendLine()
                            append("Actual content rating: $actualPictureRating. Expected content rating: $storedPictureRating")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun execute_with_correct_picture_id_return_picture_with_correct_comments_count() {
        runInEnvironment { _, storage, executor ->

            val envIDList = listOf<Byte?>(null, -1, 0, 1, 100)

            storage.pictures.takeRandomValues(10).forEach { randomPicture ->

                envIDList.forEach envIDCycle@{ envID ->

                    val filter = PictureByIDFilter(
                        pictureID = randomPicture.id,
                        environmentLangID = envID
                    )

                    val actualPicture = executor.execute(filter).extractModelOrFail()

                    val actualPictureCommentsCount = actualPicture.commentsCount
                    val storedPictureCommentsCount = storage.comments
                        .count { it.contentID == actualPicture.id }
                        .toLong()

                    assert(actualPictureCommentsCount == storedPictureCommentsCount) {
                        buildString {
                            append("Incorrect result. Content comment count mismatch.")
                            appendLine()
                            append("Actual content comment count: $actualPictureCommentsCount. Expected content comment count: $storedPictureCommentsCount")
                        }
                    }
                }
            }
        }
    }

    private fun runInEnvironment(action: (database: DBTest, storage: TestModelsStorage, executor: GetPictureByIDRequestExecutor) -> Unit) {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetPictureByIDRequestExecutor(database, availableLanguages)
        action(database, storage, executor)
    }
}