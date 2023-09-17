package database.internal.executor

import common.mapListValuesNotNull
import database.external.filter.VideoListFilter
import database.external.result.SimpleListResult
import database.internal.ContentType
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.user.UserTable
import database.internal.entity.video.VideoTable
import database.internal.entity.video.relation.RelationVideoAndAuthorTable
import database.internal.entity.video.relation.RelationVideoAndLanguageTable
import database.internal.entity.video.relation.RelationVideoAndTagTable
import database.internal.executor.common.*
import database.internal.executor.common.getLangIDCondition
import database.internal.executor.common.isLangIDSupported
import database.internal.executor.common.selectByContentType
import database.internal.executor.common.tryToGetLangInfo
import database.internal.utils.applyLimitableIfNeeded
import declaration.entity.Video
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.select

@Suppress("RemoveRedundantQualifierName")
internal class GetVideosRequestExecutor {

    fun execute(filter: VideoListFilter) : SimpleListResult<Video> {
        return try {
            val videosQueries = ContentRegisterTable.innerJoin(
                otherTable = VideoTable,
                onColumn = { contentID },
                otherColumn = { id },
            )
                .selectByContentType(ContentType.VIDEO)
                .andWhere { getLangIDCondition(filter.langID, VideoTable.languageID) }
                .applyLimitableIfNeeded(filter)

            if (videosQueries.empty()) {
                return SimpleListResult.Data(emptyList())
            }

            val videosIDs = videosQueries.map { it[ContentRegisterTable.contentID] }
            val videosLanguages = VideoTable.leftJoin(
                otherTable = LanguageTable,
                onColumn = { VideoTable.languageID },
                otherColumn = { LanguageTable.id }
            )
                .select { VideoTable.id inList videosIDs }
                .associateBy(
                    keySelector = { it[VideoTable.id].value },
                    valueTransform = { it.tryToGetLangInfo() }
                )

            val videosAvailableLanguages = RelationVideoAndLanguageTable.leftJoin(
                otherTable = LanguageTable,
                onColumn = { RelationVideoAndLanguageTable.langID },
                otherColumn = { LanguageTable.id }
            )
                .select { RelationVideoAndLanguageTable.videoID inList videosIDs }
                .groupBy { it[RelationVideoAndLanguageTable.videoID].value }
                .mapListValuesNotNull { it.tryToGetLangInfo() }

            val videosUsers = VideoTable.leftJoin(
                otherTable = UserTable,
                onColumn = { VideoTable.userID },
                otherColumn = { UserTable.id }
            )
                .select { VideoTable.id inList videosIDs }
                .associateBy(
                    keySelector = { it[VideoTable.id].value },
                    valueTransform = { it.tryToGetUserInfo() }
                )

            val videosAuthors = RelationVideoAndAuthorTable.leftJoin(
                otherTable = AuthorTable,
                onColumn = { RelationVideoAndAuthorTable.authorID },
                otherColumn = { AuthorTable.id }
            )
                .select { RelationVideoAndAuthorTable.videoID inList videosIDs }
                .groupBy { it[RelationVideoAndAuthorTable.videoID].value }
                .mapListValuesNotNull { it.tryToGetAuthorInfo() }

            val videosTags = RelationVideoAndTagTable
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
                .select { RelationVideoAndTagTable.videoID inList videosIDs }
                .andWhere {
                    val envLangID = when {
                        filter.environmentLangID == null -> Language.ENGLISH.langID
                        !isLangIDSupported(filter.environmentLangID) -> Language.ENGLISH.langID
                        else -> filter.environmentLangID
                    }
                    TagTranslationTable.langID eq envLangID
                }
                .groupBy { it[RelationVideoAndTagTable.videoID].value }
                .mapListValuesNotNull { it.tryToGetTag() }

            val commentsCount = CommentTable.select {
                CommentTable.contentID inList videosIDs
            }
                .groupBy { it[CommentTable.contentID].value }

            val videosResponse = videosQueries.map {
                val videoRegisterID = it[ContentRegisterTable.id].value
                val videoEntityID = it[ContentRegisterTable.contentID]
                Video(
                    id = videoRegisterID,
                    title = it[VideoTable.title],
                    url = it[VideoTable.url],
                    language = videosLanguages[videoEntityID],
                    availableLanguages = videosAvailableLanguages[videoEntityID],
                    authors = videosAuthors[videoEntityID],
                    user = videosUsers[videoEntityID]!!,
                    tags = videosTags[videoEntityID]!!,
                    rating = it[VideoTable.rating],
                    commentsCount = commentsCount[videoEntityID]?.size?.toLong() ?: 0L
                )
            }

            SimpleListResult.Data(videosResponse)
        } catch (t: Throwable) {
            SimpleListResult.Error(t)
        }
    }
}