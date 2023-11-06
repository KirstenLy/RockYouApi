package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import database.external.ContentType
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.filter.VideoByIDFilter
import database.external.model.Video
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.arguments.ReadByIDMethodArguments
import rockyouapi.arguments.generateEnvironmentID
import rockyouapi.arguments.generateNotExistedContentID
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.video.videoReadByIDRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class VideoReadByIDRouteTest {

    private fun invalidArguments() = Stream.of(
        ReadByIDMethodArguments(null, null),
        ReadByIDMethodArguments(null, "null"),
        ReadByIDMethodArguments(null, generateRandomTextByUUID()),
        ReadByIDMethodArguments(null, "-1"),
        ReadByIDMethodArguments(null, "-1111"),
        ReadByIDMethodArguments(null, ""),
        ReadByIDMethodArguments(null, "    "),
        ReadByIDMethodArguments("null", null),
        ReadByIDMethodArguments(generateRandomTextByUUID(), null),
        ReadByIDMethodArguments("-1", "null"),
        ReadByIDMethodArguments("", null),
        ReadByIDMethodArguments("    ", null),
        ReadByIDMethodArguments(generateRandomTextByUUID(), generateRandomTextByUUID()),
        ReadByIDMethodArguments("-1", generateRandomTextByUUID()),
        ReadByIDMethodArguments("null", "null"),
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
    fun video_by_id_with_invalid_arguments_return_400(invalidArguments: ReadByIDMethodArguments) {
        runTestSimple {
            client.makeVideoByIDRequest(
                videoID = invalidArguments.contentID,
                environmentID = invalidArguments.environmentID
            )
                .badRequestOrFail()
        }
    }

    @RepeatedTest(10)
    fun video_by_id_with_non_existed_video_id_return_404() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            client.makeVideoByIDRequest(
                videoID = generateNotExistedContentID().toString(),
                environmentID = testDatabaseAPI
                    .getAllSupportedLanguageID()
                    .random()
                    .toString()
            )
                .notFoundOrFail()
        }
    }

    @RepeatedTest(10)
    fun video_by_id_with_existed_video_id_return_200_with_correct_video() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val videoID = testDatabaseAPI.getRandomContentIDForEntity(ContentType.VIDEO)
            val environmentID = generateEnvironmentID()

            val actualVideo = client.makeVideoByIDRequest(
                videoID = videoID.toString(),
                environmentID = environmentID?.toString()
            )
                .okOrFail()
                .decodeAs<BaseResponse<Video>>()
                .data!!

            val expectedVideo = productionDatabaseAPI.getVideoByID(
                VideoByIDFilter(
                    videoID = videoID,
                    environmentLangID = environmentID
                )
            )
                .extractModelOrFail()

            assertEquals(expectedVideo, actualVideo)
        }
    }

    private suspend fun HttpClient.makeVideoByIDRequest(videoID: String?, environmentID: String?) =
        get(Routes.VideoByID.path) {
            url {
                appendToParameters(videoID, Routes.VideoByID.getVideoIDArgName())
                appendToParameters(environmentID, Routes.VideoByID.getEnvLangIDArgName())
            }
        }
}