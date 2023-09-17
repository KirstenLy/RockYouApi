package rockyouapi.tests.routes.search

import declaration.GetContentByTextResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.appendToParameters
import rockyouapi.decodeAs
import rockyouapi.route.Routes
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import kotlin.test.Test

/** @see rockyouapi.route.search.searchByTextRoute */
internal class SearchRouteTest {

    @Test
    fun story_read_by_id_with_correct_arguments_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val response = client.getSearchByTextRequest("Картинка")

            assert(response.status == HttpStatusCode.OK) {
                "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${response.status}"
            }

            val decodedResponse = response.decodeAs<GetContentByTextResult>()
            assert(decodedResponse.pictures.size == 40) { "Not all pictures was found by text" }
        }
    }

    private suspend fun HttpClient.getSearchByTextRequest(searchText: String?) =
        get(Routes.SearchByText.path) {
            url {
                appendToParameters(searchText, Routes.SearchByText.getSearchTextArgName())
            }
        }
}