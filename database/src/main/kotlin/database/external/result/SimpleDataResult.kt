package database.external.result

/** Result used for simple DB read model type methods. */
sealed interface SimpleDataResult<T> {

    data class Data<T>(val data: T) : SimpleDataResult<T>

    data class Error<T>(val t: Throwable) : SimpleDataResult<T>
}