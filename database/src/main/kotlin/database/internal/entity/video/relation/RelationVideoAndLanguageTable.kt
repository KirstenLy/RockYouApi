package database.internal.entity.video.relation

import database.internal.entity.lang.LanguageTable
import database.internal.entity.video.VideoTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationVideoAndLanguageTable : IntIdTable("RelationVideoAndLanguage") {

    val videoID = reference("videoID", VideoTable)

    val langID = reference("langID", LanguageTable)
}