package rockyouapi.route.picture

import common.utils.zeroOnNull
import database.external.contract.ProductionDatabaseAPI
import database.external.filter.PictureListFilter
import database.external.result.common.SimpleListResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import rockyouapi.configuration.COMMENT_MAXIMUM_LENGTH
import rockyouapi.configuration.LIMIT_ARGUMENT_MAXIMUM_VALUE
import rockyouapi.model.Picture
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*
import database.external.model.Picture as DBPicture

/**
 * Route to get picture list.
 *
 * Requirements: limit.
 * Additional: offset, environmentID, text, langIDs, authorIDs, tagIDs, userIDs.
 *
 * @param minimalSearchTextLength - minimal size of "text" argument, make it null of size not reached.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with list of [Picture].
 * - [HttpStatusCode.BadRequest] If limit not presented or invalid or additional arguments invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.pictureListRoute(
    productionDatabaseAPI: ProductionDatabaseAPI,
    minimalSearchTextLength: Int,
) {

    get(Routes.PictureList.path) {

        val limit = call.parameters.readNotNullableNotNegativeLong(
            argName = Routes.PictureList.getLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.PictureList.getLimitArgName())
                return@get
            },
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.PictureList.getLimitArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.PictureList.getLimitArgName())
                return@get
            }
        )

        if (limit > LIMIT_ARGUMENT_MAXIMUM_VALUE) {
            call.respondAsIntArgumentTooBig(Routes.PictureList.getLimitArgName(), COMMENT_MAXIMUM_LENGTH)
            return@get
        }

        val offset = call.parameters.readNullableNotNegativeLong(
            argName = Routes.PictureList.getOffsetArgName(),
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.PictureList.getOffsetArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.PictureList.getOffsetArgName())
                return@get
            }
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.PictureList.getEnvironmentLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.PictureList.getEnvironmentLangIDArgName())
                return@get
            }
        )

        val searchText = call.parameters.readNullableFilledString(
            argName = Routes.PictureList.getSearchTextArgName(),
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.PictureList.getSearchTextArgName())
                return@get
            }
        )
            ?.takeIf { it.length >= minimalSearchTextLength }

        val languageIDList = try {
            val languageListAsString = call.parameters.readNullableFilledString(
                argName = Routes.PictureList.getLangIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.PictureList.getLangIDListArgName())
                    return@get
                }
            )

            languageListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.PictureList.getLangIDListArgName())
            return@get
        }

        val authorIDList = try {
            val authorIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.PictureList.getAuthorIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.PictureList.getAuthorIDListArgName())
                    return@get
                }
            )

            authorIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.PictureList.getAuthorIDListArgName())
            return@get
        }

        val userIDList = try {
            val userIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.PictureList.getUserIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.PictureList.getUserIDListArgName())
                    return@get
                }
            )

            userIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.PictureList.getUserIDListArgName())
            return@get
        }

        val tagIDList = try {
            val tagIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.PictureList.getTagIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.PictureList.getTagIDListArgName())
                    return@get
                }
            )

            tagIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.PictureList.getTagIDListArgName())
            return@get
        }

        val requestFilter = PictureListFilter(
            limit = limit,
            offset = offset.zeroOnNull(),
            environmentLangID = environmentLangID,
            searchText = searchText,
            languageIDList = languageIDList.orEmpty(),
            authorIDList = authorIDList.orEmpty(),
            includedTagIDList = tagIDList.orEmpty(),
            userIDList = userIDList.orEmpty()
        )

        when (val getPictureListRequestResult = productionDatabaseAPI.getPictures(requestFilter)) {

            is SimpleListResult.Error -> {
                val exception = getPictureListRequestResult.t
                call.logErrorToFile("Failed to get picture list. Filter: $requestFilter", exception)
                call.respondAsErrorByException(getPictureListRequestResult.t)
                return@get
            }

            is SimpleListResult.Data -> {
                try {
                    val response = getPictureListRequestResult.data.map(DBPicture::toWeb)
                    call.respondAsOkWithData(response)
                } catch (t: Throwable) {
                    val errorText = buildString {
                        append("Get picture list return ok, but something happen after.")
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