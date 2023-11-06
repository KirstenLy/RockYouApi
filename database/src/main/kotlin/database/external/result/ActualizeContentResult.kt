package database.external.result

/** @see database.external.contract.ProductionDatabaseAPI.createActualizeContentRequest */
sealed interface ActualizeContentResult {

    /** Content reported. */
    data object Ok : ActualizeContentResult

    /** Reporter not exists. */
    data object UserNotExist : ActualizeContentResult

    /** Try to report not existed content. */
    data object ContentNotExist : ActualizeContentResult

    /** Error. */
    class Error(val t: Throwable) : ActualizeContentResult
}