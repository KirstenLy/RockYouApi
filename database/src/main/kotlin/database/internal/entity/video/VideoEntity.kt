package database.internal.entity.video

import database.internal.entity.author.AuthorEntity
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.tag.TagEntity
import database.internal.entity.user.UserEntity
import database.internal.entity.video.relation.RelationVideoAndAuthorTable
import database.internal.entity.video.relation.RelationVideoAndLanguageTable
import database.internal.entity.video.relation.RelationVideoAndTagTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class VideoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VideoEntity>(VideoTable)

    val title by VideoTable.title

    val url by VideoTable.url

    val user by UserEntity referencedOn VideoTable.userID

    val authors by AuthorEntity via RelationVideoAndAuthorTable

    val language by LanguageEntity optionalReferencedOn VideoTable.languageID

    val availableLanguages by LanguageEntity via RelationVideoAndLanguageTable

    val tags by TagEntity via RelationVideoAndTagTable

    val rating by VideoTable.rating
}