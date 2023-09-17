package database.external.result

/** @see database.external.DatabaseAPI.createUser */
sealed interface CreateUserResult {

    class Ok(val userID: Int) : CreateUserResult

    data object SameUserAlreadyExist : CreateUserResult

    class UnexpectedError(val t: Throwable) : CreateUserResult
}