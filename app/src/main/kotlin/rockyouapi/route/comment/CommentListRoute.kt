package rockyouapi.route.comment

import rockyouapi.route.Routes
import rockyouapi.utils.*
import database.external.DatabaseAPI
import database.external.filter.CommentListFilter
import database.external.result.SimpleListResult
import declaration.entity.Comment
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Route to get comments to content. Pagination support.
 * Requirements: contentID, limit.
 * Additional: offset.
 * */
internal fun Route.commentListRoute(databaseAPI: DatabaseAPI) {

    get(Routes.CommentList.path) {

        val contentID = call.parameters.readNotNullableNotNegativeInt(
            argName = Routes.CommentList.getContentIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.CommentList.getContentIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.CommentList.getContentIDArgName())
                return@get
            },
            onArgumentNegativeIntError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.CommentList.getContentIDArgName())
                return@get
            },
        )

        val limit = call.parameters.readNotNullablePositiveInt(
            argName = Routes.CommentList.getContentLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.CommentList.getContentLimitArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.CommentList.getContentLimitArgName())
                return@get
            },
            onArgumentNegativeOrZeroIntError = {
                call.respond(HttpStatusCode.OK, emptyList<Comment>())
                return@get
            },
        )

        val offset = call.parameters.readNullablePositiveLong(
            argName = Routes.CommentList.getContentOffsetArgName(),
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.CommentList.getContentOffsetArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.CommentList.getContentOffsetArgName())
                return@get
            },
        )

        val requestFilter = CommentListFilter(
            contentID = contentID,
            offset = offset,
            limit = limit.toLong()
        )
        when (val getCommentsRequestResult = databaseAPI.getComments(requestFilter)) {
            is SimpleListResult.Data -> {
                call.respond(HttpStatusCode.OK, getCommentsRequestResult.data)
                return@get
            }

            is SimpleListResult.Error -> {
                call.respondAsUnexpectedError(getCommentsRequestResult.t)
                return@get
            }
        }
    }
}