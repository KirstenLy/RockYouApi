package database.internal.executor

import database.external.model.language.LanguageFull
import database.external.model.language.LanguageTranslation
import database.external.result.GetAllLanguageWithTranslationsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import migrations.Language
import rockyouapi.Database
import rockyouapi.languagetranslation.SelectAllLanguageTranslations

/** @see execute */
internal class ReadAllLanguageWithTranslationsListExecutor(private val database: Database) {

    /**
     * Get all supported languages.
     *
     * Respond as:
     * - [GetAllLanguageWithTranslationsResult.Data] Request finished without errors.
     * - [GetAllLanguageWithTranslationsResult.LanguagesNotExist] Database doesn't contain any languages.
     * - [GetAllLanguageWithTranslationsResult.DefaultLanguageMissed] Database doesn't contain information about default language.
     * - [GetAllLanguageWithTranslationsResult.TranslationsNotExist] Database doesn't contain any language translations.
     * - [GetAllLanguageWithTranslationsResult.TranslationsMissed] Some language miss translation for one or more languages.
     * - [GetAllLanguageWithTranslationsResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(): GetAllLanguageWithTranslationsResult {
        return withContext(Dispatchers.IO) {
            try {
                val supportedLanguageBaseInfoList = database
                    .selectLanguageQueries
                    .selectAllLanguages()
                    .executeAsList()

                if (supportedLanguageBaseInfoList.isEmpty()) {
                    return@withContext GetAllLanguageWithTranslationsResult.LanguagesNotExist
                }

                if (supportedLanguageBaseInfoList.none(Language::isDefault)) {
                    return@withContext GetAllLanguageWithTranslationsResult.DefaultLanguageMissed
                }

                val supportedLanguageTranslationList = database
                    .selectLanguageTranslationQueries
                    .selectAllLanguageTranslations()
                    .executeAsList()
                    .map(SelectAllLanguageTranslations::toSupportedLanguageTranslation)

                if (supportedLanguageTranslationList.isEmpty()) {
                    return@withContext GetAllLanguageWithTranslationsResult.TranslationsNotExist
                }

                val supportedLanguageIDList = supportedLanguageBaseInfoList.map(Language::id)

                val languageFullList = supportedLanguageBaseInfoList.map { languageBaseInfo ->

                    val translationListForLanguage = supportedLanguageTranslationList.filter {
                        it.languageID == languageBaseInfo.id
                    }

                    // Every language must be translated for every language.
                    // F.e if we have 50 languages, every language must have 50 translations.
                    val isAllTranslationPresented = translationListForLanguage
                        .map(LanguageTranslation::environmentID)
                        .containsAll(supportedLanguageIDList)

                    if (!isAllTranslationPresented) {
                        return@withContext GetAllLanguageWithTranslationsResult.TranslationsMissed
                    }

                    LanguageFull(
                        languageID = languageBaseInfo.id,
                        isDefault = languageBaseInfo.isDefault,
                        translations = translationListForLanguage
                    )
                }

                return@withContext GetAllLanguageWithTranslationsResult.Data(languageFullList)
            } catch (t: Throwable) {
                return@withContext GetAllLanguageWithTranslationsResult.Error(t)
            }
        }
    }
}

private fun SelectAllLanguageTranslations.toSupportedLanguageTranslation() = LanguageTranslation(
    languageID = langID,
    environmentID = envID,
    translation = translation
)