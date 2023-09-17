package database.internal.test

import common.pairToRandomValueFrom
import common.takeRandomValues
import common.times
import database.external.ContentType
import database.external.test.*
import database.internal.model.ForkVariant
import database.internal.model.StoryNode
import database.internal.test.model.TestCommentCreationData
import database.internal.test.model.TestModelsStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import rockyouapi.DBTest

internal fun DBTest.fillFullByGeneratedContent(): TestModelsStorage {
    val users = insertDefaultUsers()
    val usersAuthData = insertDefaultUsersAuthData()
    val authors = insertDefaultAuthors()
    val languages = insertLanguages()
    val tags = insertTags(languages.map(TestLanguage::id))

    val contentRegisters = insertRandomContentRegisters()

    val picturesIDs = contentRegisters.filterByContentType(ContentType.PICTURE).map(TestContentRegister::id)
    val videosIDs = contentRegisters.filterByContentType(ContentType.VIDEO).map(TestContentRegister::id)
    val storiesIDs = contentRegisters.filterByContentType(ContentType.STORY).map(TestContentRegister::id)
    val storyChaptersIDs = contentRegisters.filterByContentType(ContentType.STORY_CHAPTER).map(TestContentRegister::id)

    val usersIDs = users.map(TestUser::id)
    val authorsIDs = authors.map(TestAuthor::id)
    val languageIDs = languages.map(TestLanguage::id)
    val tagsIDs = tags.map(TestTag::id)

    val pictures = insertPictures(
        availableRegisterIDs = picturesIDs,
        availableUsersIDs = usersIDs,
        availableAuthorsIDs = authorsIDs,
        availableLanguagesIDs = languageIDs,
        availableTagsIDs = tagsIDs
    )

    val videos = insertVideos(
        availableRegisterIDs = videosIDs,
        availableUsersIDs = usersIDs,
        availableAuthorsIDs = authorsIDs,
        availableLanguagesIDs = languageIDs,
        availableTagsIDs = tagsIDs
    )

    val stories = insertStoriesWithChapters(
        availableStoriesIDs = storiesIDs,
        availableChaptersIDs = storyChaptersIDs,
        availableUsersIDs = usersIDs,
        availableAuthorsIDs = authorsIDs,
        availableLanguagesIDs = languageIDs,
        availableTagsIDs = tagsIDs
    )

    val comments = contentRegisters
        .map(TestContentRegister::id)
        .times(20)
        .shuffled()
        .pairToRandomValueFrom(users)
        .takeRandomValues()
        .map { (registerID, user) -> TestCommentCreationData(registerID, user.id, user.name) }
        .let(::insertComments)

    return TestModelsStorage(
        users = users,
        authors = authors,
        languages = languages,
        tags = tags,
        usersAuthData = usersAuthData,
        contentRegisters = contentRegisters,
        pictures = pictures,
        videos = videos,
        stories = stories,
        storyChapters = emptyList(),
        storyNodes = emptyList(),
        favorites = emptyList(),
        comments = comments,
    )
}

internal fun DBTest.fillPartiallyByGeneratedContent(): TestModelsStorage {
    val users = insertDefaultUsers()
    val authors = insertDefaultAuthors()
    val languages = insertLanguages()
    val tags = insertTags(languages.map(TestLanguage::id))

    return TestModelsStorage(
        users = users,
        authors = authors,
        languages = languages,
        tags = tags,
        usersAuthData = emptyList(),
        contentRegisters = emptyList(),
        pictures = emptyList(),
        videos = emptyList(),
        stories = emptyList(),
        storyChapters = emptyList(),
        storyNodes = emptyList(),
        favorites = emptyList(),
        comments = emptyList(),
    )
}

internal fun DBTest.insertRandomContentRegisters(): List<TestContentRegister> {
    val registers = generateContentRegisters(
        picturesSize = 40,
        videosSize = 40,
        storiesSize = 40,
        chaptersSize = 120
    )
    registers.forEach {
        contentRegisterQueries.insert(
            id = it.id,
            contentType = it.contentType.toByte(),
            contentID = it.contentID
        )
    }
    return registers
}

