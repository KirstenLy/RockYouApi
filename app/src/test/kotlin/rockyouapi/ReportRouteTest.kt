package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
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
import rockyouapi.responce.LoggedOrCreatedUserResponse
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.report.reportRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ReportRouteTest {

    private fun invalidArguments() = Stream.of(
        ReportArguments(null, null),
        ReportArguments(null, ""),
        ReportArguments(null, "    "),
        ReportArguments("1", null),
        ReportArguments("null", null),
        ReportArguments("-1", null),
        ReportArguments(generateRandomTextByUUID(), null),
        ReportArguments("1", ""),
        ReportArguments("1", "   "),
        ReportArguments("", generateRandomTextByUUID()),
        ReportArguments("  ", generateRandomTextByUUID()),
        ReportArguments(generateRandomTextByUUID(), generateRandomTextByUUID()),
        ReportArguments("-1", ""),
        ReportArguments("-1", "      "),
        ReportArguments("null", "null"),
    )

    private fun validArguments() = Stream.of(
        ReportArguments("1", generateRandomTextByUUID()),
        ReportArguments("-1", generateRandomTextByUUID()),
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
    fun report_without_token_and_with_invalid_arguments_return_400(invalidArguments: ReportArguments) {
        runTestSimple {
            client.makeReportRequest(
                contentID = invalidArguments.contentID,
                report = invalidArguments.report,
                token = null
            )
                .badRequestOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("validArguments")
    fun report_with_refresh_token_and_with_valid_arguments_return_400(validArguments: ReportArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeReportRequest(
                contentID = validArguments.contentID,
                report = validArguments.report,
                token = registerResponse.refreshToken
            )
                .unauthorizedOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun report_with_token_and_with_invalid_arguments_return_400(invalidArguments: ReportArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeReportRequest(
                contentID = invalidArguments.contentID,
                report = invalidArguments.report,
                token = registerResponse.accessToken
            )
                .badRequestOrFail()
        }
    }

    @RepeatedTest(10)
    fun report_without_token_and_with_not_existed_content_id_return_400() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            client.makeReportRequest(
                contentID = generateNotExistedContentID().toString(),
                report = generateRandomTextByUUID(),
                token = null
            )
                .badRequestOrFail()
        }
    }

    @RepeatedTest(10)
    fun report_with_token_and_with_not_existed_content_id_return_400() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            client.makeReportRequest(
                contentID = generateNotExistedContentID().toString(),
                report = generateRandomTextByUUID(),
                token = registerResponse.accessToken
            )
                .badRequestOrFail()
        }
    }

    @RepeatedTest(10)
    fun report_with_token_and_with_valid_arguments_return_200_and_insert_record_to_database_with_user_id() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val registerResponse = client.makeAuthRegisterRequest(
                login = generateRandomTextByUUID(),
                password = generateRandomTextByUUID()
            )
                .okOrFail()
                .decodeAs<BaseResponse<LoggedOrCreatedUserResponse>>()
                .data!!

            val randomContentID = testDatabaseAPI.getRandomContentID()
            val report = generateRandomTextByUUID()
            client.makeReportRequest(
                contentID = randomContentID.toString(),
                report = report,
                token = registerResponse.accessToken
            )
                .okOrFail()

            val insertedReportRecord = testDatabaseAPI.readAllReports().last()

            assertEquals(randomContentID, insertedReportRecord.contentID)
            assertEquals(registerResponse.user.id, insertedReportRecord.userID)
            assertEquals(report, insertedReportRecord.reportText)
            assertEquals(false, insertedReportRecord.isClosed)
        }
    }

    @RepeatedTest(10)
    fun report_without_token_and_with_valid_arguments_return_200_and_insert_record_to_database_without_user_id() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val randomContentID = testDatabaseAPI.getRandomContentID()
            val report = generateRandomTextByUUID()
            client.makeReportRequest(
                contentID = randomContentID.toString(),
                report = report,
                token = null
            )
                .okOrFail()

            val insertedReportRecord = testDatabaseAPI.readAllReports().last()

            assertEquals(randomContentID, insertedReportRecord.contentID)
            assertEquals(null, insertedReportRecord.userID)
            assertEquals(report, insertedReportRecord.reportText)
            assertEquals(false, insertedReportRecord.isClosed)
        }
    }

    internal class ReportArguments(
        val contentID: String?,
        val report: String?
    )

    private suspend fun HttpClient.makeReportRequest(
        contentID: String?,
        report: String?,
        token: String? = null
    ) = post(Routes.Report.path) {

        token?.let(::bearerAuth)

        setBody(FormDataContent(Parameters.build {
            contentID?.let { append(Routes.Report.getContentIDArgName(), contentID) }
            report?.let { append(Routes.Report.getReportTextArgName(), report) }
            build()
        }))

        url {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }
}