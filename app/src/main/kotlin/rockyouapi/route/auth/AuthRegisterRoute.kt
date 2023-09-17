package rockyouapi.route.auth

import rockyouapi.route.Routes
import database.external.DatabaseAPI
import database.external.result.CreateUserResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserTokensManager
import rockyouapi.utils.respondAsArgumentRequiredError
import rockyouapi.utils.respondAsUnexpectedError

/**
 * Route to add comment.
 * Requirements: login, password, user token.
 * No contact data (as email/phone) required.
 * */
internal fun Route.authCreateUserRoute(
    databaseAPI: DatabaseAPI,
    tokensManager: UserTokensManager,
    minimumPasswordLength: Int
) {

    get(Routes.AuthRegister.path) {
        val login = call.parameters[Routes.AuthRegister.getLoginArgName()]
        if (login == null) {
            call.respondAsArgumentRequiredError(Routes.AuthRegister.getLoginArgName())
            return@get
        }

        val password = call.parameters[Routes.AuthRegister.getPasswordArgName()]
        if (password == null) {
            call.respondAsArgumentRequiredError(Routes.AuthRegister.getPasswordArgName())
            return@get
        }

        if (password.length < minimumPasswordLength) {
            call.respond(HttpStatusCode.BadRequest, "Password minimum length: $minimumPasswordLength")
            return@get
        }

        when (val createUserResult = databaseAPI.createUser(login, password)) {
            is CreateUserResult.Ok -> {
                val token = tokensManager.putUserToken(createUserResult.userID)
                call.respond(HttpStatusCode.OK, token)
                return@get
            }

            is CreateUserResult.SameUserAlreadyExist -> {
                call.respond(HttpStatusCode.Conflict, "User with login \"$login\" already exist")
                return@get
            }

            is CreateUserResult.UnexpectedError -> {
                call.respondAsUnexpectedError(createUserResult.t)
                return@get
            }
        }
    }
}