package database.test.picture

import database.internal.creator.test.connectToDatabase
import database.internal.test.fillPartiallyByGeneratedContent
import database.test.deleteFromContentRegisterByID
import database.test.isRecordInContentRegisterExist
import database.utils.*
import rockyouapi.DBTest
import java.util.UUID
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.fail

internal class PictureBaseTests {

    @Test
    fun select_from_empty_database_not_fail_test_01() {
        val database = connectToDatabase()

        val allPictureRecords = database.pictureSelectSeveralQueries
            .selectAll()
            .executeAsList()

        assert(allPictureRecords.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_02() {
        val database = connectToDatabase()

        val allPictureRecords = database.pictureQueries
            .selectPictureBaseInfoListByID(emptyList())
            .executeAsList()

        assert(allPictureRecords.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_03() {
        val database = connectToDatabase()

        val pictureBaseInfoList = database.pictureQueries
            .selectPictureBaseInfoListByID(listOf(1, 2, 3))
            .executeAsList()

        assert(pictureBaseInfoList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_04() {
        val database = connectToDatabase()

        val picturesAuthorsList = database.pictureQueries
            .selectPicturesAuthorList(emptyList())
            .executeAsList()

        assert(picturesAuthorsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_05() {
        val database = connectToDatabase()

        val picturesAuthorsList = database.pictureQueries
            .selectPicturesAuthorList(listOf(1, 2, 3))
            .executeAsList()

        assert(picturesAuthorsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_06() {
        val database = connectToDatabase()

        val picturesLanguagesList = database.pictureQueries
            .selectPicturesAvailableLanguageList(emptyList())
            .executeAsList()

        assert(picturesLanguagesList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_07() {
        val database = connectToDatabase()

        val picturesLanguagesList = database.pictureQueries
            .selectPicturesAvailableLanguageList(listOf(1, 2, 3))
            .executeAsList()

        assert(picturesLanguagesList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_08() {
        val database = connectToDatabase()

        val picturesTagsList = database.pictureQueries
            .selectPicturesTagList(emptyList(), 0)
            .executeAsList()

        assert(picturesTagsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_09() {
        val database = connectToDatabase()

        val picturesTagsList = database.pictureQueries
            .selectPicturesTagList(emptyList(), 1)
            .executeAsList()

        assert(picturesTagsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_10() {
        val database = connectToDatabase()

        val picturesTagsList = database.pictureQueries
            .selectPicturesTagList(emptyList(), -1)
            .executeAsList()

        assert(picturesTagsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_11() {
        val database = connectToDatabase()

        val picturesTagsList = database.pictureQueries
            .selectPicturesTagList(listOf(1, 2, 3), -1)
            .executeAsList()

        assert(picturesTagsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_12() {
        val database = connectToDatabase()

        val picturesTagsList = database.pictureQueries
            .selectPicturesTagList(listOf(1, 2, 3), 0)
            .executeAsList()

        assert(picturesTagsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun select_from_empty_database_not_fail_test_13() {
        val database = connectToDatabase()

        val picturesTagsList = database.pictureQueries
            .selectPicturesTagList(listOf(1, 2, 3), -1)
            .executeAsList()

        assert(picturesTagsList.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_01() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        try {
            val randomInsertedLanguageID = testModels.languages.random().id
            val randomInsertedUserID = testModels.users.random().id

            database.pictureInsertQueries.insert(
                id = Random.nextInt(), // Expect to fail
                title = UUID.randomUUID().toString(),
                url = UUID.randomUUID().toString(),
                languageID = randomInsertedLanguageID.toInt(),
                userID = randomInsertedUserID,
                rating = Random.nextInt()
            )
        } catch (t: Throwable) {
            return
        }

        fail("Picture insert with not existed ID in content register table must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_02() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            val randomInsertedLanguageID = testModels.languages.random().id
            val randomInsertedUserID = testModels.users.random().id
            database.pictureInsertQueries.insert(
                id = Random.nextInt(from = 2, until = 10), // Expect to fail
                title = UUID.randomUUID().toString(),
                url = UUID.randomUUID().toString(),
                languageID = randomInsertedLanguageID.toInt(),
                userID = randomInsertedUserID,
                rating = Random.nextInt()
            )
        } catch (t: Throwable) {
            return
        }

        fail("Picture insert with not existed ID in content register table must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_03() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            val randomInsertedUserID = testModels.users.random().id
            database.pictureInsertQueries.insert(
                id = 1,
                title = UUID.randomUUID().toString(),
                url = UUID.randomUUID().toString(),
                languageID = 100, // Expect to fail
                userID = randomInsertedUserID,
                rating = Random.nextInt()
            )
        } catch (t: Throwable) {
            return
        }

        fail("Picture insert with not existed language ID must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_04() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            val randomInsertedLanguageID = testModels.languages.random().id
            database.pictureInsertQueries.insert(
                id = 1,
                title = UUID.randomUUID().toString(),
                url = UUID.randomUUID().toString(),
                languageID = randomInsertedLanguageID.toInt(),
                userID = 100, // Expect to fail
                rating = Random.nextInt()
            )
        } catch (t: Throwable) {
            return
        }

        fail("Picture insert with not existed userID must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_05() {
        val database = connectToDatabase().also { it.fillPartiallyByGeneratedContent() }

        try {
            database.insertRelationBetweenPictureAndAuthor(
                pictureID = 1, // Expect to fail, ContentRegister table not filled
                authorID = 1
            )
        } catch (t: Throwable) {
            return
        }

        fail("[Picture <-> Authors] insert with not existed picture record must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_06() {
        val database = connectToDatabase().also { it.fillPartiallyByGeneratedContent() }

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            database.insertRelationBetweenPictureAndAuthor(
                pictureID = 1,
                authorID = 999 // Expect to fail, author with this ID not exists
            )
        } catch (t: Throwable) {
            return
        }

        fail("[Picture <-> Authors] insert with not existed authorID must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_07() {
        val database = connectToDatabase().also { it.fillPartiallyByGeneratedContent() }

        try {
            database.insertRelationBetweenPictureAndLanguage(
                pictureID = 1, // Expect to fail, picture with this ID not exists
                languageID = 1
            )
        } catch (t: Throwable) {
            return
        }

        fail("[Picture <-> Available languages] insert with not existed picture must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_08() {
        val database = connectToDatabase().also { it.fillPartiallyByGeneratedContent() }

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            database.insertRelationBetweenPictureAndLanguage(
                pictureID = 1,
                languageID = 100 // Expect to fail, language with this ID not exists
            )
        } catch (t: Throwable) {
            return
        }

        fail("[Picture <-> Available languages] insert with not existed language must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_09() {
        val database = connectToDatabase().also { it.fillPartiallyByGeneratedContent() }

        try {
            database.insertRelationBetweenPictureAndTag(
                pictureID = 1, // Expect to fail, language with this ID not exists
                tagID = 1
            )
        } catch (t: Throwable) {
            return
        }

        fail("[Picture <-> Tags] insert with not existed picture must fall")
    }

    @Test
    fun inset_fail_if_insert_break_entities_constraints_test_10() {
        val database = connectToDatabase().also { it.fillPartiallyByGeneratedContent() }

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            database.insertRelationBetweenPictureAndTag(
                pictureID = 1,
                tagID = 1000 // Expect to fail, tag with this ID not exists
            )
        } catch (t: Throwable) {
            return
        }

        fail("[Picture <-> Tags] insert with not existed tag must fall")
    }

    @Test
    fun inset_not_fail_if_insert_not_break_entities_constraints_test_01() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            val randomInsertedLanguageID = testModels.languages.random().id
            val randomInsertedUserID = testModels.users.random().id

            database.pictureInsertQueries.insert(
                id = 1,
                title = UUID.randomUUID().toString(),
                url = UUID.randomUUID().toString(),
                languageID = randomInsertedLanguageID.toInt(),
                userID = randomInsertedUserID,
                rating = Random.nextInt()
            )
        } catch (t: Throwable) {
            fail("Picture insert without breaking constraints must finish without exception")
        }
    }

    @Test
    fun inset_not_fail_if_insert_not_break_entities_constraints_test_02() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            val randomInsertedUserID = testModels.authors.random().id
            database.insertRelationBetweenPictureAndAuthor(
                pictureID = 1,
                authorID = randomInsertedUserID
            )
        } catch (t: Throwable) {
            fail("[Picture <-> Authors] insert without breaking constraints must finish without exception")
        }
    }

    @Test
    fun inset_not_fail_if_insert_not_break_entities_constraints_test_03() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            val randomInsertedLanguageID = testModels.languages.random().id
            database.insertRelationBetweenPictureAndLanguage(
                pictureID = 1,
                languageID = randomInsertedLanguageID.toInt()
            )
        } catch (t: Throwable) {
            fail("[Picture <-> Available language] insert without breaking constraints must finish without exception")
        }
    }

    @Test
    fun inset_not_fail_if_insert_not_break_entities_constraints_test_04() {
        val database = connectToDatabase()
        val testModels = database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)

        try {
            val randomInsertedTagID = testModels.tags.random().id
            database.insertRelationBetweenPictureAndTag(
                pictureID = 1,
                tagID = randomInsertedTagID.toInt()
            )
        } catch (t: Throwable) {
            fail("[Picture <-> Tag] insert without breaking constraints must finish without exception")
        }
    }

    @Test
    fun delete_constraint_relation_if_picture_deleted_test() {
        val database = connectToDatabase()
        database.fillPartiallyByGeneratedContent()

        database.insertIntoContentRegisterForPictureWithID(1)
        database.insertIntoPictureBaseInfoWithID(1)
        database.insertRelationBetweenPictureAndAuthor(1, 1)
        database.insertRelationBetweenPictureAndAuthor(1, 2)
        database.insertRelationBetweenPictureAndAuthor(1, 3)

        database.insertRelationBetweenPictureAndLanguage(1, 1)
        database.insertRelationBetweenPictureAndLanguage(1, 2)
        database.insertRelationBetweenPictureAndLanguage(1, 3)

        database.deleteFromContentRegisterByID(1)

        val isRemovedRecordStillExist = database.isRecordInContentRegisterExist(1)
        if (isRemovedRecordStillExist) fail("Content register record not deleted")

        val isPictureRecordStillExist = database.isRecordInPicturesExist(1)
        if (isPictureRecordStillExist) fail("Picture record not deleted, but it's parent did")

        val isRelationsBetweenPictureAndAuthorStillExist =
            database.isAnyRecordInRelationsBetweenPictureAndAuthorExist(1)
        if (isRelationsBetweenPictureAndAuthorStillExist) fail("Picture record deleted, but it's relation with authors not")

        val isRelationsBetweenPictureAndLanguageStillExist =
            database.isAnyRecordInRelationsBetweenPictureAndLanguageExist(1)
        if (isRelationsBetweenPictureAndLanguageStillExist) fail("Picture record deleted, but it's relation with language not")

        val isRelationsBetweenPictureAndTagStillExist = database.isAnyRecordInRelationsBetweenPictureAndTagExist(1)
        if (isRelationsBetweenPictureAndTagStillExist) fail("Picture record deleted, but it's relation with tag not")
    }

    private fun DBTest.insertIntoContentRegisterForPictureWithID(pictureID: Int) {
        contentRegisterQueries.insert(
            id = pictureID,
            contentType = 1,
            contentID = 1
        )
    }

    private fun DBTest.insertIntoPictureBaseInfoWithID(pictureID: Int) {
        pictureInsertQueries.insert(
            id = pictureID,
            title = UUID.randomUUID().toString(),
            url = UUID.randomUUID().toString(),
            languageID = 1,
            userID = 1,
            rating = 1
        )
    }

    private fun DBTest.insertRelationBetweenPictureAndAuthor(pictureID: Int, authorID: Int) {
        relationPictureAndAuthorInsertQueries.insert(
//            id = null,
            pictureID = pictureID,
            authorID = authorID
        )
    }

    private fun DBTest.insertRelationBetweenPictureAndLanguage(pictureID: Int, languageID: Int) {
        relationPictureAndLanguageInsertQueries.insert(
            id = null,
            pictureID = pictureID,
            langID = languageID
        )
    }

    private fun DBTest.insertRelationBetweenPictureAndTag(pictureID: Int, tagID: Int) {
        relationPictureAndTagInsertQueries.insert(
            id = null,
            pictureID = pictureID,
            tagID = tagID.toShort()
        )
    }

    private fun DBTest.isRecordInPicturesExist(pictureID: Int): Boolean {
        return pictureOtherQueries.checkIfRecordExist(pictureID).executeAsOneOrNull() != null
    }

    private fun DBTest.isAnyRecordInRelationsBetweenPictureAndAuthorExist(pictureID: Int): Boolean {
        return relationPictureAndAuthorOtherQueries
            .checkIfAnyRecordsExist(pictureID)
            .executeAsList()
            .isNotEmpty()
    }

    private fun DBTest.isAnyRecordInRelationsBetweenPictureAndLanguageExist(pictureID: Int): Boolean {
        return relationPictureAndLanguageOtherQueries
            .checkIfAnyRecordsExist(pictureID)
            .executeAsList()
            .isNotEmpty()
    }

    private fun DBTest.isAnyRecordInRelationsBetweenPictureAndTagExist(pictureID: Int): Boolean {
        return relationPictureAndTagOtherQueries
            .checkIfAnyRecordsExist(pictureID)
            .executeAsList()
            .isNotEmpty()
    }
}