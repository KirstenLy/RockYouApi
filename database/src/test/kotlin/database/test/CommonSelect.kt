package database.test

import database.internal.AvailableLanguageModel
import database.internal.LanguageTranslationModel
import rockyouapi.DBTest

internal fun DBTest.selectAvailableLanguages(): List<AvailableLanguageModel> {
    val availableLanguagesResult = languageQueries
        .selectAllWithTranslations()
        .executeAsList()

    val availableLanguages = mutableListOf<AvailableLanguageModel>()
    availableLanguagesResult.groupBy { it.id }.forEach { t, u ->
        availableLanguages.add(
            AvailableLanguageModel(
                languageID = t.toByte(),
                isDefault = u.all { it.isDefault },
                translations = u.map { LanguageTranslationModel(it.langID.toByte(), it.envID.toByte(), it.translation) }
            )
        )
    }
    return availableLanguages
}