package rockyouapi.route.comment

import common.utils.zeroOnNull
import database.external.contract.ProductionDatabaseAPI
import database.external.filter.CommentListFilter
import database.external.result.common.SimpleListResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rockyouapi.configuration.COMMENT_MAXIMUM_LENGTH
import rockyouapi.configuration.LIMIT_ARGUMENT_MAXIMUM_VALUE
import rockyouapi.model.Comment
import database.external.model.Comment as DBComment
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*

/**
 * Route to get comment list for content.
 *
 * Requirements: contentID, limit.
 * Additional: offset.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with list of [Comment].
 * - [HttpStatusCode.BadRequest] If contentID or limit not presented or invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.commentListRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    get(Routes.CommentList.path) {

        val contentID = call.parameters.readNotNullableInt(
            argName = Routes.CommentList.getContentIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.CommentList.getContentIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.CommentList.getContentIDArgName())
                return@get
            },
        )

        val limit = call.parameters.readNotNullablePositiveLong(
            argName = Routes.CommentList.getContentLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.CommentList.getContentLimitArgName())
                return@get
            },
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.CommentList.getContentLimitArgName())
                return@get
            },
            onArgumentNegativeOrZeroLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.CommentList.getContentLimitArgName())
                return@get
            },
        )

        if (limit > LIMIT_ARGUMENT_MAXIMUM_VALUE) {
            call.respondAsIntArgumentTooBig(Routes.CommentList.getContentLimitArgName(), COMMENT_MAXIMUM_LENGTH)
            return@get
        }

        val offset = call.parameters.readNullableNotNegativeLong(
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
            offset = offset.zeroOnNull(),
            limit = limit
        )

        when (val getCommentsRequestResult = productionDatabaseAPI.getComments(requestFilter)) {

            is SimpleListResult.Error -> {
                call.logErrorToFile("Failed to get comments. Filter: $requestFilter", getCommentsRequestResult.t)
                call.respondAsErrorByException(getCommentsRequestResult.t)
                return@get
            }

            is SimpleListResult.Data -> {
                try {
                    val responseData = getCommentsRequestResult.data.map(DBComment::toWeb)
                    call.respondAsOkWithData(responseData)
                } catch (t: Throwable) {
                    call.logErrorToFile("Respond as data failed. Stage: get comment list. Filter: $requestFilter", t)
                    call.respondAsErrorByException(t)
                }
                return@get
            }
        }
    }
}