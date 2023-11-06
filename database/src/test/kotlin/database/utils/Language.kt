package database.utils

import database.internal.mock.allLanguages
import database.internal.model.DBLanguage
import database.internal.model.DBLanguageTranslation
import database.internal.model.DBTagTranslation

/**
 * Find language translation for default environmentID.
 * Get default environmentID from [allLanguages] variable by [getDefaultLangID].
 * @throws [NoSuchElementException]. Throw if default language not exist, see [getDefaultLangID]
 * */
@Throws(NoSuchElementException::class)
internal fun List<DBLanguageTranslation>.getLanguageTranslationForDefaultEnvironment() = first {
    it.envID == allLanguages.getDefaultLangID()
}
    .name

/**
 * Find translation for [envID].
 * @throws [NoSuchElementException]. Throw if translation not founded.
 * */
@Throws(NoSuchElementException::class)
internal fun List<DBLanguageTranslation>.getLanguageTranslationForEnvironment(envID: Int) =
    first { it.envID == envID }.name


internal fun getLanguageByID(languageID: Int) = allLanguages.first { it.id == languageID }

/**
 * Find default language id or throw exception.
 * The List MUST contain default language ID, because client can require content and not specify environment identifier.
 * @throws [NoSuchElementException]. Throw if default language not exist.
 * */
@Throws(NoSuchElementException::class)
internal fun List<DBLanguage>.getDefaultLangID() = first(DBLanguage::isDefault).id