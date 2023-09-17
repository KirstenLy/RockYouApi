package rockyouapi.route.story

import rockyouapi.route.Routes
import rockyouapi.utils.*
import rockyouapi.utils.respondAsArgumentRequiredError
import database.external.DatabaseAPI
import database.external.result.SimpleOptionalDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Route to get text of one chapter.
 * Requirements: chapterID.
 * */
internal fun Route.storyReadChapterTextByIDRoute(databaseAPI: DatabaseAPI) {

    get(Routes.ReadChapterTextByID.path) {
        val chapterID = call.parameters.readNotNullablePositiveInt(
            argName = Routes.ReadChapterTextByID.getChapterIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.ReadChapterTextByID.getChapterIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.ReadChapterTextByID.getChapterIDArgName())
                return@get
            },
            onArgumentNegativeOrZeroIntError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.ReadChapterTextByID.getChapterIDArgName())
                return@get
            }
        )

        when (val getStoryTextResult = databaseAPI.getStoryChapterTextByID(chapterID)) {
            is SimpleOptionalDataResult.Data -> {
                call.respond(HttpStatusCode.OK, getStoryTextResult.model)
                return@get
            }

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotExistByID(chapterID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                call.respondAsUnexpectedError(getStoryTextResult.t)
                return@get
            }
        }
    }
}