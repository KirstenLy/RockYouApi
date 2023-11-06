package rockyouapi.route.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import database.external.contract.ProductionDatabaseAPI
import database.external.result.common.SimpleBooleanResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.route.Routes
import rockyouapi.route.Routes.Companion.JWT_AUTHORIZATION_PROVIDER_KEY
import rockyouapi.security.*
import rockyouapi.utils.*
import java.util.*
import kotlin.time.Duration

/**
 * Route to refresh access token.
 *
 * Requirements: refresh token.
 *
 * @param secret - Key to decode/encode JWT keys.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Token created. Created access token passes as response, see [UpdatedAccessToken].
 * - [HttpStatusCode.Unauthorized] If refresh token broken, expired or invalid by any another reason(missed claims f.e).
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.authRefreshRoute(
    productionDatabaseAPI: ProductionDatabaseAPI,
    accessTokenLifeTime: Duration,
    secret: String
) {

    authenticate(JWT_AUTHORIZATION_PROVIDER_KEY) {

        post(Routes.AuthRefreshAccessToken.path) {

            val principal = call.principal<JWTPrincipal>() ?: run {
                call.respondAsTokenInvalid()
                return@post
            }

            principal.checkTokenType(
                tokenTypeClaimKey = CLAIM_KEY_TOKEN_TYPE,
                onClaimNotPresented = {
                    call.respondAsTokenHasNoType()
                    return@post
                },
                onAccessType = {
                    call.respondAsTokenHasAccessType()
                    return@post
                },
                onRefreshType = {
                    // It's expected scenario, continue execution
                },
                onUnknownType = {
                    call.respondAsTokenHasInvalidType()
                    return@post
                }
            )

            val parameters = call.receiveParameters()

            val userID = parameters.readNotNullableInt(
                argName = Routes.AuthRefreshAccessToken.getUserIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.AuthRefreshAccessToken.getUserIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.AuthRefreshAccessToken.getUserIDArgName())
                    return@post
                }
            )

            val refreshTokenOwner = principal.tryToExtractUserID {
                call.respondAsTokenHasNoOwner()
                return@post
            }

            // Make sure refresh token belong to user who request new access token.
            // If check fail, answer as "Token invalid"(not "Token belong to another user") to prevent knowledge
            // about connection between userID and token.
            if (refreshTokenOwner != userID) {
                call.respondAsTokenInvalid()
                return@post
            }

            val refreshToken = call.request.authorization()?.extractBearerToken() ?: run {
                call.respondAsTokenInvalid()
                return@post
            }

            // Check is passed refresh token exist and belong to user.
            // If token not exist or belong to another user send 401.
            val checkRefreshTokenResult = productionDatabaseAPI.checkIsRefreshTokenExistAndBelongToUser(
                userID = userID,
                token = refreshToken
            )

            when (checkRefreshTokenResult) {

                is SimpleBooleanResult.Error -> {
                    val errorMsg = buildString {
                        append("Failed to check is refresh token exist and belong to user.")
                        appendLine()
                        append("Token owner: $refreshTokenOwner")
                        appendLine()
                        append("Refresh token: $refreshToken")
                    }
                    call.logErrorToFile(errorMsg, checkRefreshTokenResult.t)
                    call.respondAsErrorByException(checkRefreshTokenResult.t)
                    return@post
                }

                is SimpleBooleanResult.False -> {
                    call.respondAsTokenNotExistOrBelongToAnotherUser()
                    return@post
                }

                is SimpleBooleanResult.True -> Unit
            }

            val newAccessToken = JWT.create()
                .withClaim(CLAIM_KEY_TOKEN_USER, refreshTokenOwner)
                .withClaim(CLAIM_KEY_TOKEN_TYPE, TokenType.ACCESS.typeID)
                .withExpiresAt(Date(System.currentTimeMillis() + accessTokenLifeTime.inWholeMilliseconds))
                .sign(Algorithm.HMAC256(secret))

            call.respondAsOkWithData(newAccessToken)
            return@post
        }
    }
}

private suspend fun ApplicationCall.respondAsTokenNotExistOrBelongToAnotherUser() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Token not exist or belong to another user. Log in to get new token."
    )
}