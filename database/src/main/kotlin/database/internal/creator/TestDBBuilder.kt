package database.internal.creator

import database.external.DatabaseAPI
import database.external.TestDatabaseBuilder
import database.external.model.*
import database.internal.DatabaseAPIImpl
import database.internal.entity.author.AuthorTable
import database.internal.entity.chapter.ChapterTable
import database.internal.entity.chapter.relation.RelationChapterAndAuthorTable
import database.internal.entity.chapter.relation.RelationChapterAndLanguageTable
import database.internal.entity.chapter.relation.RelationChapterAndTagTable
import database.internal.entity.chapter_text.ChapterTextTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang_translation.LangTranslationTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.lang.RelationLangAndTranslationTable
import database.internal.entity.picture.PictureTable
import database.internal.entity.picture.relation.RelationPictureAndAuthorTable
import database.internal.entity.picture.relation.RelationPictureAndLanguageTable
import database.internal.entity.picture.relation.RelationPictureAndTagTable
import database.internal.entity.story.StoryTable
import database.internal.entity.story.relation.RelationStoryAndAuthorTable
import database.internal.entity.story.relation.RelationStoryAndChapterTable
import database.internal.entity.story.relation.RelationStoryAndLanguageTable
import database.internal.entity.story.relation.RelationStoryAndTagTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.user.UserTable
import database.internal.entity.video.VideoTable
import database.internal.entity.video.relation.RelationVideoAndAuthorTable
import database.internal.entity.video.relation.RelationVideoAndLanguageTable
import database.internal.entity.video.relation.RelationVideoAndTagTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

/** @see database.external.TestDatabaseBuilder */
@Suppress("RemoveRedundantQualifierName")
// TODO: Напротив большинства методов написать,что в них не проверяются свящи с другими конкретными сущностями и они должны быть установлены правильно
internal class TestDBBuilder : TestDatabaseBuilder {

    private val languages: MutableList<TestLanguage> = mutableListOf()
    private val users: MutableList<TestUser> = mutableListOf()
    private val authors: MutableList<TestAuthor> = mutableListOf()
    private val contentRegisters: MutableList<TestContentRegister> = mutableListOf()
    private val pictures: MutableList<TestPicture> = mutableListOf()
    private val videos: MutableList<TestVideo> = mutableListOf()
    private val stories: MutableList<TestStory> = mutableListOf()
    private val storyChapters: MutableList<TestStoryChapter> = mutableListOf()
    private val tags: MutableList<TestTag> = mutableListOf()
    private val comments: MutableList<TestComment> = mutableListOf()

    private val tagsRelationsWithTranslations: MutableList<TestRelationBetweenTagAndTagTranslation> = mutableListOf()
    private val picturesRelationsWithTags: MutableList<TestRelationBetweenPictureAndTag> = mutableListOf()

    override fun setLanguages(languages: List<TestLanguage>) {
        this.languages.clear()
        this.languages.addAll(languages)
    }

    override fun setUsers(users: List<TestUser>) {
        this.users.clear()
        this.users.addAll(users)
    }

    override fun setAuthors(authors: List<TestAuthor>) {
        this.authors.clear()
        this.authors.addAll(authors)
    }

    override fun setContentRegisters(contentRegisters: List<TestContentRegister>) {
        this.contentRegisters.clear()
        this.contentRegisters.addAll(contentRegisters)
    }

    override fun setPictures(pictures: List<TestPicture>) {
        this.pictures.clear()
        this.pictures.addAll(pictures)
    }

    override fun setVideos(videos: List<TestVideo>) {
        this.videos.clear()
        this.videos.addAll(videos)
    }

    override fun setStories(stories: List<TestStory>) {
        this.stories.clear()
        this.stories.addAll(stories)
    }

    override fun setStoryChapters(storyChapters: List<TestStoryChapter>) {
        this.storyChapters.clear()
        this.storyChapters.addAll(storyChapters)
    }

    override fun setTags(tags: List<TestTag>) {
        this.tags.clear()
        this.tags.addAll(tags)
    }

    override fun setComments(comments: List<TestComment>) {
        this.comments.clear()
        this.comments.addAll(comments)
    }

    override fun setRelationsBetweenTagsAndTranslations(tagsAndTranslationsRelations: List<TestRelationBetweenTagAndTagTranslation>) {
        this.tagsRelationsWithTranslations.clear()
        this.tagsRelationsWithTranslations.addAll(tagsAndTranslationsRelations)
    }

    // TODO: Это перестало быть нужным, как и генератор этой модели
    override fun setRelationsBetweenPicturesAndTags(picturesAndTagsRelations: List<TestRelationBetweenPictureAndTag>) {
        this.picturesRelationsWithTags.clear()
        this.picturesRelationsWithTags.addAll(picturesAndTagsRelations)
    }

