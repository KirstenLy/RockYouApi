package database.internal.executor

import database.external.result.CheckUserCredentialsResult
import database.internal.executor.common.selectUserByLogin

@Deprecated("LoginUserRequestExecutor")
internal class CheckUserCredentialsRequestExecutor {

    fun execute(login: String, password: String): CheckUserCredentialsResult {
        return try {
            val userData = selectUserByLogin(login) ?: return CheckUserCredentialsResult.UserNotExist

            if (userData.password != password) {
                return CheckUserCredentialsResult.PasswordMismatch
            }
            return CheckUserCredentialsResult.UserNotExist
        } catch (t: Throwable) {
            CheckUserCredentialsResult.UnexpectedError(t)
        }
    }
}