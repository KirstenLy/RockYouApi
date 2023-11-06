package database.external.result

import database.external.model.user.UserFull
import database.external.model.user.UserRole

/** @see database.external.contract.ProductionDatabaseAPI.register */
sealed interface RegisterUserResult {

    /** User registered. */
    class Ok(val userSimple: UserFull) : RegisterUserResult

    /** User with same login already exists. */
    data object SameUserAlreadyExist : RegisterUserResult

    /** Attempt to create user with unknown role. Role list: [UserRole] */
    data object UnknownUserRole : RegisterUserResult

    /** Error. */
    class Error(val t: Throwable) : RegisterUserResult
}