package rockyouapi.utils

import io.ktor.server.application.*

/** Log error to file. */
internal fun ApplicationCall.logErrorToFile(msg: String) {
    application.log.error(msg)
}

/** Log error to file. */
internal fun ApplicationCall.logErrorToFile(msg: String, t: Throwable) {
    application.log.error(msg, t)
}