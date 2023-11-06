package rockyouapi

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import database.external.DatabaseFeature.connectToProductionDatabase
import database.external.contract.ProductionDatabaseAPI
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import rockyouapi.configuration.env.readDatabaseConfiguration
import rockyouapi.configuration.env.readLimitConfiguration
import rockyouapi.configuration.env.readPathConfiguration
import rockyouapi.configuration.env.readTokenConfiguration
import rockyouapi.route.Routes.Companion.JWT_AUTHORIZATION_PROVIDER_KEY
import rockyouapi.route.auth.authLoginRoute
import rockyouapi.route.auth.authLogoutRoute
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
import rockyouapi.route.upload.uploadContentRoute
import rockyouapi.route.user.readUserFullInfo
import rockyouapi.route.video.videoListRoute
import rockyouapi.route.video.videoReadByIDRoute
import rockyouapi.route.vote.voteRoute
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.CLAIM_KEY_TOKEN_USER
import rockyouapi.security.containClaim
import rockyouapi.utils.respondAsTokenInvalid
import kotlin.time.Duration

/** Application entry point. */
fun main(args: Array<String>) {
    EngineMain.main(args)
}

internal fun Application.module() {
    launch(coroutineContext) {

        val databaseConfiguration = environment.readDatabaseConfiguration()
        val tokenConfiguration = environment.readTokenConfiguration()
        val limitConfiguration = environment.readLimitConfiguration()
        val pathConfiguration = environment.readPathConfiguration()

        configurePlugins(tokenConfiguration.tokenSecret)

        configureRouting(
            productionDatabaseAPI = connectToProductionDatabase(databaseConfiguration),
            accessTokenLifeTime = tokenConfiguration.accessTokenLifeTime,
            refreshTokenLifeTime = tokenConfiguration.refreshTokenLifeTime,
            minimumPasswordLength = limitConfiguration.userPasswordMinimumLength,
            maximumPasswordLength = limitConfiguration.userPasswordMaximumLength,
            minimumSearchTextLength = limitConfiguration.searchByTextMinimumRequestLength,
            maximumFilesToUploadAtOnceNumber = limitConfiguration.maximumFilesToUploadAtOnceNumber,
            uploadFolderPath = pathConfiguration.uploadContentFolder,
            availableFileSizeInMB = limitConfiguration.availableFileSizeInMB,
            secret = tokenConfiguration.tokenSecret
        )
    }
}

private fun Application.configurePlugins(secret: String) {
    install(ContentNegotiation) { json() }
    install(AutoHeadResponse)
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

private fun Application.configureRouting(
    productionDatabaseAPI: ProductionDatabaseAPI,
    accessTokenLifeTime: Duration,
    refreshTokenLifeTime: Duration,
    minimumPasswordLength: Int,
    maximumPasswordLength: Int,
    maximumFilesToUploadAtOnceNumber: Int,
    minimumSearchTextLength: Int,
    uploadFolderPath: String,
    availableFileSizeInMB: Int,
    secret: String
) {
    routing {

        authRegisterRoute(
            productionDatabaseAPI = productionDatabaseAPI,
            accessTokenLifeTime = accessTokenLifeTime,
            refreshTokenLifeTime = refreshTokenLifeTime,
            secret = secret,
            minimumPasswordLength = minimumPasswordLength,
            maximumPasswordLength = maximumPasswordLength
        )

        authLoginRoute(
            productionDatabaseAPI = productionDatabaseAPI,
            accessTokenLifeTime = accessTokenLifeTime,
            refreshTokenLifeTime = refreshTokenLifeTime,
            passwordMaximumLength = maximumPasswordLength,
            secret = secret
        )

        authRefreshRoute(
            productionDatabaseAPI = productionDatabaseAPI,
            accessTokenLifeTime = accessTokenLifeTime,
            secret = secret
        )

        authLogoutRoute(
            productionDatabaseAPI = productionDatabaseAPI
        )

        readUserFullInfo(productionDatabaseAPI)

        readContentByIDRoute(productionDatabaseAPI)

        pictureListRoute(productionDatabaseAPI, minimumSearchTextLength)
        pictureReadByIDRoute(productionDatabaseAPI)

        videoListRoute(productionDatabaseAPI, minimumSearchTextLength)
        videoReadByIDRoute(productionDatabaseAPI)

        storyListRoute(productionDatabaseAPI, minimumSearchTextLength)
        storyReadByIDRoute(productionDatabaseAPI)
        storyReadChapterTextByIDRoute(productionDatabaseAPI)

        voteRoute(productionDatabaseAPI)

        commentListRoute(productionDatabaseAPI)
        commentAddRoute(productionDatabaseAPI)

        addOrRemoveFavoriteRoute(productionDatabaseAPI)

        reportRoute(productionDatabaseAPI)

        uploadContentRoute(
            productionDatabaseAPI = productionDatabaseAPI,
            maximumFilesToUploadAtOnceNumber = maximumFilesToUploadAtOnceNumber,
            availableFileSizeInMB = availableFileSizeInMB,
            uploadFolderPath = uploadFolderPath
        )
    }
}