package rockyouapi

import common.toMap
import database.external.test.*
import rockyouapi.model.UserWithPassword

// TODO: Избавиться тут от привязок к 1 2 3 в ContentType
internal fun TestEnv.extractContentAsPicturesMap() = modelsStorage.contentRegisters.toMap(
    keyTransformer = TestContentRegister::id,
    valueTransformer = {
        if (it.contentType == 1) {
            modelsStorage.pictures.firstOrNull { picture -> it.contentID == picture.id }
        } else {
            null
        }
    }
)

internal fun TestEnv.extractContentAsVideosMap() = modelsStorage.contentRegisters.toMap(
    keyTransformer = TestContentRegister::id,
    valueTransformer = {
        if (it.contentType == 2) {
            modelsStorage.videos.firstOrNull { video -> it.contentID == video.id }
        } else {
            null
        }
    }
)

internal fun TestEnv.extractFirstPictures() = modelsStorage.contentRegisters.mapIndexedNotNull { idx, register ->
    if (register.contentType != 1) return@mapIndexedNotNull null
    val pictureID = register.contentID
    val picture = modelsStorage.pictures.findByID(pictureID) ?: return@mapIndexedNotNull null
    return@mapIndexedNotNull register.id to picture
}

internal fun TestEnv.extractVideosFromNewToOld() = modelsStorage.contentRegisters
    .reversed()
    .mapNotNull { register ->
        if (register.contentType != 2) return@mapNotNull null
        val videoID = register.contentID
        val video = modelsStorage.videos.findByID(videoID) ?: return@mapNotNull null
        return@mapNotNull register.id to video
    }

internal fun TestEnv.getRandomUserWithPassword(): UserWithPassword {
    val user = modelsStorage.users.random()
    val authDataForUser = modelsStorage.usersAuthData.first { it.userID == user.id }
    return UserWithPassword(user.id!!, user.name, authDataForUser.password)
}

internal fun List<TestContentRegister>.getRegisterIDsByContentType(contentType: Int) =
    filter { it.contentType == contentType }.map(TestContentRegister::id)

internal fun List<TestPicture>.findByID(pictureID: Int) = find { it.id == pictureID }

internal fun List<TestVideo>.findByID(videoID: Int) = find { it.id == videoID }

internal fun List<TestTag>.findByID(tagID: Short) = find { it.id == tagID }

internal fun List<TestTagTranslation>.findByLangID(langID: Byte) = find { it.envID == langID }

internal fun List<TestLanguage>.findByLangID(langID: Byte) = find { it.id == langID }

internal fun List<TestLanguage>.findDefaultLangID() = first(TestLanguage::isDefault).id

internal fun List<TestLanguageTranslation>.findByLangID(langID: Byte) = find { it.langID == langID }

internal fun List<TestComment>.countCommentsByContentID(contentID: Int) = count { it.contentID == contentID }

internal fun List<TestComment>.getCommentsByContentID(contentID: Int) = filter { it.contentID == contentID }