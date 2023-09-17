package common

/** Get indexes of all elements match predicate. */
fun <T> List<T>.indexesOf(predicate: (item: T) -> Boolean) = mapIndexedNotNull { index, t ->
    if (predicate(t)) index else null
}

/** Offset all list elements by n. */
fun List<Int>.offsetAll(n: Int) = map { it + n }

/** Return [this] is list not empty, null otherwise */
fun <T> List<T>.takeIfNotEmpty() = takeIf(List<T>::isNotEmpty)

/** Return [this] is list not empty, null otherwise */
inline fun List<Int>.runIfEmpty(action: () -> Unit) {
    if (isEmpty()) action()
}

/** Return [this] is list not empty, null otherwise */
fun <T> List<T>.takeRandomValues() = shuffled().take((1..size).random())

/** Return [this] is list not empty, null otherwise */
fun <T, V> List<T>.pairToRandomValueFrom(anotherList: List<V>) = map { it to anotherList.random() }

/** Return [this] is list not empty, null otherwise */
fun <T, K, V> List<T>.toMap(keyTransformer: (element: T) -> K, valueTransformer: (element: T) -> V): Map<K, V> {
    return associate { keyTransformer(it) to valueTransformer(it) }
}