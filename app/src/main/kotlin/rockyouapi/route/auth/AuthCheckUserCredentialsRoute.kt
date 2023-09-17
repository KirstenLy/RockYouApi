package rockyouapi.route.auth

import rockyouapi.route.Routes
import database.external.DatabaseAPI
import database.external.result.CheckUserCredentialsResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rockyouapi.auth.UserTokensManager
import rockyouapi.utils.respondAsArgumentRequiredError
import rockyouapi.utils.respondAsUnexpectedError

/**
 * Route to check user credentials. When credentials correct, create, hold and return auth token.
 * Requirements: login, password.
 * */
internal fun Route.authCheckUserCredentialsRoute(databaseAPI: DatabaseAPI, tokensManager: UserTokensManager) {

    get(Routes.AuthCheckUserCredentials.path) {
        val login = call.parameters[Routes.AuthCheckUserCredentials.getLoginArgName()]
        if (login == null) {
            call.respondAsArgumentRequiredError(Routes.AuthCheckUserCredentials.getLoginArgName())
            return@get
        }

        val password = call.parameters[Routes.AuthCheckUserCredentials.getPasswordArgName()]
        if (password == null) {
            call.respondAsArgumentRequiredError(Routes.AuthCheckUserCredentials.getPasswordArgName())
            return@get
        }

        when (val checkCredentialResult = databaseAPI.isUserCredentialsCorrect(login, password)) {

            is CheckUserCredentialsResult.Ok -> {
                val token = tokensManager.putUserToken(checkCredentialResult.userID)
                call.respond(HttpStatusCode.OK, token)
                return@get
            }

            is CheckUserCredentialsResult.UserNotExist -> {
                call.respond(HttpStatusCode.Unauthorized, "User \"$login\" not exist")
                return@get
            }

            is CheckUserCredentialsResult.PasswordMismatch -> {
                call.respond(HttpStatusCode.Unauthorized, "Password mismatch for user \"$login\"")
                return@get
            }

            is CheckUserCredentialsResult.UnexpectedError -> {
                call.respondAsUnexpectedError(checkCredentialResult.t)
                return@get
            }
        }
    }
}