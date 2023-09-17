package database.internal.executor

import database.external.result.CheckUserCredentialsResult
import database.internal.entity.toDomain
import declaration.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.DBTest

/**
 * Check user's credentials. Return user model wrapped by [CheckUserCredentialsResult] if check passed.
 * Don't check login and password conditions, it is caller's task.
 * */
internal class CheckUserCredentialsExecutor(private val database: DBTest) {

    suspend fun execute(login: String, password: String): CheckUserCredentialsResult = withContext(Dispatchers.IO) {
        try {
            val user = database.userProdQueries
                .selectAllByName(login)
                .executeAsOneOrNull()
                ?: return@withContext CheckUserCredentialsResult.UserNotExist

            val userPassword = database.userAuthDataQueries
                .selectPasswordByUserID(user.id)
                .executeAsOneOrNull()
                ?: return@withContext CheckUserCredentialsResult.UserNotExist

            if (userPassword!= password) {
                return@withContext CheckUserCredentialsResult.PasswordMismatch
            }

            return@withContext CheckUserCredentialsResult.Ok(User(user.id, user.name))
        } catch (t: Throwable) {
            return@withContext CheckUserCredentialsResult.UnexpectedError(t)
        }
    }
}