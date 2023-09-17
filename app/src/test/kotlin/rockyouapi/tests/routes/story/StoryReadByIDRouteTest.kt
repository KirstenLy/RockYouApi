package rockyouapi.tests.routes.story

import common.takeRandomValues
import database.external.ContentType
import database.external.test.TestStoryNode
import declaration.entity.story.StoryNew
import declaration.entity.story.StoryNode
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.*
import rockyouapi.route.Routes
import java.util.*
import kotlin.collections.List
import kotlin.test.Test

/** @see rockyouapi.route.story.storyReadByIDRoute */
internal class StoryReadByIDRouteTest {

    @Test
    fun story_read_by_id_without_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest(null, null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_without_story_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest(null, "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_without_story_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest(null, "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_without_story_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest(null, "-1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_incorrect_story_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest(null, null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_incorrect_story_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest("Broken", null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_incorrect_story_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest("-1", null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_incorrect_story_id_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest("Broken", "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_incorrect_story_id_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest("-1", "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_incorrect_env_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest("1", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_incorrect_env_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.getStoryByIDRequest("1", UUID.randomUUID().toString())

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun story_read_by_id_with_correct_arguments_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val storiesIDs = testEnv.modelsStorage.contentRegisters
                .getRegisterIDsByContentType(ContentType.STORY.typeID)
                .takeRandomValues()

            storiesIDs.forEach { storyID ->
                val response = client.getStoryByIDRequest(storyID.toString(), null)
                assert(response.status == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${response.status}"
                }

                val storyByResponse = response.decodeAs<StoryNew>()
                val storyByStorage = testEnv.modelsStorage
                    .contentRegisters
                    .first { it.id == storyID }
                    .contentID
                    .let { storyEntityID -> testEnv.modelsStorage.stories.first { it.id == storyEntityID } }

                val isStoryCorrectByID = storyByResponse.id == storyID
                assert(isStoryCorrectByID) { "Responded story incorrect by id" }

                val isStoryCorrectByTitle = storyByResponse.title == storyByStorage.title
                assert(isStoryCorrectByTitle) { "Responded story incorrect by title" }

                val storyNodesByResponse = storyByResponse.nodes
                val storyNodesByStorage = storyByStorage.storyNodes

                assert(storyNodesByStorage.isNotEmpty()) { "Nodes for story not found" }
                assert(storyNodesByResponse.size == storyNodesByStorage.size) {
                    "Nodes for stores story and responded story has different sizes"
                }
            }
        }
    }

    private suspend fun HttpClient.getStoryByIDRequest(storyID: String?, environmentID: String?) =
        get(Routes.StoryByID.path) {
            url {
                appendToParameters(storyID, Routes.StoryByID.getStoryIDArgName())
                appendToParameters(environmentID, Routes.StoryByID.getEnvLangIDArgName())
            }
        }
}