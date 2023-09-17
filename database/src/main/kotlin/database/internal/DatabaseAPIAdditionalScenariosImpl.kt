package database.internal

import database.external.test.database.DatabaseAPIAdditionalScenarios
import database.external.test.TestReport
import database.external.test.TestVote
import database.external.result.SimpleListResult
import database.internal.executor.test.GetAllReportsForContentExecutor
import database.internal.executor.test.GetAllStoriesNodesExecutor
import database.internal.executor.test.GetAllVotesExecutor

internal class DatabaseAPIAdditionalScenariosImpl(
    private val getAllReportsForContentExecutor: GetAllReportsForContentExecutor,
    private val getAllVotesExecutor: GetAllVotesExecutor,
    private val getAllStoriesNodesExecutor: GetAllStoriesNodesExecutor
) : DatabaseAPIAdditionalScenarios {

    override suspend fun readAllReportsForContent(contentID: Int): SimpleListResult<TestReport> {
        return getAllReportsForContentExecutor.execute(contentID)
    }

    override suspend fun readAllVotes(): SimpleListResult<TestVote> {
        return getAllVotesExecutor.execute()
    }

    override suspend fun readAllStoryNodes(): List<String> {
        return getAllStoriesNodesExecutor.execute()
    }
}