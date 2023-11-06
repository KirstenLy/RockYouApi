package database

import database.external.model.language.LanguageFull
import database.internal.util.getDefaultLangID
import database.internal.util.resolveEnvironmentLangID
import database.internal.mock.allLanguagesAsSupportedLanguages
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/** Test of [resolveEnvironmentLangID]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetEnvironmentLanguageIDDependOnAvailableLanguagesUtilTestSimple {

    @ParameterizedTest
    @ValueSource(ints = [-1000, -10, -1, 1000])
    fun call_with_invalid_env_id_return_default_lang_id(testData: Int) {
        val environmentID = resolveEnvironmentLangID(
            supposedEnvironmentLangID = testData,
            availableLanguageList = allLanguagesAsSupportedLanguages
        )
        assertEquals(allLanguagesAsSupportedLanguages.getDefaultLangID(), environmentID)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4])
    fun call_with_valid_env_id_return_env_id_as_lang_id(testData: Int) {
        val environmentID = resolveEnvironmentLangID(
            supposedEnvironmentLangID = testData,
            availableLanguageList = allLanguagesAsSupportedLanguages
        )
        assertEquals(testData, environmentID)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1000, -10, -1, 1000])
    fun call_with_invalid_env_id_throw_exception_if_no_default_language_in_supported_languages(testData: Int) {
        try {
            resolveEnvironmentLangID(
                supposedEnvironmentLangID = testData,
                availableLanguageList = allLanguagesAsSupportedLanguages.copyWithoutDefaultLangID()
            )
        } catch (_: Throwable) {
            return
        }
        fail("Fail expected")
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4])
    fun call_with_valid_env_id_return_env_id_as_lang_id_even_if_no_default_language_exist(testData: Int) {
        val environmentID = resolveEnvironmentLangID(
            supposedEnvironmentLangID = testData,
            availableLanguageList = allLanguagesAsSupportedLanguages.copyWithoutDefaultLangID()
        )
        assertEquals(testData, environmentID)
    }

    private fun List<LanguageFull>.copyWithoutDefaultLangID() = map {
        it.copy(isDefault = false)
    }
}