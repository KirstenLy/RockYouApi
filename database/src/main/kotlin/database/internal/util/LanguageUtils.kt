package database.internal.util

import database.external.model.language.LanguageFull
import database.external.model.tag.TagFull
import database.external.model.language.LanguageSimple
import database.external.model.tag.TagSimple

/**
 * Resolve supposed environmentID depend on supported languages.
 * Used to prevent situation when invalid [supposedEnvironmentLangID] used.
 *
 * @return [supposedEnvironmentLangID] if it supported, default languageID otherwise.
 *
 * @throws [NoSuchElementException].
 * Throw when [supposedEnvironmentLangID] is null/unknown and there is no default language in [availableLanguageList].
 * [supposedEnvironmentLangID] is null/unsupported means caller want to get valid environmentID,
 * so it must be defined in [availableLanguageList].
 * */
internal fun resolveEnvironmentLangID(
    supposedEnvironmentLangID: Int?,
    availableLanguageList: List<LanguageFull>
) = when {
    supposedEnvironmentLangID == null -> availableLanguageList.getDefaultLangID()
    availableLanguageList.isLanguageIDSupported(supposedEnvironmentLangID) -> supposedEnvironmentLangID
    else -> availableLanguageList.getDefaultLangID()
}

/**
 * Select language with specific name translation.
 *
 * @return [LanguageSimple] if language with [languageID] and translation for [environmentID] exist.
 *
 * @throws [IllegalStateException]. Throw when:
 *  - Language for [languageID] not exist.
 *  - Language translation for [environmentID] not exist.
 *  */
internal fun List<LanguageFull>.selectByIdAndEnv(languageID: Int, environmentID: Int): LanguageSimple {
    val languageModel = firstOrNull { it.languageID == languageID }
        ?: throw IllegalStateException("Can't find language for langID: $languageID")

    val languageTranslation = languageModel.translations
        .firstOrNull { it.environmentID == environmentID }
        ?.translation
        ?: throw IllegalStateException("Can't find language translation for langID: $languageID and envID: $environmentID")

    return LanguageSimple(languageModel.languageID, languageTranslation)
}

/**
 * Select tag with specific name translation.
 *
 * @return [TagSimple] if tag with [tagID] and translation for [environmentID] exist.
 *
 * @throws [IllegalStateException]. Throw when:
 *  - Tag for [tagID] not exist.
 *  - Tag translation for [environmentID] not exist.
 *  */
internal fun List<TagFull>.selectTagByIdAndEnv(tagID: Int, environmentID: Int): TagSimple {
    val tagModel = firstOrNull { it.tagID == tagID } ?: throw IllegalStateException("Can't find tag for tagID: $tagID")

    val tagTranslation = tagModel.translations
        .firstOrNull { it.environmentID == environmentID }
        ?.translation
        ?: throw IllegalStateException("Can't find language translation for tagID: $tagID and envID: $environmentID")

    return TagSimple(tagModel.tagID, tagTranslation, false)
}

/**
 * Find default language id or throw exception.
 * The List MUST contain default language ID, because client can require content and not specify environment identifier.
 * @throws [NoSuchElementException]. Throw if default language not exist.
 * */
@Throws(NoSuchElementException::class)
internal fun List<LanguageFull>.getDefaultLangID() = first(LanguageFull::isDefault).languageID

/**
 * Check the language with [languageID] supported.
 * Supported means it is a valid environment language.
 * */
private fun List<LanguageFull>.isLanguageIDSupported(languageID: Int) =
    languageID in map(LanguageFull::languageID)
