package database.external.model.test

import org.jetbrains.annotations.TestOnly
import java.time.LocalDateTime

/** Model used by external modules to test functionality about report. */
@TestOnly
data class ReportRecord(
    val id: Int,
    val contentID: Int,
    val userID: Int?,
    val reportText: String,
    val isClosed: Boolean,
    val creationDate: LocalDateTime,
)