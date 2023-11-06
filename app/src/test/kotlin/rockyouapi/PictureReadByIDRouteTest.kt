package rockyouapi

import database.external.ContentType
import database.external.filter.PictureByIDFilter
import database.external.model.Picture
import io.ktor.client.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.base.DatabaseApiDelegate
import rockyouapi.base.DatabaseApiDelegateImpl
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.data.PictureReadByIDRouteTSimpleTestData
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*

/** @see rockyouapi.route.picture.pictureReadByIDRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PictureReadByIDRouteTest : DatabaseApiDelegate by DatabaseApiDelegateImpl() {

    @ParameterizedTest
    @MethodSource("rockyouapi.data.PictureReadByIDRouteTestDataStreamCreator#invalidArgumentsTestData")
    fun picture_by_id_with_invalid_arguments_return_400(testData: PictureReadByIDRouteTSimpleTestData) {
        runTestSimple {
            client.makePictureByIDRequest(
                pictureID = testData.pictureID,
                environmentID = testData.environmentLangID
            )
                .badRequestOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("rockyouapi.data.PictureReadByIDRouteTestDataStreamCreator#nonExistedArgumentsTestData")
    fun picture_by_id_with_non_existed_picture_id_return_404(testData: PictureReadByIDRouteTSimpleTestData) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            client.makePictureByIDRequest(
                pictureID = testData.pictureID,
                environmentID = testData.environmentLangID
            )
                .notFoundOrFail()
        }
    }

    @RepeatedTest(10)
    fun picture_by_id_with_existed_picture_id_return_200_with_correct_picture() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val randomPictureID = testDatabaseAPI.getRandomContentIDForEntity(ContentType.PICTURE)
            val validEnvironmentLanguageIDList = testDatabaseAPI.getAllSupportedLanguageID()
            val invalidEnvironmentLanguageIDList = testDatabaseAPI.getNotExistedSupportedLanguageIDList()
            val randomEnvironmentLanguageID = validEnvironmentLanguageIDList
                .plus(invalidEnvironmentLanguageIDList)
                .random()

            val actualPicture = client.makePictureByIDRequest(
                pictureID = randomPictureID.toString(),
                environmentID = randomEnvironmentLanguageID?.toString()
            )
                .okOrFail()
                .decodeAs<BaseResponse<Picture>>()
                .data

            val expectedPictureFilter = PictureByIDFilter(
                pictureID = randomPictureID,
                environmentLangID = randomEnvironmentLanguageID
            )
            val expectedPicture = productionDatabaseAPI
                .getPictureByID(expectedPictureFilter)
                .extractModelOrFail()

            assertEquals(expectedPicture, actualPicture)
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