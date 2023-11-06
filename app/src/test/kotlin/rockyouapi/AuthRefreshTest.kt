package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import rockyouapi.base.DEFAULT_ACCESS_TOKEN_LIFETIME
import rockyouapi.base.DEFAULT_REFRESH_TOKEN_LIFETIME
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.CLAIM_KEY_TOKEN_USER
import rockyouapi.security.TokenType
import rockyouapi.utils.*
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/** @see rockyouapi.route.auth.authRefreshRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AuthRefreshTest {

    private val databaseAPI by lazy {
        runBlocking {
            getOrCreateValue(KEY_STATIC_MAP_DB) {
                connectToProductionDatabaseWithTestApi(readDatabaseConfigurationFromEnv())
            }
        }
    }

    private val productionDatabaseAPI get() = databaseAPI.first

    @Test
    fun refresh_without_token_return_401() {
        runTestSimple {
            client.makeRefreshRequest(
                token = null
            )
                .unauthorizedOrFail()
        }
    }

    @Test
    fun refresh_with_access_token_in_header_return_401() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeRefreshRequest(
                token = registerResponse.accessToken // refresh token must be in header, so 401 here
            )
                .unauthorizedOrFail()
        }
    }

    @Test
    fun refresh_with_expired_refresh_token_in_header_return_401() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            delay(DEFAULT_REFRESH_TOKEN_LIFETIME + 1.seconds)

            client.makeRefreshRequest(
                token = registerResponse.refreshToken
            )
                .unauthorizedOrFail()
        }
    }

    @Test
    fun refresh_with_valid_refresh_token_return_200_and_generate_new_correct_access_token() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            // Refresh token request create token for user registered milliseconds ago.
            // If there is no delay between registration and refresh token request,
            // new access token will be the same as when user registered.
            // It happens because calls made at one second with same secret,
            // so access tokens are totally equals by content, and they be same after encrypt.
            // If there is delay between calls, tokens different at least by creation time.
            delay(2000)

            val newAccessToken = client.makeRefreshRequest(
                token = registerResponse.refreshToken
            )
                .okOrFail()
                .decodeAs<BaseResponse<String>>()
                .data!!

            assertNotEquals(registerResponse.accessToken, newAccessToken)

            // Check is user's tokens correct by payloads and expire date
            val registeredUser = registerResponse.user
            val decrypt = getJWTDecryptByDefaultSecretWord()
            val decryptedAccessToken = decrypt.verify(newAccessToken)

            val userIDFromAccessToken = decryptedAccessToken.getClaim(CLAIM_KEY_TOKEN_USER).asInt()
            assertEquals(registeredUser.id, userIDFromAccessToken)

            val tokenTypeFromAccessToken = decryptedAccessToken.getClaim(CLAIM_KEY_TOKEN_TYPE).asInt()
            assertEquals(TokenType.ACCESS.typeID, tokenTypeFromAccessToken)

            // Convert remain time to seconds
            val actualRemainTimeForAccessToken = (decryptedAccessToken.expiresAt.time - Date().time) / 1000
            // Expected remain time is token lifecycle minus 1 second,
            // because there is a delay between token creation and test execution
            val expectedRemainTimeForAccessToken = DEFAULT_ACCESS_TOKEN_LIFETIME
                .minus(1.seconds)
                .toLong(DurationUnit.SECONDS)

            assertEquals(expectedRemainTimeForAccessToken, actualRemainTimeForAccessToken)
        }
    }

    private suspend fun HttpClient.makeRefreshRequest(token: String?) =
        post(Routes.AuthRefreshAccessToken.path) {
            token?.let(::bearerAuth)
        }
}