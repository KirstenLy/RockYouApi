package database.internal.executor

import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleListResult.Data
import database.external.model.CharacterBasicInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.relationcontentandcharacter.SelectAllCharacerBySingleContentID

/** @see execute */
internal class ReadCharactersForSingleContentExecutor(private val database: Database) {

    /**
     * Read characters for content.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentID: Int): SimpleListResult<CharacterBasicInfo> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectRelationContentAndCharacterQueries
                    .selectAllCharacerBySingleContentID(contentID)
                    .executeAsList()
                    .map(SelectAllCharacerBySingleContentID::toExternal)
                    .let(::Data)
            } catch (t: Throwable) {
                SimpleListResult.Error(t)
            }
        }
    }
}

private fun SelectAllCharacerBySingleContentID.toExternal() = CharacterBasicInfo(
    id = characterID,
    name = characterName
)