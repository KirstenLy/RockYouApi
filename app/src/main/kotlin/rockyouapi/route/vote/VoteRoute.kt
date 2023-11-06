package rockyouapi.route.vote

import database.external.contract.ProductionDatabaseAPI
import database.external.result.VoteResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.operation.ChangeRatingOperation
import rockyouapi.operation.FavoriteOperation
import rockyouapi.route.Routes
import rockyouapi.route.Routes.Companion.JWT_AUTHORIZATION_PROVIDER_KEY
import rockyouapi.security.*
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.checkTokenType
import rockyouapi.utils.*
import database.external.operation.VoteOperation as DBChangeRatingOperation

/**
 * Route to vote for content.
 *
 * Requirements: token.
 * Additional: languageID, offset, environmentID.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Content voted. Respond with ok status only.
 * - [HttpStatusCode.BadRequest] If limit not presented or invalid or languageID/offset/environmentID invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.voteRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    authenticate(JWT_AUTHORIZATION_PROVIDER_KEY) {

        post(Routes.Vote.path) {

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

            val userID = principal.tryToExtractUserID {
                call.respondAsTokenHasNoOwner()
                return@post
            }

            val parameters = call.receiveParameters()

            val contentID = parameters.readNotNullableInt(
                argName = Routes.Vote.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.Vote.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.Vote.getContentIDArgName())
                    return@post
                },
            )

            val operationType = parameters.readNotNullableInt(
                argName = Routes.Vote.getOperationTypeArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.Vote.getOperationTypeArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.Vote.getOperationTypeArgName())
                    return@post
                },
            )

            val dbOperationType = when (operationType) {
                ChangeRatingOperation.UPVOTE.operationArgument -> DBChangeRatingOperation.UPVOTE
                ChangeRatingOperation.DOWNVOTE.operationArgument -> DBChangeRatingOperation.DOWNVOTE
                else -> {
                    call.respondAsInvalidOperationType()
                    return@post
                }
            }

            when (val upvoteResult = productionDatabaseAPI.vote(userID, contentID, dbOperationType)) {

                is VoteResult.ContentNotExist -> {
                    call.respondAsContentNotFoundByID(contentID)
                    return@post
                }

                is VoteResult.UserNotExist -> {
                    call.respondAsUserNotExistByID(userID)
                    return@post
                }

                is VoteResult.AlreadyVoted -> {
                    call.respondAsAlreadyVoted()
                    return@post
                }

                is VoteResult.AlreadyDownVoted -> {
                    call.respondAsAlreadyDownvoted()
                    return@post
                }

                is VoteResult.Error -> {
                    val errorText = buildString {
                        append("Failed to vote.")
                        appendLine()
                        append("ContentID: $contentID")
                        appendLine()
                        append("UserID: $userID")
                        appendLine()
                        append("OperationType: $operationType")
                    }
                    call.logErrorToFile(errorText, upvoteResult.t)
                    call.respondAsErrorByException(upvoteResult.t)
                    return@post
                }

                is VoteResult.OK -> {
                    call.respondAsOkWithUnit()
                    return@post
                }
            }
        }
    }
}

private suspend fun ApplicationCall.respondAsInvalidOperationType() {
    val argName = Routes.Vote.getOperationTypeArgName()
    val upvoteOperationArgument = FavoriteOperation.ADD.operationArgument
    val downvoteOperationArgument = FavoriteOperation.REMOVE.operationArgument
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "$argName argument must be $upvoteOperationArgument for upvote and $downvoteOperationArgument for downvote"
    )
}

private suspend fun ApplicationCall.respondAsAlreadyVoted() {
    respondAsError(
        errorCode = HttpStatusCode.Conflict,
        errorText = "User already vote same way"
    )
}

private suspend fun ApplicationCall.respondAsAlreadyDownvoted() {
    respondAsError(
        errorCode = HttpStatusCode.Conflict,
        errorText = "User already vote same way"
    )
}