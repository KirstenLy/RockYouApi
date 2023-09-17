package database.internal.executor

import database.external.result.CheckUserCredentialsResult
import database.internal.executor.common.selectUserByLogin

internal class CheckUserCredentialsRequestExecutor {

    fun execute(login: String, password: String): CheckUserCredentialsResult {
        return try {
            val userData = selectUserByLogin(login) ?: return CheckUserCredentialsResult.UserNotExist

            if (userData.password != password) {
                return CheckUserCredentialsResult.PasswordMismatch
            }
            return CheckUserCredentialsResult.Ok(userData.id.value)
        } catch (t: Throwable) {
            CheckUserCredentialsResult.UnexpectedError(t)
        }
    }
}