package rockyouapi

import common.toMap
import database.external.model.*

// TODO: Избавиться тут от привязок к 1 2 3 в ContentType
internal fun TestModelsContainer.extractContentAsPicturesMap() = contentRegisters.toMap(
    keyTransformer = TestContentRegister::id,
    valueTransformer = {
        if (it.contentType == 1) {
            pictures.firstOrNull { picture -> it.contentID == picture.id }
        } else {
            null
        }
    }
)

internal fun TestModelsContainer.extractFirstPictures() = contentRegisters.mapIndexedNotNull { idx, register ->
    if (register.contentType != 1) return@mapIndexedNotNull null
    val pictureID = register.contentID
    val picture = pictures.findByID(pictureID) ?: return@mapIndexedNotNull null
    return@mapIndexedNotNull register.id to picture
}

internal fun List<TestContentRegister>.getRegisterIDsByContentType(contentType: Int) =
    filter { it.contentType == contentType }.map(TestContentRegister::id)

internal fun List<TestPicture>.findByID(pictureID: Int) = find { it.id == pictureID }

internal fun List<TestTag>.findByID(tagID: Int) = find { it.id == tagID }

internal fun List<TestTagTranslation>.findByLangID(langID: Int) = find { it.langID == langID }

internal fun List<TestLanguage>.findByLangID(langID: Int) = find { it.id == langID }

internal fun List<TestLanguage>.findDefaultLangID() = first(TestLanguage::isDefault).id

internal fun List<TestLanguageTranslation>.findByLangID(langID: Int) = find { it.langID == langID }

internal fun List<TestComment>.countCommentsByContentID(contentID: Int) = count { it.contentID == contentID }.toLong()


