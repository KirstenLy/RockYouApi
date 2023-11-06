package database.data

import common.utils.generateRandomTextByUUID
import database.internal.executor.UpdateRefreshTokenExecutor
import database.internal.mock.user1
import database.internal.mock.user4
import database.internal.mock.user8
import java.util.stream.Stream

/** Model of data to test [UpdateRefreshTokenExecutor] in simple tests. */
internal data class PutOrReplaceRefreshTokenWithoutExpectedValueTestData(
    val userID: Int,
    val refreshToken: String
)

/** Data creator to test [UpdateRefreshTokenExecutor].*/
internal object PutOrReplaceRefreshTokenTestDataStreamCreator {

    /** Data to test [UpdateRefreshTokenExecutor] when [PutOrReplaceRefreshTokenWithoutExpectedValueTestData.userID] link to not existed user.*/
    @JvmStatic
    fun notExistedUserScenarioTestData(): Stream<PutOrReplaceRefreshTokenWithoutExpectedValueTestData> = Stream.of(
        PutOrReplaceRefreshTokenWithoutExpectedValueTestData(
            userID = -100000,
            refreshToken = ""
        ),
        PutOrReplaceRefreshTokenWithoutExpectedValueTestData(
            userID = 0,
            refreshToken = " "
        ),
        PutOrReplaceRefreshTokenWithoutExpectedValueTestData(
            userID = 100000,
            refreshToken = generateRandomTextByUUID()
        ),
    )

    /** Data to test [UpdateRefreshTokenExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<PutOrReplaceRefreshTokenWithoutExpectedValueTestData> = Stream.of(
        PutOrReplaceRefreshTokenWithoutExpectedValueTestData(
            userID = user1.id,
            refreshToken = ""
        ),
        PutOrReplaceRefreshTokenWithoutExpectedValueTestData(
            userID = user8.id,
            refreshToken = " "
        ),
        PutOrReplaceRefreshTokenWithoutExpectedValueTestData(
            userID = user4.id,
            refreshToken = generateRandomTextByUUID()
        )
    )
}