internal fun DBTest.insertPictures(
    availableRegisterIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>
): List<TestPicture> {
    val pictures = generateTestPictures(
        availableRegisterIDs = availableRegisterIDs,
        availableUsersIDs = availableUsersIDs,
        availableAuthorsIDs = availableAuthorsIDs,
        availableLanguagesIDs = availableLanguagesIDs,
        availableTagsIDs = availableTagsIDs
    )

    pictures.forEach { picture ->
        pictureInsertQueries.insert(
            id = picture.id,
            title = picture.title,
            url = picture.url,
            languageID = picture.languageID?.toInt(),
            userID = picture.userID,
            rating = picture.rating,
        )

        picture.authorsIDs?.forEach { pictureAuthorID ->
            relationPictureAndAuthorInsertQueries.insert(
//                id = null,
                pictureID = picture.id,
                authorID = pictureAuthorID
            )
        }

        picture.availableLanguagesIDs?.forEach { pictureAvailableLangID ->
            relationPictureAndLanguageInsertQueries.insert(
                id = null,
                pictureID = picture.id,
                langID = pictureAvailableLangID.toInt()
            )
        }

        picture.tagsIDs?.forEach { pictureTagID ->
            relationPictureAndTagInsertQueries.insert(
                id = null,
                pictureID = picture.id,
                tagID = pictureTagID
            )
        }
    }
    return pictures
}

internal fun DBTest.insertVideos(
    availableRegisterIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>
): List<TestVideo> {
    val videos = generateTestVideos(
        availableRegisterIDs = availableRegisterIDs,
        availableUsersIDs = availableUsersIDs,
        availableAuthorsIDs = availableAuthorsIDs,
        availableLanguagesIDs = availableLanguagesIDs,
        availableTagsIDs = availableTagsIDs
    )

    videos.forEach { video ->
        videoInsertQueries.insert(
            id = video.id,
            title = video.title,
            url = video.url,
            languageID = video.languageID?.toInt(),
            userID = video.userID,
            rating = video.rating,
        )

        video.authorsIDs?.forEach { videoAuthorID ->
            relationVideoAndAuthorInsertQueries.insert(
                id = null,
                videoID = video.id,
                authorID = videoAuthorID
            )
        }

        video.availableLanguagesIDs?.forEach { videoAvailableLangID ->
            relationVideoAndLanguageInsertQueries.insert(
                id = null,
                videoID = video.id,
                langID = videoAvailableLangID.toInt()
            )
        }

        video.tagsIDs?.forEach { videoTagID ->
            relationVideoAndTagInsertQueries.insert(
                id = null,
                videoID = video.id,
                tagID = videoTagID
            )
        }
    }

    return videos
}

internal fun DBTest.insertStoriesWithChapters(
    availableStoriesIDs: List<Int>,
    availableChaptersIDs: List<Int>,
    availableUsersIDs: List<Int>,
    availableAuthorsIDs: List<Int>,
    availableLanguagesIDs: List<Byte>,
    availableTagsIDs: List<Short>
): List<TestStory> {
    val stories = generateTestStories(
        availableStoriesIDs = availableStoriesIDs,
        availableChaptersIDs = availableChaptersIDs,
        availableUsersIDs = availableUsersIDs,
        availableAuthorsIDs = availableAuthorsIDs,
        availableLanguagesIDs = availableLanguagesIDs,
        availableTagsIDs = availableTagsIDs
    )

    stories.forEach { story ->
        val chaptersScheme = Json.encodeToString(story.storyNodes.toDeclarationScheme())
        storyInsertQueries.insert(
            id = story.id,
            title = story.title,
            languageID = story.languageID.toInt(),
            userID = story.userID,
            rating = story.rating,
            scheme = chaptersScheme
        )

        story.authorsIDs?.forEach { storyAuthorID ->
            relationStoryAndAuthorInsertQueries.insert(
                id = null,
                storyID = story.id,
                authorID = storyAuthorID
            )
        }

        story.availableLanguagesIDs.forEach { storyAvailableLangID ->
            relationStoryAndLanguageInsertQueries.insert(
                id = null,
                storyID = story.id,
                langID = storyAvailableLangID.toInt()
            )
        }

        story.tagsIDs.forEach { storyTagID ->
            relationStoryAndTagInsertQueries.insert(
                id = null,
                storyID = story.id,
                tagID = storyTagID
            )
        }

        story.storyNodes.extractAllChapters().forEach { storyChapter ->
            chapterInsertQueries.insert(
                id = storyChapter.id,
                storyID = storyChapter.storyID,
                title = storyChapter.title,
                languageID = storyChapter.languageID.toInt(),
                userID = storyChapter.userID,
                rating = storyChapter.rating,
                text = storyChapter.text
            )

            storyChapter.authorsIDs?.forEach { storyChapterAuthorID ->
                relationChapterAndAuthorInsertQueries.insert(
                    id = null,
                    chapterID = storyChapter.id,
                    authorID = storyChapterAuthorID
                )
            }

            storyChapter.availableLanguagesIDs.forEach { storyChapterAvailableLangID ->
                relationChapterAndLanguageInsertQueries.insert(
                    id = null,
                    chapterID = storyChapter.id,
                    langID = storyChapterAvailableLangID.toInt()
                )
            }

            storyChapter.tagsIDs.forEach { storyChapterTagID ->
                relationChapterAndTagInsertQueries.insert(
                    id = null,
                    chapterID = storyChapter.id,
                    tagID = storyChapterTagID
                )
            }
        }
    }

    return stories
}

