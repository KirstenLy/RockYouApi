package database.internal.model

import java.time.LocalDateTime

/** Comment model to fill test database. */
internal data class DBComment(
    val id: Int,
    val contentID: Int,
    val userID: Int,
    val text: String,
    val creationDate: LocalDateTime
)