package rockyouapi.route.comment

import rockyouapi.route.Routes
import rockyouapi.utils.*
import database.external.DatabaseAPI
import database.external.result.SimpleUnitResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserIDPrincipal

/**
 * Route to add comment.
 * Requirements: contentID, commentText, user token.
 * @see rockyouapi.route.auth.authLoginRoute for token info.
 * */
internal fun Route.commentAddRoute(databaseAPI: DatabaseAPI) {

    authenticate("auth-bearer") {

        post(Routes.CommentAdd.path) {

            val parameters = call.parameters

            val contentID = parameters.readNotNullablePositiveInt(
                argName = Routes.CommentAdd.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.CommentAdd.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.CommentAdd.getContentIDArgName())
                    return@post
                },
                onArgumentNegativeOrZeroIntError = {
                    call.respondAsMustBeNonNegativeArgumentValue(Routes.CommentAdd.getContentIDArgName())
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

            val userID = call.principal<UserIDPrincipal>()?.userID ?: run {
                call.respondAsUserUnexpectedMissed()
                return@post
            }

            when (val addCommentResult = databaseAPI.addComment(userID, contentID, commentText)) {
                is SimpleUnitResult.Ok -> {
                    call.respond(HttpStatusCode.OK)
                    return@post
                }

                is SimpleUnitResult.Error -> {
                    call.respondAsUnexpectedError(addCommentResult.t)
                    return@post
                }
            }
        }
    }
}