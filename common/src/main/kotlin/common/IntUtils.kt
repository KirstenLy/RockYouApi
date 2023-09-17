package common

/** Return [this] if positive, null otherwise */
fun Int.takeIfPositive() = takeIf { it > 0 }
