package rockyouapi.route.vote

import database.external.DatabaseAPI
import database.external.result.VoteResult
import database.internal.VoteType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserIDPrincipal
import rockyouapi.route.Routes
import rockyouapi.utils.*

/**
 * Route to change(up) content rating.
 * Requirements: contentID, user token.
 * @see rockyouapi.route.auth.authCheckUserCredentialsRoute for token info.
 * */
internal fun Route.vote(databaseAPI: DatabaseAPI) {

    authenticate("auth-bearer") {
        post(Routes.Vote.path) {
            val parameters = call.receiveParameters()
            val contentID = parameters.readNotNullablePositiveInt(
                argName = Routes.Vote.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.Vote.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.Vote.getContentIDArgName())
                    return@post

                },
                onArgumentNegativeOrZeroIntError = {
                    call.respondAsMustBeNonNegativeArgumentValue(Routes.Vote.getContentIDArgName())
                    return@post

                }
            )

            val userID = call.principal<UserIDPrincipal>()?.userID ?: run {
                call.respondAsUserUnexpectedMissed()
                return@post
            }

            when (val upvoteResult = databaseAPI.upvote(userID, contentID, VoteType.UPVOTE)) {
                is VoteResult.OK -> {
                    call.respond(HttpStatusCode.OK)
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