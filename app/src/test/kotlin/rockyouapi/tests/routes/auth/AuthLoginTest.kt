package rockyouapi.tests.routes.auth

import io.ktor.http.*
import rockyouapi.delayByTokenLifetimeAndOneSecAhead
import rockyouapi.getRandomUserWithPassword
import rockyouapi.tests.routes.makeAuthLoginRequestWithDecodedResponse
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import java.util.*
import kotlin.test.Test
import kotlin.test.fail

/** @see rockyouapi.route.auth.authLoginRoute */
internal class AuthLoginTest {

    @Test
    fun login_without_login_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = null,
                password = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_login_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = null,
                password = "123",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_login_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = null,
                password = "fdsfdsffds",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_login_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = null,
                password = "-1",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_login_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = "",
                password = "-1",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_login_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = "",
                password = "Test",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_login_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = "",
                password = UUID.randomUUID().toString(),
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_password_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = "fds",
                password = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_password_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = "-1",
                password = null,
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_password_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = "-1",
                password = "",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_without_password_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            client.makeAuthLoginRequestWithDecodedResponse(
                login = UUID.randomUUID().toString(),
                password = "",
                onFinishedByError = {
                    assert(it == HttpStatusCode.BadRequest) {
                        "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: $it"
                    }
                    return@runTestInConfiguredApplicationWithoutDBConnection
                },
            )
            fail("Unreachable code reached")
        }
    }

    @Test
    fun login_with_correct_data_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val usersWithTheirAuthData = List(10) { testEnv.getRandomUserWithPassword() }
            usersWithTheirAuthData.forEach { userWithPassword ->

                val response = client.makeAuthLoginRequestWithDecodedResponse(
                    login = userWithPassword.name,
                    password = userWithPassword.password,
                    onFinishedByError = {
                        fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                    }
                )

                val loggedUserID = response.user.id
                val loggedUserName = response.user.name
                val loggedUserAuthToken = response.token

                val isSameUserReturned = loggedUserName == userWithPassword.name
                assert(isSameUserReturned) { "Logged user not match passed arguments." }

                val tokenManager = testEnv.tokenManager
                val createdTokenOwner = tokenManager.getTokenOwnerID(loggedUserAuthToken)
                assert(createdTokenOwner == loggedUserID) { "Created token not belong to logged user." }

                val isCreatedTokenValid = tokenManager.isTokenExistAndValid(loggedUserAuthToken)
                assert(isCreatedTokenValid) { "Created token is not valid" }

                delayByTokenLifetimeAndOneSecAhead()

                val isCreatedTokenStillValid = tokenManager.isTokenExistAndValid(loggedUserAuthToken)
                assert(!isCreatedTokenStillValid) { "Created token is valid but must be expired" }
            }
        }
    }
}