package rockyouapi.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import rockyouapi.responce.BaseResponse


//region Common "Ok" responses
internal suspend fun ApplicationCall.respondAsOkWithUnit() {
    respond(HttpStatusCode.OK, BaseResponse(errorText = null, data = null))
}

internal suspend inline fun <reified T> ApplicationCall.respondAsOkWithData(data: T) {
    respond(HttpStatusCode.OK, BaseResponse(errorText = null, data = data))
}
//endregion


//region Common error responses
internal suspend fun ApplicationCall.respondAsError(errorCode: HttpStatusCode, errorText: String) {
    respond(errorCode, BaseResponse(errorText = errorText, data = null))
}

internal suspend fun ApplicationCall.respondAsErrorByException(t: Throwable) {
    val errorText = t.message ?: "Internal error"
    respondAsError(
        errorCode = HttpStatusCode.InternalServerError,
        errorText = "Unexpected error, please try your request later. Cause: $errorText"
    )
}
//endregion


//region Invalid argument responses
internal suspend fun ApplicationCall.respondAsIncorrectArgumentType(
    argName: String,
    expectedTypeName: ArgumentType
) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "'$argName' argument must be '${expectedTypeName.typeName}' type"
    )
}

internal suspend fun ApplicationCall.respondAsIncorrectTypeWhenIntExpected(argName: String) {
    respondAsIncorrectArgumentType(argName, ArgumentType.INT)
}

internal suspend fun ApplicationCall.respondAsIncorrectTypeWhenLongExpected(argName: String) {
    respondAsIncorrectArgumentType(argName, ArgumentType.LONG)
}

internal suspend fun ApplicationCall.respondAsStringArgMustBeNotEmpty(argName: String) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "'$argName' must be filled"
    )
}

internal suspend fun ApplicationCall.respondAsMustBeNonNegativeArgumentValue(argName: String) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "'$argName' argument must be non negative"
    )
}

internal suspend fun ApplicationCall.respondAsArgumentRequiredError(argName: String) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "Argument '$argName' required"
    )
}

internal suspend fun ApplicationCall.respondAsJSONBroken(argName: String) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "Broken JSON on argument: $argName"
    )
}

internal suspend fun ApplicationCall.respondAsIntArgumentTooBig(argName: String, maximumValue: Int) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "'$argName' value exceeded. Maximum value: $maximumValue"
    )
}

internal suspend fun ApplicationCall.respondAsPasswordTooLong(passwordMaximumLength: Int) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "Password length exceeded, maximum length: $passwordMaximumLength"
    )
}
//endregion


//region Content not found responses
internal suspend fun ApplicationCall.respondAsContentNotFoundByID(contentID: Int) {
    respondAsError(
        errorCode = HttpStatusCode.NotFound,
        errorText = "Content with ID: '$contentID' not exist"
    )
}

internal suspend fun ApplicationCall.respondAsContentNotExistByID(contentID: Int) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "Content with ID: '$contentID' not exist"
    )
}
//endregion


//region User not found responses
internal suspend fun ApplicationCall.respondAsUserNotExistByID(userID: Int) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "User with ID: '$userID' not exist"
    )
}

internal suspend fun ApplicationCall.respondAsUserNotExist(userName: String) {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "User '$userName' not exist"
    )
}
//endregion


//region Token error responses
internal suspend fun ApplicationCall.respondAsTokenNotFoundOrNotSupported() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Token not found or not supported"
    )
}

internal suspend fun ApplicationCall.respondAsTokenInvalid() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Token invalid or has expired"
    )
}

internal suspend fun ApplicationCall.respondAsTokenHasNoOwner() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Invalid token. Reason: userID parameter lost"
    )
}

internal suspend fun ApplicationCall.respondAsTokenBelongToAnotherUser() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Invalid token. Reason: token belong to another user"
    )
}

internal suspend fun ApplicationCall.respondAsTokenHasNoType() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Invalid token. Reason: tokenType parameter lost"
    )
}

internal suspend fun ApplicationCall.respondAsTokenHasInvalidType() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Invalid token. Reason: unknown tokenType"
    )
}

internal suspend fun ApplicationCall.respondAsTokenHasAccessType() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Invalid token. Reason: access token passed, refresh token expected"
    )
}

internal suspend fun ApplicationCall.respondAsTokenHasRefreshType() {
    respondAsError(
        errorCode = HttpStatusCode.Unauthorized,
        errorText = "Invalid token. Reason: refresh token passed, access token expected"
    )
}
//endregion