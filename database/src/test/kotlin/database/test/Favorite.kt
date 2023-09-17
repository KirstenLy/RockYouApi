package database.test

import common.takeRandomValues
import common.toMap
import common.toMapWithListValues
import database.external.test.TestContentRegister
import database.external.test.TestUser
import database.internal.creator.test.connectToDatabase
import database.internal.test.insertDefaultUsers
import database.internal.test.insertRandomContentRegisters
import kotlin.test.Test
import kotlin.test.fail

internal class FavoriteTest {

    @Test
    fun select_from_empty_database_not_fail() {
        val database = connectToDatabase()

        val allUserFavoriteRecords = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allUserFavoriteRecords.isEmpty()) { "Unexpected records when database is empty" }
    }

    @Test
    fun simple_add_to_favorite() {
        val database = connectToDatabase()

        val users = database.insertDefaultUsers()
        val contentRegisters = database.insertRandomContentRegisters()

        // Select random user
        val userIDToInsertFavoriteRecords = users.random().id

        // Select random content to insert as random user's favorite
        val contentIDToInsert = contentRegisters.random().id

        // Insert as user try to add content to favorite
        database.insertFavoriteQueries.insert(
            id = null,
            userID = userIDToInsertFavoriteRecords,
            contentID = contentIDToInsert
        )

        val allUserFavoriteRecords = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allUserFavoriteRecords.size == 1) { "Unexpected record size after insert" }
        assert(allUserFavoriteRecords.first().contentID == contentIDToInsert) { "Invalid record by content id" }
        assert(allUserFavoriteRecords.first().userID == userIDToInsertFavoriteRecords) { "Invalid record by user id" }
    }

    @Test
    fun simple_remove_from_favorite() {
        val database = connectToDatabase()

        val users = database.insertDefaultUsers()
        val contentRegisters = database.insertRandomContentRegisters()

        // Select random user
        val userIDToInsertFavoriteRecords = users.random().id

        // Select random content to insert as random user's favorite
        val contentIDToInsert = contentRegisters.random().id

        // Insert as user try to add content to favorite
        database.insertFavoriteQueries.insert(
            id = null,
            userID = userIDToInsertFavoriteRecords,
            contentID = contentIDToInsert
        )

        val allFavoriteRecordsAfterInsert = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allFavoriteRecordsAfterInsert.size == 1) { "Unexpected record size after insert" }
        assert(allFavoriteRecordsAfterInsert.first().contentID == contentIDToInsert) { "Invalid record by content id" }
        assert(allFavoriteRecordsAfterInsert.first().userID == userIDToInsertFavoriteRecords) { "Invalid record by user id" }

        // Delete as user decide to remove content from favorite
