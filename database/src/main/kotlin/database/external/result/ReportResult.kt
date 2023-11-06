package database.external.result

/** @see database.external.contract.ProductionDatabaseAPI.report */
sealed interface ReportResult {

    /** Content reported. */
    data object Ok : ReportResult

    /** Reporter not exists. */
    data object UserNotExist : ReportResult

    /** Try to report not existed content. */
    data object ContentNotExist : ReportResult

    /** Error. */
    class Error(val t: Throwable) : ReportResult
}