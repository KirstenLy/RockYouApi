package database.external.model

import java.time.LocalDateTime

/** Comment of some content. */
data class Comment(
    val id: Int,
    val contentID: Int,
    val userID: Int,
    val userName: String,
    val text: String,
    val creationDate: LocalDateTime
)