package database.utils

import database.internal.mock.allLanguages
import database.internal.mock.allTags
import database.internal.model.DBTagTranslation

internal fun getTagByID(tagID: Int) = allTags.first { it.id == tagID }

/**
 * Find tag translation for default environmentID.
 * Get default environmentID from [allLanguages] variable by [getDefaultLangID].
 * @throws [NoSuchElementException]. Throw if default language not exist, see [getDefaultLangID]
 * */
@Throws(NoSuchElementException::class)
internal fun List<DBTagTranslation>.getTagTranslationForDefaultEnvironment() = first {
    it.envID == allLanguages.getDefaultLangID()
}
    .name

/**
 * Find translation for [envID].
 * @throws [NoSuchElementException]. Throw if translation not founded.
 * */
@Throws(NoSuchElementException::class)
internal fun List<DBTagTranslation>.getTagTranslationForEnvironment(envID: Int) = first { it.envID == envID }.name
