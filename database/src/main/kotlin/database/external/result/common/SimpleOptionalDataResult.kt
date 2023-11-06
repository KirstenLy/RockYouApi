package database.external.result.common

/** Result used for simple scenarios that return some optional model. */
sealed interface SimpleOptionalDataResult<T> {

    /** Success. */
    data class Data<T>(val model: T) : SimpleOptionalDataResult<T>

    /** Success, but data not found. */
    class DataNotFounded<T> : SimpleOptionalDataResult<T>

    /** Error. */
    data class Error<T>(val t: Throwable) : SimpleOptionalDataResult<T>
}