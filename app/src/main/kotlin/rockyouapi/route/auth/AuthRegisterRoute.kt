package rockyouapi.route.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import database.external.contract.ProductionDatabaseAPI
import database.external.result.RegisterUserResult
import database.external.result.UpdateRefreshTokenResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.route.Routes
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.CLAIM_KEY_TOKEN_USER
import rockyouapi.security.TokenType
import rockyouapi.toWeb
import rockyouapi.utils.*
import java.util.*
import kotlin.time.Duration

/**
 * Route to register new user. Generate access and refresh tokens after creation.
 *
 * Requirements: login and password. No contact data (as email/phone) is required.
 *
 * @param secret - Key to decode/encode JWT keys.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Successful registration. Respond as [LoggedOrCreatedUserResponse]. Put created refresh token to the database.
 * - [HttpStatusCode.BadRequest] If login or password not presented or password minimum length not match, or password maximum length exceeded.
 * - [HttpStatusCode.Conflict] If login already in uses.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.authRegisterRoute(
    productionDatabaseAPI: ProductionDatabaseAPI,
    accessTokenLifeTime: Duration,
    refreshTokenLifeTime: Duration,
    secret: String,
    minimumPasswordLength: Int,
    maximumPasswordLength: Int,
) {

    post(Routes.AuthRegister.path) {

        val parameters = call.receiveParameters()

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
                call.respondAsArgumentRequiredError(Routes.AuthRegister.getPasswordArgName())
                return@post
            },
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.AuthRegister.getPasswordArgName())
                return@post
            }
        )

        if (password.length <= minimumPasswordLength) {
            call.respondAsPasswordMinimumLengthMismatch(minimumPasswordLength)
            return@post
        }

        if (password.length > maximumPasswordLength) {
            call.respondAsPasswordTooLong(maximumPasswordLength)
            return@post
        }

        // Try to register user
        val registeredUser = when (val registerResult = productionDatabaseAPI.register(login, password)) {

            is RegisterUserResult.SameUserAlreadyExist -> {
                call.respondAsUserWithSameLoginAlreadyExist(login)
                return@post
            }

            is RegisterUserResult.Error -> {
                val errorText = buildString {
                    append("Failed to register user. Stage: register")
                    appendLine()
                    append("Login: $login")
                    appendLine()
                    append("Password: $password")
                }
                call.logErrorToFile(errorText, registerResult.t)
                call.respondAsErrorByException(registerResult.t)
                return@post
            }

            is RegisterUserResult.UnknownUserRole -> {
                val errorText = buildString {
                    append("Failed to register user. Stage: register")
                    appendLine()
                    append("Unknown user role, registration failed.")
                }
                call.logErrorToFile(errorText)
                call.respondAsError(HttpStatusCode.InternalServerError, "Unknown error, please try again later")
                return@post
            }

            is RegisterUserResult.Ok -> registerResult.userSimple
        }

        val accessToken = JWT.create()
            .withClaim(CLAIM_KEY_TOKEN_USER, registeredUser.id)
            .withClaim(CLAIM_KEY_TOKEN_TYPE, TokenType.ACCESS.typeID)
            .withExpiresAt(Date(System.currentTimeMillis() + accessTokenLifeTime.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(secret))

        val refreshToken = JWT.create()
            .withClaim(CLAIM_KEY_TOKEN_USER, registeredUser.id)
            .withClaim(CLAIM_KEY_TOKEN_TYPE, TokenType.REFRESH.typeID)
            .withExpiresAt(Date(System.currentTimeMillis() + refreshTokenLifeTime.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(secret))

        // Put created user's refresh token to database
        val updateRefreshTokenRequestResult = productionDatabaseAPI.updateRefreshToken(registeredUser.id, refreshToken)

        when (updateRefreshTokenRequestResult) {

            is UpdateRefreshTokenResult.UserNotExists -> {
                call.respondAsUserNotExist(login)
                return@post
            }

            is UpdateRefreshTokenResult.Error -> {
                val errorText = buildString {
                    append("Failed to register user. State: put created refresh token")
                    appendLine()
                    append("Login: $login")
                }
                call.logErrorToFile(errorText, updateRefreshTokenRequestResult.t)
                call.respondAsErrorByException(updateRefreshTokenRequestResult.t)
                return@post
            }

            is UpdateRefreshTokenResult.Ok -> Unit
        }

        try {
            val responseData = LoggedOrCreatedUserResponse(
                user = registeredUser.toWeb(),
                accessToken = accessToken,
                refreshToken = refreshToken,
                voteHistory = emptyList(),
                favorite = emptyList()
            )
            call.respondAsOkWithData(responseData)
        } catch (t: Throwable) {
            call.logErrorToFile("Respond as data failed. Stage: user logged successful. Login: $login", t)
            call.respondAsErrorByException(t)
        }

        return@post
    }
}

private suspend fun ApplicationCall.respondAsPasswordMinimumLengthMismatch(minimumPasswordLength: Int) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "Password minimum length: $minimumPasswordLength"
    )
}

private suspend fun ApplicationCall.respondAsUserWithSameLoginAlreadyExist(login: String) {
    respondAsError(
        errorCode = HttpStatusCode.Conflict,
        errorText = "User with login '$login' already exist"
    )
}