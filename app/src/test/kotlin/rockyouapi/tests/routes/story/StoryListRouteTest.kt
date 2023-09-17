package rockyouapi.tests.routes.story

import declaration.entity.story.StoryNew
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.appendToParameters
import rockyouapi.decodeAs
import rockyouapi.route.Routes
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import kotlin.test.Test

/** @see rockyouapi.route.story.storyListRoute */
internal class StoryListRouteTest {

    @Test
    fun story_read_by_id_without_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryListRequest(null, null, null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_correct_arguments_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val response = client.getStoryListRequest(
                langID = null,
                offset = "10",
                limit = "10"
            )

            assert(response.status == HttpStatusCode.OK) {
                "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${response.status}"
            }

            val storyListByResponse = response.decodeAs<List<StoryNew>>()
            assert(storyListByResponse.size == 10) {
                "Expected response size don't 10"
            }
        }
    }

    private suspend fun HttpClient.getStoryListRequest(langID: String?, offset: String?, limit: String?) =
        get(Routes.StoriesList.path) {
            url {
                appendToParameters(langID, Routes.StoriesList.getLangIDArgName())
                appendToParameters(offset, Routes.StoriesList.getOffsetArgName())
                appendToParameters(limit, Routes.StoriesList.getLimitArgName())
            }
        }
}