package rockyouapi.tests.routes.picture

import database.external.test.TestLanguage
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Picture
import declaration.entity.Tag
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.*
import rockyouapi.route.Routes
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
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            testEnv.extractContentAsPicturesMap().onEachIndexed { idx, (pictureRegisterID, picture) ->


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


                val decodedPicture = response.decodeAs<Picture>()
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
                val isDecodedPictureCorrectByTagsIDs = when {
                    sourcePictureTagsIDs == null && decodedPictureTagsIDs == null -> true
                    sourcePictureTagsIDs == null && decodedPictureTagsIDs != null -> false
                    sourcePictureTagsIDs != null && decodedPictureTagsIDs == null -> false
                    sourcePictureTagsIDs?.isEmpty() == true && decodedPictureTagsIDs?.isEmpty() == true -> true
                    sourcePictureTagsIDs!!.any { it !in decodedPictureTagsIDs!! } -> false
                    decodedPictureTagsIDs!!.any { it !in sourcePictureTagsIDs } -> false
                    else -> true
                }
                assert(isDecodedPictureCorrectByTagsIDs)

                val defaultLangID = testEnv.modelsStorage.languages.findDefaultLangID()
                val isDecodedPictureCorrectByTagsEnv = decodedPicture.tags?.all { decodedPictureTag ->
                    val tagTestModel = testEnv.modelsStorage.tags.findByID(decodedPictureTag.id)
                    val decodedPictureTagExceptedTranslation = tagTestModel?.translations?.findByLangID(defaultLangID)
                    decodedPictureTag.name == decodedPictureTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedPictureCorrectByTagsEnv)


                val isDecodedPictureCorrectByAvailableLangEnv =
                    decodedPicture.availableLanguages?.all { decodedPictureAvailableLang ->
                        val langModel = testEnv.modelsStorage.languages.findByLangID(decodedPictureAvailableLang.id)
                        val decodedPictureLangExceptedTranslation = langModel?.translations?.findByLangID(defaultLangID)
                        decodedPictureAvailableLang.name == decodedPictureLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedPictureCorrectByAvailableLangEnv)


                val sourcePictureCommentsNumber =
                    testEnv.modelsStorage.comments.countCommentsByContentID(pictureRegisterID)
                val decodedPictureCommentsNumber = decodedPicture.commentsCount.toInt()
                val isDecodedPictureCorrectByComments = sourcePictureCommentsNumber == decodedPictureCommentsNumber
                assert(isDecodedPictureCorrectByComments)
            }
        }
    }

    @Test
    fun picture_by_id_with_non_existed_env_id_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            testEnv.extractContentAsPicturesMap().onEachIndexed { idx, (pictureRegisterID, picture) ->


                println("Launched test IDX: $idx")
                println("PictureRegisterID: $pictureRegisterID, Picture: $picture")


                val response = client.makePictureByIDRequest(pictureRegisterID.toString(), "999")
                val actualResponseCode = response.status
                val expectedResponseCode = if (picture == null) HttpStatusCode.NotFound else HttpStatusCode.OK
                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404
                if (actualResponseCode != HttpStatusCode.OK) return@onEachIndexed


                val decodedPicture = response.decodeAs<Picture>()
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


                val defaultLangID = testEnv.modelsStorage.languages.findDefaultLangID()
                val isDecodedPictureCorrectByTagsEnv = decodedPicture.tags?.all { decodedPictureTag ->
                    val tagTestModel = testEnv.modelsStorage.tags.findByID(decodedPictureTag.id)
                    val decodedPictureTagExceptedTranslation = tagTestModel?.translations?.findByLangID(defaultLangID)
                    decodedPictureTag.name == decodedPictureTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedPictureCorrectByTagsEnv)


                val isDecodedPictureCorrectByAvailableLangEnv =
                    decodedPicture.availableLanguages?.all { decodedPictureAvailableLang ->
                        val langModel = testEnv.modelsStorage.languages.findByLangID(decodedPictureAvailableLang.id)
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
                val isDecodedPictureCorrectByTagsIDs = when {
                    sourcePictureTagsIDs == null && decodedPictureTagsIDs == null -> true
                    sourcePictureTagsIDs == null && decodedPictureTagsIDs != null -> false
                    sourcePictureTagsIDs != null && decodedPictureTagsIDs == null -> false
                    sourcePictureTagsIDs?.isEmpty() == true && decodedPictureTagsIDs?.isEmpty() == true -> true
                    sourcePictureTagsIDs!!.any { it !in decodedPictureTagsIDs!! } -> false
                    decodedPictureTagsIDs!!.any { it !in sourcePictureTagsIDs } -> false
                    else -> true
                }
                assert(isDecodedPictureCorrectByTagsIDs)


                val sourcePictureCommentsNumber =
                    testEnv.modelsStorage.comments.countCommentsByContentID(pictureRegisterID)
                val decodedPictureCommentsNumber = decodedPicture.commentsCount.toInt()
                val isDecodedPictureCorrectByComments = sourcePictureCommentsNumber == decodedPictureCommentsNumber
                assert(isDecodedPictureCorrectByComments)
            }
        }
    }

    @Test
    fun picture_by_id_with_correct_env_id_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val envIDs = testEnv.modelsStorage.languages.map(TestLanguage::id)
            testEnv.extractContentAsPicturesMap().onEachIndexed { idx, (pictureRegisterID, picture) ->


                println("Launched test IDX: $idx")
                println("PictureRegisterID: $pictureRegisterID, Picture: $picture")


                val envID: Byte = 0
                val response = client.makePictureByIDRequest(pictureRegisterID.toString(), envID.toString())
                val actualResponseCode = response.status
                val expectedResponseCode = if (picture == null) HttpStatusCode.NotFound else HttpStatusCode.OK
                assert(actualResponseCode == expectedResponseCode)


                // Test ended for 404
                if (actualResponseCode != HttpStatusCode.OK) return@onEachIndexed


                val decodedPicture = response.decodeAs<Picture>()
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
                val isDecodedPictureCorrectByTagsIDs = when {
                    sourcePictureTagsIDs == null && decodedPictureTagsIDs == null -> true
                    sourcePictureTagsIDs == null && decodedPictureTagsIDs != null -> false
                    sourcePictureTagsIDs != null && decodedPictureTagsIDs == null -> false
                    sourcePictureTagsIDs?.isEmpty() == true && decodedPictureTagsIDs?.isEmpty() == true -> true
                    sourcePictureTagsIDs!!.any { it !in decodedPictureTagsIDs!! } -> false
                    decodedPictureTagsIDs!!.any { it !in sourcePictureTagsIDs } -> false
                    else -> true
                }
                assert(isDecodedPictureCorrectByTagsIDs)


                val isDecodedPictureTagsCorrectByEnv = decodedPicture.tags?.all { decodedPictureTag ->
                    val tagModel = testEnv.modelsStorage.tags.findByID(decodedPictureTag.id)
                    val decodedPictureTagExceptedTranslation = tagModel?.translations?.findByLangID(envID)
                    decodedPictureTag.name == decodedPictureTagExceptedTranslation?.name
                } ?: true
                assert(isDecodedPictureTagsCorrectByEnv)


                val isDecodedPictureCorrectByAvailableLangEnv =
                    decodedPicture.availableLanguages?.all { decodedPictureAvailableLang ->
                        val langModel = testEnv.modelsStorage.languages.findByLangID(decodedPictureAvailableLang.id)
                        val decodedPictureLangExceptedTranslation = langModel?.translations?.findByLangID(envID)
                        decodedPictureAvailableLang.name == decodedPictureLangExceptedTranslation?.name
                    } ?: true
                assert(isDecodedPictureCorrectByAvailableLangEnv)


                val sourcePictureCommentsNumber =
                    testEnv.modelsStorage.comments.countCommentsByContentID(pictureRegisterID)
                val decodedPictureCommentsNumber = decodedPicture.commentsCount.toInt()
                val isDecodedPictureCorrectByComments = sourcePictureCommentsNumber == decodedPictureCommentsNumber
                assert(isDecodedPictureCorrectByComments)
            }
        }
    }

    private suspend fun HttpClient.makePictureByIDRequest(pictureID: String?, environmentID: String?) =
        get(Routes.PictureByID.path) {
            url {
                appendToParameters(pictureID, Routes.PictureByID.getPictureIDArgName())
                appendToParameters(environmentID, Routes.PictureByID.getEnvLangIDArgName())
            }
        }
}