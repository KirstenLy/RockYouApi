package rockyouapi.configuration

import rockyouapi.configuration.model.LimitConfiguration
import rockyouapi.configuration.model.TokenConfiguration

/** Default value of [TokenConfiguration.accessTokenLifeTime]*/
internal const val DEFAULT_ACCESS_TOKEN_LIFE_TIME_IN_SEC = 900

/** Default value of [TokenConfiguration.refreshTokenLifeTime]*/
internal const val DEFAULT_REFRESH_TOKEN_LIFE_TIME_IN_SEC = 7200

/** Default value of [LimitConfiguration.userPasswordMinimumLength]*/
internal const val DEFAULT_VALUE_USER_PASSWORD_MINIMUM_LENGTH = 3

/** Default value of [LimitConfiguration.userPasswordMaximumLength]*/
internal const val DEFAULT_VALUE_USER_PASSWORD_MAXIMUM_LENGTH = 32

/** Default value of [LimitConfiguration.searchByTextMinimumRequestLength]*/
internal const val DEFAULT_VALUE_SEARCH_BY_TEXT_MINIMUM_REQUEST_LENGTH = 3

/** Default value of [LimitConfiguration.maximumFilesToUploadAtOnceNumber]*/
internal const val DEFAULT_VALUE_UPLOAD_MAXIMUM_AT_ONCE = 10

/** Default value of [LimitConfiguration.maximumFilesToUploadAtOnceNumber]*/
internal const val DEFAULT_VALUE_UPLOAD_MAXIMUM_FILE_SIZE_MB = 2048

/** Default value of comment length. It's not configurable. */
internal const val COMMENT_MAXIMUM_LENGTH = 65536

/** Default value of report length. It's not configurable. */
internal const val REPORT_MAXIMUM_LENGTH = 65536

/** Default value of actualize content request length. It's not configurable. */
internal const val ACTUALIZE_CONTENT_REQUEST_MAXIMUM_LENGTH = 65536

/** Default value of 'limit' argument in list methods. It's not configurable. */
internal const val LIMIT_ARGUMENT_MAXIMUM_VALUE = 100