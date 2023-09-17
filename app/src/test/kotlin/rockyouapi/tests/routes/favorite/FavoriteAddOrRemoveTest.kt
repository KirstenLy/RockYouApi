package rockyouapi.tests.routes.favorite

import common.takeRandomValues
import database.external.test.TestContentRegister
import database.external.test.TestFavorite
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.appendToParameters
import rockyouapi.getRandomUserWithPassword
import rockyouapi.operation.FavoriteOperation
import rockyouapi.route.Routes
import rockyouapi.runTestInConfiguredApplicationWithDBFullFilledFromScratch
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import rockyouapi.tests.routes.makeAuthLoginRequestWithDecodedResponse
import java.util.UUID
import kotlin.test.Test
import kotlin.test.fail

/** @see rockyouapi.route.favorite.addOrRemoveFavoriteRoute */
internal class FavoriteAddOrRemoveTest {

    @Test
    fun add_or_remove_favorite_without_token_return_401_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = null,
                contentID = null,
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_without_token_return_401_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "1",
                contentID = null,
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_without_token_return_401_test_3() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = null,
                contentID = "1",
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_without_token_return_401_test_4() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "1",
                contentID = "1",
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_without_token_return_401_test_5() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "dsdsfdsfds",
                contentID = null,
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_without_token_return_401_test_6() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = null,
                contentID = "dsfdsfds",
                token = null
            )

            assert(response.status == HttpStatusCode.Unauthorized) {
                "Expected response status: ${HttpStatusCode.Unauthorized}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_operation_type_return_400_test_0() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = null,
                contentID = null,
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_operation_type_return_400_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "-1",
                contentID = null,
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_operation_type_return_400_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "555",
                contentID = null,
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_operation_type_return_400_test_3() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "0.5",
                contentID = null,
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_operation_type_return_400_test_4() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "0.5",
                contentID = "1",
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_operation_type_return_400_test_5() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "0.5",
                contentID = "-1",
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_operation_type_return_400_test_6() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "-1",
                contentID = "dsssssssssssssssss",
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_content_id_return_400_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = null,
                contentID = "Broken",
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_content_id_return_400_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = null,
                contentID = UUID.randomUUID().toString(),
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_content_id_return_400_test_3() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "1",
                contentID = "Broken",
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_or_remove_favorite_with_token_and_invalid_content_id_return_400_test_4() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val response = client.makeAddOrRemoveFavoriteRequest(
                operationType = "0.5",
                contentID = "Broken",
                token = token
            )

            assert(response.status == HttpStatusCode.BadRequest) {
                "Expected response status: ${HttpStatusCode.BadRequest}, Actual response status: ${response.status}"
            }
        }
    }

    @Test
    fun add_favorite_with_token_and_valid_args_work_correct_test_1() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val allContentIDs = testEnv.modelsStorage.contentRegisters.map(TestContentRegister::id)
            val contentIDsWithFavoriteRecords = testEnv.modelsStorage.favorites.map(TestFavorite::contentID)
            val contentIDsWithoutFavoriteRecords = allContentIDs - contentIDsWithFavoriteRecords.toSet()
            val contentIDsToInsertRecords = contentIDsWithoutFavoriteRecords.takeRandomValues()

            contentIDsToInsertRecords.forEach { contentID ->
                val responseToInsert = client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.ADD.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = token
                )

                assert(responseToInsert.status == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${responseToInsert.status}"
                }

                val responseToInsertAgain = client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.ADD.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = token
                )

                assert(responseToInsertAgain.status == HttpStatusCode.Conflict) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${responseToInsertAgain.status}"
                }
            }
        }
    }

    @Test
    fun add_favorite_with_token_and_valid_args_work_correct_test_2() {
        runTestInConfiguredApplicationWithDBFullFilledFromScratch { testEnv ->
            val user = testEnv.getRandomUserWithPassword()
            val token = client.makeAuthLoginRequestWithDecodedResponse(
                login = user.name,
                password = user.password,
                onFinishedByError = { fail("Failed to login, error code: $it") }
            ).token

            val contentIDsWithFavoriteRecordsForUser = testEnv.modelsStorage
                .favorites
                .filter { it.userID == user.id }
                .map(TestFavorite::contentID)
                .distinct()
            val contentIDsToRemoveRecords = contentIDsWithFavoriteRecordsForUser.takeRandomValues()

            contentIDsToRemoveRecords.forEach { contentID ->
                val responseToRemoveRecord = client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.REMOVE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = token
                )

                assert(responseToRemoveRecord.status == HttpStatusCode.OK) {
                    "Expected response status: ${HttpStatusCode.OK}, Actual response status: ${responseToRemoveRecord.status}"
                }

                val responseToRemoveRecordAgain = client.makeAddOrRemoveFavoriteRequest(
                    operationType = FavoriteOperation.REMOVE.operationArgument.toString(),
                    contentID = contentID.toString(),
                    token = token
                )

                assert(responseToRemoveRecordAgain.status == HttpStatusCode.Conflict) {
                    "Expected response status: ${HttpStatusCode.Conflict}, Actual response status: ${responseToRemoveRecordAgain.status}"
                }
            }
        }
    }

    private suspend fun HttpClient.makeAddOrRemoveFavoriteRequest(
        operationType: String?,
        contentID: String?,
        token: String?
    ) = post(Routes.FavoriteAddOrRemove.path) {
        token?.let(::bearerAuth)
        url {
            appendToParameters(operationType, Routes.FavoriteAddOrRemove.getOperationTypeArgName())
            appendToParameters(contentID, Routes.FavoriteAddOrRemove.getContentIDArgName())
        }
    }
}