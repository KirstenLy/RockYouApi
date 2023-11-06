package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import database.external.ContentType
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.arguments.generateNotExistedContentID
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.story.storyReadChapterTextByIDRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class StoryReadChapterTextByIDRouteTest {

    private fun invalidArguments() = Stream.of(
        generateRandomTextByUUID(),
        "",
        "   ",
    )

    private val databaseAPI by lazy {
        runBlocking {
            getOrCreateValue(KEY_STATIC_MAP_DB) {
                connectToProductionDatabaseWithTestApi(readDatabaseConfigurationFromEnv())
            }
        }
    }

    private val productionDatabaseAPI get() = databaseAPI.first

    private val testDatabaseAPI get() = databaseAPI.second

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun chapter_text_by_id_with_invalid_arguments_return_400(invalidArgument: String) {
        runTestSimple {
            client.makeChapterTextByIDRequest(
                chapterID = invalidArgument
            )
                .badRequestOrFail()
        }
    }

    @RepeatedTest(10)
    fun chapter_text_with_not_existed_chapter_id_return_404() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            client.makeChapterTextByIDRequest(
                chapterID = generateNotExistedContentID().toString(),
            )
                .notFoundOrFail()
        }
    }

    @RepeatedTest(10)
    fun chapter_text_with_existed_chapter_id_return_200_and_correct_text() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val chapterID = testDatabaseAPI.getRandomContentIDForEntity(ContentType.STORY_CHAPTER)

            val actualChapterText = client.makeChapterTextByIDRequest(
                chapterID = chapterID.toString(),
            )
                .okOrFail()
                .decodeAs<BaseResponse<String>>()
                .data!!

            val expectedChapterText = testDatabaseAPI.getChapterTextID(chapterID)
            assertEquals(expectedChapterText, actualChapterText)
        }
    }

    private suspend fun HttpClient.makeChapterTextByIDRequest(chapterID: String?) =
        get(Routes.ReadChapterTextByID.path) {
            url {
                appendToParameters(chapterID, Routes.ReadChapterTextByID.getChapterIDArgName())
            }
        }
}