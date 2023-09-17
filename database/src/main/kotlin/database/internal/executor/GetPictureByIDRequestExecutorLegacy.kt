package database.internal.executor

import common.takeIfNotEmpty
import database.external.filter.PictureByIDFilter
import database.external.result.SimpleOptionalDataResult
import database.internal.ContentType
import database.internal.entity.author.AuthorTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.lang.LanguageTable
import database.internal.entity.lang.RelationLangAndTranslationTable
import database.internal.entity.lang_translation.LangTranslationTable
import database.internal.entity.picture.PictureTable
import database.internal.entity.picture.relation.RelationPictureAndAuthorTable
import database.internal.entity.picture.relation.RelationPictureAndLanguageTable
import database.internal.entity.picture.relation.RelationPictureAndTagTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.toDomain
import database.internal.entity.user.UserEntity
import database.internal.executor.common.*
import database.internal.executor.common.tryToGetAuthorInfo
import database.internal.executor.common.tryToGetLangInfo
import database.internal.executor.common.tryToGetTag
import declaration.entity.Picture
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.select

@Suppress("RemoveRedundantQualifierName")
@Deprecated("Use GetPictureByIDRequestExecutor")
internal class GetPictureByIDRequestExecutorLegacy {

    fun execute(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
        val pictureEntityResultRow = ContentRegisterTable.leftJoin(
            otherTable = PictureTable,
            onColumn = { ContentRegisterTable.contentID },
            otherColumn = { PictureTable.id }
        )
            .selectByContentType(ContentType.PICTURE)
            .andWhere { ContentRegisterTable.id eq filter.pictureID }
            .limit(1)
            .firstOrNull()
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val pictureEntityID = pictureEntityResultRow
            .getOrNull(ContentRegisterTable.contentID)
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val pictureRegisterID = pictureEntityResultRow
            .getOrNull(ContentRegisterTable.id)
            ?.value
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val supportedLanguagesIDs = LanguageEntity.all().map { it.id.value }
        val pictureLanguageID = pictureEntityResultRow.getOrNull(PictureTable.languageID)?.value
        val pictureLanguage = pictureLanguageID?.let(LanguageEntity::findById)
        val defaultLangID = LanguageEntity.all().first { it.isDefault }.id.value
        val pictureAvailableLanguages = RelationPictureAndLanguageTable
            .leftJoin(
                otherTable = LanguageTable,
                onColumn = { RelationPictureAndLanguageTable.langID },
                otherColumn = { LanguageTable.id }
            )
            .leftJoin(
                otherTable = RelationLangAndTranslationTable,
                onColumn = { LanguageTable.id },
                otherColumn = { RelationLangAndTranslationTable.langID }
            )
            .leftJoin(
                otherTable = LangTranslationTable,
                onColumn = { RelationLangAndTranslationTable.translationID },
                otherColumn = { LangTranslationTable.id }
            )
            .select { RelationPictureAndLanguageTable.imageID eq pictureEntityID }
            .andWhere {
                val envLangID = when (filter.environmentLangID) {
                    null -> defaultLangID
                    !in supportedLanguagesIDs -> defaultLangID
                    else -> filter.environmentLangID
                }
                LangTranslationTable.langID eq envLangID
            }
            .mapNotNull { it.tryToGetLangInfo() }
            .takeIfNotEmpty()

        val pictureAuthors = RelationPictureAndAuthorTable.leftJoin(
            otherTable = AuthorTable,
            onColumn = { RelationPictureAndAuthorTable.authorID },
            otherColumn = { AuthorTable.id }
        )
            .select { RelationPictureAndAuthorTable.imageID eq pictureEntityID }
            .mapNotNull { it.tryToGetAuthorInfo() }
            .takeIfNotEmpty()

        val pictureUserID = pictureEntityResultRow.getOrNull(PictureTable.userID)?.value
        val pictureUser = pictureUserID?.let(UserEntity::findById)

        val picturesTags = RelationPictureAndTagTable
            .leftJoin(
                otherTable = TagTable,
                onColumn = { RelationPictureAndTagTable.tagID },
                otherColumn = { TagTable.id }
            )
            .leftJoin(
                otherTable = RelationTagAndTranslationTable,
                onColumn = { TagTable.id },
                otherColumn = { RelationTagAndTranslationTable.tagID }
            )
            .leftJoin(
                otherTable = TagTranslationTable,
                onColumn = { RelationTagAndTranslationTable.translationID },
                otherColumn = { TagTranslationTable.id },
            )
            .select { RelationPictureAndTagTable.imageID eq pictureEntityID }
            .andWhere {
                val envLangID = when (filter.environmentLangID) {
                    null -> defaultLangID
                    !in supportedLanguagesIDs -> defaultLangID
                    else -> filter.environmentLangID
                }
                TagTranslationTable.langID eq envLangID
            }
            .mapNotNull { it.tryToGetTag() }
            .takeIfNotEmpty()

        val commentsCount = CommentTable
            .select { CommentTable.contentID eq pictureRegisterID }
            .count()

        val picture = Picture(
            id = pictureRegisterID,
            title = pictureEntityResultRow[PictureTable.title],
            url = pictureEntityResultRow[PictureTable.url],
            language = pictureLanguage?.toDomain(),
            availableLanguages = pictureAvailableLanguages,
            authors = pictureAuthors,
            user = pictureUser?.toDomain(),
            tags = picturesTags,
            rating = pictureEntityResultRow[PictureTable.rating],
            commentsCount = commentsCount
        )

        return SimpleOptionalDataResult.Data(picture)
    }
}