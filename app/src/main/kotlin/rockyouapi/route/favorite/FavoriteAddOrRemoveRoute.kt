package rockyouapi.route.favorite

import database.external.contract.ProductionDatabaseAPI
import database.external.result.AddOrRemoveFavoriteResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.route.Routes
import rockyouapi.route.Routes.Companion.JWT_AUTHORIZATION_PROVIDER_KEY
import rockyouapi.security.*
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.checkTokenType
import rockyouapi.utils.*
import database.external.operation.FavoriteOperation as DBAPIFavoriteOperation
import rockyouapi.operation.FavoriteOperation as APIFavoriteOperation

/**
 * Route to add or remove content from/to favorite.
 *
 * Requirements: operation type(add/remove), contentID, userID and user access token.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Content added to favorite or removed from favorite.
 * - [HttpStatusCode.Unauthorized] On different tokens error, like missed climes.
 * - [HttpStatusCode.BadRequest] If contentID/userID/operationType  presented or invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.addOrRemoveFavoriteRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    authenticate(JWT_AUTHORIZATION_PROVIDER_KEY) {

        post(Routes.FavoriteAddOrRemove.path) {

            val principal = call.principal<JWTPrincipal>() ?: run {
                call.respondAsTokenNotFoundOrNotSupported()
                return@post
            }

            principal.checkTokenType(
                tokenTypeClaimKey = CLAIM_KEY_TOKEN_TYPE,
                onClaimNotPresented = {
                    call.respondAsTokenHasNoType()
                    return@post
                },
                onRefreshType = {
                    call.respondAsTokenHasRefreshType()
                    return@post
                },
                onAccessType = {
                    // It's expected scenario, continue execution
                },
                onUnknownType = {
                    call.respondAsTokenHasInvalidType()
                    return@post
                }
            )

            val parameters = call.receiveParameters()

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
                    call.respondAsInvalidOperationType()
                    return@post
                }
            }

            val contentID = parameters.readNotNullableInt(
                argName = Routes.FavoriteAddOrRemove.getContentIDArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.FavoriteAddOrRemove.getContentIDArgName())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.FavoriteAddOrRemove.getContentIDArgName())
                    return@post
                },
            )

            val userID = parameters.readNotNullableInt(
                argName = Routes.FavoriteAddOrRemove.getUserID(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.FavoriteAddOrRemove.getUserID())
                    return@post
                },
                onArgumentNotIntError = {
                    call.respondAsIncorrectTypeWhenIntExpected(Routes.FavoriteAddOrRemove.getUserID())
                    return@post
                }
            )

            val tokenOwner = principal.tryToExtractUserID {
                call.respondAsTokenHasNoOwner()
                return@post
            }

            // Make sure token belong to user who request change favorite.
            // if check fail, answer as "Token invalid"(not "Token belong to another user") to prevent knowledge
            // about connection between userID and token.
            if (tokenOwner != userID) {
                call.respondAsTokenInvalid()
                return@post
            }

            when (val addOrRemoveFavoriteResult = productionDatabaseAPI.addOrRemoveFavorite(
                operation = dbOperationType,
                userID = userID,
                contentID = contentID
            )) {

                is AddOrRemoveFavoriteResult.ContentNotExists -> {
                    call.respondAsContentNotExistByID(userID)
                    return@post
                }

                is AddOrRemoveFavoriteResult.UserNotExists -> {
                    call.respondAsUserNotExistByID(userID)
                    return@post
                }

                is AddOrRemoveFavoriteResult.AlreadyInFavorite -> {
                    call.respondAsAlreadyInFavorite()
                    return@post
                }

                is AddOrRemoveFavoriteResult.NotInFavorite -> {
                    call.respondAsNotInFavorite()
                    return@post
                }

                is AddOrRemoveFavoriteResult.Error -> {
                    val errorText = buildString {
                        append("Failed to add or remove content to/from favorite.")
                        appendLine()
                        append("Operation type: $operationType, ContentID: $contentID, UserID: $userID")
                    }
                    call.logErrorToFile(errorText, addOrRemoveFavoriteResult.t)
                    call.respondAsErrorByException(addOrRemoveFavoriteResult.t)
                    return@post
                }

                is AddOrRemoveFavoriteResult.Ok -> {
                    call.respondAsOkWithUnit()
                    return@post
                }
            }
        }
    }
}

private suspend fun ApplicationCall.respondAsInvalidOperationType() {
    val argName = Routes.FavoriteAddOrRemove.getOperationTypeArgName()
    val addOperationArgument = APIFavoriteOperation.ADD.operationArgument
    val removeOperationArgument = APIFavoriteOperation.REMOVE.operationArgument
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "$argName argument must be $addOperationArgument for add or $removeOperationArgument for remove"
    )
}

private suspend fun ApplicationCall.respondAsAlreadyInFavorite() {
    respondAsError(
        errorCode = HttpStatusCode.Conflict,
        errorText = "Content already in favorite for this user"
    )
}

private suspend fun ApplicationCall.respondAsNotInFavorite() {
    respondAsError(
        errorCode = HttpStatusCode.Conflict,
        errorText = "Content not in favorite for this user"
    )
}