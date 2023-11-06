package rockyouapi.utils

import kotlinx.serialization.json.Json

/** Check is string represent valid JSON. */
internal inline fun <reified T> String.isValidJson(): Boolean {
    return try {
        Json.decodeFromString<T>(this)
        true
    } catch (e: Exception) {
        false
    }
}