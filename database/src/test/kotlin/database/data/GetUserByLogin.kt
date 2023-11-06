package database.data

import common.utils.generateRandomTextByUUID
import database.internal.executor.ReadUserByLoginExecutor
import database.internal.mock.user1
import database.internal.mock.user9
import database.external.model.user.UserSimple
import java.util.stream.Stream

/** Model of data to test [ReadUserByLoginExecutor] in simple tests. */
internal data class GetUserByLoginWithoutExpectedValueTestData(
    val login: String,
)

/** Model of data to test [ReadUserByLoginExecutor] in tests where result expected. */
internal data class GetUserByLoginTestData(
    val login: String,
    val expectedUserSimple: UserSimple
)

/** Data creator to test [ReadUserByLoginExecutor].*/
internal object GetUserByLoginTestDataStreamCreator {

    /** Data to test [ReadUserByLoginExecutor] when [GetUserByLoginWithoutExpectedValueTestData.login] link to not existed login. */
    @JvmStatic
    fun notExistedUserLoginScenarioTestData(): Stream<GetUserByLoginWithoutExpectedValueTestData> = Stream.of(
        GetUserByLoginWithoutExpectedValueTestData(
            login = ""
        ),
        GetUserByLoginWithoutExpectedValueTestData(
            login = "   "
        ),
        GetUserByLoginWithoutExpectedValueTestData(
            login = generateRandomTextByUUID()
        ),
    )

    /** Data to test [ReadUserByLoginExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<GetUserByLoginTestData> = Stream.of(
        GetUserByLoginTestData(
            login = user1.name,
            expectedUserSimple = UserSimple(
                id = user1.id,
                name = user1.name
            )
        ),
        GetUserByLoginTestData(
            login = user9.name,
            expectedUserSimple = UserSimple(
                id = user9.id,
                name = user9.name
            )
        ),
    )
}