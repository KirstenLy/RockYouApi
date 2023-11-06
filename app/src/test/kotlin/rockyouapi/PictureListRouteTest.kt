package rockyouapi

import database.external.filter.PictureListFilter
import database.external.model.Picture
import io.ktor.client.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import rockyouapi.arguments.*
import rockyouapi.base.DatabaseApiDelegate
import rockyouapi.base.DatabaseApiDelegateImpl
import rockyouapi.base.runTest
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.test.runTest as createTestCoroutineScope

/** @see rockyouapi.route.picture.pictureListRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
internal class PictureListRouteTest : DatabaseApiDelegate by DatabaseApiDelegateImpl() {

    @Test
    fun execute_with_args_1() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            createTestCoroutineScope(timeout = 60.minutes) {
                runTestCore(
                    client = client,
                    limitArguments = limitArguments1,
                    offsetArguments = offsetArguments1,
                    environmentLangIDArguments = environmentIDArguments1,
                    searchTextArguments = searchTextArgumentsPicture,
                    languageIDList = langIDListArguments1,
                    authorIDArguments = authorIDListArguments1,
                    tagIDArguments = tagIDListArguments1,
                    userIDArguments = userIDListArguments1,
                )
            }
        }
    }

    @Test
    fun execute_with_args_2() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            createTestCoroutineScope(timeout = 60.minutes) {
                runTestCore(
                    client = client,
                    limitArguments = limitArguments2,
                    offsetArguments = offsetArguments2,
                    environmentLangIDArguments = environmentIDArguments2,
                    searchTextArguments = searchTextArgumentsPicture,
                    languageIDList = langIDListArguments2,
                    authorIDArguments = authorIDListArguments2,
                    tagIDArguments = tagIDListArguments2,
                    userIDArguments = userIDListArguments2,
                )
            }
        }
    }

    @Test
    fun execute_with_args_3() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            createTestCoroutineScope(timeout = 60.minutes) {
                runTestCore(
                    client = client,
                    limitArguments = limitArguments3,
                    offsetArguments = offsetArguments3,
                    environmentLangIDArguments = environmentIDArguments3,
                    searchTextArguments = searchTextArgumentsPicture,
                    languageIDList = langIDListArguments3,
                    authorIDArguments = authorIDListArguments3,
                    tagIDArguments = tagIDListArguments3,
                    userIDArguments = userIDListArguments3,
                )
            }
        }
    }

    @Test
    fun execute_with_args_4() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            createTestCoroutineScope(timeout = 60.minutes) {
                runTestCore(
                    client = client,
                    limitArguments = limitArguments4,
                    offsetArguments = offsetArguments4,
                    environmentLangIDArguments = environmentIDArguments4,
                    searchTextArguments = searchTextArgumentsPicture,
                    languageIDList = langIDListArguments4,
                    authorIDArguments = authorIDListArguments4,
                    tagIDArguments = tagIDListArguments4,
                    userIDArguments = userIDListArguments4,
                )
            }
        }
    }

    @Test
    fun execute_with_args_5() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            createTestCoroutineScope(timeout = 60.minutes) {
                runTestCore(
                    client = client,
                    limitArguments = limitArguments5,
                    offsetArguments = offsetArguments5,
                    environmentLangIDArguments = environmentIDArguments5,
                    searchTextArguments = searchTextArgumentsPicture,
                    languageIDList = langIDListArguments5,
                    authorIDArguments = authorIDListArguments5,
                    tagIDArguments = tagIDListArguments5,
                    userIDArguments = userIDListArguments5,
                )
            }
        }
    }

    @Test
    fun execute_with_args_6() {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            createTestCoroutineScope(timeout = 60.minutes) {
                runTestCore(
                    client = client,
                    limitArguments = limitArguments6,
                    offsetArguments = offsetArguments6,
                    environmentLangIDArguments = environmentIDArguments6,
                    searchTextArguments = searchTextArgumentsPicture,
                    languageIDList = langIDListArguments6,
                    authorIDArguments = authorIDListArguments6,
                    tagIDArguments = tagIDListArguments6,
                    userIDArguments = userIDListArguments6,
                )
            }
        }
    }

    private suspend fun runTestCore(
        client: HttpClient,
        limitArguments: List<String?>,
        offsetArguments: List<String?>,
        environmentLangIDArguments: List<String?>,
        searchTextArguments: List<String?>,
        languageIDList: List<String?> = emptyList(),
        authorIDArguments: List<String?> = emptyList(),
        tagIDArguments: List<String?> = emptyList(),
        userIDArguments: List<String?> = emptyList()
    ) {
        limitArguments.forEach { limit ->

            offsetArguments.forEach { offset ->

                environmentLangIDArguments.forEach { environmentID ->

                    languageIDList.forEach { langIDList ->

                        authorIDArguments.forEach { authorIDList ->

                            tagIDArguments.forEach { tagIDList ->

                                userIDArguments.forEach { userIDList ->

                                    searchTextArguments.forEach closestCycle@{ searchText ->

                                        fun buildArgumentString() = buildString {
                                            append("Limit: $limit")
                                            appendLine()
                                            append("Offset: $offset")
                                            appendLine()
                                            append("Env: $environmentID")
                                            appendLine()
                                            append("Search text: $searchText")
                                            appendLine()
                                            append("Lang: $langIDList")
                                            appendLine()
                                            append("Author: $authorIDList")
                                            appendLine()
                                            append("Tag: $tagIDList")
                                            appendLine()
                                            append("User: $userIDList")
                                            appendLine()
                                        }

                                        val actualPictureListResponse = client.makePictureListRequest(
                                            limit = limit,
                                            offset = offset,
                                            environmentID = environmentID,
                                            searchText = searchText,
                                            langIDList = langIDList,
                                            authorIDList = authorIDList,
                                            tagIDList = tagIDList,
                                            userIDList = userIDList,
                                        )

                                        val isLimitArgumentValid = limit.isNonNegativeInt()
                                        val isOffsetArgumentValid = offset.isNullOrNonNegativeInt()
                                        val isEnvironmentIDValid = environmentID.isNullOrInt()
                                        val isSearchTextValid = searchText == null || searchText.isNotBlank()
                                        val isLangIDListValid = langIDList.isNullOrJsonOf<List<Int>>()
                                        val isAuthorIDListValid = authorIDList.isNullOrJsonOf<List<Int>>()
                                        val isTagIDListValid = tagIDList.isNullOrJsonOf<List<Int>>()
                                        val isUserIDListValid = userIDList.isNullOrJsonOf<List<Int>>()

                                        if (
                                            !isLimitArgumentValid
                                            || !isOffsetArgumentValid
                                            || !isEnvironmentIDValid
                                            || !isSearchTextValid
                                            || !isLangIDListValid
                                            || !isAuthorIDListValid
                                            || !isTagIDListValid
                                            || !isUserIDListValid
                                        ) {

                                            actualPictureListResponse.badRequestOrFail(buildArgumentString())
                                            return@closestCycle
                                        }

                                        val actualPictureList = actualPictureListResponse
                                            .okOrFail(buildArgumentString())
                                            .decodeAs<BaseResponse<List<Picture>>>()
                                            .data!!

                                        val filter = PictureListFilter(
                                            limit = limit?.toLong() ?: 0L,
                                            offset = offset?.toLong() ?: 0L,
                                            environmentLangID = environmentID?.toInt(),
                                            searchText = searchText,
                                            languageIDList = langIDList?.decodeAs<List<Int>>().orEmpty(),
                                            authorIDList = authorIDList?.decodeAs<List<Int>>().orEmpty(),
                                            includedTagIDList = tagIDList?.decodeAs<List<Int>>().orEmpty(),
                                            userIDList = userIDList?.decodeAs<List<Int>>().orEmpty()
                                        )

                                        val expectedPictureList = productionDatabaseAPI
                                            .getPictures(filter)
                                            .extractDataOrFail()

                                        val actualPictureListSize = actualPictureList.size
                                        val expectedPictureListSize = expectedPictureList.size
                                        assertEquals(expectedPictureListSize, actualPictureListSize, buildArgumentString())

                                        actualPictureList
                                            .zip(expectedPictureList)
                                            .forEach { (actual, expected) -> assertEquals(actual, expected, buildArgumentString()) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun HttpClient.makePictureListRequest(
        limit: String?,
        offset: String?,
        environmentID: String?,
        searchText: String?,
        langIDList: String?,
        authorIDList: String?,
        tagIDList: String?,
        userIDList: String?,
    ) = get(Routes.PictureList.path) {
        url {
            appendToParameters(limit, Routes.PictureList.getLimitArgName())
            appendToParameters(offset, Routes.PictureList.getOffsetArgName())
            appendToParameters(environmentID, Routes.PictureList.getEnvironmentLangIDArgName())
            appendToParameters(searchText, Routes.PictureList.getSearchTextArgName())
            appendToParameters(langIDList, Routes.PictureList.getLangIDListArgName())
            appendToParameters(authorIDList, Routes.PictureList.getAuthorIDListArgName())
            appendToParameters(tagIDList, Routes.PictureList.getTagIDListArgName())
            appendToParameters(userIDList, Routes.PictureList.getUserIDListArgName())
        }
    }
}