package rockyouapi.route.story

import database.external.contract.ProductionDatabaseAPI
import database.external.filter.StoryByIDFilter
import database.external.result.common.SimpleOptionalDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rockyouapi.model.story.Story
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*

/**
 * Route to get story by ID.
 *
 * Requirements: storyID.
 * Additional: environmentID.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with [Story].
 * - [HttpStatusCode.BadRequest] If storyID not presented or invalid or environmentID invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.storyReadByIDRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    get(Routes.StoryByID.path) {

        val storyID = call.parameters.readNotNullableInt(
            argName = Routes.StoryByID.getStoryIDArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.StoryByID.getStoryIDArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.StoryByID.getStoryIDArgName())
                return@get
            },
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.StoryByID.getEnvLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.StoryByID.getEnvLangIDArgName())
                return@get
            }
        )

        val requestFilter = StoryByIDFilter(
            storyID = storyID,
            environmentLangID = environmentLangID
        )

        when (val getStoryByIDResult = productionDatabaseAPI.getStoryByID(requestFilter)) {

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsContentNotFoundByID(storyID)
                return@get
            }

            is SimpleOptionalDataResult.Error -> {
                call.logErrorToFile("Failed to read story by ID. Filter: $requestFilter", getStoryByIDResult.t)
                call.respondAsErrorByException(getStoryByIDResult.t)
                return@get
            }

            is SimpleOptionalDataResult.Data -> {
                try {
                    val response = getStoryByIDResult.model.toWeb()
                    call.respondAsOkWithData(response)
                } catch (t: Throwable) {
                    val errorText = buildString {
                        append("Get story by ID return ok, but something happen after.")
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