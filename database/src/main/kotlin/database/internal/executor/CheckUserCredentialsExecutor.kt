package database.internal.executor

import at.favre.lib.crypto.bcrypt.BCrypt
import database.external.result.CheckUserCredentialsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database

/** @see execute */
internal class CheckUserCredentialsExecutor(private val database: Database) {

    /**
     * Check match between user and password.
     *
     * Respond as:
     * - [CheckUserCredentialsResult.Ok] User and password matched.
     * - [CheckUserCredentialsResult.UserNotExist] User not found by [login].
     * - [CheckUserCredentialsResult.PasswordMismatch] User password not matched with [password].
     * - [CheckUserCredentialsResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(login: String, password: String): CheckUserCredentialsResult {
        return withContext(Dispatchers.IO) {
            try {
                val existedUserID = database.selectUserQueries
                    .selectOneIDByName(login)
                    .executeAsOneOrNull()
                    ?: return@withContext CheckUserCredentialsResult.UserNotExist

                val existedUserPasswordHash = database.selectAuthQueries
                    .selectOnePasswordHashByUserID(existedUserID)
                    .executeAsOne()

                val isUserPasswordMatches = BCrypt.verifyer()
                    .verify(password.toCharArray(), existedUserPasswordHash)
                    .verified

                if (!isUserPasswordMatches) {
                    return@withContext CheckUserCredentialsResult.PasswordMismatch
                }

                return@withContext CheckUserCredentialsResult.Ok
            } catch (t: Throwable) {
                return@withContext CheckUserCredentialsResult.Error(t)
            }
        }
    }
}