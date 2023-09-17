package rockyouapi.route.auth

import rockyouapi.route.Routes
import database.external.DatabaseAPI
import database.external.result.RegisterUserResult
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
 * Route to register new user.
 * Requirements: login and password.
 * No contact data (as email/phone) required.
 * Return created user and his auth token if creation finished correct. Put token to manager.
 * */
internal fun Route.authRegisterRoute(
    databaseAPI: DatabaseAPI,
    tokensManager: UserTokensManager,
    minimumPasswordLength: Int
) {

    post(Routes.AuthRegister.path) {

        val parameters = call.parameters

        val login = parameters.readNotNullableFilledString(
            argName = Routes.AuthRegister.getLoginArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.AuthRegister.getLoginArgName())
                return@post
            },
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.AuthRegister.getLoginArgName())
                return@post
            }
        )

        val password = parameters.readNotNullableFilledString(
            argName = Routes.AuthRegister.getPasswordArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.AuthRegister.getLoginArgName())
                return@post
            },
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.AuthRegister.getLoginArgName())
                return@post
            }
        )

        if (password.length < minimumPasswordLength) {
            call.respond(HttpStatusCode.BadRequest, "Password minimum length: $minimumPasswordLength")
            return@post
        }

        when (val registerResult = databaseAPI.register(login, password)) {
            is RegisterUserResult.Ok -> {
                val token = tokensManager.putUserToken(registerResult.user.id)
                call.respond(HttpStatusCode.OK, UserWithToken(registerResult.user, token))
                return@post
            }

            is RegisterUserResult.SameUserAlreadyExist -> {
                call.respond(HttpStatusCode.Conflict, "User with login \"$login\" already exist")
                return@post
            }

            is RegisterUserResult.UnexpectedError -> {
                call.respondAsUnexpectedError(registerResult.t)
                return@post
            }
        }
    }
}