package rockyouapi.route.favorite

import database.external.DatabaseAPI
import database.external.result.AddOrRemoveFavoriteResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserIDPrincipal
import rockyouapi.route.Routes
import rockyouapi.utils.*
import database.external.operation.FavoriteOperation as DBAPIFavoriteOperation
import rockyouapi.operation.FavoriteOperation as APIFavoriteOperation

/**
 * Route to add or remove content from favorite.
 * Requirements: operation type(add/remove), contentID, user token.
 * @see rockyouapi.route.auth.authLoginRoute for token info.
 * */
internal fun Route.addOrRemoveFavoriteRoute(databaseAPI: DatabaseAPI) {

    authenticate("auth-bearer") {

        post(Routes.FavoriteAddOrRemove.path) {

            val parameters = call.parameters

            val operationType = parameters.readNotNullableInt(
                argName = Routes.FavoriteAddOrRemove.getOperationTypeArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.FavoriteAddOrRemove.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.FavoriteAddOrRemove.getContentIDArgName())
                    return@post
                },
            )

            val dbOperationType = when (operationType) {
                APIFavoriteOperation.ADD.operationArgument -> DBAPIFavoriteOperation.ADD
                APIFavoriteOperation.REMOVE.operationArgument -> DBAPIFavoriteOperation.REMOVE
                else -> {
                    val argName = Routes.FavoriteAddOrRemove.getOperationTypeArgName()
                    call.respond(HttpStatusCode.BadRequest, "$argName argument must be 0(add) or 1(remove)")
                    return@post
                }
            }

            val contentID = parameters.readNotNullablePositiveInt(
                argName = Routes.FavoriteAddOrRemove.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.FavoriteAddOrRemove.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.FavoriteAddOrRemove.getContentIDArgName())
                    return@post
                },
                onArgumentNegativeOrZeroIntError = {
                    call.respondAsMustBeNonNegativeArgumentValue(Routes.FavoriteAddOrRemove.getContentIDArgName())
                    return@post
                }
            )

            val userID = call.principal<UserIDPrincipal>()?.userID ?: run {
                call.respondAsUserUnexpectedMissed()
                return@post
            }

            when (val addOrRemoveFavoriteResult = databaseAPI.addOrRemoveFavorite(dbOperationType, userID, contentID)) {
                is AddOrRemoveFavoriteResult.Ok -> {
                    call.respond(HttpStatusCode.OK)
                    return@post
                }

                is AddOrRemoveFavoriteResult.AlreadyInFavorite -> {
                    call.respond(HttpStatusCode.Conflict, "Content already in favorite for this user")
                    return@post
                }

                is AddOrRemoveFavoriteResult.NotInFavorite -> {
                    call.respond(HttpStatusCode.Conflict, "Content not in favorite for this user")
                    return@post
                }

                is AddOrRemoveFavoriteResult.Error -> {
                    call.respondAsUnexpectedError(addOrRemoveFavoriteResult.t)
                    return@post
                }
            }
        }
    }
}