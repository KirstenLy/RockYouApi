package database.internal.creator

import database.external.contract.TestDatabaseAPI
import database.internal.TestDatabaseAPIImpl
import database.internal.executor.*
import rockyouapi.Database

/** [TestDatabaseAPI] creator */
internal fun createProductionDatabaseWithTestAPI(database: Database): TestDatabaseAPI {
    return TestDatabaseAPIImpl(
        readAllReportsExecutor = ReadAllReportsExecutor(database),
        readRandomUserNameExecutor = ReadRandomUserNameExecutor(database),
        getUserRefreshTokenExecutor = GetUserRefreshTokenExecutor(database),
        readRandomContentIDExecutor = ReadRandomContentIDExecutor(database),
        readContentRatingExecutor = ReadContentRatingExecutor(database),
        readAllContentIDExecutor = ReadAllContentIDExecutor(database),
        readRandomContentIDListExecutor = ReadRandomContentIDListExecutor(database),
        readAllCommentForContentExecutor = ReadAllCommentForContentExecutor(database),
        readAllVoteHistoryExecutor = ReadAllVoteHistoryExecutor(database),
        readAllFavoriteExecutor = ReadAllFavoriteExecutor(database),
        readAllSupportedLanguageIDExecutor = ReadAllSupportedLanguageIDExecutor(database),
        readRandomContentIDForEntityExecutor = ReadRandomContentIDForEntityExecutor(database),
        readChapterTextByIDForTestExecutor = ReadChapterTextByIDForTestExecutor(database)
    )
}