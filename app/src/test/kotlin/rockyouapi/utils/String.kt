package rockyouapi.utils

import kotlinx.serialization.json.Json

internal inline fun <reified T> String.decodeAs() = Json.decodeFromString<T>(this)

internal fun String?.isNullOrInt(): Boolean = this == null || (toIntOrNull() != null)

internal fun String?.isNullOrNonNegativeInt(): Boolean =
    this == null || (toIntOrNull()?.takeIf { it >= 0 } != null)

internal fun String?.isNonNegativeInt(): Boolean =
    this != null && (toIntOrNull()?.takeIf { it >= 0 } != null)

internal inline fun <reified T> String?.isNullOrJsonOf() = this == null || isValidJson<T>()