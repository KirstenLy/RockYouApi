package common

/** Return [this] if positive, null otherwise */
fun Int.takeIfPositive() = takeIf { it > 0 }

/** Return [this] if positive, null otherwise */
fun Int.thisIfLessAndValueIfMore(value: Int) = if (this <= value) this else value

/** Return [this] if positive, null otherwise */
fun Int.toZeroIfNegative() = if (this < 0) 0 else this