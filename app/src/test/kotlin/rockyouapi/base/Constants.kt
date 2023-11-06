package rockyouapi.base

import common.storage.StaticMapStorage
import rockyouapi.configuration.model.TokenConfiguration
import kotlin.time.Duration.Companion.seconds

/** Default value of [TokenConfiguration.accessTokenLifeTime]. */
internal val DEFAULT_ACCESS_TOKEN_LIFETIME = 3.seconds

/** Default value of [TokenConfiguration.refreshTokenLifeTime]. */
internal val DEFAULT_REFRESH_TOKEN_LIFETIME = 6.seconds

/** Default value of [TokenConfiguration.tokenSecret]. */
internal const val DEFAULT_SECRET_WORD = "DEFAULT_SECRET_WORD"

/** Key of test database in [StaticMapStorage]. */
internal const val KEY_STATIC_MAP_DB = "KEY_STATIC_MAP_DB"