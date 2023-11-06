package database.internal.executor

import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleDataResult.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.relationcontentandlanguage.SelectContentLanguageListForContentList1

/** @see execute */
internal class ReadLanguagesForMultipleContentExecutor(private val database: Database) {

    /**
     * Read languages for content list.
     *
     * Respond as:
     * - [SimpleDataResult.Data] Request finished without errors. Contains the map: [contentID <-> ListOfContentLanguage].
     * - [SimpleDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentIDList: List<Int>): SimpleDataResult<Map<Int, List<Int>>> {
        if (contentIDList.isEmpty()) return Data(emptyMap())

        return withContext(Dispatchers.IO) {
            try {
                database.selectRelationContentAndLanguageQueries
                    .selectContentLanguageListForContentList1(contentIDList)
                    .executeAsList()
                    .groupBy(
                        keySelector = SelectContentLanguageListForContentList1::contentID,
                        valueTransform = SelectContentLanguageListForContentList1::languageID
                    )
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleDataResult.Error(t)
            }
        }
    }
}