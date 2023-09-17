package rockyouapi.route.story

import database.external.DatabaseAPI
import database.external.filter.StoryListFilter
import database.external.result.SimpleListResult
import declaration.entity.Picture
import declaration.entity.story.Story
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.route.Routes
import rockyouapi.utils.*

/**
 * Route to get story list.
 * Requirements: limit.
 * */
internal fun Route.storyListRoute(databaseAPI: DatabaseAPI) {

    get(Routes.StoriesList.path) {

        val langID = call.parameters.readNullableInt(
            argName = Routes.StoriesList.getLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.StoriesList.getLangIDArgName())
                return@get
            }
        )

        val limit = call.parameters.readNotNullablePositiveInt(
            argName = Routes.StoriesList.getLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.StoriesList.getLimitArgName())
                return@get
            },
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.StoriesList.getLimitArgName())
                return@get

            },
            onArgumentNegativeOrZeroIntError = {
                call.respond(HttpStatusCode.OK, emptyList<Story>())
                return@get
            }
        )

        val offset = call.parameters.readNotNullableNotNegativeLong(
            argName = Routes.StoriesList.getOffsetArgName(),
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.StoriesList.getOffsetArgName())
                return@get
            },
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.StoriesList.getOffsetArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.StoriesList.getOffsetArgName())
                return@get
            }
        )

        val requestFilter = StoryListFilter(
            langID = langID,
            limit = limit.toLong(),
            offset = offset
        )
        when (val getStoriesRequestResult = databaseAPI.getStories(requestFilter)) {
            is SimpleListResult.Data -> {
                call.respond(HttpStatusCode.OK, getStoriesRequestResult.data)
                return@get
            }

            is SimpleListResult.Error -> {
                call.respondAsUnexpectedError(getStoriesRequestResult.t)
                return@get
            }
        }
    }
}