package database.internal.entity.picture.relation

import database.internal.entity.picture.PictureTable
import database.internal.entity.lang.LanguageTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationPictureAndLanguageTable : IntIdTable("RelationPictureAndLanguage") {

    val imageID = reference("imageID", PictureTable)

    val langID = reference("langID", LanguageTable)
}