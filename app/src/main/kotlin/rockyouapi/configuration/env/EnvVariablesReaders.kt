package rockyouapi.configuration.env

import io.ktor.server.application.*
import rockyouapi.configuration.*

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readDatabaseName() = readDatabaseNameFromProperty()
    ?: throw IllegalStateException("Failed to read dbName environment variable. Non empty String expected.")

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readDatabaseURL() = readDatabaseURLFromProperty()
    ?: throw IllegalStateException("Failed to read dbURL environment variable. Non empty String expected.")

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readDatabaseDriver() = readDatabaseDriverFromProperty()
    ?: throw IllegalStateException("Failed to read dbDriver environment variable. Non empty String expected.")

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readDatabaseUser() = readDatabaseUserFromProperty()
    ?: throw IllegalStateException("Failed to read dbUser environment variable. Non empty String expected.")

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readDatabaseUserPassword() = readDatabaseUserPasswordFromProperty()
    ?: throw IllegalStateException("Failed to read dbPassword environment variable. Non empty String expected.")

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readIsNeedToDropTablesAndFillByMocks(): Boolean {
    val isNeedToDropTablesAndFillByMocksAsString = readIsNeedToDropTablesAndFillByMocksFromProperty() ?: return false

    val isNeedToDropTablesAndFillByMocksAsBoolean = isNeedToDropTablesAndFillByMocksAsString.toBooleanStrictOrNull()
    if (isNeedToDropTablesAndFillByMocksAsBoolean == null) {
        val exMessage = buildString {
            append("Failed to read isNeedToDropTablesAndFillByMocks environment variable. Boolean value expected.")
            appendLine()
            append("Actual isNeedToDropTablesAndFillByMocks value: $isNeedToDropTablesAndFillByMocksAsString")
        }
        throw IllegalStateException(exMessage)
    }

    return isNeedToDropTablesAndFillByMocksAsBoolean
}


