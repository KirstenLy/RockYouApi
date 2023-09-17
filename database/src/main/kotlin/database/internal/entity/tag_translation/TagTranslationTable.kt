package database.internal.entity.tag_translation

import org.jetbrains.exposed.dao.id.IntIdTable

internal object TagTranslationTable : IntIdTable("TagTranslation") {

    val langID = integer("langID")

    val translation = varchar("translation", 255)
}