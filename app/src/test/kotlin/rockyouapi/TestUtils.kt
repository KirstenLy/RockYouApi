package rockyouapi

import common.removeWhitespaces
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

internal suspend fun delayByTokenLifetimeAndOneSecAhead() {
    delay(TIME_TEST_TOKEN_LIFETIME_IN_SEC.seconds.plus(1.seconds))
}

internal suspend inline fun <reified T> HttpResponse.decodeAs() = Json.decodeFromString<T>(bodyAsText())

internal fun URLBuilder.appendToParameters(key: String?, name: String) {
    key?.let { parameters.append(name, it) }
}