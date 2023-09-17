package rockyouapi.tests.routes.video

import database.external.test.TestLanguage
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Tag
import declaration.entity.Video
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.*
import rockyouapi.route.Routes
import kotlin.test.Test

/** @see rockyouapi.route.video.videoReadByIDRoute */
internal class VideoReadByIDRouteTest {

    @Test
    fun video_by_id_without_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest(null, null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_without_video_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest(null, null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_without_video_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest(null, "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_without_video_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest(null, "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_invalid_video_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("Broken", null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_invalid_video_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("Broken", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_invalid_video_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("Broken", "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_negative_video_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("-100", null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_negative_video_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("-100", "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_negative_video_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("-100", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_invalid_env_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("1", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_invalid_env_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest(null, "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_invalid_env_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeVideoByIDRequest("999999", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun video_by_id_with_non_existed_video_id_must_return_404_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch {
            val response = client.makeVideoByIDRequest("999999", null)

            assert(response.status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun video_by_id_with_non_existed_video_id_must_return_404_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch {
            val response = client.makeVideoByIDRequest("999999", "1")

            assert(response.status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun video_with_correct_video_id_and_without_env_id_work_correctly() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            testEnv.extractContentAsVideosMap().onEachIndexed { idx, (videoRegisterID, video) ->


                println("Launched test IDX: $idx")
                println("videoRegisterID: $videoRegisterID, video: $video")


                val response = client.makeVideoByIDRequest(videoRegisterID.toString(), null)
                val actualResponseCode = response.status
                val expectedResponseCode = if (video == null) HttpStatusCode.NotFound else HttpStatusCode.OK


                println("Actual response code: $actualResponseCode")
                println("Expected response code: $expectedResponseCode")


                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404 or else
                if (actualResponseCode == HttpStatusCode.NotFound) return@onEachIndexed


                val decodedVideo = response.decodeAs<Video>()
                println("Decoded video: $decodedVideo")


                val isDecodedVideoCorrectByID = decodedVideo.id == videoRegisterID
                val isDecodedVideoCorrectByTitle = decodedVideo.title == video?.title
                val isDecodedVideoCorrectByLang = decodedVideo.language?.id == video?.languageID
                val isDecodedVideoCorrectByUser = decodedVideo.user?.id == video?.userID
                val isDecodedVideoCorrectByRating = decodedVideo.rating == video?.rating
                assert(isDecodedVideoCorrectByID)
                assert(isDecodedVideoCorrectByTitle)
                assert(isDecodedVideoCorrectByLang)
                assert(isDecodedVideoCorrectByUser)
                assert(isDecodedVideoCorrectByRating)


                val sourceVideoAvailableLanguagesIDs = video?.availableLanguagesIDs
                val decodedVideoAvailableLanguagesIDs = decodedVideo.availableLanguages?.map(Lang::id)
                val isDecodedVideoCorrectByAvailableLanguages =
                    sourceVideoAvailableLanguagesIDs == decodedVideoAvailableLanguagesIDs
                assert(isDecodedVideoCorrectByAvailableLanguages)


                val sourceVideoAuthorsIDs = video?.authorsIDs
                val decodedVideoAuthorsIDs = decodedVideo.authors?.map(Author::id)
                val isDecodedVideoCorrectByAuthors = sourceVideoAuthorsIDs == decodedVideoAuthorsIDs
                assert(isDecodedVideoCorrectByAuthors)


                val sourceVideoTagsIDs = video?.tagsIDs
                val decodedVideoTagsIDs = decodedVideo.tags?.map(Tag::id)
                val isDecodedVideoCorrectByTagsIDs = when {
                    sourceVideoTagsIDs == null && decodedVideoTagsIDs == null -> true
                    sourceVideoTagsIDs == null && decodedVideoTagsIDs != null -> false
                    sourceVideoTagsIDs != null && decodedVideoTagsIDs == null -> false
                    sourceVideoTagsIDs?.isEmpty() == true && decodedVideoTagsIDs?.isEmpty() == true -> true
                    sourceVideoTagsIDs!!.any { it !in decodedVideoTagsIDs!! } -> false
                    decodedVideoTagsIDs!!.any { it !in sourceVideoTagsIDs } -> false
                    else -> true
                }
                assert(isDecodedVideoCorrectByTagsIDs)

                val defaultLangID = testEnv.modelsStorage.languages.findDefaultLangID()
                val isDecodedVideoCorrectByTagsEnv = decodedVideo.tags?.all { decodedVideoTag ->
                    val tagTestModel = testEnv.modelsStorage.tags.findByID(decodedVideoTag.id)
                    val decodedVideoTagExceptedTranslation = tagTestModel?.translations?.findByLangID(defaultLangID)
                    decodedVideoTag.name == decodedVideoTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedVideoCorrectByTagsEnv)


                val isDecodedVideoCorrectByAvailableLangEnv =
                    decodedVideo.availableLanguages?.all { decodedVideoAvailableLang ->
                        val langModel = testEnv.modelsStorage.languages.findByLangID(decodedVideoAvailableLang.id)
                        val decodedVideoLangExceptedTranslation = langModel?.translations?.findByLangID(defaultLangID)
                        decodedVideoAvailableLang.name == decodedVideoLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedVideoCorrectByAvailableLangEnv)


                val sourceVideoCommentsNumber = testEnv.modelsStorage
                    .comments
                    .countCommentsByContentID(videoRegisterID)
                val decodedVideoCommentsNumber = decodedVideo.commentsCount.toInt()
                val isDecodedVideoCorrectByComments = sourceVideoCommentsNumber == decodedVideoCommentsNumber
                assert(isDecodedVideoCorrectByComments)
            }
        }
    }

    @Test
    fun video_by_id_with_non_existed_env_id_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            testEnv.extractContentAsVideosMap().onEachIndexed { idx, (videoRegisterID, video) ->


                println("Launched test IDX: $idx")
                println("videoRegisterID: $videoRegisterID, video: $video")


                val response = client.makeVideoByIDRequest(videoRegisterID.toString(), "999")
                val actualResponseCode = response.status
                val expectedResponseCode = if (video == null) HttpStatusCode.NotFound else HttpStatusCode.OK
                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404
                if (actualResponseCode != HttpStatusCode.OK) return@onEachIndexed


                val decodedVideo = response.decodeAs<Video>()
                println("Decoded video: $decodedVideo")


                val isDecodedVideoCorrectByID = decodedVideo.id == videoRegisterID
                val isDecodedVideoCorrectByTitle = decodedVideo.title == video?.title
                val isDecodedVideoCorrectByLang = decodedVideo.language?.id == video?.languageID
                val isDecodedVideoCorrectByUser = decodedVideo.user?.id == video?.userID
                val isDecodedVideoCorrectByRating = decodedVideo.rating == video?.rating
                assert(isDecodedVideoCorrectByID)
                assert(isDecodedVideoCorrectByTitle)
                assert(isDecodedVideoCorrectByLang)
                assert(isDecodedVideoCorrectByUser)
                assert(isDecodedVideoCorrectByRating)


                val sourceVideoAvailableLanguagesIDs = video?.availableLanguagesIDs
                val decodedVideoAvailableLanguagesIDs = decodedVideo.availableLanguages?.map(Lang::id)
                val isDecodedVideoCorrectByAvailableLanguages =
                    (sourceVideoAvailableLanguagesIDs == decodedVideoAvailableLanguagesIDs)
                assert(isDecodedVideoCorrectByAvailableLanguages)


                val defaultLangID = testEnv.modelsStorage.languages.findDefaultLangID()
                val isDecodedVideoCorrectByTagsEnv = decodedVideo.tags?.all { decodedVideoTag ->
                    val tagTestModel = testEnv.modelsStorage.tags.findByID(decodedVideoTag.id)
                    val decodedVideoTagExceptedTranslation = tagTestModel?.translations?.findByLangID(defaultLangID)
                    decodedVideoTag.name == decodedVideoTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedVideoCorrectByTagsEnv)


                val isDecodedVideoCorrectByAvailableLangEnv =
                    decodedVideo.availableLanguages?.all { decodedVideoAvailableLang ->
                        val langModel = testEnv.modelsStorage.languages.findByLangID(decodedVideoAvailableLang.id)
                        val decodedVideoLangExceptedTranslation = langModel?.translations?.findByLangID(defaultLangID)
                        decodedVideoAvailableLang.name == decodedVideoLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedVideoCorrectByAvailableLangEnv)


                val sourceVideoAuthorsIDs = video?.authorsIDs
                val decodedVideoAuthorsIDs = decodedVideo.authors?.map(Author::id)
                val isDecodedVideoCorrectByAuthors = sourceVideoAuthorsIDs == decodedVideoAuthorsIDs
                assert(isDecodedVideoCorrectByAuthors)


                val sourceVideoTagsIDs = video?.tagsIDs
                val decodedVideoTagsIDs = decodedVideo.tags?.map(Tag::id)
                val isDecodedVideoCorrectByTagsIDs = when {
                    sourceVideoTagsIDs == null && decodedVideoTagsIDs == null -> true
                    sourceVideoTagsIDs == null && decodedVideoTagsIDs != null -> false
                    sourceVideoTagsIDs != null && decodedVideoTagsIDs == null -> false
                    sourceVideoTagsIDs?.isEmpty() == true && decodedVideoTagsIDs?.isEmpty() == true -> true
                    sourceVideoTagsIDs!!.any { it !in decodedVideoTagsIDs!! } -> false
                    decodedVideoTagsIDs!!.any { it !in sourceVideoTagsIDs } -> false
                    else -> true
                }
                assert(isDecodedVideoCorrectByTagsIDs)


                val sourceVideoCommentsNumber = testEnv.modelsStorage
                    .comments
                    .countCommentsByContentID(videoRegisterID)
                val decodedVideoCommentsNumber = decodedVideo.commentsCount.toInt()
                val isDecodedVideoCorrectByComments = sourceVideoCommentsNumber == decodedVideoCommentsNumber
                assert(isDecodedVideoCorrectByComments)
            }
        }
    }

    @Test
    fun video_by_id_with_correct_env_id_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val envIDs = testEnv.modelsStorage.languages.map(TestLanguage::id)
            testEnv.extractContentAsVideosMap().onEachIndexed { idx, (videoRegisterID, video) ->


                println("Launched test IDX: $idx")
                println("videoRegisterID: $videoRegisterID, video: $video")


                val envID : Byte = 0
                val response = client.makeVideoByIDRequest(videoRegisterID.toString(), envID.toString())
                val actualResponseCode = response.status
                val expectedResponseCode = if (video == null) HttpStatusCode.NotFound else HttpStatusCode.OK
                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404
                if (actualResponseCode != HttpStatusCode.OK) return@onEachIndexed


                val decodedVideo = response.decodeAs<Video>()
                println("Decoded video: $decodedVideo")


                val isDecodedVideoCorrectByID = decodedVideo.id == videoRegisterID
                val isDecodedVideoCorrectByTitle = decodedVideo.title == video?.title
                val isDecodedVideoCorrectByLang = decodedVideo.language?.id == video?.languageID
                val isDecodedVideoCorrectByUser = decodedVideo.user?.id == video?.userID
                val isDecodedVideoCorrectByRating = decodedVideo.rating == video?.rating
                assert(isDecodedVideoCorrectByID)
                assert(isDecodedVideoCorrectByTitle)
                assert(isDecodedVideoCorrectByLang)
                assert(isDecodedVideoCorrectByUser)
                assert(isDecodedVideoCorrectByRating)


                val sourceVideoAvailableLanguagesIDs = video?.availableLanguagesIDs
                val decodedVideoAvailableLanguagesIDs = decodedVideo.availableLanguages?.map(Lang::id)
                val isDecodedVideoCorrectByAvailableLanguages =
                    sourceVideoAvailableLanguagesIDs == decodedVideoAvailableLanguagesIDs
                assert(isDecodedVideoCorrectByAvailableLanguages)


                val sourceVideoAuthorsIDs = video?.authorsIDs
                val decodedVideoAuthorsIDs = decodedVideo.authors?.map(Author::id)
                val isDecodedVideoCorrectByAuthors = sourceVideoAuthorsIDs == decodedVideoAuthorsIDs
                assert(isDecodedVideoCorrectByAuthors)


                val sourceVideoTagsIDs = video?.tagsIDs
                val decodedVideoTagsIDs = decodedVideo.tags?.map(Tag::id)
                val isDecodedVideoCorrectByTagsIDs = when {
                    sourceVideoTagsIDs == null && decodedVideoTagsIDs == null -> true
                    sourceVideoTagsIDs == null && decodedVideoTagsIDs != null -> false
                    sourceVideoTagsIDs != null && decodedVideoTagsIDs == null -> false
                    sourceVideoTagsIDs?.isEmpty() == true && decodedVideoTagsIDs?.isEmpty() == true -> true
                    sourceVideoTagsIDs!!.any { it !in decodedVideoTagsIDs!! } -> false
                    decodedVideoTagsIDs!!.any { it !in sourceVideoTagsIDs } -> false
                    else -> true
                }
                assert(isDecodedVideoCorrectByTagsIDs)


                val isDecodedVideoTagsCorrectByEnv = decodedVideo.tags?.all { decodedVideoTag ->
                    val tagModel = testEnv.modelsStorage.tags.findByID(decodedVideoTag.id)
                    val decodedVideoTagExceptedTranslation = tagModel?.translations?.findByLangID(envID)
                    decodedVideoTag.name == decodedVideoTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedVideoTagsCorrectByEnv)


                val isDecodedVideoCorrectByAvailableLangEnv =
                    decodedVideo.availableLanguages?.all { decodedVideoAvailableLang ->
                        val langModel = testEnv.modelsStorage.languages.findByLangID(decodedVideoAvailableLang.id)
                        val decodedVideoLangExceptedTranslation = langModel?.translations?.findByLangID(envID)
                        decodedVideoAvailableLang.name == decodedVideoLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedVideoCorrectByAvailableLangEnv)


                val sourceVideoCommentsNumber = testEnv.modelsStorage
                    .comments
                    .countCommentsByContentID(videoRegisterID)
                val decodedVideoCommentsNumber = decodedVideo.commentsCount.toInt()
                val isDecodedVideoCorrectByComments = sourceVideoCommentsNumber == decodedVideoCommentsNumber
                assert(isDecodedVideoCorrectByComments)
            }
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