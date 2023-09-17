package database.internal.contract

/**
 * Contract for filters with pagination support.
 * @see database.internal.utils.applyLimitableIfNeeded
 * */
internal interface Limitable {
    val limit: Int?
    val offset: Long?
}