package rockyouapi.route.video

import rockyouapi.route.Routes
import rockyouapi.utils.*
import rockyouapi.utils.respondAsArgumentRequiredError
import database.external.DatabaseAPI
import database.external.filter.VideoByIDFilter
import database.external.result.SimpleOptionalDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Route to get one video.
 * Requirements: contentID.
 * */
internal fun Route.videoReadByIDRoute(databaseAPI: DatabaseAPI) {

    get(Routes.VideoByID.path) {
        val videoID = call.parameters.readNotNullablePositiveInt(
            argName = Routes.VideoByID.getVideoIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.VideoByID.getVideoIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoByID.getVideoIDArgName())
                return@get

            },
            onArgumentNegativeOrZeroIntError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.VideoByID.getVideoIDArgName())
                return@get
            }
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.VideoByID.getEnvLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoByID.getEnvLangIDArgName())
                return@get
            }
        )

        val filter = VideoByIDFilter(
            videoID = videoID,
            environmentLangID = environmentLangID
        )

        when (val getVideoRequestResult = databaseAPI.getVideoByID(filter)) {
            is SimpleOptionalDataResult.Data -> {
                call.respond(HttpStatusCode.OK, getVideoRequestResult.model)
                return@get
            }

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotExistByID(videoID)
                return@get

            }

            is SimpleOptionalDataResult.Error -> {
                call.respondAsUnexpectedError(getVideoRequestResult.t)
                return@get
            }
        }
    }
}