internal fun DBTest.insertDefaultUsers(): List<TestUser> {
    val users = generateUsers()
    users.forEach {
        userProdQueries.insert(id = it.id, name = it.name)
    }
    return users
}

internal fun DBTest.insertDefaultUsersAuthData(): List<TestUserAuthData> {
    val usersAuthData = generateUsersDefaultAuthData()
    usersAuthData.forEach {
        userAuthDataInsertQueries.insert(
            id = it.id,
            userID = it.userID,
            password = it.password
        )
    }
    return usersAuthData
}

internal fun DBTest.insertDefaultAuthors(): List<TestAuthor> {
    val authors = getAuthors()
    authors.forEach {
        authorTestQueries.insert(id = it.id, name = it.name)
    }
    return authors
}

// Тут написать что вставляются сначала языки, затем переводы, чтобы не ломать ссылочную целостность
internal fun DBTest.insertLanguages(): List<TestLanguage> {
    val languages = getLanguages()

    languages.forEach { language ->
        languageInsertQueries.insert(
            id = language.id.toInt(),
            isDefault = language.isDefault
        )
    }

    // Тут переделать чуть
    languages.forEach { language ->
        language.translations.forEach { translation ->
            languageTranslationInsertQueries.insert(
                id = translation.id.toInt(),
                langID = language.id.toInt(),
                envID = translation.envID.toInt(),
                translation = translation.name
            )
        }
    }

    return languages
}

internal fun DBTest.insertTags(availableLanguagesIDs: List<Byte>): List<TestTag> {
    val tags = getTags(availableLanguagesIDs)

    tags.forEach { tag ->
        tagInsertQueries.insert(id = tag.id)

        tag.translations.forEach { translation ->
            tagTranslationTestQueries.insert(
                id = translation.id,
                tagID = tag.id,
                langID = translation.envID.toInt(),
                translation = translation.name
            )
        }
    }

    return tags
}

internal fun DBTest.insertComments(commentModelList: List<TestCommentCreationData>): List<TestComment> {
    val comments = generateTestComments(commentModelList)
    comments.forEach { comment ->
        insertCommentQueries.insert(
            id = comment.id,
            contentID = comment.contentID,
            userID = comment.userID,
            text = comment.text
        )
    }
    return comments
}

internal fun List<TestContentRegister>.filterByContentType(contentType: ContentType) = filter {
    it.contentType == contentType.typeID
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

private fun List<TestStoryNode>.toDeclarationScheme(): List<StoryNode> {
    val resultScheme = mutableListOf<StoryNode>()
    forEach { node ->
        when (node) {
            is TestStoryNode.TestChapterNode -> resultScheme.add(StoryNode.ChapterNode(node.chapter.id))

            is TestStoryNode.TestForkNode -> {
                val forkNode = StoryNode.ForkNode(
                    variants = node.variants.toDeclarationForkVariants()
                )
                resultScheme.add(forkNode)
            }
        }
    }
    return resultScheme
}

private fun List<TestStoryForkVariant>.toDeclarationForkVariants(): List<ForkVariant> {
    val resultScheme = mutableListOf<ForkVariant>()
    forEach { forkVariant ->
        resultScheme.add(
            ForkVariant(
                variantText = forkVariant.variantText,
                nodes = forkVariant.nodes.toDeclarationScheme()
            )
        )
    }
    return resultScheme
}