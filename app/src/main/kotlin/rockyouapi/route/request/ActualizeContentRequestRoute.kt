package rockyouapi.route.request

import database.external.contract.ProductionDatabaseAPI
import database.external.result.ActualizeContentResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.configuration.ACTUALIZE_CONTENT_REQUEST_MAXIMUM_LENGTH
import rockyouapi.route.Routes
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.checkTokenType
import rockyouapi.security.tryToExtractUserID
import rockyouapi.utils.*

/**
 * Route to create request for content actualization. Can be done without user identification.
 *
 * Requirements: contentID, requestText.
 * Additional: userID.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Request created.
 * - [HttpStatusCode.BadRequest] If contentID or requestText not presented or invalid, or if userID presented and invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.createActualizeContentRequestRoute(databaseAPI: ProductionDatabaseAPI) {

    authenticate(Routes.JWT_AUTHORIZATION_PROVIDER_KEY, optional = true) {

        post(Routes.ActualizeContentRequestCreate.path) {

            val principal = call.principal<JWTPrincipal>()

            principal?.checkTokenType(
                tokenTypeClaimKey = CLAIM_KEY_TOKEN_TYPE,
                onClaimNotPresented = {
                    call.respondAsTokenHasNoType()
                    return@post
                },
                onRefreshType = {
                    call.respondAsTokenHasRefreshType()
                    return@post
                },
                onAccessType = {
                    // It's expected scenario, continue execution
                },
                onUnknownType = {
                    call.respondAsTokenHasInvalidType()
                    return@post
                }
            )

            val tokenOwner = principal?.tryToExtractUserID {
                call.respondAsTokenHasNoOwner()
                return@post
            }

            val parameters = call.receiveParameters()

            val contentID = parameters.readNotNullableInt(
                argName = Routes.ActualizeContentRequestCreate.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.ActualizeContentRequestCreate.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.ActualizeContentRequestCreate.getContentIDArgName())
                    return@post
                },
            )

            val requestText = parameters.readNotNullableFilledString(
                argName = Routes.ActualizeContentRequestCreate.getRequestTextArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.ActualizeContentRequestCreate.getRequestTextArgName())
                    return@post
                },
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.ActualizeContentRequestCreate.getRequestTextArgName())
                    return@post
                }
            )

            if (requestText.length > ACTUALIZE_CONTENT_REQUEST_MAXIMUM_LENGTH) {
                call.respondAsIntArgumentTooBig(
                    Routes.ActualizeContentRequestCreate.getRequestTextArgName(),
                    ACTUALIZE_CONTENT_REQUEST_MAXIMUM_LENGTH
                )
                return@post
            }

            val userID = parameters.readNullableInt(
                argName = Routes.ActualizeContentRequestCreate.getUserIDArgName(),
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.ActualizeContentRequestCreate.getUserIDArgName())
                    return@post
                }
            )

            // If token presented, server must be sure it's belong to caller
            // Server compare token owner and userID, so if token presented userID must be presented too.
            if (tokenOwner != null && userID == null) {
                call.respondAsArgumentRequiredError(Routes.ActualizeContentRequestCreate.getUserIDArgName())
                return@post
            }

            // Make sure token belong to user who create request.
            // if check fail, answer as "Token invalid"(not "Token belong to another user") to prevent knowledge
            // about connection between userID and token.
            if (tokenOwner != null && tokenOwner != userID) {
                call.respondAsTokenInvalid()
                return@post
            }

            val createActualizeContentRequestResult = databaseAPI.createActualizeContentRequest(
                contentID,
                requestText,
                userID
            )

            when (createActualizeContentRequestResult) {

                is ActualizeContentResult.ContentNotExist -> {
                    call.respondAsContentNotExistByID(contentID)
                    return@post
                }

                is ActualizeContentResult.UserNotExist -> {
                    userID?.let {
                        call.respondAsUserNotExistByID(it)
                        return@post
                    }
                    call.userIDMissedError()
                    return@post
                }

                is ActualizeContentResult.Error -> {
                    val errorText = buildString {
                        append("Failed to create actualize content request")
                        appendLine()
                        append("ContentID: $contentID")
                        appendLine()
                        append("UserID: $userID")
                        appendLine()
                        append("RequestText: $requestText")
                    }
                    call.logErrorToFile(errorText, createActualizeContentRequestResult.t)
                    call.respondAsErrorByException(createActualizeContentRequestResult.t)
                    return@post
                }

                is ActualizeContentResult.Ok -> {
                    call.respondAsOkWithUnit()
                    return@post
                }
            }
        }
    }
}

private suspend fun ApplicationCall.userIDMissedError() {
    respondAsError(
        errorCode = HttpStatusCode.InternalServerError,
        errorText = "Unexpected error: userID missed"
    )
}