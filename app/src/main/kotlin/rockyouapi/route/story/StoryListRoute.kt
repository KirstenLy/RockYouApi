package rockyouapi.route.story

import common.utils.zeroOnNull
import database.external.contract.ProductionDatabaseAPI
import database.external.filter.StoryListFilter
import database.external.model.story.Story as DBStory
import database.external.result.common.SimpleListResult
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import rockyouapi.configuration.COMMENT_MAXIMUM_LENGTH
import rockyouapi.configuration.LIMIT_ARGUMENT_MAXIMUM_VALUE
import rockyouapi.model.story.Story
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*

/**
 * Route to get story list.
 *
 * Requirements: limit.
 * Additional: offset, environmentID, text, langIDs, authorIDs, tagIDs, userIDs.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond with list of [Story].
 * - [HttpStatusCode.BadRequest] If limit not presented or invalid or additional arguments invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.storyListRoute(
    productionDatabaseAPI: ProductionDatabaseAPI,
    minimalSearchTextLength: Int
) {

    get(Routes.StoriesList.path) {

        val limit = call.parameters.readNotNullableNotNegativeLong(
            argName = Routes.StoriesList.getLimitArgName(),
            onArgumentNullError = {
                call.respondAsArgumentRequiredError(Routes.StoriesList.getLimitArgName())
                return@get
            },
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.StoriesList.getLimitArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.StoriesList.getLimitArgName())
                return@get
            }
        )

        if (limit > LIMIT_ARGUMENT_MAXIMUM_VALUE) {
            call.respondAsIntArgumentTooBig(Routes.StoriesList.getLimitArgName(), COMMENT_MAXIMUM_LENGTH)
            return@get
        }

        val offset = call.parameters.readNullableNotNegativeLong(
            argName = Routes.StoriesList.getOffsetArgName(),
            onArgumentNotLongError = {
                call.respondAsIncorrectTypeWhenLongExpected(Routes.StoriesList.getOffsetArgName())
                return@get
            },
            onArgumentNegativeLongError = {
                call.respondAsMustBeNonNegativeArgumentValue(Routes.StoriesList.getOffsetArgName())
                return@get
            }
        )

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.StoriesList.getEnvironmentLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.StoriesList.getEnvironmentLangIDArgName())
                return@get
            }
        )

        val searchText = call.parameters.readNullableFilledString(
            argName = Routes.StoriesList.getSearchTextArgName(),
            onArgumentEmptyError = {
                call.respondAsStringArgMustBeNotEmpty(Routes.StoriesList.getSearchTextArgName())
                return@get
            }
        )
            ?.takeIf { it.length >= minimalSearchTextLength }

        val languageIDList = try {
            val languageListAsString = call.parameters.readNullableFilledString(
                argName = Routes.StoriesList.getLangIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.StoriesList.getLangIDListArgName())
                    return@get
                }
            )

            languageListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.StoriesList.getLangIDListArgName())
            return@get
        }

        val authorIDList = try {
            val authorIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.StoriesList.getAuthorIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.StoriesList.getAuthorIDListArgName())
                    return@get
                }
            )

            authorIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.StoriesList.getAuthorIDListArgName())
            return@get
        }

        val userIDList = try {
            val userIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.StoriesList.getUserIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.StoriesList.getUserIDListArgName())
                    return@get
                }
            )

            userIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.StoriesList.getUserIDListArgName())
            return@get
        }

        val tagIDList = try {
            val tagIDListAsString = call.parameters.readNullableFilledString(
                argName = Routes.StoriesList.getTagIDListArgName(),
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.StoriesList.getTagIDListArgName())
                    return@get
                }
            )

            tagIDListAsString?.let { Json.decodeFromString<List<Int>>(it) }
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.StoriesList.getTagIDListArgName())
            return@get
        }

        val requestFilter = StoryListFilter(
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

        when (val getStoryListRequestResult = productionDatabaseAPI.getStories(requestFilter)) {

            is SimpleListResult.Error -> {
                val exception = getStoryListRequestResult.t
                call.logErrorToFile("Failed to get story list. Filter: $requestFilter", exception)
                call.respondAsErrorByException(getStoryListRequestResult.t)
                return@get
            }

            is SimpleListResult.Data -> {
                try {
                    val response = getStoryListRequestResult.data.map(DBStory::toWeb)
                    call.respondAsOkWithData(response)
                } catch (t: Throwable) {
                    val errorText = buildString {
                        append("Get story list return ok, but something happen after.")
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