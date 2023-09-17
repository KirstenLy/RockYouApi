package database.internal.entity.picture.relation

import database.internal.entity.picture.PictureTable
import database.internal.entity.tag.TagTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationPictureAndTagTable : IntIdTable("RelationPictureAndTag") {

    val imageID = reference("imageID", PictureTable)

    val tagID = reference("tagID", TagTable)
}