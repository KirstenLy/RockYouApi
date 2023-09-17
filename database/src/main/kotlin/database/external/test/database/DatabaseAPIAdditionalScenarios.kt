package database.external.test.database

import database.external.test.TestReport
import database.external.test.TestVote
import database.external.result.SimpleListResult

/** Contract of the database that realizes additional test-only scenarios. */
interface DatabaseAPIAdditionalScenarios {

    suspend fun readAllReportsForContent(contentID: Int): SimpleListResult<TestReport>

    suspend fun readAllVotes(): SimpleListResult<TestVote>

    suspend fun readAllStoryNodes() : List<String>
}