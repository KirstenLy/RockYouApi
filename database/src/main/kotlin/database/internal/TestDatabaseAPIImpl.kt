package database.internal

import database.external.ContentType
import database.external.contract.TestDatabaseAPI
import database.external.model.language.LanguageFull
import database.external.model.Comment
import database.internal.executor.*
import database.internal.mock.allLanguagesAsSupportedLanguages
import migrations.VoteHistory

/** @see [TestDatabaseAPI]. */
internal class TestDatabaseAPIImpl(
    private val readAllReportsExecutor: ReadAllReportsExecutor,
    private val readRandomUserNameExecutor: ReadRandomUserNameExecutor,
    private val getUserRefreshTokenExecutor: GetUserRefreshTokenExecutor,
    private val readRandomContentIDExecutor: ReadRandomContentIDExecutor,
    private val readRandomContentIDForEntityExecutor: ReadRandomContentIDForEntityExecutor,
    private val readContentRatingExecutor: ReadContentRatingExecutor,
    private val readAllContentIDExecutor: ReadAllContentIDExecutor,
    private val readAllCommentForContentExecutor: ReadAllCommentForContentExecutor,
    private val readRandomContentIDListExecutor: ReadRandomContentIDListExecutor,
    private val readAllVoteHistoryExecutor: ReadAllVoteHistoryExecutor,
    private val readAllFavoriteExecutor: ReadAllFavoriteExecutor,
    private val readAllSupportedLanguageIDExecutor: ReadAllSupportedLanguageIDExecutor,
    private val readChapterTextByIDForTestExecutor: ReadChapterTextByIDForTestExecutor
) : TestDatabaseAPI {

    override suspend fun readAllReports(): List<database.external.model.test.ReportRecord> {
        return readAllReportsExecutor.execute()
    }

    override suspend fun getRandomUserName(): String {
        return readRandomUserNameExecutor.execute()
    }

    override suspend fun getUserRefreshToken(userID: Int): String? {
        return getUserRefreshTokenExecutor.execute(userID)
    }

    override suspend fun getAllContentID(): List<Int> {
        return readAllContentIDExecutor.execute()
    }

    override suspend fun getRandomContentID(): Int {
        return readRandomContentIDExecutor.execute()
    }

    override suspend fun getRandomContentIDForEntity(contentType: ContentType): Int {
        return readRandomContentIDForEntityExecutor.execute(contentType)
    }

    override suspend fun getRandomContentIDList(size: Long): List<Int> {
        return readRandomContentIDListExecutor.execute(size)
    }

    override suspend fun getContentRating(contentID: Int): Int {
        return readContentRatingExecutor.execute(contentID)
    }

    override suspend fun getAllCommentForContent(contentID: Int): List<Comment> {
        return readAllCommentForContentExecutor.execute(contentID)
    }

    override suspend fun getAllVoteHistory(): List<VoteHistory> {
        return readAllVoteHistoryExecutor.execute()
    }

    override suspend fun getAllFavoriteRecord(): List<database.external.model.test.FavoriteRecord> {
        return readAllFavoriteExecutor.execute()
    }

    override suspend fun getAllSupportedLanguageID(): List<Int> {
        return readAllSupportedLanguageIDExecutor.execute()
    }

    override suspend fun getAllSupportedLanguage(): List<LanguageFull> {
        return allLanguagesAsSupportedLanguages
    }

    override suspend fun getNotExistedSupportedLanguageIDList(): List<Int?> {
        return listOf(null, -1, 100000)
    }

    override suspend fun getChapterTextID(chapterID: Int): String {
        return readChapterTextByIDForTestExecutor.execute(chapterID)
    }
}