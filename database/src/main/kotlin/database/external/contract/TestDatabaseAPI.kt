package database.external.contract

import database.external.ContentType
import database.external.model.language.LanguageFull
import database.external.model.Comment
import database.external.model.test.FavoriteRecord
import database.external.model.test.ReportRecord
import migrations.VoteHistory
import org.jetbrains.annotations.TestOnly

/**
 * Contract of the database that realizes additional test-only scenarios.
 * Used by external modules to test their functionality.
 * */
@TestOnly
interface TestDatabaseAPI {

    /** Read all reports. */
    suspend fun readAllReports(): List<ReportRecord>

    /** Get random user name from database. */
    suspend fun getRandomUserName(): String

    /** Get user refresh token. */
    suspend fun getUserRefreshToken(userID: Int): String?

    /** Get all content id. */
    suspend fun getAllContentID(): List<Int>

    /** Get random content id. */
    suspend fun getRandomContentID(): Int

    /** Get random content id list sized by [size]. */
    suspend fun getRandomContentIDList(size: Long): List<Int>

    /** Get random content id by [ContentType]. */
    suspend fun getRandomContentIDForEntity(contentType: ContentType): Int

    /** Get content rating. */
    suspend fun getContentRating(contentID: Int): Int

    /** Get all comments for content. */
    suspend fun getAllCommentForContent(contentID: Int): List<Comment>

    /** Get all vote history.*/
    suspend fun getAllVoteHistory(): List<VoteHistory>

    /** Get all favorite records.*/
    suspend fun getAllFavoriteRecord(): List<FavoriteRecord>

    /** Get all supported language ID.*/
    suspend fun getAllSupportedLanguageID(): List<Int>

    /** Get environmentID info. */
    suspend fun getAllSupportedLanguage(): List<LanguageFull>

    /** Get all supported language ID.*/
    suspend fun getNotExistedSupportedLanguageIDList(): List<Int?>

    /** Get chapter text. */
    suspend fun getChapterTextID(chapterID: Int): String
}