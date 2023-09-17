package database.internal.executor.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import rockyouapi.DBTest

/**
 * Read all votes from database.
 * Used for test purpose to analyze if vote was actually inserted or deleted.
 * */
@TestOnly
internal class GetAllStoriesNodesExecutor(private val database: DBTest) {

    suspend fun execute(): List<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            emptyList()
//            database.storyNodeQueries
//                .selectAll()
//                .executeAsList()
        } catch (t: Throwable) {
            listOf()
        }
    }
}