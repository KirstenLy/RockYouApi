package rockyouapi

import common.utils.generateRandomTextByUUID
import database.external.result.RegisterUserResult
import kotlinx.coroutines.delay
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.junit.jupiter.MockitoExtension
import rockyouapi.base.*
import rockyouapi.responce.BaseResponse
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.CLAIM_KEY_TOKEN_USER
import rockyouapi.security.TokenType
import rockyouapi.utils.*
import java.util.*
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/** @see rockyouapi.route.auth.authLoginRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
internal class AuthLoginTest : DatabaseApiDelegate by DatabaseApiDelegateImpl() {

    private fun argumentsWithInvalidLoginAndPasswordPair() = Stream.of(
        AuthLoginArguments(null, null),
        AuthLoginArguments(null, "1"),
        AuthLoginArguments(null, "12345"),
        AuthLoginArguments(null, generateRandomTextByUUID()),
        AuthLoginArguments("1", null),
        AuthLoginArguments("12345", null),
        AuthLoginArguments(generateRandomTextByUUID(), null),
        AuthLoginArguments("", null),
        AuthLoginArguments(null, ""),
        AuthLoginArguments("", ""),
        AuthLoginArguments("", "123"),
        AuthLoginArguments("", generateRandomTextByUUID()),
        AuthLoginArguments("123", ""),
        AuthLoginArguments(generateRandomTextByUUID(), ""),
        AuthLoginArguments("-1", ""),
        AuthLoginArguments("", "-11"),
        AuthLoginArguments("", "-11"),
        AuthLoginArguments("", "   "),
        AuthLoginArguments("   ", ""),
        AuthLoginArguments("   ", "     "),
    )

    @ParameterizedTest
    @MethodSource("argumentsWithInvalidLoginAndPasswordPair")
    fun login_with_invalid_argument_must_return_400(arguments: AuthLoginArguments) {
        runTestSimple {
            client.makeAuthLoginRequest(
                login = arguments.login,
                password = arguments.password,
            )
                .badRequestOrFail()
        }
    }

    @Test
    fun login_with_wrong_login_return_401() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val password = generateRandomTextByUUID()

            val registerResponse = productionDatabaseAPI.register(
                login = generateRandomTextByUUID(),
                password = password
            )

            when (registerResponse) {
                is RegisterUserResult.SameUserAlreadyExist -> fail("Something strange happen")
                is RegisterUserResult.Error -> fail(registerResponse.t.message.orEmpty())
                is RegisterUserResult.Ok -> Unit
                RegisterUserResult.UnknownUserRole -> TODO()
            }

            client.makeAuthLoginRequest(
                login = generateRandomTextByUUID(),
                password = password
            )
                .unauthorizedOrFail()
        }
    }

    @Test
    fun login_with_wrong_password_return_401() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val login = generateRandomTextByUUID()

            val registerResponse = productionDatabaseAPI.register(
                login = login,
                password = generateRandomTextByUUID()
            )

            when (registerResponse) {
                is RegisterUserResult.SameUserAlreadyExist -> fail("Something strange happen")
                is RegisterUserResult.Error -> fail(registerResponse.t.message.orEmpty())
                is RegisterUserResult.Ok -> Unit
                RegisterUserResult.UnknownUserRole -> TODO()
            }

            client.makeAuthLoginRequest(
                login = login,
                password = generateRandomTextByUUID()
            )
                .unauthorizedOrFail()
        }
    }

    @Test
    fun login_with_correct_user_and_password_return_200_with_token_and_refresh_access_token() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val login = generateRandomTextByUUID()
            val password = generateRandomTextByUUID()

            // At first register new user to login them on next steps.
            // Fresh user needed to get fresh tokens.
            val registeredUser = client.makeAuthRegisterRequest(
                login = login,
                password = password
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            // After user registered, his refresh token created. Check if it exists.
            val registerUserDatabaseRefreshToken = testDatabaseAPI.getUserRefreshToken(registeredUser.user.id)
            assert(registerUserDatabaseRefreshToken.isNullOrEmpty().not())

            // Auth request recreate and update user's refresh token.
            // If there is no delay between registration and authentication,
            // new refresh token will be the same as when user registered.
            // It happens because calls made at one second with same secret,
            // so refresh tokens are totally equals by content, and they be same after encrypt.
            // If there is delay between calls, tokens different at least by creation time.
            delay(2000)

            val loggedUser = client.makeAuthLoginRequest(
                login = login,
                password = password
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            // Refresh token updated after login, check it not missed.
            val loggedUserDatabaseRefreshToken = testDatabaseAPI.getUserRefreshToken(loggedUser.user.id)
            assert(loggedUserDatabaseRefreshToken.isNullOrEmpty().not())

            // Refresh token updated after login, check it changes.
            assertNotEquals(loggedUserDatabaseRefreshToken, registerUserDatabaseRefreshToken)

            // Check is logged user same as registered
            val registeredUserID = registeredUser.user.id
            val loggedUserID = loggedUser.user.id
            assertEquals(registeredUserID, loggedUserID)

            val registeredUserName = registeredUser.user.name
            val loggedUserName = loggedUser.user.name
            assertEquals(registeredUserName, loggedUserName)

            // Check is user's tokens correct by payloads and expire date
            val decrypt = getJWTDecryptByDefaultSecretWord()

            val loggedUserAccessToken = loggedUser.accessToken
            val loggedUserRefreshToken = loggedUser.refreshToken

            val decryptedAccessToken = decrypt.verify(loggedUserAccessToken)
            val decryptedRefreshToken = decrypt.verify(loggedUserRefreshToken)

            val userIDFromAccessToken = decryptedAccessToken.getClaim(CLAIM_KEY_TOKEN_USER).asInt()
            assertEquals(loggedUserID, userIDFromAccessToken)

            val userIDFromRefreshToken = decryptedRefreshToken.getClaim(CLAIM_KEY_TOKEN_USER).asInt()
            assertEquals(loggedUserID, userIDFromRefreshToken)

            val tokenTypeFromAccessToken = decryptedAccessToken.getClaim(CLAIM_KEY_TOKEN_TYPE).asInt()
            assertEquals(TokenType.ACCESS.typeID, tokenTypeFromAccessToken)

            val tokenTypeFromRefreshToken = decryptedRefreshToken.getClaim(CLAIM_KEY_TOKEN_TYPE).asInt()
            assertEquals(TokenType.REFRESH.typeID, tokenTypeFromRefreshToken)

            // Convert remain time to seconds
            val actualRemainTimeForAccessToken = (decryptedAccessToken.expiresAt.time - Date().time) / 1000
            // Expected remain time is token lifecycle minus 1 second,
            // because there is a delay between token creation and test execution
            val expectedRemainTimeForAccessToken = DEFAULT_ACCESS_TOKEN_LIFETIME
                .minus(1.seconds)
                .toLong(DurationUnit.SECONDS)

            assertEquals(expectedRemainTimeForAccessToken, actualRemainTimeForAccessToken)

            // Convert remain time to seconds
            val actualRemainTimeForRefreshToken = (decryptedRefreshToken.expiresAt.time - Date().time) / 1000
            // Expected remain time is token lifecycle minus 1 second,
            // because there is a delay between token creation and test execution
            val expectedRemainTimeForRefreshToken = DEFAULT_REFRESH_TOKEN_LIFETIME
                .minus(1.seconds)
                .toLong(DurationUnit.SECONDS)

            assertEquals(expectedRemainTimeForRefreshToken, actualRemainTimeForRefreshToken)
        }
    }

    internal class AuthLoginArguments(val login: String?, val password: String?)
}