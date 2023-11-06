package database.external.result

/** @see database.external.contract.ProductionDatabaseAPI.checkUserCredentials */
sealed interface CheckUserCredentialsResult {

    /** Login and password matches. */
    data object Ok : CheckUserCredentialsResult

    /** User not exists. */
    data object UserNotExist : CheckUserCredentialsResult

    /** Password not match with user. */
    data object PasswordMismatch : CheckUserCredentialsResult

    /** Error. */
    class Error(val t: Throwable) : CheckUserCredentialsResult
}