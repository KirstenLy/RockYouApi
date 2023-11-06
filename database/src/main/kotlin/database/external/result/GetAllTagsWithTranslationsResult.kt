package database.external.result

import database.external.model.tag.TagFull

/** @see database.internal.executor.ReadAllTagsWithTranslationsListExecutor */
sealed interface GetAllTagsWithTranslationsResult {

    /** Supported tags fetched. */
    class Data(val tagFullList: List<TagFull>) : GetAllTagsWithTranslationsResult

    /** Tags not exists. */
    data object TagsNotExist : GetAllTagsWithTranslationsResult

    /** Translations for tags not exists. */
    data object TranslationsNotExist : GetAllTagsWithTranslationsResult

    /** Some tag missed translation. */
    data object TranslationsMissed : GetAllTagsWithTranslationsResult

    /** Error. */
    class Error(val t: Throwable) : GetAllTagsWithTranslationsResult
}