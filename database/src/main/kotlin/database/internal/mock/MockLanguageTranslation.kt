package database.internal.mock

import database.internal.model.DBLanguageTranslation

// 1 - Russian
// 2 - English (Default language)
// 3 - French
// 4 - German
internal val languageID1Translations = listOf(
    DBLanguageTranslation(
        id = 1,
        langID = 1,
        envID = 1,
        name = "Русский"
    ),
    DBLanguageTranslation(
        id = 2,
        langID = 1,
        envID = 2,
        name = "Russian"
    ),
    DBLanguageTranslation(
        id = 3,
        langID = 1,
        envID = 3,
        name = "Russe"
    ),
    DBLanguageTranslation(
        id = 4,
        langID = 1,
        envID = 4,
        name = "Russen"
    )
)

internal val languageID2Translations = listOf(
    DBLanguageTranslation(
        id = 5,
        langID = 2,
        envID = 1,
        name = "Английский"
    ),
    DBLanguageTranslation(
        id = 6,
        langID = 2,
        envID = 2,
        name = "English"
    ),
    DBLanguageTranslation(
        id = 7,
        langID = 2,
        envID = 3,
        name = "Anglais"
    ),
    DBLanguageTranslation(
        id = 8,
        langID = 2,
        envID = 4,
        name = "Englisch"
    )
)

internal val languageID3Translations = listOf(
    DBLanguageTranslation(
        id = 9,
        langID = 3,
        envID = 1,
        name = "Французский"
    ),
    DBLanguageTranslation(
        id = 10,
        langID = 3,
        envID = 2,
        name = "French"
    ),
    DBLanguageTranslation(
        id = 11,
        langID = 3,
        envID = 3,
        name = "Français"
    ),
    DBLanguageTranslation(
        id = 12,
        langID = 3,
        envID = 4,
        name = "Französisch"
    )
)

internal val languageID4Translations = listOf(
    DBLanguageTranslation(
        id = 13,
        langID = 4,
        envID = 1,
        name = "Немецкий"
    ),
    DBLanguageTranslation(
        id = 14,
        langID = 4,
        envID = 2,
        name = "German"
    ),
    DBLanguageTranslation(
        id = 15,
        langID = 4,
        envID = 3,
        name = "Saksan kieli"
    ),
    DBLanguageTranslation(
        id = 16,
        langID = 4,
        envID = 4,
        name = "Deutsch"
    )
)