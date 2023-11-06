package database.internal.executor

import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleDataResult.Data
import database.external.result.common.SimpleListResult
import database.external.model.CharacterBasicInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.relationcontentandcharacter.SelectAllCharacerByMultipleContentID

/** @see execute */
internal class ReadCharactersForMultipleContentExecutor(private val database: Database) {

    /**
     * Read characters for content.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentIDs: List<Int>): SimpleDataResult<Map<Int, List<CharacterBasicInfo>>> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectRelationContentAndCharacterQueries
                    .selectAllCharacerByMultipleContentID(contentIDs)
                    .executeAsList()
                    .groupBy(
                        keySelector = SelectAllCharacerByMultipleContentID::contentID,
                        valueTransform = SelectAllCharacerByMultipleContentID::toExternal
                    )
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleDataResult.Error(t)
            }
        }
    }
}

private fun SelectAllCharacerByMultipleContentID.toExternal() = CharacterBasicInfo(
    id = characterID,
    name = characterName
)