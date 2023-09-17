package rockyouapi.route.video

import rockyouapi.route.Routes
import rockyouapi.utils.*
import rockyouapi.utils.readNullableInt
import rockyouapi.utils.respondAsIncorrectTypeWhenIntExpected
import database.external.DatabaseAPI
import database.external.filter.VideoListFilter
import database.external.result.SimpleListResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Route to get videos. Pagination support.
 * Requirements: limit.
 * */
internal fun Route.videoListRoute(databaseAPI: DatabaseAPI) {

    get(Routes.VideoList.path) {

        val limit = call.parameters.readNotNullablePositiveInt(
            argName = Routes.VideoList.getLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.VideoList.getLimitArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoList.getLimitArgName())
                return@get

            },
            onArgumentNegativeOrZeroIntError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.VideoList.getLimitArgName())
                return@get
            }
        )

        val langID = call.parameters.readNullableInt(
            argName = Routes.VideoList.getLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoList.getLangIDArgName())
                return@get
            }
        )

        val offset = call.parameters.readNullablePositiveLong(
            argName = Routes.VideoList.getOffsetArgName(),
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoList.getOffsetArgName())
                return@get

            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.VideoList.getOffsetArgName())
                return@get
            }
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.VideoList.getEnvLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoList.getEnvLangIDArgName())
                return@get
            }
        )

        val requestFilter = VideoListFilter(
            langID = langID,
            environmentLangID = environmentLangID,
            limit = limit,
            offset = offset
        )
        when (val getPicturesRequestResult = databaseAPI.getVideos(requestFilter)) {
            is SimpleListResult.Data -> {
                call.respond(HttpStatusCode.OK, getPicturesRequestResult.data)
                return@get
            }

            is SimpleListResult.Error -> {
                call.respondAsUnexpectedError(getPicturesRequestResult.t)
                return@get
            }
        }
    }
}