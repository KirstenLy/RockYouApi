package rockyouapi.route.vote

import database.external.DatabaseAPI
import database.external.result.VoteResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserIDPrincipal
import rockyouapi.operation.ChangeRatingOperation
import rockyouapi.route.Routes
import rockyouapi.utils.*
import database.external.operation.VoteOperation as DBChangeRatingOperation

/**
 * Route to change content rating.
 * Requirements: operation type(upvote/downvote), contentID, user token.
 * @see rockyouapi.route.auth.authLoginRoute for token info.
 * */
internal fun Route.voteRoute(databaseAPI: DatabaseAPI) {

    authenticate("auth-bearer") {

        post(Routes.Vote.path) {

            val parameters = call.parameters

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
                    val argName = Routes.Vote.getOperationTypeArgName()
                    call.respond(HttpStatusCode.BadRequest, "$argName argument must be 0(downvotw) or 1(upvote)")
                    return@post
                }
            }

            val userID = call.principal<UserIDPrincipal>()?.userID ?: run {
                call.respondAsUserUnexpectedMissed()
                return@post
            }

            when (val upvoteResult = databaseAPI.vote(userID, contentID, dbOperationType)) {
                is VoteResult.OK -> {
                    call.respond(HttpStatusCode.OK)
                    return@post
                }

                is VoteResult.ContentNotExist -> {
                    call.respondAsContentNotExistByID(contentID)
                    return@post
                }

                is VoteResult.AlreadyVoted -> {
                    call.respond(HttpStatusCode.Conflict, "User already vote same way")
                    return@post
                }

                is VoteResult.AlreadyDownVoted -> {
                    call.respond(HttpStatusCode.Conflict, "User already vote same way")
                    return@post
                }

                is VoteResult.Error -> {
                    call.respondAsUnexpectedError(upvoteResult.t)
                    return@post
                }
            }
        }
    }
}