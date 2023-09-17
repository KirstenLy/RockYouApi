package database.internal.utils

import database.internal.contract.Limitable
import org.jetbrains.exposed.sql.SizedIterable

internal fun <T> SizedIterable<T>.limitByOneAndGetFirstOrNull(): T? {
    return limit(1).firstOrNull()
}

internal fun <T> SizedIterable<T>.applyLimitableIfNeeded(limitable: Limitable): SizedIterable<T> {
    val limit = limitable.limit
    val offset = limitable.offset
    return when {
        limit != null && offset != null -> limit(limit, offset)
        limit != null && offset == null -> limit(limit, 0)
        else -> this
    }
}