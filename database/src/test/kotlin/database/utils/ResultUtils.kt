package database.utils

import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.external.result.SimpleUnitResult
import kotlin.test.fail

internal fun SimpleUnitResult.asOkOrFailed() = (this as? SimpleUnitResult.Ok) ?: fail(
    "Incorrect result. Actual result: $this. Expected result: Ok"
)

internal fun SimpleUnitResult.asErrorOrFailed() = (this as? SimpleUnitResult.Error) ?: fail(
    "Incorrect result. Actual result: $this. Expected result: Error"
)

internal fun <T> SimpleListResult<T>.extractDataOrFail() = (this as? SimpleListResult.Data)?.data ?: fail(
    "Incorrect result. Actual result: $this. Expected result: Data"
)

internal fun <T> SimpleOptionalDataResult<T>.extractModelOrFail() =
    (this as? SimpleOptionalDataResult.Data)?.model ?: fail(
        "Incorrect result. Actual result: $this. Expected result: Data"
    )

internal fun <T> SimpleOptionalDataResult<T>.asDataNotFoundOrFail() {
    (this as? SimpleOptionalDataResult.DataNotFounded) ?: fail(
        "Incorrect result. Actual result: $this. Expected result: Data not found"
    )
}