package rockyouapi.route.content

import database.external.contract.ProductionDatabaseAPI
import database.external.result.common.SimpleDataResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import rockyouapi.model.result.GetContentByIDResult
import rockyouapi.route.Routes
import rockyouapi.toWeb
import rockyouapi.utils.*
import database.external.model.Picture as DBPicture
import database.external.model.Video as DBVideo
import database.external.model.story.Story as DBStory
import database.external.model.Chapter as DBChapter

/**
 * Route to read content by contentID list.
 *
 * Requirements: contentIDList.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Data fetched. Respond as [GetContentByIDResult].
 * - [HttpStatusCode.BadRequest] If contentID or limit not presented or invalid.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.readContentByIDRoute(productionDatabaseAPI: ProductionDatabaseAPI) {

    get(Routes.ContentReadByIDList.path) {

        val contentIDList = try {
            val contentIDListAsString = call.parameters.readNotNullableFilledString(
                argName = Routes.ContentReadByIDList.getContentIDListArgName(),
                onArgumentNullError = {
                    call.respondAsArgumentRequiredError(Routes.ContentReadByIDList.getContentIDListArgName())
                    return@get
                },
                onArgumentEmptyError = {
                    call.respondAsStringArgMustBeNotEmpty(Routes.ContentReadByIDList.getContentIDListArgName())
                    return@get
                },
            )

            Json.decodeFromString<List<Int>>(contentIDListAsString)
        } catch (t: SerializationException) {
            call.respondAsJSONBroken(Routes.ContentReadByIDList.getContentIDListArgName())
            return@get
        }

        if (contentIDList.isEmpty()) {
            call.respondAsOkWithData(GetContentByIDResult())
            return@get
        }

        val environmentLangID = call.parameters.readNullableInt(
            argName = Routes.ContentReadByIDList.getEnvironmentLangIDArgName(),
            onArgumentNotIntError = {
                call.respondAsIncorrectTypeWhenIntExpected(Routes.ContentReadByIDList.getEnvironmentLangIDArgName())
                return@get
            }
        )

        when (val getContentByIDListResult = productionDatabaseAPI.getContentByID(contentIDList, environmentLangID)) {

            is SimpleDataResult.Error -> {
                call.respondAsErrorByException(getContentByIDListResult.t)
                return@get
            }

            is SimpleDataResult.Data -> {
                try {
                    val response = GetContentByIDResult(
                        pictures = getContentByIDListResult.data.pictures.map(DBPicture::toWeb),
                        videos = getContentByIDListResult.data.videos.map(DBVideo::toWeb),
                        stories = getContentByIDListResult.data.stories.map(DBStory::toWeb),
                        chapters = getContentByIDListResult.data.chapters.map(DBChapter::toWeb)
                    )
                    call.respondAsOkWithData(response)
                } catch (t: Throwable) {
                    val errorText = buildString {
                        append("Get content by ID list return ok, but something happen after.")
                        appendLine()
                        append("Content ID List: $contentIDList")
                    }
                    call.logErrorToFile(errorText, t)
                    call.respondAsErrorByException(t)
                }
                return@get
            }
        }
    }
}