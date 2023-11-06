package rockyouapi.route.video

import common.utils.zeroOnNull
import database.external.contract.ProductionDatabaseAPI
import database.external.filter.VideoListFilter
import database.external.result.common.SimpleListResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import rockyouapi.configuration.COMMENT_MAXIMUM_LENGTH
import rockyouapi.configuration.LIMIT_ARGUMENT_MAXIMUM_VALUE
import rockyouapi.model.Video
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*
import database.external.model.Video as DBVideo

/**
 * Route to get video list.
 *
 * Requirements: limit.
 * Additional: offset, environmentID, text, langIDs, authorIDs, tagIDs, userIDs.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with list of [Video].
 * - [HttpStatusCode.BadRequest] If limit not presented or invalid or additional arguments invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.videoListRoute(productionDatabaseAPI: ProductionDatabaseAPI, minimalSearchTextLength: Int) {

    get(Routes.VideoList.path) {

        val limit = call.parameters.readNotNullableNotNegativeLong(
            argName = Routes.VideoList.getLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.VideoList.getLimitArgName())
                return@get
            },
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.VideoList.getLimitArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.VideoList.getLimitArgName())
                return@get
            }
        )

        if (limit > LIMIT_ARGUMENT_MAXIMUM_VALUE) {
            call.respondAsIntArgumentTooBig(Routes.VideoList.getLimitArgName(), COMMENT_MAXIMUM_LENGTH)
            return@get
        }

        val offset = call.parameters.readNullableNotNegativeLong(
            argName = Routes.VideoList.getOffsetArgName(),
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.VideoList.getOffsetArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.VideoList.getOffsetArgName())
                return@get
            }
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.VideoList.getEnvironmentLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.VideoList.getEnvironmentLangIDArgName())
                return@get
            }
        )

        val searchText = call.parameters.readNullableFilledString(
            argName = Routes.VideoList.getSearchTextArgName(),
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.VideoList.getSearchTextArgName())
                return@get
            }
        )
            ?.takeIf { it.length >= minimalSearchTextLength }

        val languageIDList = try {
            val languageListAsString = call.parameters.readNullableFilledString(
                argName = Routes.VideoList.getLangIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.VideoList.getLangIDListArgName())
                    return@get
                }
            )

            languageListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.VideoList.getLangIDListArgName())
            return@get
        }

        val authorIDList = try {
            val authorIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.VideoList.getAuthorIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.VideoList.getAuthorIDListArgName())
                    return@get
                }
            )

            authorIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.VideoList.getAuthorIDListArgName())
            return@get
        }

        val userIDList = try {
            val userIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.VideoList.getUserIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.VideoList.getUserIDListArgName())
                    return@get
                }
            )

            userIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.VideoList.getUserIDListArgName())
            return@get
        }

        val tagIDList = try {
            val tagIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.VideoList.getTagIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.VideoList.getTagIDListArgName())
                    return@get
                }
            )

            tagIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.VideoList.getTagIDListArgName())
            return@get
        }

        val requestFilter = VideoListFilter(
            limit = limit,
            offset = offset.zeroOnNull(),
            environmentLangID = environmentLangID,
            searchText = searchText,
            languageIDList = languageIDList.orEmpty(),
            authorIDList = authorIDList.orEmpty(),
            includedTagIDList = emptyList(),
            excludedTagIDList = emptyList(),
            userIDList = userIDList.orEmpty()
        )

        when (val getVideoListRequestResult = productionDatabaseAPI.getVideos(requestFilter)) {

            is SimpleListResult.Error -> {
                call.logErrorToFile("Failed to get video list. Filter: $requestFilter", getVideoListRequestResult.t)
                call.respondAsErrorByException(getVideoListRequestResult.t)
                return@get
            }

            is SimpleListResult.Data -> {
                try {
                    val response = getVideoListRequestResult.data.map(DBVideo::toWeb)
                    call.respondAsOkWithData(response)
                } catch (t: Throwable) {
                    val errorText = buildString {
                        append("Get video list return ok, but something happen after.")
                        appendLine()
                        append("Filter: $requestFilter")
                    }
                    call.logErrorToFile(errorText, t)
                    call.respondAsErrorByException(t)
                }
                return@get
            }
        }
    }
}