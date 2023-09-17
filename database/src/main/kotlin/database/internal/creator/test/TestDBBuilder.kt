package database.internal.creator.test

import database.external.DatabaseAPI
import database.external.test.database.TestDatabaseBuilder
import database.external.test.*
import database.internal.AvailableLanguageModel
import database.internal.DatabaseAPIImpl
import database.internal.LanguageTranslationModel
import database.internal.executor.*
import database.internal.model.ForkVariant as DBForkVariant
import database.internal.model.StoryNode as DBStoryNode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import rockyouapi.DBTest

/** @see database.external.TestDatabaseBuilder */
// TODO: Напротив большинства методов написать,что в них не проверяются свящи с другими конкретными сущностями и они должны быть установлены правильно
internal class TestDBBuilder(private val database: DBTest) : TestDatabaseBuilder {

    private val languages: MutableList<TestLanguage> = mutableListOf()
    private val users: MutableList<TestUser> = mutableListOf()
    private val usersAuthData: MutableList<TestUserAuthData> = mutableListOf()
    private val authors: MutableList<TestAuthor> = mutableListOf()
    private val contentRegisters: MutableList<TestContentRegister> = mutableListOf()
    private val pictures: MutableList<TestPicture> = mutableListOf()
    private val videos: MutableList<TestVideo> = mutableListOf()
    private val stories: MutableList<TestStory> = mutableListOf()
    private val storyNodes: MutableList<Pair<Int, List<TestStoryNode>>> = mutableListOf()
    private val storyChapters: MutableList<TestStoryChapter> = mutableListOf()
    private val favorites: MutableList<TestFavorite> = mutableListOf()
    private val tags: MutableList<TestTag> = mutableListOf()
    private val comments: MutableList<TestComment> = mutableListOf()

    private val tagsRelationsWithTranslations: MutableList<TestRelationBetweenTagAndTagTranslation> = mutableListOf()

    override fun setLanguages(languages: List<TestLanguage>) {
        this.languages.clear()
        this.languages.addAll(languages)
    }

    override fun setUsers(users: List<TestUser>) {
        this.users.clear()
        this.users.addAll(users)
    }

