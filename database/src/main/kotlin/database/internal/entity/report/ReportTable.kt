package database.internal.entity.report

import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object ReportTable : IntIdTable("Report") {

    val contentID = reference("contentID", ContentRegisterTable)

    val report = text("report")

    val userID = reference("userID", UserTable).nullable()

    val isClosed = bool("isClosed")
}