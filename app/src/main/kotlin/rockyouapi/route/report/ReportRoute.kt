package rockyouapi.route.report

import database.external.contract.ProductionDatabaseAPI
import database.external.result.ReportResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.configuration.REPORT_MAXIMUM_LENGTH
import rockyouapi.model.Picture
import rockyouapi.route.Routes
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.checkTokenType
import rockyouapi.security.tryToExtractUserID
import rockyouapi.utils.*

/**
 * Route to report the content.
 *
 * Requirements: contentID, report.
 * Additional: userID, token.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with [Picture].
 * - [HttpStatusCode.Unauthorized] On different tokens error, like missed climes.
 * - [HttpStatusCode.BadRequest] If contentID or report not presented or invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.reportRoute(databaseAPI: ProductionDatabaseAPI) {

    authenticate(Routes.JWT_AUTHORIZATION_PROVIDER_KEY, optional = true) {

        post(Routes.Report.path) {

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
                argName = Routes.Report.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.Report.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.Report.getContentIDArgName())
                    return@post
                },
            )

            val report = parameters.readNotNullableFilledString(
                argName = Routes.Report.getReportTextArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.Report.getReportTextArgName())
                    return@post
                },
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.Report.getReportTextArgName())
                    return@post
                }
            )

            if (report.length > REPORT_MAXIMUM_LENGTH) {
                call.respondAsIntArgumentTooBig(Routes.Report.getReportTextArgName(), REPORT_MAXIMUM_LENGTH)
                return@post
            }

            val userID = parameters.readNullableInt(
                argName = Routes.FavoriteAddOrRemove.getUserID(),
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.Report.getUserIDArgName())
                    return@post
                }
            )

            // If token presented, server must be sure it's belong to caller
            // Server compare token owner and userID, so if token presented userID must be presented too.
            if (tokenOwner != null && userID == null) {
                call.respondAsArgumentRequiredError(Routes.Report.getUserIDArgName())
                return@post
            }

            // Make sure token belong to user who create request.
            // if check fail, answer as "Token invalid"(not "Token belong to another user") to prevent knowledge
            // about connection between userID and token.
            if (tokenOwner != null && tokenOwner != userID) {
                call.respondAsTokenInvalid()
                return@post
            }

            when (val setReportResult = databaseAPI.report(contentID, report, userID)) {

                is ReportResult.ContentNotExist -> {
                    call.respondAsContentNotExistByID(contentID)
                    return@post
                }

                is ReportResult.UserNotExist -> {
                    userID?.let {
                        call.respondAsUserNotExistByID(it)
                        return@post
                    }
                    call.userIDMissedError()
                    return@post
                }

                is ReportResult.Error -> {
                    val errorText = buildString {
                        append("Failed to report content")
                        appendLine()
                        append("ContentID: $contentID")
                        appendLine()
                        append("Report: $report")
                        appendLine()
                        append("UserID: $userID")
                    }
                    call.logErrorToFile(errorText, setReportResult.t)
                    call.respondAsErrorByException(setReportResult.t)
                    return@post
                }

                is ReportResult.Ok -> {
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