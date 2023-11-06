package rockyouapi.configuration.model

import kotlin.time.Duration

/**
 * Token configuration.
 *
 * @param accessTokenLifeTime - Lifetime of access token.
 * @param refreshTokenLifeTime - Lifetime of refresh token.
 * @param tokenSecret - Secret word to crypt JWT signature.
 * */
internal class TokenConfiguration(
    val accessTokenLifeTime: Duration,
    val refreshTokenLifeTime: Duration,
    val tokenSecret: String
)