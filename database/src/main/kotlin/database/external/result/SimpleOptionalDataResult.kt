package database.external.result

/** Result used for simple DB read model type methods. */
sealed interface SimpleOptionalDataResult<T> {

    data class Data<T>(val model: T) : SimpleOptionalDataResult<T>

    class DataNotFounded<T> : SimpleOptionalDataResult<T>

    data class Error<T>(val t: Throwable) : SimpleOptionalDataResult<T>
}