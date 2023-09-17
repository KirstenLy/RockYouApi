package database.external.result

/** Simple result used for simple DB methods. */
sealed interface SimpleUnitResult {

    data object Ok : SimpleUnitResult

    data class Error(val t: Throwable) : SimpleUnitResult
}