package rockyouapi.tests.routes.video

import declaration.entity.Video
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.*
import rockyouapi.route.Routes
import kotlin.math.max
import kotlin.test.Test

/** @see rockyouapi.route.video.videoListRoute */
internal class VideoListRouteTest {

    @Test
    fun videos_list_without_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_all_invalid_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun videos_list_without_limit_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = "1",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = "1",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_without_limit_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = null,
                langID = "Broken",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_limit_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_limit_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_limit_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_limit_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_limit_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_limit_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = "1",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_limit_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "Broken",
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_limit_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_limit_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_limit_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_limit_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_limit_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun videos_list_with_negative_limit_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = "1",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun videos_list_with_negative_limit_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_limit_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = "1",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_limit_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                limit = "-1",
                langID = "Broken",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = "1",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = "Broken",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_lang_id_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                langID = "Broken",
                limit = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = null,
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = "1",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = "Broken",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = null,
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = null,
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = "1",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = "Broken",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = "1",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_invalid_offset_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "Broken",
                langID = "Broken",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = null,
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = "1",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = "Broken",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = null,
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = null,
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = "1",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = "Broken",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = "1",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_negative_offset_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoListRequest(
                offset = "-1",
                langID = "Broken",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun videos_list_with_correct_limit_only_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testModelsContainer ->
            val videos = testModelsContainer.extractVideosFromNewToOld()

            listOf("10", "30", "40", "50", "500", "1").forEach { limit ->
                val response = client.makeVideoListRequest(
                    langID = null,
                    limit = limit,
                    offset = null
                )

                val actualResponseCode = response.status
                assert(actualResponseCode == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: $actualResponseCode"
                }

                val decodedResponse = response.decodeAs<List<Video>>()
                val actualResponseSize = decodedResponse.size
                val expectedResponseSize = when {
                    videos.size >= limit.toInt() -> limit.toInt()
                    else -> videos.size
                }
                assert(actualResponseSize == expectedResponseSize) {
                    "Decoded response size not as expected. Actual size: $actualResponseSize, expected size: $expectedResponseSize"
                }

                decodedResponse.forEachIndexed { index, videoResponse ->
                    val (expectedRegisterID, expectedVideoModel) = videos[index]

                    val expectedVideoID = expectedRegisterID
                    val responseVideoID = videoResponse.id
                    val isVideoValidByID = expectedRegisterID == responseVideoID
                    assert(isVideoValidByID) { "Response video invalid by id! Expected: $expectedVideoID, Actual: $responseVideoID" }

                    val expectedVideoTitle = expectedVideoModel.title
                    val responseVideoTitle = videoResponse.title
                    val isVideoValidByTitle = expectedVideoTitle == responseVideoTitle
                    assert(isVideoValidByTitle) { "Response video invalid by title! Expected: $expectedVideoTitle, Actual: $responseVideoTitle" }
                }
            }
        }
    }

    @Test
    fun videos_list_offset_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testModelsContainer ->
            val videos = testModelsContainer.extractVideosFromNewToOld()
            val limit = 10

            listOf("0", "10", "20", "30", "40", "50").forEach { offset ->
                val response = client.makeVideoListRequest(
                    langID = null,
                    limit = "10",
                    offset = offset
                )

                val actualResponseCode = response.status
                assert(actualResponseCode == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: $actualResponseCode"
                }

                val decodedResponse = response.decodeAs<List<Video>>()
                val actualResponseSize = decodedResponse.size
                val expectedResponseSize = when {
                    videos.size >= (limit + offset.toInt()) -> limit
                    videos.size - offset.toInt() >= limit -> limit // videos.size - offset.toInt() is how many videos can be returned by fact
                    else -> max(videos.size - offset.toInt(), 0)
                }
                assert(actualResponseSize == expectedResponseSize) {
                    "Decoded response size not as expected. Actual size: $actualResponseSize, expected size: $expectedResponseSize"
                }

                // Response return empty list, nothing to test
                if (expectedResponseSize == 0) return@forEach

                decodedResponse.forEachIndexed { index, videoResponse ->
                    val (expectedRegisterID, expectedVideoModel) = videos[index + offset.toInt()]

                    val responseVideoID = videoResponse.id
                    val isVideoValidByID = expectedRegisterID == responseVideoID
                    assert(isVideoValidByID) { "Response video invalid by id! Expected: $expectedRegisterID, Actual: $responseVideoID" }

                    val expectedVideoTitle = expectedVideoModel.title
                    val responseVideoTitle = videoResponse.title
                    val isVideoValidByTitle = expectedVideoTitle == responseVideoTitle
                    assert(isVideoValidByTitle) { "Response video invalid by title! Expected: $expectedVideoTitle, Actual: $responseVideoTitle" }
                }
            }
        }
    }

    private suspend fun HttpClient.makeVideoListRequest(langID: String?, limit: String?, offset: String?) =
        get(Routes.VideoList.path) {
            url {
                appendToParameters(langID, Routes.VideoList.getLangIDArgName())
                appendToParameters(limit, Routes.VideoList.getLimitArgName())
                appendToParameters(offset, Routes.VideoList.getOffsetArgName())
            }
        }
}