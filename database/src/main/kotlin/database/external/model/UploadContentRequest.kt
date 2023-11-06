package database.external.model

import java.time.LocalDateTime

/** Model of request created by user to upload new content. */
data class UploadContentRequest(
    val fileName: String,
    val isApproved: Boolean,
    val isClosed: Boolean,
    val message: String?,
    val creationDate: LocalDateTime
)