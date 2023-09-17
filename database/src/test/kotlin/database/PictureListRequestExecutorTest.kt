package database

import database.external.filter.PictureListFilter
import database.external.result.SimpleListResult
import database.internal.creator.test.connectToDatabaseForTest
import database.internal.executor.GetPictureByIDRequestExecutor
import database.internal.executor.GetPictureListRequestExecutor
import database.test.selectAvailableLanguages
import database.utils.findDefaultLanguageID
import database.utils.getPicturesByImitateListRequest
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Tag
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.fail

/** Test of [GetPictureListRequestExecutor]. */
internal class PictureListRequestExecutorTest {

    @Test
    fun execute_with_zero_limit_return_empty_results_test() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetPictureListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            PictureListFilter(
                limit = 0,
                offset = if (Random.nextBoolean()) (0..10).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..10).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..10).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                PictureListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executePictureListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Not expected result from `GetPictureListRequestExecutor`, actual result: $result, expected result: Data"
            )

            assert(executePictureListResult.isEmpty()) {
                "Not expected result from `GetPictureListRequestExecutor`, empty data expected but something returned"
            }
        }
    }

    @Test
    fun execute_with_negative_limit_return_error_test() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetPictureListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            PictureListFilter(
                limit = -1,
                offset = if (Random.nextBoolean()) (0..10).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..10).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..10).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                PictureListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            assert(result is SimpleListResult.Error) {
                "Unexpected result from `GetPictureListRequestExecutor`, actual result: $result, expected result: Error"
            }
        }
    }

    @Test
    fun execute_with_negative_offset_return_error_test() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetPictureListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            PictureListFilter(
                limit = Random.nextLong(),
                offset = -1,
                environmentLangID = if (Random.nextBoolean()) (0..10).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..10).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                PictureListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            assert(result is SimpleListResult.Error) {
                "Unexpected result from `GetPictureListRequestExecutor`, actual result: $result, expected result: Error"
            }
        }
    }

    @Test
    fun execute_with_invalid_lang_id_return_empty_results_test() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetPictureListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            PictureListFilter(
                limit = (1..10).random().toLong(),
                offset = (1..5).random().toLong(),
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = -1,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                PictureListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executePictureListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Not expected result from `GetPictureListRequestExecutor`, actual result: $result, expected result: Data"
            )

            assert(executePictureListResult.isEmpty()) {
                "Not expected result from `GetPictureListRequestExecutor`, empty data expected but something returned"
            }
        }
    }

    @Test
    fun execute_with_non_existed_lang_id_return_empty_results_test() {
        val (database, _) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetPictureListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            PictureListFilter(
                limit = (1..10).random().toLong(),
                offset = (1..5).random().toLong(),
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = 999,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                PictureListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executePictureListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Not expected result from `GetPictureListRequestExecutor`, actual result: $result, expected result: Data"
            )

            assert(executePictureListResult.isEmpty()) {
                "Not expected result from `GetPictureListRequestExecutor`, empty data expected but something returned"
            }
        }
    }

    @Test
    fun execute_with_correct_data_return_pictures_with_correct_data_test() {
        val (database, storage) = connectToDatabaseForTest()
        val availableLanguages = database.selectAvailableLanguages()
        val executor = GetPictureListRequestExecutor(database, availableLanguages)

        val filters = List(100) {
            PictureListFilter(
                limit = (0..50).random().toLong(),
                offset = if (Random.nextBoolean()) (0..50).random().toLong() else null,
                environmentLangID = if (Random.nextBoolean()) (0..3).random().toByte() else null,
                langID = if (Random.nextBoolean()) (0..3).random() else null,
            )
        }

        filters.forEach { filter ->
            val result = executor.execute(
                PictureListFilter(
                    limit = filter.limit,
                    offset = filter.offset,
                    environmentLangID = filter.environmentLangID,
                    langID = filter.langID,
                )
            )

            val executePictureListResult = (result as? SimpleListResult.Data)?.data ?: fail(
                "Not expected result from `GetPictureListRequestExecutor`, actual result: $result, expected result: Data"
            )

            val requiredOffset = filter.offset ?: 0L
            val requiredPicturesLanguageID = filter.langID
            val requiredEnvironmentID = filter.environmentLangID

            // Count all pictures that confirm "langID" condition
            val totalAvailablePicturesCount = if (requiredPicturesLanguageID == null) {
                storage.pictures.count()
            } else {
                storage.pictures.count { it.languageID == requiredPicturesLanguageID.toByte() }
            }

            // If offset is too big, it's okay that executor return an empty result, especially if language filter set.
            // Check it and finish with this filter if so.
            val isAnyPicturesExpected = totalAvailablePicturesCount > requiredOffset
            if (!isAnyPicturesExpected) {
                assert(executePictureListResult.isEmpty()) { "Empty result from GetPictureListRequestExecutor expected, but result is not empty" }
                return@forEach
            }

            // Imitate request on storage
            val expectedPictures = storage.pictures.getPicturesByImitateListRequest(
                limit = filter.limit,
                offset = filter.offset ?: 0,
                langID = filter.langID?.toByte()
            )

            assert(expectedPictures.size == executePictureListResult.size) {
                "Seems like wrong result from `GetPictureListRequestExecutor`, expected and actual content size mismatch." +
                        "Expected size: ${expectedPictures.size}, actual size: ${executePictureListResult.size}"
            }

            executePictureListResult.forEachIndexed { idx, actualPicture ->
                val expectedPicture = expectedPictures[idx]

                val actualPictureID = actualPicture.id
                val expectedPictureID = expectedPicture.id
                assert(actualPictureID == expectedPictureID) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Content id mismatch.")
                        appendLine()
                        append("Actual pictureID: $actualPictureID. Expected pictureID: $expectedPictureID")
                    }
                }

                val actualPictureTitle = actualPicture.title
                val expectedPictureTitle = expectedPicture.title
                assert(actualPictureTitle == expectedPictureTitle) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Content title mismatch.")
                        appendLine()
                        append("Actual picture title: $actualPictureTitle. Expected picture title: $expectedPictureTitle")
                    }
                }

                val actualPictureURL = actualPicture.url
                val expectedPictureURL = expectedPicture.url
                assert(actualPictureURL == expectedPictureURL) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Content url mismatch.")
                        appendLine()
                        append("Actual picture url: $actualPictureURL. Expected picture url: $expectedPictureURL")
                    }
                }

                val actualPictureLanguageID = actualPicture.language?.id
                val expectedPictureLanguageID = expectedPicture.languageID
                assert(actualPictureLanguageID == expectedPictureLanguageID) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Content languageID mismatch.")
                        appendLine()
                        append("Actual picture languageID: $actualPictureLanguageID. Expected picture languageID: $expectedPictureLanguageID")
                    }
                }

                if (actualPictureLanguageID != null) {
                    val actualPictureLanguageTranslation = actualPicture.language
                        ?.name
                        ?: fail("Language model must be not null at this time")

                    // Pick translation by environmentID. If env not define, executor must pick default environment.
                    val envID = requiredEnvironmentID ?: storage.findDefaultLanguageID()

                    val expectedPictureLanguageTranslation = storage.languages
                        .firstOrNull { it.id == actualPictureLanguageID }
                        ?.translations
                        ?.firstOrNull { it.envID == envID }
                        ?.name
                        ?: fail("Something wrong happen, can't get correct language translation for pictureLangID: $actualPictureLanguageID")

                    assert(actualPictureLanguageTranslation == expectedPictureLanguageTranslation) {
                        buildString {
                            append("Seems like wrong result from `GetPictureListRequestExecutor`. Picture language translation mismatch.")
                            appendLine()
                            append("Actual picture language translation: $actualPictureLanguageTranslation. Expected picture language translation: $expectedPictureLanguageTranslation")
                            appendLine()
                            append("Actual picture languageID: $actualPictureLanguageID, EnvID: $envID")
                        }
                    }
                }

                val actualPictureAvailableLanguageIDList = actualPicture.availableLanguages?.map(Lang::id)
                val expectedPictureAvailableLanguageIDList = expectedPicture.availableLanguagesIDs
                assert(actualPictureAvailableLanguageIDList == expectedPictureAvailableLanguageIDList) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Content available language IDs mismatch.")
                        appendLine()
                        append("Actual picture available language IDs: $actualPictureAvailableLanguageIDList. Expected picture available language IDs: $expectedPictureAvailableLanguageIDList")
                    }
                }

                if (actualPictureAvailableLanguageIDList != null) {
                    // Pick translation by environmentID. If env not define, executor must pick default environment.
                    val envID = requiredEnvironmentID ?: storage.findDefaultLanguageID()
                    actualPicture.availableLanguages?.forEach { actualPictureAvailableLanguage ->
                        val expectedPictureAvailableLanguageTranslation = storage.languages
                            .firstOrNull { it.id == actualPictureAvailableLanguage.id }
                            ?.translations
                            ?.firstOrNull { it.envID == envID }
                            ?.name
                            ?: fail("Something wrong happen, can't get correct language translation for pictureLangID: $actualPictureLanguageID")

                        assert(actualPictureAvailableLanguage.name == expectedPictureAvailableLanguageTranslation) {
                            buildString {
                                append("Seems like wrong result from `GetPictureListRequestExecutor`. Picture available language translation mismatch.")
                                appendLine()
                                append("Actual picture available language translation: ${actualPictureAvailableLanguage.name}. Expected picture language translation: $expectedPictureAvailableLanguageTranslation")
                                appendLine()
                                append("Actual picture available languageID: ${actualPictureAvailableLanguage.id}, EnvID: $envID")
                            }
                        }
                    }
                }


                val actualPictureAuthorIDList = actualPicture.authors?.map(Author::id)
                val expectedPictureAuthorIDList = expectedPicture.authorsIDs?.sorted()
                assert(actualPictureAuthorIDList == expectedPictureAuthorIDList) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Content authors IDs mismatch.")
                        appendLine()
                        append("Actual picture authors IDs: $actualPictureAuthorIDList. Expected picture authors IDs: $expectedPictureAuthorIDList")
                    }
                }

                actualPicture.authors?.forEach { actualPictureAuthor ->
                    val expectedPictureAuthor = storage.authors
                        .firstOrNull { it.id == actualPictureAuthor.id }
                        ?: fail("Something wrong happen, can't get author from storage by authorID: ${actualPictureAuthor.id}")

                    assert(actualPictureAuthor.name == expectedPictureAuthor.name) {
                        buildString {
                            append("Seems like wrong result from `GetPictureListRequestExecutor`. Author name mismatch.")
                            appendLine()
                            append("Actual picture author name: ${actualPictureAuthor.name}. Expected picture author name: ${expectedPictureAuthor.name}")
                            appendLine()
                            append("Actual picture author ID:  ${actualPictureAuthor.id}. Expected picture author ID: ${expectedPictureAuthor.id}")
                        }
                    }
                }

                val actualPictureUserID = actualPicture.user?.id
                val expectedPictureUserID = expectedPicture.userID
                assert(actualPictureUserID == expectedPictureUserID) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Content user ID mismatch.")
                        appendLine()
                        append("Actual picture user ID: $actualPictureUserID. Expected picture user ID: $expectedPictureUserID")
                    }
                }

                actualPicture.user?.let { actualPictureUser ->
                    val expectedUserName = storage.users
                        .firstOrNull { it.id == actualPictureUser.id }
                        ?.name
                        ?: fail("Something wrong happen, can't get user from storage by userID: ${actualPictureUser.id}")

                    assert(actualPictureUser.name == expectedUserName) {
                        buildString {
                            append("Seems like wrong result from `GetPictureListRequestExecutor`. Content user name mismatch.")
                            appendLine()
                            append("Actual picture user name: ${actualPictureUser.name}. Expected picture user name: $expectedUserName")
                        }
                    }
                }

                val actualPictureTagIDList = actualPicture.tags?.map(Tag::id)
                val expectedPictureTagIDList = expectedPicture.tagsIDs?.sorted()
                assert(actualPictureTagIDList == expectedPictureTagIDList) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Tags by ID mismatch.")
                        appendLine()
                        append("Actual picture tagID list: $actualPictureTagIDList. Expected picture tagID list: $expectedPictureTagIDList")
                    }
                }

                actualPicture.tags?.forEach { actualPictureTag ->
                    // Pick translation by environmentID. If env not define, executor must pick default environment.
                    val envID = requiredEnvironmentID ?: storage.findDefaultLanguageID()

                    val expectedPictureTagTranslation = storage.tags
                        .firstOrNull { it.id == actualPictureTag.id }
                        ?.translations
                        ?.firstOrNull { it.envID == envID }
                        ?.name
                        ?: fail("Something wrong happen, can't get correct tag translation for tag: $actualPictureTag")

                    assert(actualPictureTag.name == expectedPictureTagTranslation) {
                        buildString {
                            append("Seems like wrong result from `GetPictureListRequestExecutor`. Tag translation mismatch.")
                            appendLine()
                            append("Actual picture tag translation: ${actualPictureTag.name}. Expected picture tag translation: $expectedPictureTagTranslation")
                            appendLine()
                            append("Actual picture tagID: ${actualPictureTag.id}, EnvID: $envID")
                        }
                    }
                }

                val actualPictureRating = actualPicture.rating
                val expectedPictureRating = expectedPicture.rating
                assert(actualPictureRating == expectedPictureRating) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Rating mismatch.")
                        appendLine()
                        append("Actual picture rating: $actualPictureRating. Expected picture rating: $expectedPictureRating")
                    }
                }

                val actualPictureCommentCount = actualPicture.commentsCount.toInt()
                val expectedPictureCommentCount = storage.comments.count { it.contentID == actualPictureID }
                assert(actualPictureCommentCount == expectedPictureCommentCount) {
                    buildString {
                        append("Seems like wrong result from `GetPictureListRequestExecutor`. Comments count mismatch.")
                        appendLine()
                        append("Actual picture comments count: $actualPictureCommentCount. Expected picture comments count: $expectedPictureCommentCount")
                    }
                }
            }
        }
    }
}