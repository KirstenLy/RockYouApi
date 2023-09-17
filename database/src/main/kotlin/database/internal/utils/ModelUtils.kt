package database.internal.utils

import database.internal.AvailableLanguageModel
import database.internal.LanguageTranslationModel
import declaration.entity.Lang
import kotlin.jvm.Throws

/**
 * Find language translation for specific language or for default if specific language not presented.
 * [langID] can not be presented if clients don't put it in request.
 * */
@Throws(IllegalStateException::class)
internal fun List<LanguageTranslationModel>.findByLangIDOrGetDefault(langID: Byte?, defaultLanguageID: Byte) =
    firstOrNull { it.languageID == langID }
        ?: firstOrNull { it.languageID == defaultLanguageID }
        ?: throw IllegalStateException("No default language found by default language ID: $defaultLanguageID")


/**
 * Find default language id or throw exception.
 * The List MUST contain default language ID, because client can require content and not specify environment identifier.
 * */
// TODO: Тут бы бросать своё IllegalStateException
@Throws(NoSuchElementException::class)
internal fun List<AvailableLanguageModel>.getDefaultLangID() = first(AvailableLanguageModel::isDefault).languageID

/**
 * Check the language with [langID] ID supported.
 * Supported means it is a valid environment language.
 * */
internal fun List<AvailableLanguageModel>.isLangIDSupported(langID: Byte) =
    langID in map(AvailableLanguageModel::languageID)

/** Select language with specific name translation. */
// TODO: Тут тоже надо бы бросать исключение, т.к не факт что по аргументам что - то найдётся
internal fun List<AvailableLanguageModel>.selectByIdAndEnv(langID: Byte, envID: Byte): Lang {
    val languageModel = firstOrNull { it.languageID == langID }
        ?: throw IllegalStateException("Can't find language for langID: $langID")

    val languageTranslation = languageModel.translations
        .firstOrNull { it.environmentID == envID }
        ?.translation
        ?: throw IllegalStateException("Can't find language translation for langID: $langID and envID: $envID")

    return Lang(languageModel.languageID, languageTranslation)
}