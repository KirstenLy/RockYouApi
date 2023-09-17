package database.internal.executor

import common.mapListValuesNotNull
import database.external.filter.PictureListFilter
import database.external.result.SimpleListResult
import database.internal.ContentType
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.lang.LanguageTable
import database.internal.entity.lang_translation.LangTranslationTable
import database.internal.entity.picture.PictureTable
import database.internal.entity.picture.relation.RelationPictureAndAuthorTable
import database.internal.entity.picture.relation.RelationPictureAndLanguageTable
import database.internal.entity.picture.relation.RelationPictureAndTagTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.user.UserTable
import database.internal.executor.common.*
import database.internal.executor.common.getLangIDCondition
import database.internal.executor.common.isLangIDSupported
import database.internal.executor.common.selectByContentType
import database.internal.executor.common.tryToGetLangInfo
import database.internal.utils.applyLimitableIfNeeded
import declaration.entity.Picture
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.select

@Suppress("RemoveRedundantQualifierName")
internal class GetPicturesRequestExecutor {

    fun execute(filter: PictureListFilter) : SimpleListResult<Picture> {
        return try {
            val picturesQueries = ContentRegisterTable.innerJoin(
                otherTable = PictureTable,
                onColumn = { contentID },
                otherColumn = { id },
            )
                .selectByContentType(ContentType.PICTURE)
                .andWhere { getLangIDCondition(filter.langID, PictureTable.languageID) }
                .applyLimitableIfNeeded(filter)

            if (picturesQueries.empty()) {
                return SimpleListResult.Data(emptyList())
            }

            val picturesIDs = picturesQueries.map { it[ContentRegisterTable.contentID] }
            val supportedLanguagesIDs = LanguageEntity.all().map { it.id.value }
            val defaultLangID = LanguageEntity.all().first { it.isDefault }.id.value

            val picturesLanguages = PictureTable.leftJoin(
                otherTable = LanguageTable,
                onColumn = { PictureTable.languageID },
                otherColumn = { LanguageTable.id }
            )
                .select { PictureTable.id inList picturesIDs }
                .associateBy(
                    keySelector = { it[PictureTable.id].value },
                    valueTransform = { it.tryToGetLangInfo() }
                )

            val picturesAvailableLanguages = RelationPictureAndLanguageTable.leftJoin(
                otherTable = LanguageTable,
                onColumn = { RelationPictureAndLanguageTable.langID },
                otherColumn = { LanguageTable.id }
            )
                .select { RelationPictureAndLanguageTable.imageID inList picturesIDs }
                .groupBy { it[RelationPictureAndLanguageTable.imageID].value }
                .mapListValuesNotNull { it.tryToGetLangInfo() }

            val picturesUsers = PictureTable.leftJoin(
                otherTable = UserTable,
                onColumn = { PictureTable.userID },
                otherColumn = { UserTable.id }
            )
                .select { PictureTable.id inList picturesIDs }
                .associateBy(
                    keySelector = { it[PictureTable.id].value },
                    valueTransform = { it.tryToGetUserInfo() }
                )

            val pictureAuthors = RelationPictureAndAuthorTable.leftJoin(
                otherTable = AuthorTable,
                onColumn = { RelationPictureAndAuthorTable.authorID },
                otherColumn = { AuthorTable.id }
            )
                .select { RelationPictureAndAuthorTable.imageID inList picturesIDs }
                .groupBy { it[RelationPictureAndAuthorTable.imageID].value }
                .mapListValuesNotNull { it.tryToGetAuthorInfo() }

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
                .select { RelationPictureAndTagTable.imageID inList picturesIDs }
                .andWhere {
                    val envLangID = when (filter.environmentLangID) {
                        null -> defaultLangID
                        !in supportedLanguagesIDs -> defaultLangID
                        else -> filter.environmentLangID
                    }
                    TagTranslationTable.langID eq envLangID
                }
                .groupBy { it[RelationPictureAndTagTable.imageID].value }
                .mapListValuesNotNull { it.tryToGetTag() }

            val commentsCount = CommentTable.select {
                CommentTable.contentID inList picturesIDs
            }
                .groupBy { it[CommentTable.contentID].value }

            val picturesResponse = picturesQueries.map {
                val pictureRegisterID = it[ContentRegisterTable.id].value
                val pictureEntityID = it[ContentRegisterTable.contentID]
                Picture(
                    id = pictureRegisterID,
                    title = it[PictureTable.title],
                    url = it[PictureTable.url],
                    language = picturesLanguages[pictureEntityID],
                    availableLanguages = picturesAvailableLanguages[pictureEntityID],
                    authors = pictureAuthors[pictureEntityID],
                    user = picturesUsers[pictureEntityID],
                    tags = picturesTags[pictureEntityID],
                    rating = it[PictureTable.rating],
                    commentsCount = commentsCount[pictureEntityID]?.size?.toLong() ?: 0L
                )
            }

            SimpleListResult.Data(picturesResponse)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }
}