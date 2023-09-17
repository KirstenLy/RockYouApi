package rockyouapi.route.picture

import rockyouapi.route.Routes
import rockyouapi.utils.*
import rockyouapi.utils.respondAsIncorrectTypeWhenIntExpected
import database.external.DatabaseAPI
import database.external.filter.PictureByIDFilter
import database.external.result.SimpleOptionalDataResult
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Route to get one picture.
 * Requirements: pictureID.
 * */
internal fun Route.pictureReadByIDRoute(databaseAPI: DatabaseAPI) {

    get(Routes.PictureByID.path) {
        val pictureID = call.parameters.readNotNullableNotNegativeInt(
            argName = Routes.PictureByID.getPictureIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.PictureByID.getPictureIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureByID.getPictureIDArgName())
                return@get
            },
            onArgumentNegativeIntError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.PictureByID.getPictureIDArgName())
                return@get
            }
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.PictureByID.getEnvLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureByID.getEnvLangIDArgName())
                return@get
            }
        )

        val filter = PictureByIDFilter(
            pictureID,
            environmentLangID?.toByte()
        )
        when (val getPictureByIDResult = databaseAPI.getPictureByID(filter)) {
            is SimpleOptionalDataResult.Data -> {
                call.respond(getPictureByIDResult.model)
                return@get
            }

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotExistByID(pictureID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                call.respondAsUnexpectedError(getPictureByIDResult.t)
                return@get
            }
        }
    }
}