package common.utils

/** Remove random value and return this value. */
fun <T> MutableList<T>.getRandomAndRemove(): T {
    val value = random()
    remove(value)
    return value
}
