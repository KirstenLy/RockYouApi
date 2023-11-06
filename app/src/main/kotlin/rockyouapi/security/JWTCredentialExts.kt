package rockyouapi.security

import io.ktor.server.auth.jwt.*

/** Is credential contain claim? */
internal fun JWTCredential.containClaim(claimKey: String) = payload.claims[claimKey] != null
