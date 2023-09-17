package database.external.result

/** @see database.external.DatabaseAPI.isUserCredentialsCorrect */
sealed interface CheckUserCredentialsResult {

    class Ok(val userID: Int) : CheckUserCredentialsResult

    data object UserNotExist : CheckUserCredentialsResult

    data object PasswordMismatch : CheckUserCredentialsResult

    class UnexpectedError(val t: Throwable) : CheckUserCredentialsResult
}