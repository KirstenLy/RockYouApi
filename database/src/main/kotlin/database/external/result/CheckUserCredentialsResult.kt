package database.external.result

import declaration.entity.User

/** @see database.external.DatabaseAPI.checkUserCredentials */
sealed interface CheckUserCredentialsResult {

    class Ok(val user: User) : CheckUserCredentialsResult

    data object UserNotExist : CheckUserCredentialsResult

    data object PasswordMismatch : CheckUserCredentialsResult

    class UnexpectedError(val t: Throwable) : CheckUserCredentialsResult
}