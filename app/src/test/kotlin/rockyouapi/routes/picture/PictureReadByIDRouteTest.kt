package rockyouapi.routes.picture

import database.external.model.TestLanguage
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Picture
import declaration.entity.Tag
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import rockyouapi.*
import rockyouapi.extractContentAsPicturesMap
import rockyouapi.findByID
import rockyouapi.route.Routes
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import kotlin.test.Test

/** @see rockyouapi.route.picture.pictureReadByIDRoute */
internal class PictureReadByIDRouteTest {

    @Test
    fun picture_by_id_without_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest(null, null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_without_picture_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest(null, null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_without_picture_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest(null, "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun picture_by_id_without_picture_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest(null, "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_invalid_picture_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("Broken", null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_invalid_picture_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("Broken", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_invalid_picture_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("Broken", "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_negative_picture_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("-100", null)

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_negative_picture_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("-100", "1")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_negative_picture_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("-100", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun picture_by_id_with_invalid_env_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("1", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_invalid_env_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest(null, "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_invalid_env_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureByIDRequest("999999", "Broken")

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun picture_by_id_with_non_existed_picture_id_must_return_404_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch {
            val response = client.makePictureByIDRequest("999999", null)

            assert(response.status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun picture_by_id_with_non_existed_picture_id_must_return_404_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch {
            val response = client.makePictureByIDRequest("999999", "1")

            assert(response.status == HttpStatusCode.NotFound)
        }
    }

    @Test
    fun picture_with_correct_picture_id_and_without_env_id_work_correctly() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testModelsContainer ->
            testModelsContainer.extractContentAsPicturesMap().onEachIndexed { idx, (pictureRegisterID, picture) ->


                println("Launched test IDX: $idx")
                println("PictureRegisterID: $pictureRegisterID, Picture: $picture")


                val response = client.makePictureByIDRequest(pictureRegisterID.toString(), null)
                val actualResponseCode = response.status
                val expectedResponseCode = if (picture == null) HttpStatusCode.NotFound else HttpStatusCode.OK


                println("Actual response code: $actualResponseCode")
                println("Expected response code: $expectedResponseCode")


                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404 or else
                if (actualResponseCode == HttpStatusCode.NotFound) return@onEachIndexed


                val decodedPicture = Json.decodeFromString<Picture>(response.bodyAsText())
                println("Decoded picture: $decodedPicture")


                val isDecodedPictureCorrectByID = decodedPicture.id == pictureRegisterID
                val isDecodedPictureCorrectByTitle = decodedPicture.title == picture?.title
                val isDecodedPictureCorrectByLang = decodedPicture.language?.id == picture?.languageID
                val isDecodedPictureCorrectByUser = decodedPicture.user?.id == picture?.userID
                val isDecodedPictureCorrectByRating = decodedPicture.rating == picture?.rating
                assert(isDecodedPictureCorrectByID)
                assert(isDecodedPictureCorrectByTitle)
                assert(isDecodedPictureCorrectByLang)
                assert(isDecodedPictureCorrectByUser)
                assert(isDecodedPictureCorrectByRating)


                val sourcePictureAvailableLanguagesIDs = picture?.availableLanguagesIDs
                val decodedPictureAvailableLanguagesIDs = decodedPicture.availableLanguages?.map(Lang::id)
                val isDecodedPictureCorrectByAvailableLanguages =
                    sourcePictureAvailableLanguagesIDs == decodedPictureAvailableLanguagesIDs
                assert(isDecodedPictureCorrectByAvailableLanguages)


                val sourcePictureAuthorsIDs = picture?.authorsIDs
                val decodedPictureAuthorsIDs = decodedPicture.authors?.map(Author::id)
                val isDecodedPictureCorrectByAuthors = sourcePictureAuthorsIDs == decodedPictureAuthorsIDs
                assert(isDecodedPictureCorrectByAuthors)


                val sourcePictureTagsIDs = picture?.tagsIDs
                val decodedPictureTagsIDs = decodedPicture.tags?.map(Tag::id)
                val isDecodedPictureCorrectByTagsIDs = sourcePictureTagsIDs == decodedPictureTagsIDs
                assert(isDecodedPictureCorrectByTagsIDs)


                val defaultLangID = testModelsContainer.languages.findDefaultLangID()
                val isDecodedPictureCorrectByTagsEnv = decodedPicture.tags?.all { decodedPictureTag ->
                    val tagTestModel = testModelsContainer.tags.findByID(decodedPictureTag.id)
                    val decodedPictureTagExceptedTranslation = tagTestModel?.translations?.findByLangID(defaultLangID)
                    decodedPictureTag.name == decodedPictureTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedPictureCorrectByTagsEnv)


                val isDecodedPictureCorrectByAvailableLangEnv =
                    decodedPicture.availableLanguages?.all { decodedPictureAvailableLang ->
                        val langModel = testModelsContainer.languages.findByLangID(decodedPictureAvailableLang.id)
                        val decodedPictureLangExceptedTranslation = langModel?.translations?.findByLangID(defaultLangID)
                        decodedPictureAvailableLang.name == decodedPictureLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedPictureCorrectByAvailableLangEnv)


                val sourcePictureCommentsNumber =
                    testModelsContainer.comments.countCommentsByContentID(pictureRegisterID)
                val decodedPictureCommentsNumber = decodedPicture.commentsCount
                val isDecodedPictureCorrectByComments = sourcePictureCommentsNumber == decodedPictureCommentsNumber
                assert(isDecodedPictureCorrectByComments)
            }
        }
    }

    @Test
    fun picture_by_id_with_non_existed_env_id_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testModelsContainer ->
            testModelsContainer.extractContentAsPicturesMap().onEachIndexed { idx, (pictureRegisterID, picture) ->


                println("Launched test IDX: $idx")
                println("PictureRegisterID: $pictureRegisterID, Picture: $picture")


                val response = client.makePictureByIDRequest(pictureRegisterID.toString(), "999")
                val actualResponseCode = response.status
                val expectedResponseCode = if (picture == null) HttpStatusCode.NotFound else HttpStatusCode.OK
                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404
                if (actualResponseCode != HttpStatusCode.OK) return@onEachIndexed


                val decodedPicture = Json.decodeFromString<Picture>(response.bodyAsText())
                println("Decoded picture: $decodedPicture")


                val isDecodedPictureCorrectByID = decodedPicture.id == pictureRegisterID
                val isDecodedPictureCorrectByTitle = decodedPicture.title == picture?.title
                val isDecodedPictureCorrectByLang = decodedPicture.language?.id == picture?.languageID
                val isDecodedPictureCorrectByUser = decodedPicture.user?.id == picture?.userID
                val isDecodedPictureCorrectByRating = decodedPicture.rating == picture?.rating
                assert(isDecodedPictureCorrectByID)
                assert(isDecodedPictureCorrectByTitle)
                assert(isDecodedPictureCorrectByLang)
                assert(isDecodedPictureCorrectByUser)
                assert(isDecodedPictureCorrectByRating)


                val sourcePictureAvailableLanguagesIDs = picture?.availableLanguagesIDs
                val decodedPictureAvailableLanguagesIDs = decodedPicture.availableLanguages?.map(Lang::id)
                val isDecodedPictureCorrectByAvailableLanguages =
                    (sourcePictureAvailableLanguagesIDs == decodedPictureAvailableLanguagesIDs)
                assert(isDecodedPictureCorrectByAvailableLanguages)


                val defaultLangID = testModelsContainer.languages.findDefaultLangID()
                val isDecodedPictureCorrectByTagsEnv = decodedPicture.tags?.all { decodedPictureTag ->
                    val tagTestModel = testModelsContainer.tags.findByID(decodedPictureTag.id)
                    val decodedPictureTagExceptedTranslation = tagTestModel?.translations?.findByLangID(defaultLangID)
                    decodedPictureTag.name == decodedPictureTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedPictureCorrectByTagsEnv)


                val isDecodedPictureCorrectByAvailableLangEnv =
                    decodedPicture.availableLanguages?.all { decodedPictureAvailableLang ->
                        val langModel = testModelsContainer.languages.findByLangID(decodedPictureAvailableLang.id)
                        val decodedPictureLangExceptedTranslation = langModel?.translations?.findByLangID(defaultLangID)
                        decodedPictureAvailableLang.name == decodedPictureLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedPictureCorrectByAvailableLangEnv)


                val sourcePictureAuthorsIDs = picture?.authorsIDs
                val decodedPictureAuthorsIDs = decodedPicture.authors?.map(Author::id)
                val isDecodedPictureCorrectByAuthors = sourcePictureAuthorsIDs == decodedPictureAuthorsIDs
                assert(isDecodedPictureCorrectByAuthors)


                val sourcePictureTagsIDs = picture?.tagsIDs
                val decodedPictureTagsIDs = decodedPicture.tags?.map(Tag::id)
                val isDecodedPictureCorrectByTags = sourcePictureTagsIDs == decodedPictureTagsIDs
                assert(isDecodedPictureCorrectByTags)


                val sourcePictureCommentsNumber =
                    testModelsContainer.comments.countCommentsByContentID(pictureRegisterID)
                val decodedPictureCommentsNumber = decodedPicture.commentsCount
                val isDecodedPictureCorrectByComments = sourcePictureCommentsNumber == decodedPictureCommentsNumber
                assert(isDecodedPictureCorrectByComments)
            }
        }
    }

    @Test
    fun picture_by_id_with_correct_env_id_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testModelsContainer ->
            val envIDs = testModelsContainer.languages.map(TestLanguage::id)
            testModelsContainer.extractContentAsPicturesMap().onEachIndexed { idx, (pictureRegisterID, picture) ->


                println("Launched test IDX: $idx")
                println("PictureRegisterID: $pictureRegisterID, Picture: $picture")


                val envID = envIDs.random()
                val response = client.makePictureByIDRequest(pictureRegisterID.toString(), envID.toString())
                val actualResponseCode = response.status
                val expectedResponseCode = if (picture == null) HttpStatusCode.NotFound else HttpStatusCode.OK
                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404
                if (actualResponseCode != HttpStatusCode.OK) return@onEachIndexed


                val decodedPicture = Json.decodeFromString<Picture>(response.bodyAsText())
                println("Decoded picture: $decodedPicture")


                val isDecodedPictureCorrectByID = decodedPicture.id == pictureRegisterID
                val isDecodedPictureCorrectByTitle = decodedPicture.title == picture?.title
                val isDecodedPictureCorrectByLang = decodedPicture.language?.id == picture?.languageID
                val isDecodedPictureCorrectByUser = decodedPicture.user?.id == picture?.userID
                val isDecodedPictureCorrectByRating = decodedPicture.rating == picture?.rating
                assert(isDecodedPictureCorrectByID)
                assert(isDecodedPictureCorrectByTitle)
                assert(isDecodedPictureCorrectByLang)
                assert(isDecodedPictureCorrectByUser)
                assert(isDecodedPictureCorrectByRating)


                val sourcePictureAvailableLanguagesIDs = picture?.availableLanguagesIDs
                val decodedPictureAvailableLanguagesIDs = decodedPicture.availableLanguages?.map(Lang::id)
                val isDecodedPictureCorrectByAvailableLanguages =
                    sourcePictureAvailableLanguagesIDs == decodedPictureAvailableLanguagesIDs
                assert(isDecodedPictureCorrectByAvailableLanguages)


                val sourcePictureAuthorsIDs = picture?.authorsIDs
                val decodedPictureAuthorsIDs = decodedPicture.authors?.map(Author::id)
                val isDecodedPictureCorrectByAuthors = sourcePictureAuthorsIDs == decodedPictureAuthorsIDs
                assert(isDecodedPictureCorrectByAuthors)


                val sourcePictureTagsIDs = picture?.tagsIDs
                val decodedPictureTagsIDs = decodedPicture.tags?.map(Tag::id)
                val isDecodedPictureCorrectByTags = sourcePictureTagsIDs == decodedPictureTagsIDs
                assert(isDecodedPictureCorrectByTags)


                val isDecodedPictureTagsCorrectByEnv = decodedPicture.tags?.all { decodedPictureTag ->
                    val tagModel = testModelsContainer.tags.findByID(decodedPictureTag.id)
                    val decodedPictureTagExceptedTranslation = tagModel?.translations?.findByLangID(envID)
                    decodedPictureTag.name == decodedPictureTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedPictureTagsCorrectByEnv)


                val isDecodedPictureCorrectByAvailableLangEnv =
                    decodedPicture.availableLanguages?.all { decodedPictureAvailableLang ->
                        val langModel = testModelsContainer.languages.findByLangID(decodedPictureAvailableLang.id)
                        val decodedPictureLangExceptedTranslation = langModel?.translations?.findByLangID(envID)
                        decodedPictureAvailableLang.name == decodedPictureLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedPictureCorrectByAvailableLangEnv)


                val sourcePictureCommentsNumber =
                    testModelsContainer.comments.countCommentsByContentID(pictureRegisterID)
                val decodedPictureCommentsNumber = decodedPicture.commentsCount
                val isDecodedPictureCorrectByComments = sourcePictureCommentsNumber == decodedPictureCommentsNumber
                assert(isDecodedPictureCorrectByComments)
            }
        }
    }

    private suspend fun HttpClient.makePictureByIDRequest(pictureID: String?, environmentID: String?) =
        get(Routes.PictureByID.path) {
            url {
                pictureID?.let { parameters.append(Routes.PictureByID.getPictureIDArgName(), pictureID) }
                environmentID?.let { parameters.append(Routes.PictureByID.getEnvLangIDArgName(), environmentID) }
            }
        }
}