package database.data

import database.internal.executor.RemoveRefreshTokenExecutor
import database.internal.mock.user1
import database.internal.mock.user4
import database.internal.mock.user9
import java.util.stream.Stream

/** Model of data to test [RemoveRefreshTokenExecutor] in simple tests. */
internal data class RemoveRefreshTokenWithoutExpectedValueTestData(
    val userID: Int,
)

/** Data creator to test [RemoveRefreshTokenExecutor].*/
internal object RemoveRefreshTokenTestDataStreamCreator {

    /** Data to test [RemoveRefreshTokenExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<RemoveRefreshTokenWithoutExpectedValueTestData> = Stream.of(
        RemoveRefreshTokenWithoutExpectedValueTestData(user1.id),
        RemoveRefreshTokenWithoutExpectedValueTestData(user4.id),
        RemoveRefreshTokenWithoutExpectedValueTestData(user9.id),
    )
}