    override fun build(): DatabaseAPI {
        with(connectToDatabase()) {
            dropAllTables()
            createAllTables()

            if (languages.isNotEmpty()) insertLanguages(languages)
            if (users.isNotEmpty()) insertUsers(users)
            if (authors.isNotEmpty()) insertAuthors(authors)
            if (tags.isNotEmpty()) insertTags(tags)

            if (contentRegisters.isNotEmpty()) insertContentRegisters(contentRegisters)
            if (pictures.isNotEmpty()) insertPictures(pictures)
            if (videos.isNotEmpty()) insertVideos(videos)
            if (stories.isNotEmpty()) insertStories(stories)
            if (storyChapters.isNotEmpty()) insertChapters(storyChapters)
            if (comments.isNotEmpty()) insertComments(comments)

        }
        return DatabaseAPIImpl()
    }

    private fun connectToDatabase(): Database {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        return Database.connect(jdbcURL, driverClassName)
    }

    private fun Database.dropAllTables() {
        transaction(this) {

            SchemaUtils.drop(CommentTable)

            SchemaUtils.drop(ContentRegisterTable)

            SchemaUtils.drop(RelationPictureAndAuthorTable)
            SchemaUtils.drop(RelationPictureAndLanguageTable)
            SchemaUtils.drop(RelationPictureAndTagTable)
            SchemaUtils.drop(PictureTable)

            SchemaUtils.drop(RelationVideoAndLanguageTable)
            SchemaUtils.drop(RelationVideoAndTagTable)
            SchemaUtils.drop(RelationVideoAndAuthorTable)
            SchemaUtils.drop(VideoTable)

            SchemaUtils.drop(RelationStoryAndChapterTable)
            SchemaUtils.drop(RelationStoryAndAuthorTable)
            SchemaUtils.drop(RelationStoryAndLanguageTable)
            SchemaUtils.drop(RelationStoryAndTagTable)

            SchemaUtils.drop(RelationChapterAndTagTable)
            SchemaUtils.drop(RelationChapterAndLanguageTable)
            SchemaUtils.drop(RelationChapterAndAuthorTable)

            SchemaUtils.drop(ChapterTable)
            SchemaUtils.drop(StoryTable)

            SchemaUtils.drop(ChapterTextTable)

            SchemaUtils.drop(AuthorTable)
            SchemaUtils.drop(UserTable)

            SchemaUtils.drop(RelationLangAndTranslationTable)
            SchemaUtils.drop(LangTranslationTable)
            SchemaUtils.drop(LanguageTable)

            SchemaUtils.drop(RelationTagAndTranslationTable)
            SchemaUtils.drop(TagTranslationTable)
            SchemaUtils.drop(TagTable)
        }
    }

    private fun Database.createAllTables() {
        transaction(this) {

            SchemaUtils.create(CommentTable)

            SchemaUtils.create(ContentRegisterTable)

            SchemaUtils.create(PictureTable)
            SchemaUtils.create(RelationPictureAndAuthorTable)
            SchemaUtils.create(RelationPictureAndLanguageTable)
            SchemaUtils.create(RelationPictureAndTagTable)

            SchemaUtils.create(VideoTable)
            SchemaUtils.create(RelationVideoAndLanguageTable)
            SchemaUtils.create(RelationVideoAndTagTable)
            SchemaUtils.create(RelationVideoAndAuthorTable)

            SchemaUtils.create(RelationStoryAndChapterTable)
            SchemaUtils.create(RelationStoryAndAuthorTable)
            SchemaUtils.create(RelationStoryAndLanguageTable)
            SchemaUtils.create(RelationStoryAndTagTable)

            SchemaUtils.create(ChapterTable)
            SchemaUtils.create(StoryTable)
            SchemaUtils.create(RelationChapterAndTagTable)
            SchemaUtils.create(RelationChapterAndLanguageTable)
            SchemaUtils.create(RelationChapterAndAuthorTable)

            SchemaUtils.create(ChapterTextTable)

            SchemaUtils.create(AuthorTable)
            SchemaUtils.create(UserTable)

            SchemaUtils.create(LanguageTable)
            SchemaUtils.create(LangTranslationTable)
            SchemaUtils.create(RelationLangAndTranslationTable)

            SchemaUtils.create(RelationTagAndTranslationTable)
            SchemaUtils.create(TagTranslationTable)
            SchemaUtils.create(TagTable)
        }
    }

    private fun Database.insertLanguages(languages: List<TestLanguage>) {
        transaction(this) {
            languages.forEach { language ->
                LanguageTable.insert { insertStatement ->
                    insertStatement[LanguageTable.id] = language.id
                    insertStatement[LanguageTable.isDefault] = language.isDefault
                }
            }

            languages.forEach { language ->
                language.translations.forEach { languageTranslation ->
                    val translationID = LangTranslationTable.insertAndGetId { insertStatement ->
                        insertStatement[LangTranslationTable.id] = languageTranslation.id
                        insertStatement[LangTranslationTable.langID] = languageTranslation.langID
                        insertStatement[LangTranslationTable.translation] = languageTranslation.name
                    }

                    RelationLangAndTranslationTable.insert {
                        it[RelationLangAndTranslationTable.langID] = language.id
                        it[RelationLangAndTranslationTable.translationID] = translationID
                    }
                }
            }
        }
    }

    private fun Database.insertUsers(users: List<TestUser>) {
        transaction(this) {
            users.forEach { model ->
                UserTable.insert { insertStatement ->
                    insertStatement[UserTable.id] = model.id
                    insertStatement[UserTable.name] = model.name
                }
            }
        }
    }

