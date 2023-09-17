package database.external.result

/** Result used for simple DB list type methods. */
sealed interface SimpleListResult<T> {

    data class Data<T>(val data: List<T>) : SimpleListResult<T>

    data class Error<T>(val t: Throwable) : SimpleListResult<T>
}