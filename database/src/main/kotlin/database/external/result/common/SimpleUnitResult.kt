package database.external.result.common

/** Result used for simple scenarios that finish without result. */
sealed interface SimpleUnitResult {

    /** Success. */
    data object Ok : SimpleUnitResult

    /** Error. */
    data class Error(val t: Throwable) : SimpleUnitResult
}