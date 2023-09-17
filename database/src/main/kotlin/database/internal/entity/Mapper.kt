package database.internal.entity

import database.internal.entity.author.AuthorEntity
import database.internal.entity.chapter.ChapterEntity
import database.internal.entity.lang.LanguageEntity
import database.internal.entity.picture.PictureEntity
import database.internal.entity.story.StoryEntity
import database.internal.entity.tag.TagEntity
import database.internal.entity.tag_translation.TagTranslationEntity
import database.internal.entity.user.UserEntity
import database.internal.entity.video.VideoEntity

import declaration.entity.*
import declaration.entity.story.Story
import declaration.entity.story.Chapter

internal fun PictureEntity.toDomain() = Picture(
    id = id.value,
    title = title,
    url = url,
    language = language?.toDomain(),
    availableLanguages = availableLanguages.map(LanguageEntity::toDomain),
    user = user.toDomain(),
    authors = authors.map(AuthorEntity::toDomain),
    rating = rating,
    tags = tags.map(TagEntity::toDomain),
    commentsCount = 0
)

internal fun VideoEntity.toDomain() = Video(
    id = id.value,
    title = title,
    url = url,
    language = language?.toDomain(),
    availableLanguages = availableLanguages.map(LanguageEntity::toDomain),
    user = user.toDomain(),
    authors = authors.map(AuthorEntity::toDomain),
    rating = rating,
    tags = tags.map(TagEntity::toDomain),
    commentsCount = 0
)

internal fun StoryEntity.toDomain() = Story(
    id = id.value,
    title = title,
    language = language.toDomain(),
    availableLanguages = availableLanguages.map(LanguageEntity::toDomain),
    user = user.toDomain(),
    authors = authors.map(AuthorEntity::toDomain),
    rating = rating,
    tags = tags.map(TagEntity::toDomain),
    chapters = chaptersBase.map(ChapterEntity::toDomain),
    commentsCount = 0
)

internal fun ChapterEntity.toDomain() = Chapter(
    id = id.value,
    storyID = 0,
    title = title,
    language = language.toDomain(),
    availableLanguages = availableLanguages.map(LanguageEntity::toDomain),
    user = user.toDomain(),
    authors = authors.map(AuthorEntity::toDomain),
    rating = rating,
    tags = tags.map(TagEntity::toDomain),
    commentsCount = 0
)

internal fun UserEntity.toDomain() = User(
    id = id.value,
    name = name
)

internal fun AuthorEntity.toDomain() = Author(
    id = id.value,
    name = name
)

internal fun LanguageEntity.toDomain() = Lang(
    id = id.value.toByte(),
    name = "title"
)

internal fun TagEntity.toDomain() = Tag(
    id = id.value.toShort(),
    name = translationInfo.firstOrNull()?.translation ?: ""
)

internal fun TagTranslationEntity.toDomain() = TagTranslation(
    langID = id.value,
    translation = translation
)

//internal fun DBUser.toDomain() = User(id, name)