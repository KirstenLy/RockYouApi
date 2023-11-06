package database.internal.util

/** Representation of "False" by long, used in database queries. */
internal fun <T> List<T>.isEmptyCastedToLong(): Long = if (isEmpty()) trueAsLong() else falseAsLong()

/** Representation of "False" by long, used in database queries. */
internal fun falseAsLong() = 0L

/** Representation of "True" by long, used in database queries. */
internal fun trueAsLong() = 1L

/** Representation of "True" by long, used in database queries. */
internal fun listWithNotExistedEntityID() = listOf(-1)

/** Representation of "True" by long, used in database queries. */
internal fun List<Int>.notExistedEntityIDIfEmpty() = ifEmpty { listWithNotExistedEntityID() }