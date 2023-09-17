package rockyouapi.auth

import io.ktor.server.auth.*

/** Model of [UserTokenInfo] owner, passed to routes. */
internal data class UserIDPrincipal(val userID: Int) : Principal