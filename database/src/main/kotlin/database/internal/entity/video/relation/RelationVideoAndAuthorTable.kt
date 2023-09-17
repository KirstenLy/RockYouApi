package database.internal.entity.video.relation

import database.internal.entity.author.AuthorTable
import database.internal.entity.video.VideoTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationVideoAndAuthorTable : IntIdTable("RelationVideoAndAuthor") {

    val videoID = reference("videoID", VideoTable)

    val authorID = reference("authorID", AuthorTable)
}