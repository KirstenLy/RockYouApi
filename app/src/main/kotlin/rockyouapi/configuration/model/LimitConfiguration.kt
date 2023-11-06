package rockyouapi.configuration.model

/**
 * Limits configuration.
 *
 * @param userPasswordMinimumLength - Minimum password length.
 * @param userPasswordMaximumLength - Maximum password length.
 * @param searchByTextMinimumRequestLength - Minimum search text symbols count to launch search by text.
 * @param maximumFilesToUploadAtOnceNumber - Minimum files that user can load at one load session.
 * @param availableFileSizeInMB - Maximum file size that user can download.
 * */
internal class LimitConfiguration(
    val userPasswordMinimumLength: Int,
    val userPasswordMaximumLength: Int,
    val searchByTextMinimumRequestLength: Int,
    val maximumFilesToUploadAtOnceNumber: Int,
    val availableFileSizeInMB: Int
)