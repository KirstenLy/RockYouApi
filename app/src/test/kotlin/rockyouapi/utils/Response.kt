package rockyouapi.utils

import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals

/** Check is response code [HttpStatusCode.OK] and fail if not. */
internal fun HttpResponse.okOrFail(message: String? = null): HttpResponse {
    message
        ?.let { assertEquals(HttpStatusCode.OK, status, it) }
        ?: assertEquals(HttpStatusCode.OK, status)
    return this
}

/** Check is response code [HttpStatusCode.NotFound] and fail if not. */
internal fun HttpResponse.notFoundOrFail(): HttpResponse {
    assertEquals(HttpStatusCode.NotFound, status)
    return this
}

/** Check is response code [HttpStatusCode.BadRequest] and fail if not. */
internal fun HttpResponse.badRequestOrFail(message: String? = null): HttpResponse {
    message
        ?.let { assertEquals(HttpStatusCode.BadRequest, status, it) }
        ?: assertEquals(HttpStatusCode.BadRequest, status)
    return this
}

/** Check is response code [HttpStatusCode.Unauthorized] and fail if not. */
internal fun HttpResponse.unauthorizedOrFail(): HttpResponse {
    assertEquals(HttpStatusCode.Unauthorized, status)
    return this
}

/** Check is response code [HttpStatusCode.Conflict] and fail if not. */
internal fun HttpResponse.conflictOrFail(): HttpResponse {
    assertEquals(HttpStatusCode.Conflict, status)
    return this
}

/** Decode response by JSON as T.*/
internal suspend inline fun <reified T> HttpResponse.decodeAs() = Json.decodeFromString<T>(bodyAsText())