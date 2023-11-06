package database.utils

import database.external.result.*
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleOptionalDataResult
import database.external.result.common.SimpleUnitResult
import org.junit.jupiter.api.Assertions


// SimpleUnitResult
internal fun SimpleUnitResult.asOkOrFailed() {
    Assertions.assertInstanceOf(SimpleUnitResult.Ok::class.java, this)
}

internal fun SimpleUnitResult.asErrorOrFailed() {
    Assertions.assertInstanceOf(SimpleUnitResult.Error::class.java, this)
}


// SimpleDataResult
@Suppress("UNCHECKED_CAST")
internal fun <T> SimpleDataResult<T>.asDataOrFailed() = Assertions
    .assertInstanceOf(SimpleDataResult.Data::class.java, this)
    .data as T

internal fun <T> SimpleDataResult<T>.asErrorOrFailed() {
    Assertions.assertInstanceOf(SimpleDataResult.Error::class.java, this)
}


// SimpleListResult
@Suppress("UNCHECKED_CAST")
internal fun <T> SimpleListResult<T>.extractDataOrFail(messageCreatorOnFail: ((result: SimpleListResult<T>) -> String)? = null): List<T> {
    if (messageCreatorOnFail != null) {
        return Assertions
            .assertInstanceOf(SimpleListResult.Data::class.java, this, messageCreatorOnFail(this))
            .data as List<T>
    }

    return Assertions
        .assertInstanceOf(SimpleListResult.Data::class.java, this)
        .data as List<T>
}

internal fun <T> SimpleListResult<T>.asErrorOrFailed(message: String? = null) {
    message
        ?.let { Assertions.assertInstanceOf(SimpleListResult.Error::class.java, this, it) }
        ?: Assertions.assertInstanceOf(SimpleListResult.Error::class.java, this)
}

internal fun <T> SimpleListResult<T>.isError(): Boolean {
    return this is SimpleListResult.Error
}


// SimpleOptionalDataResult
@Suppress("UNCHECKED_CAST")
internal fun <T> SimpleOptionalDataResult<T>.extractModelOrFail() = Assertions
    .assertInstanceOf(SimpleOptionalDataResult.Data::class.java, this)
    .model as T

internal fun <T> SimpleOptionalDataResult<T>.asDataNotFoundOrFail() {
    Assertions.assertInstanceOf(SimpleOptionalDataResult.DataNotFounded::class.java, this)
}


// Executors
internal fun GetAllLanguageWithTranslationsResult.asDataOrFail() = Assertions
    .assertInstanceOf(GetAllLanguageWithTranslationsResult.Data::class.java, this)
    .languageFullList


internal fun GetAllTagsWithTranslationsResult.asDataOrFail() = Assertions
    .assertInstanceOf(GetAllTagsWithTranslationsResult.Data::class.java, this)
    .tagFullList