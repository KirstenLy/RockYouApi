package database.data

import common.utils.generateRandomTextByUUID
import database.internal.executor.CheckRefreshTokenExecutor
import database.internal.mock.user1
import database.internal.mock.user2
import database.internal.mock.user3
import database.internal.mock.user4
import java.util.stream.Stream

/** Model of data to test [CheckRefreshTokenExecutor] when data valid. */
internal data class CheckRefreshTokenTestData(
    val userID: Int,
    val refreshToken: String
)

/** Data creator to test [CheckRefreshTokenExecutor].*/
internal object CheckRefreshTokenTestDataStreamCreator {

    /** Data to test [CheckRefreshTokenExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<CheckRefreshTokenTestData> = Stream.of(
        CheckRefreshTokenTestData(
            userID = user1.id,
            refreshToken = "1234567"
        ),
        CheckRefreshTokenTestData(
            userID = user2.id,
            refreshToken = "789"
        ),
        CheckRefreshTokenTestData(
            userID = user3.id,
            refreshToken = "10"
        ),
        CheckRefreshTokenTestData(
            userID = user4.id,
            refreshToken = "1"
        ),
        CheckRefreshTokenTestData(
            userID = user4.id,
            refreshToken = " "
        ),
    )

    /** Data to test [CheckRefreshTokenExecutor] when [CheckRefreshTokenTestData.refreshToken] link to not existed token. */
    @JvmStatic
    fun notExistedTokenScenarioTestData(): Stream<CheckRefreshTokenTestData> = Stream.of(
        CheckRefreshTokenTestData(
            userID = user3.id,
            refreshToken = ""
        ),
        CheckRefreshTokenTestData(
            userID = user3.id,
            refreshToken = "   "
        ),
        CheckRefreshTokenTestData(
            userID = user3.id,
            refreshToken = generateRandomTextByUUID()
        ),
    )

    /** Data to test [CheckRefreshTokenExecutor] when [CheckRefreshTokenTestData.userID] link to not existed user. */
    @JvmStatic
    fun notExistedUserScenarioTestData(): Stream<CheckRefreshTokenTestData> = Stream.of(
        CheckRefreshTokenTestData(
            userID = -100000,
            refreshToken = "123"
        ),
        CheckRefreshTokenTestData(
            userID = -1,
            refreshToken = "123"
        ),
        CheckRefreshTokenTestData(
            userID = 0,
            refreshToken = "123"
        ),
        CheckRefreshTokenTestData(
            userID = 100000,
            refreshToken = "123"
        ),
    )
}