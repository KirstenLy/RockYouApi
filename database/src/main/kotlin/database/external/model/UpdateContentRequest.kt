package database.external.model

import java.time.LocalDateTime

/** Model of request created by user to update/actualize content info. */
data class UpdateContentRequest(
    val contentName: String,
    val requestText: String,
    val isApproved: Boolean,
    val isClosed: Boolean,
    val resultText: String?,
    val creationDate: LocalDateTime
)