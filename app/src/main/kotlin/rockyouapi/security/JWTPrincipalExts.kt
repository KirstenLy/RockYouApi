package rockyouapi.security

import io.ktor.server.auth.jwt.*

/** Try to extract claim from payload.*/
internal fun JWTPrincipal.extractClaimAsInt(claimKey: String) = payload.claims[claimKey]?.asInt()

/** Try to extract token type claim and invoke scenario depend on result.*/
internal inline fun JWTPrincipal.checkTokenType(
    tokenTypeClaimKey: String,
    onClaimNotPresented: () -> Unit = {},
    onAccessType: () -> Unit = {},
    onRefreshType: () -> Unit = {},
    onUnknownType: () -> Unit = {},
) {
    when (extractClaimAsInt(tokenTypeClaimKey)) {
        null -> onClaimNotPresented()
        TokenType.ACCESS.typeID -> onAccessType()
        TokenType.REFRESH.typeID -> onRefreshType()
        else -> onUnknownType()
    }
}

internal inline fun JWTPrincipal.tryToExtractUserID(onUserIDNotPresented: () -> Int): Int {
    return extractClaimAsInt(CLAIM_KEY_TOKEN_USER) ?: onUserIDNotPresented()
}