package rockyouapi.route.auth

import database.external.contract.ProductionDatabaseAPI
import database.external.result.UpdateRefreshTokenResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.route.Routes
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.checkTokenType
import rockyouapi.security.tryToExtractUserID
import rockyouapi.utils.*

/**
 * Route to log out existed user.
 * Remove user's refresh token.
 *
 * Requirements: access token, userID. UserID contained into access token, but server need it individual to
 * make sure that it's token owner init logout.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Refresh token successfully removed.
 * - [HttpStatusCode.BadRequest] If userID not presented/invalid.
 * - [HttpStatusCode.Unauthorized] Access token invalid or belong to another user.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.authLogoutRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    authenticate(Routes.JWT_AUTHORIZATION_PROVIDER_KEY) {

        post(Routes.AuthLogout.path) {

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

            val accessTokenOwner = principal.tryToExtractUserID {
                call.respondAsTokenHasNoOwner()
                return@post
            }

            val parameters = call.receiveParameters()

            val userID = parameters.readNotNullableInt(
                argName = Routes.AuthLogout.getUserIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.AuthLogout.getUserIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.AuthLogout.getUserIDArgName())
                    return@post
                }
            )

            // Make sure token to delete belong to user who request deletion.
            // if check fail, answer as "Token invalid"(not "Token belong to another user") to prevent knowledge
            // about connection between userID and token.
            if (accessTokenOwner != userID) {
                call.respondAsTokenInvalid()
                return@post
            }

            // Remove refresh token by replacing it for null.
            when (val updateRefreshTokenRequestResult = productionDatabaseAPI.updateRefreshToken(userID, null)) {

                is UpdateRefreshTokenResult.UserNotExists -> {
                    call.respondAsUserNotExistByID(userID)
                    return@post
                }

                is UpdateRefreshTokenResult.Error -> {
                    val exception = updateRefreshTokenRequestResult.t
                    call.logErrorToFile("Failed to remove access token. UserID: $userID", exception)
                    call.respondAsErrorByException(updateRefreshTokenRequestResult.t)
                    return@post
                }

                is UpdateRefreshTokenResult.Ok -> {
                    call.respondAsOkWithUnit()
                    return@post
                }
            }
        }
    }
}