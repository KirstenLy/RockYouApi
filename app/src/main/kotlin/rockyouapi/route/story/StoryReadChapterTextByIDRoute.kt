package rockyouapi.route.story

import database.external.contract.ProductionDatabaseAPI
import database.external.result.common.SimpleOptionalDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rockyouapi.route.Routes
import rockyouapi.utils.*

/**
 * Route to get chapter text.
 *
 * Requirements: chapterID.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with list of [String].
 * - [HttpStatusCode.BadRequest] If chapterID not presented or invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.storyReadChapterTextByIDRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    get(Routes.ReadChapterTextByID.path) {

        val chapterID = call.parameters.readNotNullableInt(
            argName = Routes.ReadChapterTextByID.getChapterIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.ReadChapterTextByID.getChapterIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.ReadChapterTextByID.getChapterIDArgName())
                return@get
            },
        )

        when (val getStoryTextResult = productionDatabaseAPI.getStoryChapterTextByID(chapterID)) {

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotFoundByID(chapterID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                call.logErrorToFile("Failed to read chapter text. ChapterID: $chapterID", getStoryTextResult.t)
                call.respondAsErrorByException(getStoryTextResult.t)
                return@get
            }

            is SimpleOptionalDataResult.Data -> {
                call.respondAsOkWithData(getStoryTextResult.model)
                return@get
            }
        }
    }
}