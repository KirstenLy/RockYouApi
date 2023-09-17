package rockyouapi.route.auth

import rockyouapi.route.Routes
import database.external.DatabaseAPI
import database.external.result.CheckUserCredentialsResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserTokensManager
import rockyouapi.model.UserWithToken
import rockyouapi.utils.readNotNullableFilledString
import rockyouapi.utils.respondAsArgumentRequiredError
import rockyouapi.utils.respondAsStringArgMustBeNotEmpty
import rockyouapi.utils.respondAsUnexpectedError

/**
 * Route to log in user.
 * Requirements: login and password.
 * Not check login/password restrictions.
 * If login successful, create and store auth token. Return user as well.
 * */
internal fun Route.authLoginRoute(databaseAPI: DatabaseAPI, tokensManager: UserTokensManager) {

    post(Routes.AuthLogin.path) {

        val parameters = call.parameters

        val login = parameters.readNotNullableFilledString(
            argName = Routes.AuthLogin.getLoginArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.AuthLogin.getLoginArgName())
                return@post
            },
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.AuthLogin.getLoginArgName())
                return@post
            }
        )

        val password = parameters.readNotNullableFilledString(
            argName = Routes.AuthLogin.getPasswordArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.AuthLogin.getPasswordArgName())
                return@post
            },
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.AuthLogin.getPasswordArgName())
                return@post
            }
        )

        when (val loginResult = databaseAPI.checkUserCredentials(login, password)) {

            is CheckUserCredentialsResult.Ok -> {
                val token = tokensManager.putUserToken(loginResult.user.id)
                call.respond(HttpStatusCode.OK, UserWithToken(loginResult.user, token))
                return@post
            }

            is CheckUserCredentialsResult.UserNotExist -> {
                call.respond(HttpStatusCode.Unauthorized, "User \"$login\" not exist")
                return@post
            }

            is CheckUserCredentialsResult.PasswordMismatch -> {
                call.respond(HttpStatusCode.Unauthorized, "Password mismatch for user \"$login\"")
                return@post
            }

            is CheckUserCredentialsResult.UnexpectedError -> {
                call.respondAsUnexpectedError(loginResult.t)
                return@post
            }
        }
    }
}