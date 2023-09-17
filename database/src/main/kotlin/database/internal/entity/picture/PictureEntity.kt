package database.internal.entity.picture

import database.internal.entity.author.AuthorEntity
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.picture.relation.RelationPictureAndAuthorTable
import database.internal.entity.picture.relation.RelationPictureAndLanguageTable
import database.internal.entity.picture.relation.RelationPictureAndTagTable
import database.internal.entity.tag.TagEntity
import database.internal.entity.user.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class PictureEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PictureEntity>(PictureTable)

    val title by PictureTable.title

    val url by PictureTable.url

    val user by UserEntity referencedOn PictureTable.userID

    val authors by AuthorEntity via RelationPictureAndAuthorTable

    val language by LanguageEntity optionalReferencedOn PictureTable.languageID

    val availableLanguages by LanguageEntity via RelationPictureAndLanguageTable

    val tags by TagEntity via RelationPictureAndTagTable

    val rating by PictureTable.rating
}