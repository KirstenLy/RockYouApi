package common

/** Return [this] is list not empty, null otherwise */
fun <T> MutableList<T>.getRandomAndRemove(): T {
    val value = random()
    remove(value)
    return value
}
