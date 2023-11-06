package rockyouapi.route.comment

import database.external.contract.ProductionDatabaseAPI
import database.external.result.AddCommentResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.configuration.COMMENT_MAXIMUM_LENGTH
import rockyouapi.route.Routes
import rockyouapi.route.Routes.Companion.JWT_AUTHORIZATION_PROVIDER_KEY
import rockyouapi.security.*
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.checkTokenType
import rockyouapi.utils.*

/**
 * Route to add comment to content.
 *
 * Requirements: access token, contentID, comment text.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Comment added. Respond only with code.
 * - [HttpStatusCode.BadRequest] If contentID or comment text not presented or invalid.
 * - [HttpStatusCode.Unauthorized] On different tokens error, like missed climes.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.commentAddRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    authenticate(JWT_AUTHORIZATION_PROVIDER_KEY) {

        post(Routes.CommentAdd.path) {

            val principal = call.principal<JWTPrincipal>() ?: run {
                call.respondAsTokenNotFoundOrNotSupported()
                return@post
            }

            principal.checkTokenType(
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

            val parameters = call.receiveParameters()

            val userID = parameters.readNotNullableInt(
                argName = Routes.CommentAdd.getUserIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.CommentAdd.getUserIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.CommentAdd.getUserIDArgName())
                    return@post
                }
            )

            val contentID = parameters.readNotNullableInt(
                argName = Routes.CommentAdd.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.CommentAdd.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.CommentAdd.getContentIDArgName())
                    return@post
                },
            )

            val commentText = parameters.readNotNullableFilledString(
                argName = Routes.CommentAdd.getCommentTextArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.CommentAdd.getCommentTextArgName())
                    return@post
                },
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.CommentAdd.getCommentTextArgName())
                    return@post
                }
            )

            val tokenOwner = principal.tryToExtractUserID {
                call.respondAsTokenHasNoOwner()
                return@post
            }

            if (commentText.length > COMMENT_MAXIMUM_LENGTH) {
                call.respondAsCommentTooLong()
                return@post
            }

            // Make sure token belong to user who request comment addition.
            // if check fail, answer as "Token invalid"(not "Token belong to another user") to prevent knowledge
            // about connection between userID and token.
            if (tokenOwner != userID) {
                call.respondAsTokenInvalid()
                return@post
            }

            when (val addCommentResult = productionDatabaseAPI.addComment(userID, contentID, commentText)) {

                is AddCommentResult.ContentNotExists -> {
                    call.respondAsContentNotExistByID(contentID)
                    return@post
                }

                is AddCommentResult.UserNotExists -> {
                    call.respondAsUserNotExistByID(userID)
                    return@post
                }

                is AddCommentResult.Error -> {
                    val errorText = buildString {
                        append("Failed to add comment")
                        appendLine()
                        append("UserID: $userID")
                        appendLine()
                        append("CommentText: $commentText")
                    }
                    call.logErrorToFile(errorText, addCommentResult.t)
                    call.respondAsErrorByException(addCommentResult.t)
                    return@post
                }

                is AddCommentResult.Ok -> {
                    call.respondAsOkWithUnit()
                    return@post
                }
            }
        }
    }
}

private suspend fun ApplicationCall.respondAsCommentTooLong() {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "Comment length exceeded, maximum length: $COMMENT_MAXIMUM_LENGTH"
    )
}