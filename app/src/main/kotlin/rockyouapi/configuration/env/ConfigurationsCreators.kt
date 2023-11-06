package rockyouapi.configuration.env

import database.external.DatabaseConfiguration
import io.ktor.server.application.*
import rockyouapi.*
import rockyouapi.configuration.*
import rockyouapi.configuration.model.LimitConfiguration
import rockyouapi.configuration.model.PathConfiguration
import rockyouapi.configuration.model.TokenConfiguration
import kotlin.time.Duration.Companion.seconds

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readDatabaseConfiguration() = DatabaseConfiguration(
    name = readDatabaseName(),
    url = readDatabaseURL(),
    driver = readDatabaseDriver(),
    user = readDatabaseUser(),
    password = readDatabaseUserPassword(),
    isNeedToDropTablesAndFillByMocks = readIsNeedToDropTablesAndFillByMocks()
)

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readTokenConfiguration(): TokenConfiguration {
    return TokenConfiguration(
        accessTokenLifeTime = readAccessTokenLifeTime().seconds,
        refreshTokenLifeTime = readRefreshTokenLifeTime().seconds,
        tokenSecret = readSecret()
    )
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readLimitConfiguration(): LimitConfiguration {
    return LimitConfiguration(
        userPasswordMinimumLength = readUserPasswordMinimumLength(),
        userPasswordMaximumLength = readUserPasswordMaximumLength(),
        searchByTextMinimumRequestLength = readSearchByTextMinimumRequestLength(),
        maximumFilesToUploadAtOnceNumber = readMaximumFilesToUploadAtOnceNumber(),
        availableFileSizeInMB = readAvailableFileSizeInMB()
    )
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readPathConfiguration(): PathConfiguration {
    return PathConfiguration(readUploadContentFolder())
}