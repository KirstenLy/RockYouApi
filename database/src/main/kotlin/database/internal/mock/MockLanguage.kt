package database.internal.mock

import database.internal.model.DBLanguage
import database.internal.toSupportedLanguage

// 1 - Russian
// 2 - English (Default language)
// 3 - French
// 4 - German
internal val language1 = DBLanguage(
    id = 1,
    isDefault = false,
    translations = languageID1Translations
)

internal val language2 = DBLanguage(
    id = 2,
    isDefault = true,
    translations = languageID2Translations
)

internal val language3 = DBLanguage(
    id = 3,
    isDefault = false,
    translations = languageID3Translations
)

internal val language4 = DBLanguage(
    id = 4,
    isDefault = false,
    translations = languageID4Translations
)

internal val allLanguages get() = listOf(language1, language2, language3, language4)

internal val allLanguagesAsSupportedLanguages get() = allLanguages.map(DBLanguage::toSupportedLanguage)