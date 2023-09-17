package rockyouapi.route.report

import rockyouapi.route.Routes
import rockyouapi.utils.*
import rockyouapi.utils.respondAsArgumentRequiredError
import database.external.DatabaseAPI
import database.external.result.SimpleUnitResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserTokensManager

/**
 * Route to report the content.
 * Requirements: contentID, report.
 * Additional: token.
 * @see rockyouapi.route.auth.authLoginRoute for token info.
 * */
internal fun Route.reportRoute(databaseAPI: DatabaseAPI, tokensManager: UserTokensManager) {

    post(Routes.Report.path) {
        val parameters = call.parameters

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
            argName = Routes.Report.getReportIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.Report.getReportIDArgName())
                return@post
            },
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.Report.getReportIDArgName())
                return@post
            }
        )

        val authToken = call.request.authorization()?.extractBearerToken()
        if (authToken != null) {
            val isTokenValid = tokensManager.isTokenExistAndValid(authToken)
            if (!isTokenValid) {
                call.respondAsTokenExpired()
                return@post
            }
        }

        val userID = if (authToken != null) {
            val tokenOwner = tokensManager.getTokenOwnerID(authToken)
            if (tokenOwner == null) {
                call.respondAsUserUnexpectedMissed()
                return@post
            }
            tokenOwner
        } else null

        when (val setReportResult = databaseAPI.report(contentID, report, userID)) {
            is SimpleUnitResult.Ok -> {
                call.respond(HttpStatusCode.OK)
                return@post
            }

            is SimpleUnitResult.Error -> {
                call.respondAsUnexpectedError(setReportResult.t)
                return@post
            }
        }
    }
}