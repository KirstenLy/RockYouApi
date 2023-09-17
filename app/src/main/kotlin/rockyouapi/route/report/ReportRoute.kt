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

/**
 * Route to report the content.
 * Requirements: content identifier, report.
 * */
internal fun Route.reportRoute(databaseAPI: DatabaseAPI) {

    post(Routes.Report.path) {
        val parameters = call.parameters
        val contentID = parameters.readNotNullablePositiveInt(
            argName = Routes.Report.getContentIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.Report.getContentIDArgName())
                return@post
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.Report.getContentIDArgName())
                return@post
            },
            onArgumentNegativeOrZeroIntError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.Report.getContentIDArgName())
                return@post
            },
        )

        val report = parameters[Routes.Report.getReportIDArgName()]
        if (report.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Report text must be presented")
            return@post
        }

        val userID = parameters.readNullableInt(
            argName = Routes.Report.getUserIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.Report.getUserIDArgName())
                return@post
            }
        )

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