    override fun setUsersAuthData(authData: List<TestUserAuthData>) {
        this.usersAuthData.clear()
        this.usersAuthData.addAll(authData)
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

    override fun setStoriesNodes(nodes: List<Pair<Int, List<TestStoryNode>>>) {
        this.storyNodes.clear()
        this.storyNodes.addAll(nodes)
    }

    override fun setFavorites(favorites: List<TestFavorite>) {
        this.favorites.clear()
        this.favorites.addAll(favorites)
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

    override fun build(): DatabaseAPI {
        val database = database.apply {
            if (languages.isNotEmpty()) insertLanguages(languages)
            if (users.isNotEmpty()) insertUsers(users)
            if (usersAuthData.isNotEmpty()) insertUsersAuthData(usersAuthData)
            if (authors.isNotEmpty()) insertAuthors(authors)
            if (tags.isNotEmpty()) insertTags(tags)

            if (contentRegisters.isNotEmpty()) insertContentRegisters(contentRegisters)
            if (pictures.isNotEmpty()) insertPictures(pictures)
            if (videos.isNotEmpty()) insertVideos(videos)
            if (stories.isNotEmpty()) insertStories(stories)
//            if (storyChapters.isNotEmpty()) insertChapters(storyChapters)
//            if (storyNodes.isNotEmpty()) insertStoryNodes(storyNodes)
            if (favorites.isNotEmpty()) insertFavorites(favorites)
            if (comments.isNotEmpty()) insertComments(comments)
        }

        val availableLanguagesResult = database.languageQueries
            .selectAllWithTranslations()
            .executeAsList()

        val availableLanguages = mutableListOf<AvailableLanguageModel>()
        availableLanguagesResult.groupBy { it.id }.forEach { t, u ->
            availableLanguages.add(
                AvailableLanguageModel(
                    languageID = t.toByte(),
                    isDefault = u.all { it.isDefault },
                    translations = u.map { LanguageTranslationModel(it.langID.toByte(), it.envID.toByte(), it.translation) }
                )
            )
        }

        val getPictureByIDRequestExecutor = GetPictureByIDRequestExecutor(
            database = database,
            availableLanguages = availableLanguages
        )

        val getVideoByIDRequestExecutor = GetVideoByIDRequestExecutor(
            database = database,
            availableLanguages = availableLanguages
        )

        val getChaptersRequestExecutor = GetChaptersRequestExecutor(
            database = database,
            availableLanguages = availableLanguages
        )

        val getStoryNodesRequestExecutor = GetStoryNodesRequestExecutor(
            database = database,
            supportedLanguages = availableLanguages,
        )

        val getStoryByIDRequestExecutor = GetStoryByIDRequestExecutor(
            database = database,
            availableLanguages = availableLanguages,
            storyChaptersRequestExecutor = getChaptersRequestExecutor,
        )

        return DatabaseAPIImpl(
            getPictureListRequestExecutor = GetPictureListRequestExecutor(
                database = database,
                availableLanguages = availableLanguages,
            ),
            getPictureByIDRequestExecutor = getPictureByIDRequestExecutor,
            getVideoByIDRequestExecutor = GetVideoByIDRequestExecutor(
                database = database,
                availableLanguages = availableLanguages
            ),
            getVideosListRequestExecutor = GetVideosListRequestExecutor(
                database = database,
                availableLanguages = availableLanguages
            ),
            getStoriesChaptersRequestExecutor = getChaptersRequestExecutor,
            getStoriesRequestExecutor = GetStoriesRequestExecutor(
                database = database,
                supportedLanguages = availableLanguages,
                storyByIDRequestExecutor = getStoryByIDRequestExecutor
            ),
            getStoryByIDRequestExecutor = getStoryByIDRequestExecutor,
            getChapterTextByIDRequestExecutor = GetChapterTextByIDRequestExecutor(
                database = database
            ),
            getContentByTextRequestExecutor = GetContentByTextRequestExecutor(
                getPicturesByTextRequestExecutor = GetPicturesByTextRequestExecutor(
                    database = database,
                    getPictureByIDRequestExecutor = getPictureByIDRequestExecutor
                ),
                getVideosByTextRequestExecutor = GetVideosByTextRequestExecutor(
                    database = database,
                    getVideoByIDRequestExecutor = getVideoByIDRequestExecutor,
                ),
                getStoriesByTextRequestExecutor = GetStoriesByTextRequestExecutor(
                    database = database,
                    getStoryByIDRequestExecutor = getStoryByIDRequestExecutor
                ),
                getChaptersByTextRequestExecutor = GetChaptersByTextRequestExecutor(
                    database = database,
                    getStoryByIDRequestExecutor = getChaptersRequestExecutor
                )
            ),
            getCommentsRequestExecutor = GetCommentsRequestExecutor(
                database = database
            ),
            createUserRequestExecutor = CreateUserRequestExecutor(
                database = database
            ),
            checkUserCredentialsExecutor = CheckUserCredentialsExecutor(
                database = database
            ),
            addCommentRequestExecutor = AddCommentRequestExecutor(
                database = database
            ),
            addOrRemoveFavoriteRequestExecutor = AddOrRemoveFavoriteRequestExecutor(
                database = database
            ),
            reportRequestExecutor = ReportRequestExecutor(
                database = database
            ),
            voteRequestExecutor = VoteRequestExecutor(
                database = database
            )
        )
    }

    private fun DBTest.insertLanguages(languages: List<TestLanguage>) {
        languages.forEach { language ->
            languageInsertQueries.insert(
                id = language.id.toInt(),
                isDefault = language.isDefault
            )

            language.translations.forEach { languageTranslation ->
                languageTranslationInsertQueries.insert(
                    id = languageTranslation.id.toInt(),
                    langID = languageTranslation.langID.toInt(),
                    envID = 1,
                    translation = languageTranslation.name,
                )

                val lastTransactionID = languageTranslationSelectQueries.selectLastInsertedID().executeAsOne()

//                relationLangAndTranslationQueries.insert(
//                    id = null,
//                    langID = language.id,
//                    translationID = lastTransactionID.toShort()
//                )
            }
        }
    }

    private fun DBTest.insertUsers(users: List<TestUser>) {
        users.forEach { model ->
            userTestQueries.insert(
                id = model.id,
                name = model.name
            )
        }
    }

    private fun DBTest.insertUsersAuthData(usersAuthData: List<TestUserAuthData>) {
        usersAuthData.forEach { model ->
            userAuthDataInsertQueries.insert(
                id = model.id,
                userID = model.userID,
                password = model.password,
            )
        }
    }

    private fun DBTest.insertAuthors(authors: List<TestAuthor>) {
        authors.forEach { model ->
            authorTestQueries.insert(
                id = model.id,
                name = model.name
            )
        }
    }

    private fun DBTest.insertTags(tags: List<TestTag>) {
        tags.forEach { tag ->
            tagInsertQueries.insert(tag.id)

            tag.translations.forEach { tagTranslation ->
                tagTranslationTestQueries.insert(
                    id = tagTranslation.id,
                    tagID = tag.id,
                    langID = tagTranslation.envID.toInt(),
                    translation = tagTranslation.name,
                )

                val lastTransactionID = tagTranslationQueries.selectLastInsertedID().executeAsOne()

//                relationTagAndTranslationInsertQueries.insert(
//                    id = null,
//                    tagID = tag.id,
//                    translationID = lastTransactionID.toShort()
//                )
            }
        }
    }

    private fun DBTest.insertContentRegisters(contentRegisterModels: List<TestContentRegister>) {
        contentRegisterModels.forEach { model ->
            contentRegisterQueries.insert(
                id = model.id,
                contentType = model.contentType.toByte(),
                contentID = model.contentID
            )
        }
    }

    private fun DBTest.insertPictures(pictures: List<TestPicture>) {
        pictures.forEach { picture ->
            pictureInsertQueries.insert(
                id = picture.id,
                title = picture.title,
                url = picture.url,
                languageID = picture.languageID?.toInt(),
                userID = picture.userID,
                rating = picture.rating
            )

            val pictureAvailableLanguages = picture.availableLanguagesIDs
            if (!pictureAvailableLanguages.isNullOrEmpty()) {
                pictureAvailableLanguages.forEach { languageID ->
                    relationPictureAndLanguageInsertQueries.insert(
                        id = null,
                        pictureID = picture.id,
                        langID = languageID.toInt()
                    )
                }
            }

            val pictureAuthors = picture.authorsIDs
            if (!pictureAuthors.isNullOrEmpty()) {
                pictureAuthors.forEach { authorID ->
                    relationPictureAndAuthorInsertQueries.insert(
//                        id = null,
                        pictureID = picture.id,
                        authorID = authorID
                    )
                }
            }

            val pictureTags = picture.tagsIDs
            if (!pictureTags.isNullOrEmpty()) {
                pictureTags.forEach { tagID ->
                    relationPictureAndTagInsertQueries.insert(
                        id = null,
                        pictureID = picture.id,
                        tagID = tagID
                    )
                }
            }
        }
    }

    private fun DBTest.insertVideos(videos: List<TestVideo>) {
        videos.forEach { video ->
            videoQueries.insert(
                id = video.id,
                title = video.title,
                url = video.url,
                languageID = video.languageID?.toInt(),
                userID = video.userID,
                rating = video.rating
            )

            val videoAvailableLanguages = video.availableLanguagesIDs
            if (!videoAvailableLanguages.isNullOrEmpty()) {
                videoAvailableLanguages.forEach { languageID ->
                    relationVideoAndLanguageInsertQueries.insert(
                        id = null,
                        videoID = video.id,
                        langID = languageID.toInt()
                    )
                }
            }

            val videoAuthors = video.authorsIDs
            if (!videoAuthors.isNullOrEmpty()) {
                videoAuthors.forEach { authorID ->
                    relationVideoAndAuthorInsertQueries.insert(
                        id = null,
                        videoID = video.id,
                        authorID = authorID
                    )
                }
            }

            val videoTags = video.tagsIDs
            if (!videoTags.isNullOrEmpty()) {
                videoTags.forEach { tagID ->
                    relationVideoAndTagInsertQueries.insert(
                        id = null,
                        videoID = video.id,
                        tagID = tagID
                    )
                }
            }
        }
    }

    private fun DBTest.insertStories(stories: List<TestStory>) {
        var recordIDX = 0
        stories.forEach { story ->
//            storyQueries.insert(
//                id = story.id,
//                title = story.title,
//                languageID = story.languageID.toByte(),
//                userID = story.userID,
//                rating = story.rating
//            )

            val storyNodes = story.storyNodes
            insertChapters(storyNodes.extractAllChapters())

            val storyRegisterID = story.storyNodes
                .filterIsInstance<TestStoryNode.TestChapterNode>()
                .first()
                .chapter
                .storyID

            insertStoryNodes1(recordIDX, storyRegisterID, storyNodes)
            recordIDX++
        }
    }

    private fun DBTest.insertChapters(chapters: List<TestStoryChapter>) {
        chapters.forEach { chapter ->
//            chapterQueries.insert(
//                id = chapter.id,
//                title = chapter.title,
//                storyID = chapter.storyID,
//                languageID = chapter.languageID.toByte(),
//                userID = chapter.userID,
//                rating = chapter.rating,
//                text = chapter.text
//            )

            val chapterAvailableLanguages = chapter.availableLanguagesIDs
            if (chapterAvailableLanguages.isNotEmpty()) {
                var recordIDX = 0
//                chapterAvailableLanguages.forEach { languageID ->
//                    relationChapterAndLanguageQueries.insert(
//                        id = recordIDX,
//                        chapterID = chapter.id,
//                        langID = languageID.toByte()
//                    )
//                    recordIDX++
//                }
            }

            val chapterAuthors = chapter.authorsIDs
            if (!chapterAuthors.isNullOrEmpty()) {
                var recordIDX = 0
//                chapterAuthors.forEach { authorID ->
//                    relationChapterAndAuthorQueries.insert(
//                        id = recordIDX,
//                        chapterID = chapter.id,
//                        authorID = authorID
//                    )
//                }
                recordIDX++
            }

            val chapterTags = chapter.tagsIDs
            if (chapterTags.isNotEmpty()) {
                var recordIDX = 0
                chapterTags.forEach { tagID ->
//                    relationChapterAndTagQueries.insert(
//                        id = recordIDX,
//                        chapterID = chapter.id,
//                        tagID = tagID.toShort()
//                    )
                }
                recordIDX++
            }
        }
    }

    private fun DBTest.insertStoryNodes1(nodeID: Int, storyRegisterID: Int, nodes: List<TestStoryNode>) {
        val dbStoryNode = nodes.toDBStoryNode()
        val storyNodesAsJSON = Json.encodeToString(dbStoryNode)
//        storyNodeQueries.insert(
//            nodeID,
//            storyRegisterID,
//            storyNodesAsJSON
//        )
    }

    private fun DBTest.insertFavorites(favorites: List<TestFavorite>) {
        favorites.forEach { comment ->
            insertFavoriteQueries.insert(
                id = comment.id,
                contentID = comment.contentID,
                userID = comment.userID,
            )
        }
    }

    private fun DBTest.insertComments(comments: List<TestComment>) {
        comments.forEach { comment ->
//            commentQueries.insert(
//                id = comment.id,
//                contentID = comment.contentID,
//                userID = comment.userID,
//                text = comment.text
//            )
        }
    }

    private fun List<TestStoryNode>.toDBStoryNode(): List<DBStoryNode> = map { node ->
        when (node) {
            is TestStoryNode.TestChapterNode -> DBStoryNode.ChapterNode(node.chapter.id)
            is TestStoryNode.TestForkNode -> DBStoryNode.ForkNode(
                variants = node.variants.map { variant ->
                    DBForkVariant(
                        variantText = variant.variantText,
                        nodes = variant.nodes.toDBStoryNode()
                    )
                }
            )
        }
    }

    private fun List<TestStoryNode>.extractAllChapters(): List<TestStoryChapter> {
        val result = mutableListOf<TestStoryChapter>()
        forEach { node ->
            when (node) {
                is TestStoryNode.TestChapterNode -> result.add(node.chapter)
                is TestStoryNode.TestForkNode -> {
                    node.variants
                        .map { it.nodes.extractAllChapters() }
                        .flatten()
                        .distinct()
                        .forEach(result::add)
                }
            }
        }
        return result
    }
}