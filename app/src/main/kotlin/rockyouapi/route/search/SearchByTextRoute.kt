package rockyouapi.route.search

import rockyouapi.route.Routes
import database.external.DatabaseAPI
import database.external.result.SimpleDataResult
import declaration.GetContentByTextResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.utils.respondAsUnexpectedError

/**
 * Route to get content by text the content.
 * No requirements.
 * */
internal fun Route.searchByTextRoute(databaseAPI: DatabaseAPI, minimumRequestLength: Int) {

    get(Routes.SearchByText.path) {
        val searchTextArgument = call.getSearchText()
        if (searchTextArgument.isNullOrBlank() || searchTextArgument.length <= minimumRequestLength) {
            call.respond(GetContentByTextResult())
            return@get
        }
        when (val getContentByTextResult = databaseAPI.getContentByText(searchTextArgument)) {
            is SimpleDataResult.Data -> {
                call.respond(HttpStatusCode.OK, getContentByTextResult.data)
                return@get
            }
            is SimpleDataResult.Error -> {
                call.respondAsUnexpectedError(getContentByTextResult.t)
                return@get
            }
        }
    }
}

private fun ApplicationCall.getSearchText() = parameters[Routes.SearchByText.getSearchTextArgName()]