package database.data

import database.internal.executor.CheckUserCredentialsExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [CheckUserCredentialsExecutor]. */
internal data class CheckUserCredentialTestData(
    val login: String,
    val password: String
)

/** Data creator to test [CheckUserCredentialsExecutor].*/
internal object CheckUserCredentialTestDataStreamCreator {

    /** Data to test [CheckUserCredentialsExecutor] with expected arguments.*/
    @JvmStatic
    fun basicScenarioTestData(): Stream<CheckUserCredentialTestData> = Stream.of(
        CheckUserCredentialTestData(
            login = user1.name,
            password = user1.name
        ),
        CheckUserCredentialTestData(
            login = user2.name,
            password = user2.name
        ),
        CheckUserCredentialTestData(
            login = user3.name,
            password = user3.name
        ),
    )

    /** Data to test [CheckUserCredentialsExecutor] when [CheckUserCredentialTestData.login] link to not existed user.*/
    @JvmStatic
    fun notExistedLoginScenarioTestData(): Stream<CheckUserCredentialTestData> = Stream.of(
        CheckUserCredentialTestData(
            login = "",
            password = "123456"
        ),
        CheckUserCredentialTestData(
            login = "   ",
            password = "123456"
        ),
        CheckUserCredentialTestData(
            login = "Shrek",
            password = "123456"
        ),
    )

    /** Data to test [CheckUserCredentialsExecutor] when [CheckUserCredentialTestData.password] mismatched.*/
    @JvmStatic
    fun mismatchedPasswordScenarioTestData(): Stream<CheckUserCredentialTestData> = Stream.of(
        CheckUserCredentialTestData(
            login = user1.name,
            password = "123456"
        ),
        CheckUserCredentialTestData(
            login = user2.name,
            password = ""
        ),
        CheckUserCredentialTestData(
            login = user3.name,
            password = "    "
        ),
        CheckUserCredentialTestData(
            login = user4.name,
            password = "DROP DATABASE"
        ),
        CheckUserCredentialTestData(
            login = user5.name,
            password = "NANI???"
        ),
    )
}