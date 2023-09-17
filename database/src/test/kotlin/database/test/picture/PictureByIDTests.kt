package database.test.picture

import common.pairBy
import common.takeIfNotEmpty
import database.internal.creator.test.connectToDatabase
import database.internal.creator.test.connectToDatabaseForTest
import database.utils.*
import rockyouapi.picture.SelectPictureAuthors
import rockyouapi.picture.SelectPictureTags
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.fail

internal class PictureByIDTests {

    @Test
    fun select_from_empty_database_not_fail_test_01() {
        val database = connectToDatabase()

        val pictureBaseInfo = database.pictureSelectSingleQueries
            .selectPictureBaseInfo(Random.nextInt())
            .executeAsOneOrNull()

        assert(pictureBaseInfo == null) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_02() {
        val database = connectToDatabase()

        val pictureAuthors = database.pictureSelectSingleQueries
            .selectPictureAuthors(Random.nextInt())
            .executeAsOneOrNull()

        assert(pictureAuthors == null) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_03() {
        val database = connectToDatabase()

        val pictureAvailableLanguages = database.pictureSelectSingleQueries
            .selectPictureAvailableLangIDs(Random.nextInt())
            .executeAsOneOrNull()

        assert(pictureAvailableLanguages == null) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_04() {
        val database = connectToDatabase()

        val pictureTags = database.pictureSelectSingleQueries
            .selectPictureTags(Random.nextInt(), -1)
            .executeAsOneOrNull()

        assert(pictureTags == null) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_05() {
        val database = connectToDatabase()

        val pictureTags = database.pictureSelectSingleQueries
            .selectPictureTags(Random.nextInt(), 0)
            .executeAsOneOrNull()

        assert(pictureTags == null) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_06() {
        val database = connectToDatabase()

        val pictureTags = database.pictureSelectSingleQueries
            .selectPictureTags(Random.nextInt(), 1)
            .executeAsOneOrNull()

        assert(pictureTags == null) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_base_picture_info_work_correct_test() {
        val (database, testModels) = connectToDatabaseForTest()

        testModels.getRandomPictures().forEach { picture ->
            val dbResponse = database.pictureSelectSingleQueries
                .selectPictureBaseInfo(picture.id)
                .executeAsOneOrNull()
                ?: fail("Fail to select picture base info by ID")

            val isDBResponseCorrectByID = dbResponse.registerID == picture.id
            assert(isDBResponseCorrectByID) {
                "Selected record incorrect by ID. Expected ID: ${picture.id}, Selected ID: ${dbResponse.registerID}"
            }

            val isDBResponseCorrectByTitle = dbResponse.title == picture.title
            assert(isDBResponseCorrectByTitle) {
                "Selected record incorrect by title. Expected title: ${picture.title}, Selected title: ${dbResponse.title}"
            }

            val isDBResponseCorrectByURL = dbResponse.url == picture.url
            assert(isDBResponseCorrectByURL) {
                "Selected record incorrect by URL. Expected URL: ${picture.url}, Selected URL: ${dbResponse.url}"
            }

            val userModel = testModels.users
                .firstOrNull { it.id == picture.userID }
                ?: fail("Unexpected error to ger inserted user from test models")

            val isDBResponseCorrectByUserID = dbResponse.userID == picture.userID
            assert(isDBResponseCorrectByUserID) {
                "Selected record incorrect by UserID. Expected UserID: ${picture.userID}, Selected URL: ${dbResponse.userID}"
            }

            val isDBResponseCorrectByUserName = dbResponse.userName == userModel.name
            assert(isDBResponseCorrectByUserName) {
                "Selected record incorrect by user name. Expected user name: ${userModel.name}, Selected user name: ${dbResponse.userName}"
            }

            val isDBResponseCorrectByLanguageID = dbResponse.baseLanguageID == picture.languageID?.toInt()
            assert(isDBResponseCorrectByLanguageID) {
                "Selected record incorrect by languageID. Expected languageID: ${picture.languageID}, Selected languageID: ${dbResponse.baseLanguageID}"
            }

            val isDBResponseCorrectByRating = dbResponse.rating == picture.rating
            assert(isDBResponseCorrectByRating) {
                "Selected record incorrect by rating. Expected rating: ${picture.rating}, Selected rating: ${dbResponse.rating}"
            }

            val commentsCountForPicture = testModels.comments.count { it.contentID == picture.id }.toLong()
            val isDBResponseCorrectByCommentsCount = dbResponse.commentsCount == commentsCountForPicture
            assert(isDBResponseCorrectByCommentsCount) {
                "Selected record incorrect by comments count. Expected comments count: $commentsCountForPicture, Selected comments count: ${dbResponse.commentsCount}"
            }
        }
    }

    @Test
    fun select_picture_authors_work_correct_test() {
        val (database, testModels) = connectToDatabaseForTest()

        testModels.getRandomPictures().forEach { picture ->
            val dbResponse = database.pictureSelectSingleQueries
                .selectPictureAuthors(picture.id)
                .executeAsList()
                .takeIfNotEmpty()

            val pictureModelAuthorIDList = picture.authorsIDs?.takeIfNotEmpty()

            when {
                pictureModelAuthorIDList == null && dbResponse == null -> return@forEach
                pictureModelAuthorIDList == null && dbResponse != null -> fail(
                    "Not empty authors from database. Empty response expected"
                )

                pictureModelAuthorIDList != null && dbResponse == null -> fail(
                    "Empty authors from database. Not empty response expected"
                )
            }

            dbResponse ?: fail("dbResponse can not be null at this moment")
            pictureModelAuthorIDList ?: fail("pictureModelAuthorList can not be null at this moment")

            val isDBResponseCorrectBySize = pictureModelAuthorIDList.size == dbResponse.size
            assert(isDBResponseCorrectBySize) {
                "Selected picture authors incorrect by size. Expected size: ${pictureModelAuthorIDList.size}, Selected size: ${dbResponse.size}"
            }

            val dbResponseAuthorIDList = dbResponse.map(SelectPictureAuthors::authorID)
            val isDBResponseCorrectByAuthorIDs = pictureModelAuthorIDList == dbResponseAuthorIDList
            assert(isDBResponseCorrectByAuthorIDs) {
                "Selected picture authors incorrect by IDs. Expected IDs: $pictureModelAuthorIDList, Selected IDs: $dbResponseAuthorIDList"
            }

            // For every author from database response, find an author model and compare their names
            val isDBResponseCorrectByAuthorNames = dbResponse
                .pairBy { testModels.findAuthorByID(it.authorID) }
                .all { (responseAuthor, testModelAuthor) -> responseAuthor.authorName == testModelAuthor.name }
            assert(isDBResponseCorrectByAuthorNames) { "Selected picture authors incorrect by names" }
        }
    }

    @Test
    fun select_picture_available_languages_work_correct_test() {
        val (database, testModels) = connectToDatabaseForTest()

        testModels.getRandomPictures().forEach { picture ->
            val dbResponse = database.pictureSelectSingleQueries
                .selectPictureAvailableLangIDs(picture.id)
                .executeAsList()
                .takeIfNotEmpty()

            val pictureModelAvailableLanguageIDList = picture.availableLanguagesIDs?.takeIfNotEmpty()

            when {
                pictureModelAvailableLanguageIDList == null && dbResponse == null -> return@forEach
                pictureModelAvailableLanguageIDList == null && dbResponse != null -> fail(
                    "Not empty available languages from database. Empty response expected"
                )

                pictureModelAvailableLanguageIDList != null && dbResponse == null -> fail(
                    "Empty available languages from database. Not empty response expected"
                )
            }

            dbResponse ?: fail("dbResponse can not be null at this moment")
            pictureModelAvailableLanguageIDList
                ?: fail("pictureModelAvailableLanguageIDList can not be null at this moment")

            val isDBResponseCorrectBySize = pictureModelAvailableLanguageIDList.size == dbResponse.size
            assert(isDBResponseCorrectBySize) {
                "Selected picture available languages incorrect by size. Expected size: ${pictureModelAvailableLanguageIDList.size}, Selected size: ${dbResponse.size}"
            }

            val isDBResponseCorrectByIDs = pictureModelAvailableLanguageIDList == dbResponse
            assert(isDBResponseCorrectByIDs) { "Selected picture available languages incorrect by IDs" }
        }
    }

    @Test
    fun select_picture_tags_work_correct_test() {
        val (database, testModels) = connectToDatabaseForTest()

        (0..5).forEach { suggestedEnvironmentLangID ->
            val environmentLangID = if (testModels.isLanguageSupported(suggestedEnvironmentLangID.toByte())) {
                suggestedEnvironmentLangID.toByte()
            } else {
                testModels.findDefaultLanguageID()
            }
            testModels.getRandomPictures().forEach { picture ->
                val dbResponse = database.pictureSelectSingleQueries
                    .selectPictureTags(picture.id, environmentLangID.toInt())
                    .executeAsList()
                    .takeIfNotEmpty()

                val pictureModelTagIDList = picture.tagsIDs?.takeIfNotEmpty()

                when {
                    pictureModelTagIDList == null && dbResponse == null -> return@forEach
                    pictureModelTagIDList == null && dbResponse != null -> fail(
                        "Not empty tags from database. Empty response expected"
                    )

                    pictureModelTagIDList != null && dbResponse == null -> fail(
                        "Empty tags from database. Not empty response expected"
                    )
                }

                dbResponse ?: fail("dbResponse can not be null at this moment")
                pictureModelTagIDList ?: fail("pictureModelAuthorList can not be null at this moment")

                val isDBResponseCorrectBySize = pictureModelTagIDList.size == dbResponse.size
                assert(isDBResponseCorrectBySize) {
                    "Selected picture tags incorrect by size. Expected size: ${pictureModelTagIDList.size}, Selected size: ${dbResponse.size}"
                }

                val dbResponseTagIDList = dbResponse.map(SelectPictureTags::tagID)
                val isDBResponseCorrectByIDs = pictureModelTagIDList == dbResponseTagIDList
                assert(isDBResponseCorrectByIDs) {
                    "Selected picture tags incorrect by IDs. Expected IDs: $pictureModelTagIDList, Selected IDs: $dbResponseTagIDList"
                }

                // For every tag from database response, find a tag model and compare their names
                val isDBResponseCorrectByTagNames = dbResponse
                    .pairBy { testModels.findTagByID(it.tagID).findTranslationByEnvironmentID(environmentLangID) }
                    .all { (responseTag, testModelTagTranslation) -> responseTag.tagTranslation == testModelTagTranslation }
                assert(isDBResponseCorrectByTagNames) { "Selected picture tags incorrect by names" }
            }
        }
    }
}