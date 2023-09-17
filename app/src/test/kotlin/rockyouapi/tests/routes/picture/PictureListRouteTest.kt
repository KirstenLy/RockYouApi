package rockyouapi.tests.routes.picture

import declaration.entity.Picture
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.*
import rockyouapi.route.Routes
import kotlin.test.Test

/** @see rockyouapi.route.picture.pictureListRoute */
internal class PictureListRouteTest {

    @Test
    fun pictures_list_without_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_all_invalid_arguments_must_return_400() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun pictures_list_without_limit_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = "1",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = "1",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_without_limit_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = null,
                langID = "Broken",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_limit_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_limit_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_limit_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_limit_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_limit_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_limit_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = "1",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_limit_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "Broken",
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = "1",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }


    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = "1",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_limit_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                limit = "-1",
                langID = "Broken",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = null,
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = "1",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = "Broken",
                offset = null
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = null,
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = null,
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = "1",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = "Broken",
                offset = "1"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_lang_id_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                langID = "Broken",
                limit = "Broken",
                offset = "Broken"
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = null,
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = "1",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = "Broken",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = null,
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = null,
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = "1",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = "Broken",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = "1",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_invalid_offset_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "Broken",
                langID = "Broken",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = null,
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = "1",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = "Broken",
                limit = null,
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = null,
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = null,
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = "1",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_7() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = "Broken",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_8() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = "1",
                limit = "Broken",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_negative_offset_must_return_400_test_9() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makePictureListRequest(
                offset = "-1",
                langID = "Broken",
                limit = "1",
            )

            assert(response.status == HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun pictures_list_with_correct_limit_only_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testModelsContainer ->
//            val limits = listOf("10", "30", "40", "50", "500", "1")
            val limits = listOf("10")

            val testPictures = testModelsContainer.extractContentAsPicturesMap()
            val firstPictures = testModelsContainer.extractFirstPictures()
            limits.forEach { limit ->
                val response = client.makePictureListRequest(
                    langID = null,
                    limit = limit,
                    offset = null
                )

                val actualResponseCode = response.status
                assert(actualResponseCode == HttpStatusCode.OK)

                val decodedResponse = response.decodeAs<List<Picture>>()
                assert(decodedResponse.size == limit.toInt())
                decodedResponse.forEachIndexed { index, picture ->
                    val expectedPictureModel = firstPictures[index]
                    val expectedPictureRegisterID = expectedPictureModel.first
                    val responsePictureID = picture.id
                    val isPictureValidByID = expectedPictureRegisterID == responsePictureID
                    val isPictureValidByTitle = expectedPictureModel.second.title == picture.title

                    println(
                        "Idx: $index, Decoded picture: $picture, " +
                                "ExpectedPictureRegisterID: $expectedPictureRegisterID " +
                                "ExpectedPicture: $expectedPictureModel"
                    )

                    assert(isPictureValidByID) { "Response picture invalid by id! Expected: $expectedPictureRegisterID, Actual: $responsePictureID" }
                    assert(isPictureValidByTitle)
                }
            }
        }
//        val contentRegisters = generateContentRegisters()
//        val testPictures = generateTestPictures()
//
//        runTestInConfiguredApplicationWithDBConnection(
//            dataBuilder = {
//                setLanguages(getDefaultLanguages())
//                setUsers(getDefaultUsers())
//                setContentRegisters(contentRegisters)
//                setPictures(testPictures)
//                build()
//            }
//        ) {
//            // ContentID generated in [1..40] interval to simulate database relationship correctly(no 0 index into database).
//            // So, for example, if register same: ContentType: 1, contentID: 15, test must find picture with real 15 order.
//            // It's 14 on the generated picture list.
//            val expectedPictures = contentRegisters
//                .asSequence()
//                .filter { it.contentType == 1 }
//                .map { testPictures[it.contentID - 1] }
//                .toList()
//
//            val limits = listOf("10", "30", "40", "1")
//            limits.forEach { limit ->
//                val response = client.makePictureListRequest(
//                    langID = null,
//                    limit = limit,
//                    offset = null
//                )
//                val actualResponseCode = response.status
//                assert(actualResponseCode == HttpStatusCode.OK)
//
//                val decodedResponse = Json.decodeFromString<List<Picture>>(response.bodyAsText())
//                assert(decodedResponse.size == limit.toInt())
//                decodedResponse.forEachIndexed { index, picture ->
//                    println("Idx: $index, Decoded picture: $picture, TestPicture: ${testPictures[index]}")
//                    assert(picture.title == expectedPictures[index].title)
//                }
//            }
//        }
    }

    @Test
    fun pictures_list_offset_work_correct() {
//        val contentRegisters = generateContentRegisters()
//        val testPictures = generateTestPictures()
//
//        runTestInConfiguredApplicationWithDBConnection(
//            dataBuilder = {
//                setLanguages(getDefaultLanguages())
//                setUsers(getDefaultUsers())
//                setContentRegisters(contentRegisters)
//                setPictures(testPictures)
//                build()
//            }
//        ) {
//            // Database model count IDs from 1, no 0 index into database.
//            // This variable present positions in DB, so shift it on 1 to move from [0…n] to [1…n+1] as DB indexes.
//            val picturesPositions = contentRegisters
//                .indexesOf { it.contentType == 1 }
//                .offsetAll(1)
//
//            assert(picturesPositions.size == 40)
//
//            val offsets = listOf("0", "10", "20", "30", "40", "50")
//            offsets.forEach { offset ->
//                val response = client.makePictureListRequest(
//                    langID = null,
//                    limit = "10",
//                    offset = offset
//                )
//                val actualResponseCode = response.status
//                assert(actualResponseCode == HttpStatusCode.OK)
//
//                val decodedResponse = Json.decodeFromString<List<Picture>>(response.bodyAsText())
//
//                val isResponseMustBeEmpty = (40 - offset.toInt()) <= 0
//                if (isResponseMustBeEmpty) {
//                    assert(decodedResponse.isEmpty())
//                    return@forEach
//                }
//
//                val firstPictureFromResponse = decodedResponse.first()
//                assert(firstPictureFromResponse.id == picturesPositions[offset.toInt()])
//            }
//        }
    }

    @Test
    fun pictures_list_lang_id_work_correct() {
//        val testPictures = generateTestPictures()
//
//        runTestInConfiguredApplicationWithDBConnection(
//            dataBuilder = {
//                setLanguages(getDefaultLanguages())
//                setUsers(getDefaultUsers())
//                setContentRegisters(generateContentRegisters())
//                setPictures(testPictures)
//                build()
//            }
//        ) {
//            val langIDs = listOf(1, 2, 3, 4)
//            langIDs.forEach { langID ->
//                val response = client.makePictureListRequest(
//                    langID = langID.toString(),
//                    limit = "40",
//                    offset = null
//                )
//                val actualResponseCode = response.status
//                assert(actualResponseCode == HttpStatusCode.OK)
//
//                val decodedResponse = Json.decodeFromString<List<Picture>>(response.bodyAsText())
//
//                val picturesWithLangIDCount = testPictures.count { it.languageID == langID }
//                assert(decodedResponse.count() == picturesWithLangIDCount)
//            }
//        }
    }

    private suspend fun HttpClient.makePictureListRequest(langID: String?, limit: String?, offset: String?) =
        get(Routes.PictureList.path) {
            url {
                appendToParameters(langID, Routes.PictureList.getLangIDArgName())
                appendToParameters(limit, Routes.PictureList.getLimitArgName())
                appendToParameters(offset, Routes.PictureList.getOffsetArgName())
            }
        }
}