    private fun Database.insertAuthors(authors: List<TestAuthor>) {
        transaction(this) {
            authors.forEach { model ->
                AuthorTable.insert { insertStatement ->
                    insertStatement[AuthorTable.id] = model.id
                    insertStatement[AuthorTable.name] = model.name
                }
            }
        }
    }

    private fun Database.insertContentRegisters(contentRegisterModels: List<TestContentRegister>) {
        transaction(this) {
            contentRegisterModels.forEach { model ->
                ContentRegisterTable.insert { insertStatement ->
                    insertStatement[ContentRegisterTable.id] = model.id
                    insertStatement[ContentRegisterTable.contentID] = model.contentID
                    insertStatement[ContentRegisterTable.contentType] = model.contentType
                }
            }
        }
    }

    private fun Database.insertPictures(pictures: List<TestPicture>) {
        transaction(this) {
            pictures.forEach { picture ->
                PictureTable.insert {
                    it[PictureTable.id] = picture.id
                    it[PictureTable.title] = picture.title
                    it[PictureTable.url] = picture.url
                    it[PictureTable.languageID] = picture.languageID
                    it[PictureTable.userID] = picture.userID
                    it[PictureTable.rating] = picture.rating
                }

                val pictureAvailableLanguages = picture.availableLanguagesIDs
                if (!pictureAvailableLanguages.isNullOrEmpty()) {
                    pictureAvailableLanguages.forEach { languageID ->
                        RelationPictureAndLanguageTable.insert {
                            it[RelationPictureAndLanguageTable.imageID] = picture.id
                            it[RelationPictureAndLanguageTable.langID] = languageID
                        }
                    }
                }

                val pictureAuthors = picture.authorsIDs
                if (!pictureAuthors.isNullOrEmpty()) {
                    pictureAuthors.forEach { authorID ->
                        RelationPictureAndAuthorTable.insert {
                            it[RelationPictureAndAuthorTable.imageID] = picture.id
                            it[RelationPictureAndAuthorTable.authorID] = authorID
                        }
                    }
                }

                val pictureTags = picture.tagsIDs
                if (!pictureTags.isNullOrEmpty()) {
                    pictureTags.forEach { tagID ->
                        RelationPictureAndTagTable.insert {
                            it[RelationPictureAndTagTable.imageID] = picture.id
                            it[RelationPictureAndTagTable.tagID] = tagID
                        }
                    }
                }
            }
        }
    }

    private fun Database.insertVideos(videos: List<TestVideo>) {
        transaction(this) {
            videos.forEach { model ->
                VideoTable.insert {
                    it[VideoTable.id] = model.id
                    it[VideoTable.title] = model.title
                    it[VideoTable.url] = model.url
                    it[VideoTable.languageID] = model.languageID
                    it[VideoTable.userID] = model.userID
                    it[VideoTable.rating] = model.rating
                }
            }
        }
    }

    private fun Database.insertStories(stories: List<TestStory>) {
        transaction(this) {
            stories.forEach { model ->
                StoryTable.insert {
                    it[StoryTable.id] = model.id
                    it[StoryTable.title] = model.title
                    it[StoryTable.languageID] = model.languageID
                    it[StoryTable.userID] = model.userID
                    it[StoryTable.rating] = model.rating
                }
            }
        }
    }

    private fun Database.insertChapters(chapters: List<TestStoryChapter>) {
        transaction(this) {
            chapters.forEach { model ->
                ChapterTextTable.insert {
                    it[ChapterTextTable.text] = "model.title"
                }

                ChapterTable.insert {
                    it[ChapterTable.id] = model.id
                    it[ChapterTable.title] = model.title
                    it[ChapterTable.textID] = 1
                    it[ChapterTable.languageID] = model.languageID
                    it[ChapterTable.userID] = model.userID
                    it[ChapterTable.rating] = model.rating
                }
            }
        }
    }

    private fun Database.insertComments(comments: List<TestComment>) {
        transaction(this) {
            comments.forEach { comment ->
                CommentTable.insert {
                    it[CommentTable.contentID] = comment.contentID
                    it[CommentTable.userID] = comment.userID
                    it[CommentTable.text] = comment.text
                }
            }
        }
    }

    private fun Database.insertTags(tags: List<TestTag>) {
        transaction(this) {
            tags.forEach { tag ->
                TagTable.insert { insertStatement ->
                    insertStatement[TagTable.id] = tag.id
                }

                tag.translations.forEach { tagTranslation ->
                    val translationID = TagTranslationTable.insertAndGetId { insertStatement ->
                        insertStatement[TagTranslationTable.id] = tagTranslation.id
                        insertStatement[TagTranslationTable.langID] = tagTranslation.langID
                        insertStatement[TagTranslationTable.translation] = tagTranslation.name
                    }

                    RelationTagAndTranslationTable.insert {
                        it[RelationTagAndTranslationTable.tagID] = tag.id
                        it[RelationTagAndTranslationTable.translationID] = translationID
                    }
                }
            }
        }
    }
}