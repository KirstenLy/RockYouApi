package rockyouapi.tests.routes.auth

import database.external.test.TestUser
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.*
import rockyouapi.model.UserWithToken
import rockyouapi.route.Routes
import java.util.*
import kotlin.test.Test

/** @see rockyouapi.route.auth.authRegisterRoute */
internal class AuthRegisterTest {

    @Test
    fun register_without_login_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = null,
                password = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_without_login_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = null,
                password = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_without_login_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = null,
                password = "12345",
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_without_login_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = null,
                password = "333333333333333333333333333333333333",
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_without_password_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = null,
                password = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_without_password_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = "Text",
                password = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_without_password_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = "32432",
                password = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_without_password_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAuthRegisterRequest(
                login = "saaaaaaaaaaaaaaaaaaa",
                password = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun register_with_short_password_must_return_400_test() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val randomString = UUID.randomUUID().toString()
            val passwords = List(LENGTH_TEST_PASSWORD_MINIMUM_LENGTH - 1) { randomString.take(it + 1) }

            passwords.forEach { password ->
                val response = client.makeAuthRegisterRequest(
                    login = "Test",
                    password = password,
                )

                assert(response.status == HttpStatusCode.BadRequest) {
                    "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
                }
            }
        }
    }

    @Test
    fun register_try_with_already_existed_user_return_409() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val usersNames = testEnv.modelsStorage.users.map(TestUser::name)

            usersNames.forEach { userName ->
                val response = client.makeAuthRegisterRequest(
                    login = userName,
                    password = UUID.randomUUID().toString(),
                )

                assert(response.status == HttpStatusCode.Conflict) {
                    "Expected response status: ${HttpStatusCode.Conflict}, Actual response status: ${response.status}"
                }
            }
        }
    }

    @Test
    fun register_try_with_correct_data_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val usersNames = List(3) { UUID.randomUUID().toString() }

            usersNames.forEach { userName ->
                val response = client.makeAuthRegisterRequest(
                    login = userName,
                    password = UUID.randomUUID().toString(),
                )

                assert(response.status == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${response.status}"
                }

                val decodedResponse = response.decodeAs<UserWithToken>()

                val createdUserID = decodedResponse.user.id
                val createdUserName = decodedResponse.user.name
                val createdUserAuthToken = decodedResponse.token

                val isSameUserReturned = createdUserName == userName
                assert(isSameUserReturned) { "Created user not match passed arguments." }

                val tokenManager = testEnv.tokenManager
                val createdTokenOwner = tokenManager.getTokenOwnerID(createdUserAuthToken)
                assert(createdTokenOwner == createdUserID) { "Created token not belong to created user." }

                val isCreatedTokenValid = tokenManager.isTokenExistAndValid(createdUserAuthToken)
                assert(isCreatedTokenValid) { "Created token is not valid" }

                delayByTokenLifetimeAndOneSecAhead()

                val isCreatedTokenStillValid = tokenManager.isTokenExistAndValid(createdUserAuthToken)
                assert(!isCreatedTokenStillValid) { "Created token is valid but must be expired" }
            }
        }
    }

    private suspend fun HttpClient.makeAuthRegisterRequest(login: String?, password: String?) =
        post(Routes.AuthRegister.path) {
            url {
                appendToParameters(login, Routes.AuthRegister.getLoginArgName())
                appendToParameters(password, Routes.AuthRegister.getPasswordArgName())
            }
        }
}