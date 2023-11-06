package database.internal.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.Database

/** @see execute */
@TestOnly
internal class GetUserRefreshTokenExecutor(private val database: Database) {

    /** Get user refresh token by [userID]. */
    suspend fun execute(userID: Int): String? = withContext(Dispatchers.IO) {
        database.selectAuthQueries
            .selectRefreshTokenByUserID(userID)
            .executeAsOneOrNull()
            ?.refreshToken
    }
}
