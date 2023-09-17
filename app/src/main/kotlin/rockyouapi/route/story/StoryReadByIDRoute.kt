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
 * Route to get one story.
 * Requirements: contentID.
 * */
internal fun Route.storyReadByIDRoute(databaseAPI: DatabaseAPI) {

    get(Routes.StoryByID.path) {
        val storyID = call.parameters.readNotNullablePositiveInt(
            argName = Routes.StoryByID.getStoryIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.StoryByID.getStoryIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.StoryByID.getStoryIDArgName())
                return@get
            },
            onArgumentNegativeOrZeroIntError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.StoryByID.getStoryIDArgName())
                return@get
            }
        )

        when (val getStoryByIDResult = databaseAPI.getStoryByID(storyID)) {
            is SimpleOptionalDataResult.Data -> {
                call.respond(HttpStatusCode.OK, getStoryByIDResult.model)
                return@get
            }
            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotExistByID(storyID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                call.respondAsUnexpectedError(getStoryByIDResult.t)
                return@get
            }
        }
    }
}