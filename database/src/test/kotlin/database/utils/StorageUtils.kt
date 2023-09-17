package database.utils

import common.takeRandomValues
import database.external.ContentType
import database.external.test.TestPicture
import database.external.test.TestStory
import database.external.test.TestStoryChapter
import database.external.test.TestVideo
import database.internal.test.model.TestModelsStorage

internal fun TestModelsStorage.getRandomEntityIDsByContentType(contentType: ContentType) = when (contentType) {
    ContentType.PICTURE -> pictures.takeRandomValues().map(TestPicture::id)
    ContentType.VIDEO -> videos.takeRandomValues().map(TestVideo::id)
    ContentType.STORY -> stories.takeRandomValues().map(TestStory::id)
    ContentType.STORY_CHAPTER -> storyChapters.takeRandomValues().map(TestStoryChapter::id)
}

internal fun TestModelsStorage.getPictureListByIDList(ids: List<Int>) = pictures.first { it.id in ids }

internal fun TestModelsStorage.getRandomPictures() = pictures.takeRandomValues()

internal fun TestModelsStorage.findAuthorByID(authorID: Int) = authors.first { it.id == authorID }

internal fun TestModelsStorage.findTagByID(tagID: Short) = tags.first { it.id == tagID }

internal fun TestModelsStorage.isLanguageSupported(languageID: Byte) = languages.any { it.id == languageID }

internal fun TestModelsStorage.findDefaultLanguageID() = languages.first { it.isDefault }.id

internal fun TestModelsStorage.getPictureListWithListRequest(ids: List<Int>) = pictures.first { it.id in ids }

internal fun List<TestPicture>.getPicturesByImitateListRequest(limit: Long, offset: Long, langID: Byte?) =
    sortedByDescending(TestPicture::id)
        .filterPicturesByLangID(langID)
        .applyLimitAndOffsetByParams(limit, offset)

internal fun List<TestVideo>.getVideosByImitateListRequest(limit: Long, offset: Long, langID: Byte?) =
    sortedByDescending(TestVideo::id)
        .filterVideosByLangID(langID)
        .applyLimitAndOffsetByParams(limit, offset)

internal fun <T> List<T>.applyLimitAndOffsetByParams(limit: Long, offset: Long): List<T> {
    if (isEmpty()) return this

    val afterOffsetApplied = drop(offset.toInt())
    if (afterOffsetApplied.isEmpty()) return emptyList()

    return afterOffsetApplied.take(limit.toInt())
}

private fun List<TestPicture>.filterPicturesByLangID(langID: Byte?) = if (langID == null) this else {
    filter { it.languageID == langID }
}

private fun List<TestVideo>.filterVideosByLangID(langID: Byte?) = if (langID == null) this else {
    filter { it.languageID == langID }
}