package rockyouapi.tests.routes.report

import common.takeRandomValues
import database.external.test.TestContentRegister
import database.external.result.SimpleListResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import rockyouapi.appendToParameters
import rockyouapi.getRandomUserWithPassword
import rockyouapi.route.Routes
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import rockyouapi.tests.routes.makeAuthLoginRequestWithDecodedResponse
import java.util.UUID
import kotlin.test.Test
import kotlin.test.fail

/** @see rockyouapi.route.favorite.addOrRemoveFavoriteRoute */
internal class ReportTest {

    @Test
    fun report_without_content_id_return_400_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = null,
                report = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_content_id_return_400_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = null,
                report = "null",
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_content_id_return_400_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = null,
                report = UUID.randomUUID().toString(),
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_content_id_return_400_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = null,
                report = "-324223",
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_report_text_return_400_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = null,
                report = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_report_text_return_400_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = "1",
                report = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_report_text_return_400_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = "111111",
                report = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_report_text_return_400_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = UUID.randomUUID().toString(),
                report = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_report_text_return_400_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeReportRequest(
                contentID = "Report",
                report = null,
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun report_without_auth_and_with_correct_credentials_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val contentIDs = testEnv.modelsStorage
                .contentRegisters
                .takeRandomValues()
                .map(TestContentRegister::id)

            contentIDs.forEach { contentID ->
                val report = UUID.randomUUID().toString()
                val response = client.makeReportRequest(
                    contentID = contentID.toString(),
                    report = report,
                )

                assert(response.status == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${response.status}"
                }

                when (val getAllReportsForContentResult = testEnv.testDBApi.readAllReportsForContent(contentID)) {
                    is SimpleListResult.Data -> {
                        val lastInsertedReport = getAllReportsForContentResult.data
                            .lastOrNull()
                            ?: fail("No reports after insert was completed")

                        val isLastInsertedReportAsExpected = lastInsertedReport.reportText == report
                                && lastInsertedReport.userID == null
                                && lastInsertedReport.isClosed == false
                        assert(isLastInsertedReportAsExpected) { "Last inserted report is not as expected" }
                    }

                    is SimpleListResult.Error -> fail(getAllReportsForContentResult.t.message)
                }
            }
        }
    }

    @Test
    fun report_with_auth_and_with_correct_credentials_work_correct() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val userWithPassword = testEnv.getRandomUserWithPassword()
            val authResponse = client.makeAuthLoginRequestWithDecodedResponse(
                login = userWithPassword.name,
                password = userWithPassword.password,
                onFinishedByError = {
                    fail("Expected response status: ${HttpStatusCode.OK}, Actual response status: $it")
                }
            )

            val contentIDs = testEnv.modelsStorage
                .contentRegisters
                .takeRandomValues()
                .map(TestContentRegister::id)

            contentIDs.forEach { contentID ->
                val report = UUID.randomUUID().toString()
                val response = client.makeReportRequest(
                    contentID = contentID.toString(),
                    report = report,
                    token = authResponse.token
                )

                assert(response.status == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${response.status}"
                }

                when (val getAllReportsForContentResult = testEnv.testDBApi.readAllReportsForContent(contentID)) {
                    is SimpleListResult.Data -> {
                        val lastInsertedReport = getAllReportsForContentResult.data
                            .lastOrNull()
                            ?: fail("No reports after insert was completed")

                        val isLastInsertedReportAsExpected = lastInsertedReport.reportText == report
                                && lastInsertedReport.userID == userWithPassword.id
                                && lastInsertedReport.isClosed == false
                        assert(isLastInsertedReportAsExpected) { "Last inserted report is not as expected" }
                    }

                    is SimpleListResult.Error -> fail(getAllReportsForContentResult.t.message)
                }
            }
        }
    }

    private suspend fun HttpClient.makeReportRequest(
        contentID: String?,
        report: String?,
        token: String? = null
    ): HttpResponse {
        return post(Routes.Report.path) {
            token?.let(::bearerAuth)
            url {
                appendToParameters(contentID, Routes.Report.getContentIDArgName())
                appendToParameters(report, Routes.Report.getReportIDArgName())
            }
        }
    }
}