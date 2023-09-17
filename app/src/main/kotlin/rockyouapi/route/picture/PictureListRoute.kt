package rockyouapi.route.picture

import rockyouapi.route.Routes
import rockyouapi.utils.*
import rockyouapi.utils.readNullableInt
import database.external.DatabaseAPI
import database.external.filter.PictureListFilter
import database.external.result.SimpleListResult
import declaration.entity.Picture
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Route to get pictures. Pagination support.
 * Requirements: limit.
 * */
internal fun Route.pictureListRoute(databaseAPI: DatabaseAPI) {

    get(Routes.PictureList.path) {

        val langID = call.parameters.readNullableInt(
            argName = Routes.PictureList.getLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureList.getLangIDArgName())
                return@get
            }
        )

        val limit = call.parameters.readNotNullablePositiveInt(
            argName = Routes.PictureList.getLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.PictureList.getLimitArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureList.getLimitArgName())
                return@get

            },
            onArgumentNegativeOrZeroIntError = {
                call.respond(HttpStatusCode.OK, emptyList<Picture>())
                return@get
            }
        )

        val offset = call.parameters.readNullablePositiveLong(
            argName = Routes.PictureList.getOffsetArgName(),
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureList.getOffsetArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.PictureList.getOffsetArgName())
                return@get
            }
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.PictureList.getEnvLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureList.getEnvLangIDArgName())
                return@get
            }
        )

        val requestFilter = PictureListFilter(
            langID = langID,
            environmentLangID = environmentLangID?.toByte(),
            limit = limit.toLong(),
            offset = offset
        )
        when (val getPictureListRequestResult = databaseAPI.getPictures(requestFilter)) {
            is SimpleListResult.Data -> {
                call.respond(HttpStatusCode.OK, getPictureListRequestResult.data)
                return@get
            }

            is SimpleListResult.Error -> {
                call.respondAsUnexpectedError(getPictureListRequestResult.t)
                return@get
            }
        }
    }
}