package rockyouapi.utils

import io.ktor.http.*

/** Append key-value to parameters, used in GET calls.*/
internal fun URLBuilder.appendToParameters(key: String?, name: String) {
    key?.let { parameters.append(name, it) }
}