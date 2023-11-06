package database.external.model.test

import org.jetbrains.annotations.TestOnly

/** Model used by external modules to test functionality about favorite. */
@TestOnly
data class FavoriteRecord(
    val id: Int,
    val userID: Int,
    val contentID: Int,
)