@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readAccessTokenLifeTime(): Int {
    val accessTokenLifeTimeAsString = readAccessTokenLifeTimeFromProperty()
    if (accessTokenLifeTimeAsString == null) {
        println("Access token lifecycle not defined! Default value taken: $DEFAULT_ACCESS_TOKEN_LIFE_TIME_IN_SEC")
        return DEFAULT_ACCESS_TOKEN_LIFE_TIME_IN_SEC
    }

    val accessTokenLifeTimeAsInt = accessTokenLifeTimeAsString.toIntOrNull()
    if (accessTokenLifeTimeAsInt == null || accessTokenLifeTimeAsInt <= 0) {
        val exMessage = buildString {
            append("Failed to read accessTokenLifeTime environment variable. Non negative Int expected.")
            appendLine()
            append("Actual accessTokenLifeTime value: $accessTokenLifeTimeAsString")
        }
        throw IllegalStateException(exMessage)
    }
    return accessTokenLifeTimeAsInt
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readRefreshTokenLifeTime(): Int {
    val refreshTokenLifeTimeAsString = readRefreshTokenLifeTimeFromProperty()
    if (refreshTokenLifeTimeAsString == null) {
        println("Refresh token lifecycle not defined! Default value taken: $DEFAULT_REFRESH_TOKEN_LIFE_TIME_IN_SEC")
        return DEFAULT_REFRESH_TOKEN_LIFE_TIME_IN_SEC
    }

    val refreshTokenLifeTimeAsInt = refreshTokenLifeTimeAsString.toIntOrNull()
    if (refreshTokenLifeTimeAsInt == null || refreshTokenLifeTimeAsInt <= 0) {
        val exMessage = buildString {
            append("Failed to read refreshTokenLifeTime environment variable. Non negative Int expected.")
            appendLine()
            append("Actual refreshTokenLifeTime value: $refreshTokenLifeTimeAsString")
        }
        throw IllegalStateException(exMessage)
    }
    return refreshTokenLifeTimeAsInt
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readSecret() = readSecretFromProperty()
    ?: throw IllegalStateException("Failed to read tokenSecret environment variable. Non empty String expected.")

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readUserPasswordMinimumLength(): Int {
    val passwordMinimumLengthAsString = readUserPasswordMinimumLengthFromProperty()
    if (passwordMinimumLengthAsString == null) {
        println("Password minimum length not defined! Default value taken: $DEFAULT_VALUE_USER_PASSWORD_MINIMUM_LENGTH")
        return DEFAULT_VALUE_USER_PASSWORD_MINIMUM_LENGTH
    }

    val passwordMinimumLengthAsInt = passwordMinimumLengthAsString.toIntOrNull()
    if (passwordMinimumLengthAsInt == null || passwordMinimumLengthAsInt <= 0) {
        val exMessage = buildString {
            append("Failed to read userPasswordMinimumLength environment variable. Non negative Int value expected.")
            appendLine()
            append("Actual userPasswordMinimumLength value: $passwordMinimumLengthAsInt")
        }
        throw IllegalStateException(exMessage)
    }
    return passwordMinimumLengthAsInt
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readUserPasswordMaximumLength(): Int {
    val passwordMaximumLengthAsString = readUserPasswordMaximumLengthFromProperty()
    if (passwordMaximumLengthAsString == null) {
        println("Password maximum length not defined! Default value taken: $DEFAULT_VALUE_USER_PASSWORD_MAXIMUM_LENGTH")
        return DEFAULT_VALUE_USER_PASSWORD_MAXIMUM_LENGTH
    }

    val passwordMaximumLengthAsInt = passwordMaximumLengthAsString.toIntOrNull()
    if (passwordMaximumLengthAsInt == null || passwordMaximumLengthAsInt <= 0) {
        val exMessage = buildString {
            append("Failed to read userPasswordMaximumLength environment variable. Non negative Int value expected.")
            appendLine()
            append("Actual userPasswordMaximumLength value: $passwordMaximumLengthAsString")
        }
        throw IllegalStateException(exMessage)
    }
    return passwordMaximumLengthAsInt
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readSearchByTextMinimumRequestLength(): Int {
    val searchByTextMinimumRequestLengthAsString = readSearchByTextMinimumRequestLengthFromProperty()
    if (searchByTextMinimumRequestLengthAsString == null) {
        println("Search text minimum length not defined! Default value taken: $DEFAULT_VALUE_SEARCH_BY_TEXT_MINIMUM_REQUEST_LENGTH")
        return DEFAULT_VALUE_SEARCH_BY_TEXT_MINIMUM_REQUEST_LENGTH
    }

    val searchByTextMinimumRequestLengthAsInt = searchByTextMinimumRequestLengthAsString.toIntOrNull()
    if (searchByTextMinimumRequestLengthAsInt == null || searchByTextMinimumRequestLengthAsInt <= 0) {
        val exMessage = buildString {
            append("Failed to read searchByTextMinimumRequestLength environment variable. Non negative Int value expected.")
            appendLine()
            append("Actual searchByTextMinimumRequestLength value: $searchByTextMinimumRequestLengthAsString")
        }
        throw IllegalStateException(exMessage)
    }
    return searchByTextMinimumRequestLengthAsInt
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readMaximumFilesToUploadAtOnceNumber(): Int {
    val maximumFilesToUploadAtOnceNumberAsString = readMaximumFilesToUploadAtOnceNumberFromProperty()
    if (maximumFilesToUploadAtOnceNumberAsString == null) {
        println("Maximum files number for upload at one time not defined! Default value taken: $DEFAULT_VALUE_UPLOAD_MAXIMUM_AT_ONCE")
        return DEFAULT_VALUE_UPLOAD_MAXIMUM_AT_ONCE
    }

    val maximumFilesToUploadAtOnceNumberAsInt = maximumFilesToUploadAtOnceNumberAsString.toIntOrNull()
    if (maximumFilesToUploadAtOnceNumberAsInt == null || maximumFilesToUploadAtOnceNumberAsInt <= 0) {
        val exMessage = buildString {
            append("Failed to read maximumFilesToUploadAtOnceNumber environment variable. Non negative Int value expected.")
            appendLine()
            append("Actual maximumFilesToUploadAtOnceNumber value: $maximumFilesToUploadAtOnceNumberAsString")
        }
        throw IllegalStateException(exMessage)
    }
    return maximumFilesToUploadAtOnceNumberAsInt
}


@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readAvailableFileSizeInMB(): Int {
    val availableFileSizeInMBFromPropertyAsString = readAvailableFileSizeInMBFromProperty()
    if (availableFileSizeInMBFromPropertyAsString == null) {
        println("Maximum uploadable file size not defined! Default value taken: $DEFAULT_VALUE_UPLOAD_MAXIMUM_FILE_SIZE_MB")
        return DEFAULT_VALUE_UPLOAD_MAXIMUM_FILE_SIZE_MB
    }

    val availableFileSizeInMBFromPropertyAsInt = availableFileSizeInMBFromPropertyAsString.toIntOrNull()
    if (availableFileSizeInMBFromPropertyAsInt == null || availableFileSizeInMBFromPropertyAsInt <= 0) {
        val exMessage = buildString {
            append("Failed to read availableFileSizeInMB environment variable. Non negative Int value expected.")
            appendLine()
            append("Actual availableFileSizeInMB value: $availableFileSizeInMBFromPropertyAsString")
        }
        throw IllegalStateException(exMessage)
    }
    return availableFileSizeInMBFromPropertyAsInt
}

@Throws(IllegalStateException::class)
internal fun ApplicationEnvironment.readUploadContentFolder() = readUploadContentFolderFromProperty()
    ?: throw IllegalStateException("Failed to read uploadContentFolder environment variable. Non empty String expected.")

/// Read from property section
private fun ApplicationEnvironment.readDatabaseNameFromProperty() = config
    .propertyOrNull("ktor.deployment.dbName")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readDatabaseURLFromProperty() = config
    .propertyOrNull("ktor.deployment.dbURL")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readDatabaseDriverFromProperty() = config
    .propertyOrNull("ktor.deployment.dbDriver")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readDatabaseUserFromProperty() = config
    .propertyOrNull("ktor.deployment.dbUser")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readDatabaseUserPasswordFromProperty() = config
    .propertyOrNull("ktor.deployment.dbPassword")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readIsNeedToDropTablesAndFillByMocksFromProperty() = config
    .propertyOrNull("ktor.deployment.isNeedToDropTablesAndFillByMocks")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readAccessTokenLifeTimeFromProperty() = config
    .propertyOrNull("ktor.deployment.accessTokenLifeTime")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readRefreshTokenLifeTimeFromProperty() = config
    .propertyOrNull("ktor.deployment.refreshTokenLifeTime")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readSecretFromProperty() = config
    .propertyOrNull("ktor.deployment.tokenSecret")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readUserPasswordMinimumLengthFromProperty() = config
    .propertyOrNull("ktor.deployment.userPasswordMinimumLength")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readUserPasswordMaximumLengthFromProperty() = config
    .propertyOrNull("ktor.deployment.userPasswordMaximumLength")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readSearchByTextMinimumRequestLengthFromProperty() = config
    .propertyOrNull("ktor.deployment.searchByTextMinimumRequestLength")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readMaximumFilesToUploadAtOnceNumberFromProperty() = config
    .propertyOrNull("ktor.deployment.maximumFilesToUploadAtOnceNumber")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readAvailableFileSizeInMBFromProperty() = config
    .propertyOrNull("ktor.deployment.availableFileSizeInMB")
    ?.getString()
    ?.takeIf(String::isNotBlank)

private fun ApplicationEnvironment.readUploadContentFolderFromProperty() = config
    .propertyOrNull("ktor.deployment.uploadContentFolder")
    ?.getString()
    ?.takeIf(String::isNotBlank)