package rockyouapi.utils

import common.utils.removeWhitespaces

/** Try to extract bearer token from string. */
internal fun String.extractBearerToken() = removePrefix("Bearer")
    .removeWhitespaces()
    .takeIf(String::isNotBlank)