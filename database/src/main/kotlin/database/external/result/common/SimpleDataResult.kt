package database.external.result.common

/** Result used for simple scenarios that return some model. */
sealed interface SimpleDataResult<T> {

    /** Success. */
    data class Data<T>(val data: T) : SimpleDataResult<T>

    /** Error. */
    data class Error<T>(val t: Throwable) : SimpleDataResult<T>
}