package rockyouapi.utils

import io.ktor.http.*

internal inline fun Parameters.readNullableInt(argName: String, onArgumentNotIntError: () -> Int): Int? {
    val arg = this[argName] ?: return null
    return arg.toIntOrNull() ?: return onArgumentNotIntError()
}

internal inline fun Parameters.readNotNullableInt(
    argName: String,
    onArgumentNullError: () -> Int,
    onArgumentNotIntError: () -> Int
): Int {
    val arg = this[argName] ?: return onArgumentNullError()
    return arg.toIntOrNull() ?: return onArgumentNotIntError()
}

internal inline fun Parameters.readNotNullableNotNegativeInt(
    argName: String,
    onArgumentNullError: () -> Int,
    onArgumentNotIntError: () -> Int,
    onArgumentNegativeIntError: () -> Int
): Int {
    val arg = this[argName] ?: return onArgumentNullError()
    val argAsInt = arg.toIntOrNull() ?: return onArgumentNotIntError()
    if (argAsInt < 0) return onArgumentNegativeIntError()
    return argAsInt
}

internal inline fun Parameters.readNotNullablePositiveInt(
    argName: String,
    onArgumentNullError: () -> Int,
    onArgumentNotIntError: () -> Int,
    onArgumentNegativeOrZeroIntError: () -> Int
): Int {
    val arg = this[argName] ?: return onArgumentNullError()
    val argAsInt = arg.toIntOrNull() ?: return onArgumentNotIntError()
    if (argAsInt <= 0) return onArgumentNegativeOrZeroIntError()
    return argAsInt
}

internal inline fun Parameters.readNullablePositiveLong(
    argName: String,
    onArgumentNotLongError: () -> Long,
    onArgumentNegativeLongError: () -> Long
): Long? {
    val arg = this[argName] ?: return null
    val argAsLong = arg.toLongOrNull() ?: return onArgumentNotLongError()
    if (argAsLong < 0) return onArgumentNegativeLongError()
    return argAsLong
}

internal inline fun Parameters.readNotNullableNotNegativeLong(
    argName: String,
    onArgumentNullError: () -> Long,
    onArgumentNotLongError: () -> Long,
    onArgumentNegativeLongError: () -> Long
): Long {
    val arg = this[argName] ?: return onArgumentNullError()
    val argAsLong = arg.toLongOrNull() ?: return onArgumentNotLongError()
    if (argAsLong < 0) return onArgumentNegativeLongError()
    return argAsLong
}

internal inline fun Parameters.readNotNullableFilledString(
    argName: String,
    onArgumentNullError: () -> String,
    onArgumentEmptyError: () -> String,
): String {
    val arg = this[argName] ?: return onArgumentNullError()
    if (arg.isEmpty()) return onArgumentEmptyError()
    return arg
}
