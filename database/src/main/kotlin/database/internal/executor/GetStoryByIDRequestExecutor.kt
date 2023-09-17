package database.internal.executor

import database.external.result.SimpleOptionalDataResult
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.chapter.relation.RelationChapterAndLanguageTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.lang.LanguageTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.story.StoryTable
import database.internal.entity.story.relation.RelationStoryAndAuthorTable
import database.internal.entity.story.relation.RelationStoryAndLanguageTable
import database.internal.entity.story.relation.RelationStoryAndTagTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.toDomain
import database.internal.entity.user.UserEntity
import database.internal.executor.common.isLangIDSupported
import database.internal.executor.common.tryToGetAuthorInfo
import database.internal.executor.common.tryToGetLangInfo
import database.internal.executor.common.tryToGetTag
import declaration.entity.Story
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.select

@Suppress("RemoveRedundantQualifierName")
internal class GetStoryByIDRequestExecutor(private val storyChaptersRequestExecutor: GetChaptersRequestExecutor) {

    fun execute(storyRegisterID: Int): SimpleOptionalDataResult<Story> {
        val storyEntityRow = selectStoryInfo(storyRegisterID) ?: return SimpleOptionalDataResult.DataNotFounded()

        val storyEntityID = storyEntityRow
            .getOrNull(ContentRegisterTable.contentID)
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val storyLanguageID = storyEntityRow.getOrNull(StoryTable.languageID)?.value
        val storyLanguage = storyLanguageID?.let(LanguageEntity::findById)

        val storyAvailableLanguages = selectAvailableLanguages(storyEntityID)
        val storyAuthors = selectAuthors(storyEntityID)

        val storyUserID = storyEntityRow.getOrNull(StoryTable.userID)?.value
        val storyUser = storyUserID?.let(UserEntity::findById)

        val storyTags = selectTags(storyEntityID)

        val storyChapters = storyChaptersRequestExecutor.execute(listOf(storyEntityID), null)

        val commentsCount = CommentTable
            .select { CommentTable.contentID eq storyRegisterID }
            .count()

        val picture = Story(
            id = storyRegisterID,
            title = storyEntityRow[StoryTable.title],
            language = storyLanguage?.toDomain(),
            availableLanguages = storyAvailableLanguages,
            authors = storyAuthors,
            user = storyUser?.toDomain(),
            tags = storyTags,
            rating = storyEntityRow[StoryTable.rating],
            commentsCount = commentsCount,
            chapters = emptyList()
        )

        return SimpleOptionalDataResult.Data(picture)
    }

    private fun selectStoryInfo(storyRegisterID: Int) = StoryTable
        .select { StoryTable.id eq storyRegisterID }
        .limit(1)
        .firstOrNull()

    private fun selectAvailableLanguages(storyID: Int) = RelationStoryAndLanguageTable
        .leftJoin(
            otherTable = LanguageTable,
            onColumn = { RelationChapterAndLanguageTable.languageID },
            otherColumn = { LanguageTable.id }
        )
        .select { RelationStoryAndLanguageTable.storyID eq storyID }
        .mapNotNull { it.tryToGetLangInfo() }

    private fun selectAuthors(storyID: Int) = RelationStoryAndAuthorTable
        .leftJoin(
            otherTable = AuthorTable,
            onColumn = { RelationStoryAndAuthorTable.authorID },
            otherColumn = { AuthorTable.id }
        )
        .select { RelationStoryAndAuthorTable.storyID eq storyID }
        .mapNotNull { it.tryToGetAuthorInfo() }

    private fun selectTags(storyID: Int, environmentLangID: Int? = null) = RelationStoryAndTagTable
        .leftJoin(
            otherTable = TagTable,
            onColumn = { RelationStoryAndTagTable.tagID },
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
        .select { RelationStoryAndTagTable.storyID eq storyID }
        .andWhere {
            val envLangID = when {
                environmentLangID == null -> Language.ENGLISH.langID
                !isLangIDSupported(environmentLangID) -> Language.ENGLISH.langID
                else -> environmentLangID
            }
            TagTranslationTable.langID eq envLangID
        }
        .mapNotNull { it.tryToGetTag() }
}