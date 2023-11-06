package rockyouapi.data

import common.utils.generateRandomTextByUUID
import rockyouapi.route.picture.pictureReadByIDRoute
import java.util.stream.Stream

/** Model of data to test [pictureReadByIDRoute] where no data result expected. */
internal data class PictureReadByIDRouteTSimpleTestData(
    val pictureID: String?,
    val environmentLangID: String?
)

/** Data creator to test [pictureReadByIDRoute].*/
internal object PictureReadByIDRouteTestDataStreamCreator {

    /** Data to test [pictureReadByIDRoute] with invalid arguments. */
    @JvmStatic
    fun invalidArgumentsTestData(): Stream<PictureReadByIDRouteTSimpleTestData> = Stream.of(
        PictureReadByIDRouteTSimpleTestData(
            pictureID = null,
            environmentLangID = null,
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "-1.1",
            environmentLangID = null
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1.1",
            environmentLangID = null,
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "Hello",
            environmentLangID = null,
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = generateRandomTextByUUID(),
            environmentLangID = null,
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "999999999999999999999999999999999999999999999",
            environmentLangID = null,
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1",
            environmentLangID = "-1.1",
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1",
            environmentLangID = "1.1",
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1",
            environmentLangID = "null",
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1",
            environmentLangID = "Hi!",
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1",
            environmentLangID = "))",
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1",
            environmentLangID = generateRandomTextByUUID(),
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "1",
            environmentLangID = "999999999999999999999999999999999",
        ),
    )

    /** Data to test [pictureReadByIDRoute] with [PictureReadByIDRouteTSimpleTestData.pictureID] linked to not existed content. */
    @JvmStatic
    fun nonExistedArgumentsTestData(): Stream<PictureReadByIDRouteTSimpleTestData> = Stream.of(
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "0",
            environmentLangID = "1",
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "99999",
            environmentLangID = "1"
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "0",
            environmentLangID = "10",
        ),
        PictureReadByIDRouteTSimpleTestData(
            pictureID = "99999",
            environmentLangID = "10"
        ),
    )
}