package database.internal.executor

import database.external.filter.VideoByIDFilter
import database.external.result.SimpleOptionalDataResult
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.lang.LanguageTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.toDomain
import database.internal.entity.user.UserEntity
import database.internal.entity.video.VideoTable
import database.internal.entity.video.relation.RelationVideoAndAuthorTable
import database.internal.entity.video.relation.RelationVideoAndLanguageTable
import database.internal.entity.video.relation.RelationVideoAndTagTable
import database.internal.executor.common.isLangIDSupported
import database.internal.executor.common.tryToGetAuthorInfo
import database.internal.executor.common.tryToGetLangInfo
import database.internal.executor.common.tryToGetTag
import declaration.entity.Video
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.select

@Suppress("RemoveRedundantQualifierName")
internal class GetVideoByIDRequestExecutor {

    fun execute(filter: VideoByIDFilter) : SimpleOptionalDataResult<Video> {
        val videoEntityResultRow = ContentRegisterTable.leftJoin(
            otherTable = VideoTable,
            onColumn = { ContentRegisterTable.contentID },
            otherColumn = { VideoTable.id }
        )
            .select { ContentRegisterTable.contentID eq filter.videoID }
            .limit(1)
            .firstOrNull()
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val videoEntityID = videoEntityResultRow
            .getOrNull(ContentRegisterTable.contentID)
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val videoRegisterID = videoEntityResultRow
            .getOrNull(ContentRegisterTable.id)
            ?.value
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val videoLanguageID = videoEntityResultRow.getOrNull(VideoTable.languageID)?.value
        val videoLanguage = videoLanguageID?.let(LanguageEntity::findById)
        val videoAvailableLanguages = RelationVideoAndLanguageTable.leftJoin(
            otherTable = LanguageTable,
            onColumn = { RelationVideoAndLanguageTable.langID },
            otherColumn = { LanguageTable.id }
        )
            .select { RelationVideoAndLanguageTable.videoID eq videoEntityID }
            .mapNotNull { it.tryToGetLangInfo() }

        val videoAuthors = RelationVideoAndAuthorTable.leftJoin(
            otherTable = AuthorTable,
            onColumn = { RelationVideoAndAuthorTable.authorID },
            otherColumn = { AuthorTable.id }
        )
            .select { RelationVideoAndAuthorTable.videoID eq videoEntityID }
            .mapNotNull { it.tryToGetAuthorInfo() }

        val videoUserID = videoEntityResultRow.getOrNull(VideoTable.userID)?.value
        val videoUser = videoUserID?.let(UserEntity::findById)

        val videoTags = RelationVideoAndTagTable
            .leftJoin(
                otherTable = TagTable,
                onColumn = { RelationVideoAndTagTable.tagID },
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
            .select { RelationVideoAndTagTable.videoID eq videoRegisterID }
            .andWhere {
                val envLangID = when {
                    filter.environmentLangID == null -> Language.ENGLISH.langID
                    !isLangIDSupported(filter.environmentLangID) -> Language.ENGLISH.langID
                    else -> filter.environmentLangID
                }
                TagTranslationTable.langID eq envLangID
            }
            .mapNotNull { it.tryToGetTag() }

        val commentsCount = CommentTable
            .select { CommentTable.contentID eq videoEntityID }
            .count()

        val video = Video(
            id = videoRegisterID,
            title = videoEntityResultRow[VideoTable.title],
            url = videoEntityResultRow[VideoTable.url],
            language = videoLanguage?.toDomain(),
            availableLanguages = videoAvailableLanguages,
            authors = videoAuthors,
            user = videoUser?.toDomain()!!,
            tags = videoTags,
            rating = videoEntityResultRow[VideoTable.rating],
            commentsCount = commentsCount
        )

        return SimpleOptionalDataResult.Data(video)
    }
}