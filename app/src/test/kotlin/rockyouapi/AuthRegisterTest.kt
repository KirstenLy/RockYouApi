package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.reader.readDatabaseConfigurationFromEnv
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.base.DEFAULT_ACCESS_TOKEN_LIFETIME
import rockyouapi.base.DEFAULT_REFRESH_TOKEN_LIFETIME
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.responce.BaseResponse
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.CLAIM_KEY_TOKEN_USER
import rockyouapi.security.TokenType
import rockyouapi.utils.*
import java.util.*
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/** @see rockyouapi.route.auth.authRegisterRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AuthRegisterTest {

    private fun argumentsWithInvalidLoginAndPasswordPair() = Stream.of(
        AuthRegisterArgumentsWithPasswordLength(null, null, 1),
        AuthRegisterArgumentsWithPasswordLength(null, null, 10),
        AuthRegisterArgumentsWithPasswordLength(null, null, 100),
        AuthRegisterArgumentsWithPasswordLength(null, "1", 1),
        AuthRegisterArgumentsWithPasswordLength(null, "1", 10),
        AuthRegisterArgumentsWithPasswordLength(null, "1", 100),
        AuthRegisterArgumentsWithPasswordLength(null, "12345", 1),
        AuthRegisterArgumentsWithPasswordLength(null, "12345", 10),
        AuthRegisterArgumentsWithPasswordLength(null, "12345", 100),
        AuthRegisterArgumentsWithPasswordLength(null, "444444444444444444444444444444444444444444444", 1),
        AuthRegisterArgumentsWithPasswordLength(null, "444444444444444444444444444444444444444444444", 10),
        AuthRegisterArgumentsWithPasswordLength(null, "444444444444444444444444444444444444444444444", 100),
        AuthRegisterArgumentsWithPasswordLength("Text", null, 1),
        AuthRegisterArgumentsWithPasswordLength("Text", null, 10),
        AuthRegisterArgumentsWithPasswordLength("Text", null, 100),
        AuthRegisterArgumentsWithPasswordLength("32432", null, 1),
        AuthRegisterArgumentsWithPasswordLength("32432", null, 10),
        AuthRegisterArgumentsWithPasswordLength("32432", null, 100),
        AuthRegisterArgumentsWithPasswordLength("saaaaaaaaaaaaaaaaaaa", null, 1),
        AuthRegisterArgumentsWithPasswordLength("saaaaaaaaaaaaaaaaaaa", null, 10),
        AuthRegisterArgumentsWithPasswordLength("saaaaaaaaaaaaaaaaaaa", null, 100),
        AuthRegisterArgumentsWithPasswordLength("", null, 1),
        AuthRegisterArgumentsWithPasswordLength("", null, 10),
        AuthRegisterArgumentsWithPasswordLength("", null, 100),
        AuthRegisterArgumentsWithPasswordLength(null, "", 1),
        AuthRegisterArgumentsWithPasswordLength(null, "", 10),
        AuthRegisterArgumentsWithPasswordLength(null, "", 100),
    )

    private fun argumentsWithInvalidPasswordLength() = Stream.of(
        AuthRegisterArgumentsWithPasswordLength(UUID.randomUUID().toString(), "1", 2),
        AuthRegisterArgumentsWithPasswordLength(UUID.randomUUID().toString(), "1", 20),
        AuthRegisterArgumentsWithPasswordLength(UUID.randomUUID().toString(), "1", 200),
        AuthRegisterArgumentsWithPasswordLength(UUID.randomUUID().toString(), "11111", 6),
        AuthRegisterArgumentsWithPasswordLength(UUID.randomUUID().toString(), "11111", 60),
    )

    private val databaseAPI by lazy {
        runBlocking {
            getOrCreateValue(KEY_STATIC_MAP_DB) {
                connectToProductionDatabaseWithTestApi(readDatabaseConfigurationFromEnv())
            }
        }
    }

    private val productionDatabaseAPI get() = databaseAPI.first

    private val testDatabaseAPI get() = databaseAPI.second

    @ParameterizedTest
    @MethodSource("argumentsWithInvalidLoginAndPasswordPair")
    fun register_with_invalid_arguments_must_return_400(arguments: AuthRegisterArgumentsWithPasswordLength) {
        runTest(productionDatabaseAPI, minimumPasswordLength = arguments.passwordLength) {
            client.makeAuthRegisterRequest(
                login = arguments.login,
                password = arguments.password,
            )
                .badRequestOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsWithInvalidPasswordLength")
    fun register_with_short_password_must_return_400(arguments: AuthRegisterArgumentsWithPasswordLength) {
        runTest(productionDatabaseAPI, minimumPasswordLength = arguments.passwordLength) {
            client.makeAuthRegisterRequest(
                login = arguments.login,
                password = arguments.password,
            )

                .badRequestOrFail()
        }
    }

    @RepeatedTest(10)
    fun register_with_already_existed_user_name_return_409() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            client.makeAuthRegisterRequest(
                login = testDatabaseAPI.getRandomUserName(),
                password = generateRandomTextByUUID(),
            )
                .conflictOrFail()
        }
    }

    @RepeatedTest(2)
    fun register_with_not_existed_user_name_and_correct_password_return_200_with_user_and_tokens() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val login = generateRandomTextByUUID()

            val userWithTokenData = client.makeAuthRegisterRequest(
                login = login,
                password = generateRandomTextByUUID(),
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val createdUserName = userWithTokenData.user.name
            assertEquals(login, createdUserName)

            // Check is user's tokens correct by payloads and expire date
            val decrypt = getJWTDecryptByDefaultSecretWord()

            val createdUserAccessToken = userWithTokenData.accessToken
            val createdUserRefreshToken = userWithTokenData.refreshToken

            val decryptedAccessToken = decrypt.verify(createdUserAccessToken)
            val decryptedRefreshToken = decrypt.verify(createdUserRefreshToken)

            val userIDFromAccessToken = decryptedAccessToken.getClaim(CLAIM_KEY_TOKEN_USER).asInt()
            val createdUserID = userWithTokenData.user.id
            assertEquals(createdUserID, userIDFromAccessToken)

            val userIDFromRefreshToken = decryptedRefreshToken.getClaim(CLAIM_KEY_TOKEN_USER).asInt()
            assertEquals(createdUserID, userIDFromRefreshToken)

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

    internal class AuthRegisterArgumentsWithPasswordLength(
        val login: String?,
        val password: String?,
        val passwordLength: Int
    )
}