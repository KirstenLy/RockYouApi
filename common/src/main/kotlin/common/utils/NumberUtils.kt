package common.utils

/** Return [this] is positive, null otherwise. */
fun Int.takeIfPositive() = takeIf { it > 0 }

/** Return [this] if positive, 0 otherwise. */
fun Int.toZeroIfNegative() = if (this < 0) 0 else this

/** Is [this] positive? */
fun Int?.isPositive() = this != null && this > 0

/** Return this is positive and not null, [replacement] otherwise.  */
fun Int?.positiveOr(replacement: Int) = if (this != null && this > 0) this else replacement

/** Return [this] if not null, 0 otherwise. */
fun Long?.zeroOnNull() = this ?: 0