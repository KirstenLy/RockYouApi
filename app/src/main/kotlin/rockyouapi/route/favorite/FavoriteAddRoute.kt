package rockyouapi.route.favorite

import rockyouapi.route.Routes
import rockyouapi.utils.*
import rockyouapi.utils.respondAsArgumentRequiredError
import database.external.DatabaseAPI
import database.external.result.SimpleUnitResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserIDPrincipal

/**
 * Route to add content to favorite.
 * Requirements: contentID, user token.
 * @see rockyouapi.route.auth.authCheckUserCredentialsRoute for token info.
 * */
internal fun Route.addToFavoriteRoute(databaseAPI: DatabaseAPI) {

    authenticate("auth-bearer") {
        get(Routes.FavoriteAdd.path) {
            val contentID = call.parameters.readNotNullablePositiveInt(
                argName = Routes.FavoriteAdd.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.FavoriteAdd.getContentIDArgName())
                    return@get
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.FavoriteAdd.getContentIDArgName())
                    return@get
                },
                onArgumentNegativeOrZeroIntError = {
                    call.respondAsMustBeNonNegativeArgumentValue(Routes.FavoriteAdd.getContentIDArgName())
                    return@get
                }
            )

            val userID = call.principal<UserIDPrincipal>()?.userID ?: run {
                call.respondAsUserUnexpectedMissed()
                return@get
            }

            when (val addToFavoriteResult = databaseAPI.addToFavorite(userID, contentID)) {
                is SimpleUnitResult.Ok -> {
                    call.respond(HttpStatusCode.OK)
                    return@get
                }

                is SimpleUnitResult.Error -> {
                    call.respondAsUnexpectedError(addToFavoriteResult.t)
                    return@get
                }
            }
        }
    }
}