package database.internal.entity.tag

import database.internal.entity.tag_translation.TagTranslationTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationTagAndTranslationTable : IntIdTable("RelationTagAndTranslation") {

    val tagID = reference("tagID", TagTable)

    val translationID = reference("translationID", TagTranslationTable)
}