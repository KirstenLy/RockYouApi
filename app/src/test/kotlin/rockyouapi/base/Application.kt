package rockyouapi.base

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import database.external.contract.ProductionDatabaseAPI
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.mockito.Mockito
import rockyouapi.configuration.DEFAULT_VALUE_USER_PASSWORD_MINIMUM_LENGTH
import rockyouapi.route.Routes.Companion.JWT_AUTHORIZATION_PROVIDER_KEY
import rockyouapi.route.auth.authLoginRoute
import rockyouapi.route.auth.authRefreshRoute
import rockyouapi.route.auth.authRegisterRoute
import rockyouapi.route.comment.commentAddRoute
import rockyouapi.route.comment.commentListRoute
import rockyouapi.route.content.readContentByIDRoute
import rockyouapi.route.favorite.addOrRemoveFavoriteRoute
import rockyouapi.route.picture.pictureListRoute
import rockyouapi.route.picture.pictureReadByIDRoute
import rockyouapi.route.report.reportRoute
import rockyouapi.route.story.storyListRoute
import rockyouapi.route.story.storyReadByIDRoute
import rockyouapi.route.story.storyReadChapterTextByIDRoute
import rockyouapi.route.video.videoListRoute
import rockyouapi.route.video.videoReadByIDRoute
import rockyouapi.route.vote.voteRoute
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.CLAIM_KEY_TOKEN_USER
import rockyouapi.security.containClaim
import rockyouapi.utils.respondAsTokenInvalid
import kotlin.time.Duration

/** Configure test application with default configuration. */
internal fun runTestSimple( action: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        configureEnv()
        configurePlugins(DEFAULT_SECRET_WORD)
        configureRouting(
            productionDatabaseAPI = Mockito.mock(ProductionDatabaseAPI::class.java),
            accessTokenLifeTime = DEFAULT_ACCESS_TOKEN_LIFETIME,
            refreshTokenLifeTime = DEFAULT_REFRESH_TOKEN_LIFETIME,
            minimumPasswordLength = DEFAULT_VALUE_USER_PASSWORD_MINIMUM_LENGTH,
            secret = DEFAULT_SECRET_WORD
        )

        action()
    }
}

/** Configure test application. */
internal fun runTest(
    productionDatabaseAPI: ProductionDatabaseAPI,
    accessTokenLifeTime: Duration = DEFAULT_ACCESS_TOKEN_LIFETIME,
    refreshTokenLifeTime: Duration = DEFAULT_REFRESH_TOKEN_LIFETIME,
    minimumPasswordLength: Int = DEFAULT_VALUE_USER_PASSWORD_MINIMUM_LENGTH,
    secret: String = DEFAULT_SECRET_WORD,
    action: suspend ApplicationTestBuilder.() -> Unit
) {
    testApplication {
        configureEnv()
        configurePlugins(secret)
        configureRouting(
            productionDatabaseAPI = productionDatabaseAPI,
            accessTokenLifeTime = accessTokenLifeTime,
            refreshTokenLifeTime = refreshTokenLifeTime,
            minimumPasswordLength = minimumPasswordLength,
            secret = secret
        )

        action()
    }
}

/** Configure environment, required even if empty. */
private fun ApplicationTestBuilder.configureEnv() {
    environment {
        config = MapApplicationConfig()
    }
}

private fun ApplicationTestBuilder.configurePlugins(secret: String) {
    install(ContentNegotiation) { json() }
    install(IgnoreTrailingSlash)
    install(Resources)

    install(Authentication) {
        jwt(JWT_AUTHORIZATION_PROVIDER_KEY) {

            JWT.require(Algorithm.HMAC256(secret))
                .build()
                .also(::verifier)

            validate { credential ->
                val isUserClaimPresented = credential.containClaim(CLAIM_KEY_TOKEN_USER)
                if (!isUserClaimPresented) return@validate null

                val isTokenTypeClaimPresented = credential.containClaim(CLAIM_KEY_TOKEN_TYPE)
                if (!isTokenTypeClaimPresented) return@validate null

                return@validate JWTPrincipal(credential.payload)
            }

            challenge { _, _ -> call.respondAsTokenInvalid() }
        }
    }
}

private fun ApplicationTestBuilder.configureRouting(
    productionDatabaseAPI: ProductionDatabaseAPI,
    accessTokenLifeTime: Duration,
    refreshTokenLifeTime: Duration,
    minimumPasswordLength: Int,
    secret: String
) {
    routing {

        authRegisterRoute(
            productionDatabaseAPI = productionDatabaseAPI,
            accessTokenLifeTime = accessTokenLifeTime,
            refreshTokenLifeTime = refreshTokenLifeTime,
            secret = secret,
            minimumPasswordLength = minimumPasswordLength,
            maximumPasswordLength = 3
        )

        authLoginRoute(
            productionDatabaseAPI = productionDatabaseAPI,
            accessTokenLifeTime = accessTokenLifeTime,
            refreshTokenLifeTime = refreshTokenLifeTime,
            secret = secret,
            passwordMaximumLength = 3
        )

        authRefreshRoute(
            productionDatabaseAPI = productionDatabaseAPI,
            accessTokenLifeTime = accessTokenLifeTime,
            secret = secret
        )

        readContentByIDRoute(productionDatabaseAPI)

        pictureListRoute(productionDatabaseAPI, 0)
        pictureReadByIDRoute(productionDatabaseAPI)

        videoListRoute(productionDatabaseAPI, 0)
        videoReadByIDRoute(productionDatabaseAPI)

        storyListRoute(productionDatabaseAPI, 0)
        storyReadByIDRoute(productionDatabaseAPI)
        storyReadChapterTextByIDRoute(productionDatabaseAPI)

        addOrRemoveFavoriteRoute(productionDatabaseAPI)

        reportRoute(productionDatabaseAPI)

        voteRoute(productionDatabaseAPI)

        commentAddRoute(productionDatabaseAPI)
        commentListRoute(productionDatabaseAPI)
    }
}