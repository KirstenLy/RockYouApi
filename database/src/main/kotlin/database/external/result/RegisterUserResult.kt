package database.external.result

import declaration.entity.User

/** @see database.external.DatabaseAPI.register */
sealed interface RegisterUserResult {

    class Ok(val user: User) : RegisterUserResult

    data object SameUserAlreadyExist : RegisterUserResult

    class UnexpectedError(val t: Throwable) : RegisterUserResult
}