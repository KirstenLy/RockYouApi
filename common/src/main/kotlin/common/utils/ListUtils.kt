package common.utils

/** Get indexes of all elements match predicate. */
fun <T> List<T>.indexesOf(predicate: (item: T) -> Boolean) = mapIndexedNotNull { index, t ->
    if (predicate(t)) index else null
}

/** Offset all list elements by n. */
fun List<Int>.offsetAll(n: Int) = map { it + n }

/** Return [this] is list not empty, null otherwise. */
fun <T> List<T>.takeIfNotEmpty() = takeIf(List<T>::isNotEmpty)

/** Run [action] if list empty. */
inline fun List<Int>.runIfEmpty(action: () -> Unit) {
    if (isEmpty()) action()
}

/**
 * Take [count] random values from list.
 * @throws [IllegalArgumentException] if [count] more that list size.
 * */
@Throws(IllegalArgumentException::class)
fun <T> List<T>.takeRandomValues(count: Int): List<T> {
    return when {
        count > size -> throw IllegalArgumentException("List size must be greater that count")
        count == size -> shuffled()
        else -> shuffled().take(count)
    }
}

/**
 * Take random values from list.
 * Returned list size is random from range [minValue] - [maxValue].
 * @throws [IllegalArgumentException] if [count] more that list size.
 * */
@Throws(IllegalArgumentException::class)
fun <T> List<T>.takeRandomValues(minValue: Int = 1, maxValue: Int = size): List<T> {
    return when {
        isEmpty() -> this
        minValue > maxValue -> throw IllegalStateException("Max value must be greater that min value")
        else -> shuffled().take((minValue..maxValue).random())
    }
}

/** Return [this] but taken [multiplier] times. */
fun <T> List<T>.times(multiplier: Int): List<T> {
    val resultList = mutableListOf<T>()
    repeat(multiplier) {
        resultList += this
    }
    return resultList
}

/**
 * Pair every item from this list to random value from [anotherList].
 * Values from [anotherList] can be used several times.
 * */
fun <T, V> List<T>.pairToRandomValueFrom(anotherList: List<V>) = map { it to anotherList.random() }

/** For every pair check [Pair.first] == [Pair.second]. */
fun <A, B> List<Pair<A, B>>.isAllLeftEqualsTheirRight(): Boolean {
    return all { (left, right) -> left == right }
}