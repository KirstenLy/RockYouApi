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
fun <T> List<T>.takeRandomValues(count: Int): List<T> {
    return when {
        count > size -> throw IllegalStateException("List size must be greater that count")
        count == size -> shuffled()
        else -> shuffled().take(count)
    }
}

/** Return [this] is list not empty, null otherwise */
fun <T> List<T>.takeRandomValues(minValue: Int = 1, maxValue: Int = size): List<T> {
    return when {
        isEmpty() -> this
        minValue > maxValue -> throw IllegalStateException("Max value must be greater that min value")
        else -> shuffled().take((minValue..maxValue).random())
    }
}

/** Return [this] is list not empty, null otherwise */
fun <T> List<T>.times(increment: Int): List<T> {
    val resultList = mutableListOf<T>()
    val len = size
    repeat(increment) {
        resultList += this
    }
    return resultList
}

/** Return [this] is list not empty, null otherwise */
fun <T, V> List<T>.pairToRandomValueFrom(anotherList: List<V>) = map { it to anotherList.random() }

/** Return [this] is list not empty, null otherwise */
fun <T, K> List<T>.pairBy(getSecondAction: (value: T) -> K?) = mapNotNull { first ->
    val second = getSecondAction(first) ?: return@mapNotNull null
    first to second
}

/** Return [this] is list not empty, null otherwise */
fun <T, K, V> List<T>.toMap(keyTransformer: (element: T) -> K, valueTransformer: (element: T) -> V): Map<K, V> {
    return associate { keyTransformer(it) to valueTransformer(it) }
}

/** Return [this] is list not empty, null otherwise */
fun <T, K, V> List<T>.toMapWithListValues(
    keyTransformer: (element: T) -> K,
    valueTransformer: (element: T) -> V
): Map<K, List<V>> {
    val resultMap = hashMapOf<K, MutableList<V>>()
    forEach {
        val key = keyTransformer(it)
        val value = resultMap.getOrPut(key) { mutableListOf() }
        value.add(valueTransformer(it))
    }
    return resultMap
}