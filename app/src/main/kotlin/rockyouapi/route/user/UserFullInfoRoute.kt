package rockyouapi.route.user

import database.external.contract.ProductionDatabaseAPI
import database.external.result.common.SimpleOptionalDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*

/**
 * Route to get user full info.

 * Requirements: userID.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetcher.
 * - [HttpStatusCode.BadRequest] If userID not presented or invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.readUserFullInfo(productionDatabaseAPI: ProductionDatabaseAPI) {

    get(Routes.UserFullInfo.path) {

        val userID = call.parameters.readNotNullableInt(
            argName = Routes.UserFullInfo.getUserIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.UserFullInfo.getUserIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsArgumentRequiredError(Routes.UserFullInfo.getUserIDArgName())
                return@get
            }
        )

        when (val getUserFullInfoRequestResult = productionDatabaseAPI.getUserFullInfoByID(userID)) {

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsUserNotExistByID(userID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                val exception = getUserFullInfoRequestResult.t
                call.logErrorToFile("Get user full info route error, userID argument: $userID", exception)
                call.respondAsErrorByException(getUserFullInfoRequestResult.t)
                return@get
            }

            is SimpleOptionalDataResult.Data -> {
                try {
                    call.respondAsOkWithData(getUserFullInfoRequestResult.model.toWeb())
                } catch (t: Throwable) {
                    call.logErrorToFile("Get user full info return ok, but something happen after. UserID: $userID", t)
                    call.respondAsErrorByException(t)
                }
                return@get
            }
        }
    }
}