package database.external.result.common

/** Result used for simple scenarios that return list of models. */
sealed interface SimpleListResult<T> {

    /** Success. */
    data class Data<T>(val data: List<T>) : SimpleListResult<T>

    /** Error. */
    data class Error<T>(val t: Throwable) : SimpleListResult<T>
}