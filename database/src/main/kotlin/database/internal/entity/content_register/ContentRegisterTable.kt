package database.internal.entity.content_register

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Content register.
 * Entities primary keys stores here as [contentID].
 * F.e, video entity has own table with video info, but every video key stored here as [contentID].
 * So, other entities like comment, favorite, e.t.c linked to [contentID].
 * */
internal object ContentRegisterTable : IntIdTable("ContentRegister") {

    val contentType = integer("contentType")

    val contentID = integer("contentID")
}