package database.internal.mock

import database.external.security.hashPassword
import rockyouapi.Database

/**
 * Fill database with test data.
 * User's password is his hashed name. Name hashed by [hashPassword] util.
 * Don't fill tokens.
 * */
internal fun Database.fillFullByMockContent() {
    insertMockUsersRole()
    insertMockUsers()
    insertMockAuthors()
    insertMockLanguages()
    insertMockTags()
    insertMockUsersAuthData()

    insertMockContent()
    insertMockContentGroups()

    insertMockPictures()
    insertMockVideos()
    insertMockStory()
    insertMockChapters()

    insertMockComments()

    insertMockGroupMembers()
}

private fun Database.insertMockContent() {
    pictureContentList.forEach {
        insertRegisterQueries.insert(
            id = it.id,
            originalContentID = null,
            contentType = it.contentTypeID,
            title = it.title,
            description = it.description,
            rating = it.rating
        )
    }

    videoContent.forEach {
        insertRegisterQueries.insert(
            id = it.id,
            originalContentID = null,
            contentType = it.contentTypeID,
            title = it.title,
            description = it.description,
            rating = it.rating
        )
    }

    storyContent.forEach {
        insertRegisterQueries.insert(
            id = it.id,
            originalContentID = null,
            contentType = it.contentTypeID,
            title = it.title,
            description = it.description,
            rating = it.rating
        )
    }

    storyChapterContent.forEach {
        insertRegisterQueries.insert(
            id = it.id,
            originalContentID = null,
            contentType = it.contentTypeID,
            title = it.title,
            description = it.description,
            rating = it.rating
        )
    }
}

private fun Database.insertMockContentGroups() {
    contentGroupList.forEach {
        insertGroupQueries.insert(
            id = it.id,
            title = it.title,
        )
    }
}

private fun Database.insertMockPictures() {
    pictureList.forEach { picture ->
        insertPictureQueries.insert(
            id = picture.id,
            url = picture.url,
            languageID = picture.languageID,
            userID = picture.userID,
        )

        picture.authorsIDs?.forEach { pictureAuthorID ->
            insertRelationContentAndAuthorQueries.insert(
                contentID = picture.id,
                authorID = pictureAuthorID
            )
        }

        picture.availableLanguagesIDs?.forEach { pictureAvailableLangID ->
            insertRelationContentAndLanguageQueries.insert(
                contentID = picture.id,
                languageID = pictureAvailableLangID
            )
        }

        picture.tagsIDs?.forEach { pictureTagID ->
            insertRelationContentAndTagQueries.insert(
                contentID = picture.id,
                tagID = pictureTagID
            )
        }
    }
}

private fun Database.insertMockVideos() {
    video.forEach { video ->
        insertVideoQueries.insert(
            id = video.id,
            url = video.url,
            languageID = video.languageID,
            userID = video.userID,
        )

        video.authorsIDs?.forEach { videoAuthorID ->
            insertRelationContentAndAuthorQueries.insert(
                contentID = video.id,
                authorID = videoAuthorID
            )
        }

        video.availableLanguagesIDs?.forEach { videoAvailableLangID ->
            insertRelationContentAndLanguageQueries.insert(
                contentID = video.id,
                languageID = videoAvailableLangID
            )
        }

        video.tagsIDs?.forEach { videoTagID ->
            insertRelationContentAndTagQueries.insert(
                contentID = video.id,
                tagID = videoTagID
            )
        }
    }
}

private fun Database.insertMockStory() {
    storyList.forEach { story ->
        insertStoryQueries.insert(
            id = story.id,
            languageID = story.languageID,
            userID = story.userID,
            scheme = story.scheme
        )

        story.authorsIDs?.forEach { storyAuthorID ->
            insertRelationContentAndAuthorQueries.insert(
                contentID = story.id,
                authorID = storyAuthorID
            )
        }

        story.availableLanguagesIDs.forEach { storyAvailableLangID ->
            insertRelationContentAndLanguageQueries.insert(
                contentID = story.id,
                languageID = storyAvailableLangID
            )
        }

        story.tagsIDs.forEach { storyTagID ->
            insertRelationContentAndTagQueries.insert(
                contentID = story.id,
                tagID = storyTagID
            )
        }
    }
}

private fun Database.insertMockChapters() {
    storyChapters.forEach { chapter ->
        insertChapterQueries.insert(
            id = chapter.id,
            storyID = chapter.storyID,
            languageID = chapter.languageID,
            userID = chapter.userID,
            text = chapter.text
        )

        chapter.authorsIDs?.forEach { storyChapterAuthorID ->
            insertRelationContentAndAuthorQueries.insert(
                contentID = chapter.id,
                authorID = storyChapterAuthorID
            )
        }

        chapter.availableLanguagesIDs.forEach { storyChapterAvailableLangID ->
            insertRelationContentAndLanguageQueries.insert(
                contentID = chapter.id,
                languageID = storyChapterAvailableLangID
            )
        }

        chapter.tagsIDs.forEach { storyChapterTagID ->
            insertRelationContentAndTagQueries.insert(
                contentID = chapter.id,
                tagID = storyChapterTagID
            )
        }
    }
}

private fun Database.insertMockUsers() {
    allUsers.forEach {
        insertUserQueries.insert(
            id = it.id,
            name = it.name,
            role = it.role
        )
    }
}

private fun Database.insertMockUsersRole() {
    allUserRole.forEach {
        insertUserRoleQueries.insert(
            id = it.id,
            name = it.name
        )
    }
}

private fun Database.insertMockUsersAuthData() {
    allUserAuthData.forEach {
        insertAuthQueries.insert(
            id = it.id,
            userID = it.userID,
            passwordHash = it.password,
            refreshToken = null
        )
    }
}

private fun Database.insertMockAuthors() {
    allAuthors.forEach {
        insertAuthorQueries.insert(
            id = it.id,
            name = it.name
        )
    }
}

private fun Database.insertMockLanguages() {
    allLanguages.forEach { language ->
        insertLanguageQueries.insert(
            id = language.id,
            isDefault = language.isDefault
        )
    }

    allLanguages.forEach { language ->
        language.translations.forEach { translation ->
            insertLanguageTranslationQueries.insert(
                id = translation.id,
                langID = language.id,
                envID = translation.envID,
                translation = translation.name
            )
        }
    }
}

private fun Database.insertMockTags() {
    allTags.forEach { tag ->
        insertTagQueries.insert(id = tag.id)

        tag.translations.forEach { translation ->
            insertTagTranslationQueries.insert(
                id = translation.id,
                tagID = tag.id,
                langID = translation.envID,
                translation = translation.name
            )
        }
    }
}

private fun Database.insertMockComments() {
    allComment.forEach { comment ->
        insertCommentQueries.insert(
            id = comment.id,
            contentID = comment.contentID,
            userID = comment.userID,
            text = comment.text,
            creationDate = comment.creationDate
        )
    }
}

private fun Database.insertMockGroupMembers() {
    allContentGroupMember.forEach { groupMember ->
        insertGroupMemberQueries.insert(
            id = groupMember.id,
            contentID = groupMember.contentID,
            groupID = groupMember.groupID,
            orderIDX = groupMember.orderIDX
        )
    }
}