//        database.favoriteProdQueries.removeByUserIDAndContentID(
//            userID = userIDToInsertFavoriteRecords,
//            contentID = contentIDToInsert
//        )

        val allRecordsAfterRemove = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allRecordsAfterRemove.isEmpty()) { "Favorite record remove not happen, but expected" }
    }

    @Test
    fun if_user_removed_all_his_favorite_records_removed() {
        val database = connectToDatabase()

        val users = database.insertDefaultUsers()
        val contentRegisters = database.insertRandomContentRegisters()

        // Select random user
        val userIDToInsertFavoriteRecords = users.random().id

        // Select random contents to insert as random user's favorite
        val contentRegisterIDsToInsertAsFavorite = contentRegisters.takeRandomValues(3).map(TestContentRegister::id)

        // Insert as user try to add contents to favorite
        contentRegisterIDsToInsertAsFavorite.forEach {
            database.insertFavoriteQueries.insert(
                id = null,
                userID = userIDToInsertFavoriteRecords,
                contentID = it
            )
        }

        val allFavoriteRecordsAfterInsert = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allFavoriteRecordsAfterInsert.size == contentRegisterIDsToInsertAsFavorite.size) {
            "Unexpected record size after insert"
        }

        // Remove user. We expect that cascade will remove all his favorite records
        database.userTestQueries.removeUserByID(userIDToInsertFavoriteRecords)

        val allRecordsAfterUserRemoved = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allRecordsAfterUserRemoved.isEmpty()) { "Favorite record remove not happen, but expected" }
    }

    @Test
    fun if_content_removed_all_associated_favorite_records_removed() {
        val database = connectToDatabase()

        val users = database.insertDefaultUsers()
        val contentRegisters = database.insertRandomContentRegisters()

        // Select random user
        val userIDToInsertFavoriteRecords = users.random().id

        // Select random contents to insert as random user's favorite
        val contentRegisterIDsToInsertAsFavorite = contentRegisters.takeRandomValues(3).map(TestContentRegister::id)

        // Insert as user try to add contents to favorite
        contentRegisterIDsToInsertAsFavorite.forEach {
            database.insertFavoriteQueries.insert(
                id = null,
                userID = userIDToInsertFavoriteRecords,
                contentID = it
            )
        }

        val allFavoriteRecordsAfterInsert = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allFavoriteRecordsAfterInsert.size == contentRegisterIDsToInsertAsFavorite.size) {
            "Unexpected record size after insert"
        }

        // Select one contentID to remove
        val contentIDToRemove = contentRegisterIDsToInsertAsFavorite.random()

        // Remove one content record. All favorite records associated with it must be removed too.
        database.contentRegisterQueries.removeContentByID(contentIDToRemove)

        val allRecordsAfterContentRemoved = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        val isRecordWithRemovedContentIDDeleted = allRecordsAfterContentRemoved.none {
            it.contentID == contentIDToRemove
        }
        assert(isRecordWithRemovedContentIDDeleted) { "Favorite record remove not happen, but expected" }
    }

    @Test
    fun if_user_id_updated_all_his_favorite_records_updated() {
        val database = connectToDatabase()

        val users = database.insertDefaultUsers()
        val contentRegisters = database.insertRandomContentRegisters()

        // Select random user
        val userIDToInsertFavoriteRecords = users.random().id

        // Select random contents to insert as random user's favorite
        val contentRegisterIDsToInsertAsFavorite = contentRegisters.takeRandomValues(3).map(TestContentRegister::id)

        // Insert as user try to add contents to favorite
        contentRegisterIDsToInsertAsFavorite.forEach {
            database.insertFavoriteQueries.insert(
                id = null,
                userID = userIDToInsertFavoriteRecords,
                contentID = it
            )
        }

        val allFavoriteRecordsAfterInsert = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allFavoriteRecordsAfterInsert.size == contentRegisterIDsToInsertAsFavorite.size) {
            "Unexpected record size after insert"
        }

        // Update user by change his ID. All his favorite records must link to new userID by cascade
        database.userTestQueries.updateUserID(
            oldID = userIDToInsertFavoriteRecords,
            newID = 999
        )

        val allRecordsAfterUserUpdated = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        assert(allRecordsAfterUserUpdated.count { it.userID == 999 } == contentRegisterIDsToInsertAsFavorite.size) {
            "User updated, but seems like his favorite is not updated"
        }
    }

    @Test
    fun if_content_id_updated_all_associated_favorite_records_updated() {
        val database = connectToDatabase()

        val users = database.insertDefaultUsers()
        val contentRegisters = database.insertRandomContentRegisters()

        // Select several random users
        val userIDsToInsertFavoriteRecords = users.takeRandomValues(3).map(TestUser::id)

        // Associate every user with random contents to insert it as user's favorite
        val userIDsWithSomeContent = userIDsToInsertFavoriteRecords.associateWith {
            contentRegisters.takeRandomValues(3).map(TestContentRegister::id)
        }

        // Insert as every user try to add some contents to favorite
        userIDsWithSomeContent.forEach { (userID, userFavoriteContentRegisterIDs) ->
            userFavoriteContentRegisterIDs.forEach { registerID ->
                database.insertFavoriteQueries.insert(
                    id = null,
                    userID = userID,
                    contentID = registerID
                )
            }
        }

        // Read all favorite records for all users
        val allFavoriteRecordsAfterInsert = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        // Group favorite records contentIDs by users
        val insertedRecordsGroupedByUsers = allFavoriteRecordsAfterInsert.toMapWithListValues(
            keyTransformer = { it.userID },
            valueTransformer = { it.contentID }
        )

        assert(insertedRecordsGroupedByUsers.size == userIDsToInsertFavoriteRecords.size) {
            "Seems like records not inserted for some users"
        }

        // Check is all users has all his favorite history inserted
        insertedRecordsGroupedByUsers.forEach { (userID, userFavoriteContentRegisterIDs) ->
            val expectedForUser = userIDsWithSomeContent[userID] ?: fail()
            assert(expectedForUser == userFavoriteContentRegisterIDs)
        }

        // Pick random contentID from random favorite record to update it.
        // After contentID updated, we expect that all related favorite history will change by cascade
        val registerRecordIDToReplaceID = insertedRecordsGroupedByUsers
            .map { it.value }
            .flatten()
            .random()

        // Remember records count with contentID == registerRecordIDToReplaceID to compare it after contentID will be updated
        val registerRecordsCountForOldContentID = insertedRecordsGroupedByUsers.map { it.value }
            .flatten()
            .count { it == registerRecordIDToReplaceID }

        // Update contentID
        database.contentRegisterQueries.replaceRegisterID(
            oldRegisterID = registerRecordIDToReplaceID,
            newRegisterID = 999
        )

        // Read all favorite records for all users
        val allRecordsAfterContentIDUpdated = database.selectFavoriteQueries
            .selectAll()
            .executeAsList()

        // Check is cascade update work correct
        val isNoRecordWithOldContentID = allRecordsAfterContentIDUpdated.none {
            it.contentID == registerRecordIDToReplaceID
        }

        assert(isNoRecordWithOldContentID) { "ContentID was updated, but favorite records for it still exist" }

        val recordsWithUpdatedIDCount = allRecordsAfterContentIDUpdated.count {
            it.contentID == 999
        }

        assert(recordsWithUpdatedIDCount == registerRecordsCountForOldContentID) {
            "ContentID was updated, but some favorite records still linked on old contentID, or not exist"
        }
    }

    @Test
    fun insert_fail_if_try_with_not_existed_user() {
        val database = connectToDatabase()
        val contentRegisters = database.insertRandomContentRegisters()

        try {
            // It must fail because userID if foreign key related to records into Users table
            database.insertFavoriteQueries.insert(
                id = null,
                userID = 99999,
                contentID = contentRegisters.random().id
            )
        } catch (t: Throwable) {
            return
        }
        fail("Insert fail expected, but not happen")
    }

    @Test
    fun insert_fail_if_try_with_not_existed_content_id() {
        val database = connectToDatabase()
        val users = database.insertDefaultUsers()

        try {
            // It must fail because contentID if foreign key related to records into ContentRegister table
            database.insertFavoriteQueries.insert(
                id = null,
                userID = users.random().id,
                contentID = 999999
            )
        } catch (t: Throwable) {
            return
        }
        fail("Insert fail expected, but not happen")
    }

    @Test
    fun insert_fail_if_try_with_already_existed_data() {
        val database = connectToDatabase()

        database.insertDefaultUsers()
        database.insertRandomContentRegisters()

        database.insertFavoriteQueries.insert(
            id = null,
            userID = 1,
            contentID = 1
        )

        try {
            // It must fail because (userID + contentID) is Index
            database.insertFavoriteQueries.insert(
                id = null,
                userID = 1,
                contentID = 1
            )
        } catch (t: Throwable) {
            return
        }
        fail("Insert fail expected, but not happen")
    }

    @Test
    fun select_all_content_ids_by_user_work_correct() {
        val database = connectToDatabase()

        val users = database.insertDefaultUsers()
        val contentRegisters = database.insertRandomContentRegisters()

        // Take several random users ids and associate every user with some random content
        val randomUsersWithData = users
            .takeRandomValues(3)
            .map(TestUser::id)
            .toMap(
                keyTransformer = { it },
                valueTransformer = { contentRegisters.takeRandomValues(3).map(TestContentRegister::id) }
            )

        // Insert as every user try to add some contents to favorite
        randomUsersWithData.forEach { (userID, contentRegisterIDsToInsertAsFavorite) ->
            contentRegisterIDsToInsertAsFavorite.forEach { contentRegisterID ->
                database.insertFavoriteQueries.insert(
                    id = null,
                    userID = userID,
                    contentID = contentRegisterID
                )
            }
        }

        // Pick a random user to read his favorite content from the database
        val (randomUserID, randomUserFavoriteContentIDs) = randomUsersWithData.toList().random()

//        // Read random user favorite content
//        val userFavoriteRecordList = database.favoriteProdQueries
//            .selectFavoriteRegisterIDsByUserID(randomUserID)
//            .executeAsList()
//
//        assert(userFavoriteRecordList.isNotEmpty()) { "User's actual favorite empty, several records expected" }
//
//        val isFavoriteRecordFromDatabaseAsExpected = userFavoriteRecordList == randomUserFavoriteContentIDs.reversed()
//        assert(isFavoriteRecordFromDatabaseAsExpected) { "User's expected favorite and actual favorite are not match" }
    }
}