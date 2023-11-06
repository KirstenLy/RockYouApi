package rockyouapi.route.video

import database.external.contract.ProductionDatabaseAPI
import database.external.filter.VideoByIDFilter
import database.external.result.common.SimpleOptionalDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rockyouapi.model.Video
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*

/**
 * Route to get video by ID.
 *
 * Requirements: videoID.
 * Additional: environmentID.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with [Video].
 * - [HttpStatusCode.BadRequest] If videoID not presented or invalid or environmentID invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.videoReadByIDRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    get(Routes.VideoByID.path) {

        val videoID = call.parameters.readNotNullableInt(
            argName = Routes.VideoByID.getVideoIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.VideoByID.getVideoIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoByID.getVideoIDArgName())
                return@get

            },
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.VideoByID.getEnvLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoByID.getEnvLangIDArgName())
                return@get
            }
        )

        val requestFilter = VideoByIDFilter(
            videoID = videoID,
            environmentLangID = environmentLangID
        )

        when (val getVideoRequestResult = productionDatabaseAPI.getVideoByID(requestFilter)) {

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotFoundByID(videoID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                call.logErrorToFile("Failed to get video by ID. Filter: $requestFilter", getVideoRequestResult.t)
                call.respondAsErrorByException(getVideoRequestResult.t)
                return@get
            }

            is SimpleOptionalDataResult.Data -> {
                try {
                    val response = getVideoRequestResult.model.toWeb()
                    call.respondAsOkWithData(response)
                } catch (t: Throwable) {
                    val errorText = buildString {
                        append("Get video by ID return ok, but something happen after.")
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