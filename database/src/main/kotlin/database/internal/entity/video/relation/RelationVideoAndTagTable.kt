package database.internal.entity.video.relation

import database.internal.entity.tag.TagTable
import database.internal.entity.video.VideoTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationVideoAndTagTable : IntIdTable("RelationVideoAndTag") {

    val videoID = reference("videoID", VideoTable)

    val tagID = reference("tagID", TagTable)
}