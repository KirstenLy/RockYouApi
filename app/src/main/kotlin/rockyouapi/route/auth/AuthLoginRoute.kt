package rockyouapi.route.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import database.external.contract.ProductionDatabaseAPI
import database.external.model.Vote
import database.external.result.CheckUserCredentialsResult
import database.external.result.UpdateRefreshTokenResult
import database.external.result.common.SimpleListResult
import database.external.result.common.SimpleOptionalDataResult
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
 * Route to log in existed user.
 * There is no session support, so login is about to create access token, recreate refresh token and read use ibfo.
 *
 * Requirements: login and password.
 *
 * @param secret - Key to decode/encode JWT keys.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Successful login. Respond as [LoggedOrCreatedUserResponse]. Actualize refresh token into the database.
 * - [HttpStatusCode.BadRequest] If login or password not presented/empty/blank, or password maximum length exceeded.
 * - [HttpStatusCode.Unauthorized] If user not exists or his login mismatched.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.authLoginRoute(
    productionDatabaseAPI: ProductionDatabaseAPI,
    accessTokenLifeTime: Duration,
    refreshTokenLifeTime: Duration,
    passwordMaximumLength: Int,
    secret: String,
) {

    post(Routes.AuthLogin.path) {

        val parameters = call.receiveParameters()

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

        if (password.length > passwordMaximumLength) {
            call.respondAsPasswordTooLong(passwordMaximumLength)
            return@post
        }

        // Check is user password and login matches
        when (val checkUserCredentialsResult = productionDatabaseAPI.checkUserCredentials(login, password)) {

            is CheckUserCredentialsResult.UserNotExist -> {
                call.respondAsUserNotExist(login)
                return@post
            }

            is CheckUserCredentialsResult.PasswordMismatch -> {
                call.respondAsPasswordMismatch(login)
                return@post
            }

            is CheckUserCredentialsResult.Error -> {
                val exception = checkUserCredentialsResult.t
                call.logErrorToFile("Login failed. Stage: check user credentials. Login: $login", exception)
                call.respondAsErrorByException(checkUserCredentialsResult.t)
                return@post
            }

            is CheckUserCredentialsResult.Ok -> Unit
        }

        // Get user by login to create tokens for him
        val user = when (val getUserByLoginRequestResult = productionDatabaseAPI.getUserFullInfoByLogin(login)) {

            is SimpleOptionalDataResult.DataNotFounded -> {
                call.respondAsUserNotExist(login)
                return@post
            }

            is SimpleOptionalDataResult.Error -> {
                val exception = getUserByLoginRequestResult.t
                call.logErrorToFile("Get user failed. Stage: get user after successful login. Login: $login", exception)
                call.respondAsErrorByException(getUserByLoginRequestResult.t)
                return@post
            }

            is SimpleOptionalDataResult.Data -> getUserByLoginRequestResult.model
        }

        // Generate new tokens
        val accessToken = JWT.create()
            .withClaim(CLAIM_KEY_TOKEN_USER, user.id)
            .withClaim(CLAIM_KEY_TOKEN_TYPE, TokenType.ACCESS.typeID)
            .withExpiresAt(Date(System.currentTimeMillis() + accessTokenLifeTime.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(secret))

        val refreshToken = JWT.create()
            .withClaim(CLAIM_KEY_TOKEN_USER, user.id)
            .withClaim(CLAIM_KEY_TOKEN_TYPE, TokenType.REFRESH.typeID)
            .withExpiresAt(Date(System.currentTimeMillis() + refreshTokenLifeTime.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(secret))

        // After user login, replace his refresh token on new one
        when (val updateRefreshTokenRequestResult = productionDatabaseAPI.updateRefreshToken(user.id, refreshToken)) {

            is UpdateRefreshTokenResult.UserNotExists -> {
                call.respondAsUserNotExist(user.name)
                return@post
            }

            is UpdateRefreshTokenResult.Error -> {
                val exception = updateRefreshTokenRequestResult.t
                call.logErrorToFile("Update refresh token failed after user logged. Login: $login", exception)
                call.respondAsErrorByException(updateRefreshTokenRequestResult.t)
                return@post
            }

            is UpdateRefreshTokenResult.Ok -> Unit
        }

        // Ask logged user vote history
        val userVoteHistory = when (val getVoteHistoryResult = productionDatabaseAPI.getUserVoteHistory(user.id)) {

            is SimpleListResult.Error -> {
                call.respondAsErrorByException(getVoteHistoryResult.t)
                return@post
            }

            is SimpleListResult.Data -> getVoteHistoryResult.data
        }

        // Ask logged user favorite
        val userFavorite = when (val getFavoriteResult = productionDatabaseAPI.getUserFavoriteIDList(user.id)) {

            is SimpleListResult.Error -> {
                call.respondAsErrorByException(getFavoriteResult.t)
                return@post
            }

            is SimpleListResult.Data -> getFavoriteResult.data
        }

        try {
            val responseData = LoggedOrCreatedUserResponse(
                user = user.toWeb(),
                accessToken = accessToken,
                refreshToken = refreshToken,
                voteHistory = userVoteHistory.map(Vote::toWeb),
                favorite = userFavorite
            )
            call.respondAsOkWithData(responseData)
        } catch (t: Throwable) {
            call.logErrorToFile("Respond as data failed. Stage: user logged successful. Login: $login", t)
            call.respondAsErrorByException(t)
        }
        return@post
    }
}

private suspend fun ApplicationCall.respondAsPasswordMismatch(userName: String) {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Password mismatch for user '$userName'"
    )
}