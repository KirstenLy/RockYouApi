package database.external.result.common

/** Result used for simple scenarios that return [Boolean]. */
sealed interface SimpleBooleanResult {

    /** SelfDocumented. */
    data object True : SimpleBooleanResult

    /** SelfDocumented. */
    data object False : SimpleBooleanResult

    /** SelfDocumented. */
    data class Error(val t: Throwable) : SimpleBooleanResult
}