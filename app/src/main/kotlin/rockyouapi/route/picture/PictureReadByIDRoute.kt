package rockyouapi.route.picture

import database.external.contract.ProductionDatabaseAPI
import database.external.filter.PictureByIDFilter
import database.external.result.common.SimpleOptionalDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rockyouapi.model.Picture
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*

/**
 * Route to get picture by ID.
 *
 * Requirements: pictureID.
 * Additional: environmentID.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with [Picture].
 * - [HttpStatusCode.BadRequest] If pictureID not presented or invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.pictureReadByIDRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    get(Routes.PictureByID.path) {

        val pictureID = call.parameters.readNotNullableInt(
            argName = Routes.PictureByID.getPictureIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.PictureByID.getPictureIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureByID.getPictureIDArgName())
                return@get
            },
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.PictureByID.getEnvLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureByID.getEnvLangIDArgName())
                return@get
            }
        )

        val requestFilter = PictureByIDFilter(
            pictureID = pictureID,
            environmentLangID = environmentLangID
        )

        when (val getPictureByIDResult = productionDatabaseAPI.getPictureByID(requestFilter)) {

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotFoundByID(pictureID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                call.logErrorToFile("Failed to get picture by ID. Filter: $requestFilter", getPictureByIDResult.t)
                call.respondAsErrorByException(getPictureByIDResult.t)
                return@get
            }

            is SimpleOptionalDataResult.Data -> {
                try {
                    val response = getPictureByIDResult.model.toWeb()
                    call.respondAsOkWithData(response)
                } catch (t: Throwable) {
                    val errorText = buildString {
                        append("Get picture by ID return ok, but something happen after.")
                        appendLine()
                        append("Filter: $requestFilter")
                    }
                    call.logErrorToFile(errorText, t)
                    call.respondAsErrorByException(t)
                }
                return@get
            }
        }
    }
}