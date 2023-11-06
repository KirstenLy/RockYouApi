package database.internal.executor

import database.external.model.language.LanguageFull
import database.external.model.tag.TagFull
import database.external.model.tag.TagTranslation
import database.external.result.GetAllLanguageWithTranslationsResult
import database.external.result.GetAllTagsWithTranslationsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.tag.SelectAllTagTranslations

/** @see execute */
internal class ReadAllTagsWithTranslationsListExecutor(
    private val database: Database,
    private val languageList: List<LanguageFull>
) {

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
    suspend fun execute(): GetAllTagsWithTranslationsResult {
        return withContext(Dispatchers.IO) {
            try {
                val supportedTagIDs = database
                    .selectTagQueries
                    .selectAllTags()
                    .executeAsList()

                if (supportedTagIDs.isEmpty()) {
                    return@withContext GetAllTagsWithTranslationsResult.TagsNotExist
                }

                val supportedTagTranslations = database
                    .selectTagQueries
                    .selectAllTagTranslations()
                    .executeAsList()
                    .map(SelectAllTagTranslations::toSupportedTagTranslation)

                if (supportedTagTranslations.isEmpty()) {
                    return@withContext GetAllTagsWithTranslationsResult.TranslationsNotExist
                }

                val languageIDList = languageList.map(LanguageFull::languageID)

                val tagList = supportedTagIDs.map { tagID ->

                    val translationsForTag = supportedTagTranslations.filter { it.tagID == tagID }

                    // Every tag must be translated for every language.
                    // F.e if we have 50 tags, every tag must have 50 translations.
                    val isAllTranslationPresented = translationsForTag
                        .map(TagTranslation::environmentID)
                        .containsAll(languageIDList)

                    if (!isAllTranslationPresented) {
                        return@withContext GetAllTagsWithTranslationsResult.TranslationsMissed
                    }

                    TagFull(tagID, translationsForTag)
                }

                return@withContext GetAllTagsWithTranslationsResult.Data(tagList)
            } catch (t: Throwable) {
                return@withContext GetAllTagsWithTranslationsResult.Error(t)
            }
        }
    }
}

private fun SelectAllTagTranslations.toSupportedTagTranslation() = TagTranslation(
    tagID = tagID,
    environmentID = envID,
    translation = translation
)