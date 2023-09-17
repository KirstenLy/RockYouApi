package common

/**
 * Transform the map to another map by transform it values when value presented by List<V>.
 * Another word: Map<K, List<V>> -> Map<K, List<R>>
 * */
fun <K, V, R> Map<K, List<V>>.mapListValues(transform: (value: V) -> R) = mapValues {
    it.value.map(transform)
}

/** @see mapListValues, but exclude nullable V */
fun <K, V, R> Map<K, List<V>>.mapListValuesNotNull(transform: (value: V) -> R) = mapValues {
    it.value.mapNotNull(transform)
}