package database.internal.mock

import database.internal.model.DBTag
import database.internal.toSupportedTag

internal val tag1 = DBTag(
    id = 1,
    translations = tagID1Translations
)

internal val tag2 = DBTag(
    id = 2,
    translations = tagID2Translations
)

internal val tag3 = DBTag(
    id = 3,
    translations = tagID3Translations
)

internal val tag4 = DBTag(
    id = 4,
    translations = tagID4Translations
)

internal val tag5 = DBTag(
    id = 5,
    translations = tagID5Translations
)

internal val tag6 = DBTag(
    id = 6,
    translations = tagID6Translations
)

internal val tag7 = DBTag(
    id = 7,
    translations = tagID7Translations
)

internal val tag8 = DBTag(
    id = 8,
    translations = tagID8Translations
)

internal val tag9 = DBTag(
    id = 9,
    translations = tagID9Translations
)

internal val tag10 = DBTag(
    id = 10,
    translations = tagID10Translations
)

internal val allTags get() = listOf(tag1, tag2, tag3, tag4, tag5, tag6, tag7, tag8, tag9, tag10)

internal val allTagsAsSupportedTags get() = allTags.map(DBTag::toSupportedTag)