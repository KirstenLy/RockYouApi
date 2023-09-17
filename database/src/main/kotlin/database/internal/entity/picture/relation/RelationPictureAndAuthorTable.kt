package database.internal.entity.picture.relation

import database.internal.entity.author.AuthorTable
import database.internal.entity.picture.PictureTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationPictureAndAuthorTable : IntIdTable("RelationPictureAndAuthor") {

    val imageID = reference("imageID", PictureTable)

    val authorID = reference("authorID", AuthorTable).nullable()
}