package database.external.result

/** @see database.external.contract.ProductionDatabaseAPI.updateRefreshToken */
sealed interface UpdateRefreshTokenResult {

    /** Token updated or replaced. */
    data object Ok : UpdateRefreshTokenResult

    /** Token owner not exists. */
    data object UserNotExists : UpdateRefreshTokenResult

    /** Error. */
    class Error(val t: Throwable) : UpdateRefreshTokenResult
}