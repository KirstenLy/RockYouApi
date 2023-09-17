package rockyouapi.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal suspend fun ApplicationCall.respondAsUnexpectedError(t: Throwable) {
    val errorText = t.message ?: "Internal error"
    respond(HttpStatusCode.InternalServerError, "Unexpected error, please try your request later. Cause: $errorText")
}

internal suspend fun ApplicationCall.respondAsIncorrectArgumentType(
    argName: String,
    expectedTypeName: ArgumentType
) {
    respond(HttpStatusCode.BadRequest, "\"$argName\" argument must be \"${expectedTypeName.typeName}\" type")
}

internal suspend fun ApplicationCall.respondAsIncorrectTypeWhenIntExpected(argName: String) {
    respondAsIncorrectArgumentType(argName, ArgumentType.INT)
}

internal suspend fun ApplicationCall.respondAsIncorrectTypeWhenLongExpected(argName: String) {
    respondAsIncorrectArgumentType(argName, ArgumentType.LONG)
}

internal suspend fun ApplicationCall.respondAsStringArgMustBeNotEmpty(argName: String) {
    respond(HttpStatusCode.NotFound, "\"$argName\" must be filled")
}

internal suspend fun ApplicationCall.respondAsMustBeNonNegativeArgumentValue(argName: String) {
    respond(HttpStatusCode.BadRequest, "\"$argName\" argument must be non negative")
}

internal suspend fun ApplicationCall.respondAsArgumentRequiredError(argName: String) {
    respond(HttpStatusCode.BadRequest, "Argument \"$argName\" required")
}

internal suspend fun ApplicationCall.respondAsContentNotExistByID(contentID: Int) {
    respond(HttpStatusCode.NotFound, "Content with ID: \"$contentID\" not exist")
}

internal suspend fun ApplicationCall.respondAsUserUnexpectedMissed() {
    respond(HttpStatusCode.Unauthorized, "Unexpected authorization error: UserID missed!")
}