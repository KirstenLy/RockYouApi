package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.generateRandomTextByUUID
import common.utils.zeroOnNull
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.filter.StoryListFilter
import rockyouapi.model.story.Story
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.arguments.ListMethodArguments
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.story.storyListRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class StoryListRouteTest {

    private fun invalidArguments() = Stream.of(
        ListMethodArguments(null, null, null, null),
        ListMethodArguments(null, null, "null", "null"),
        ListMethodArguments(null, null, generateRandomTextByUUID(), "1"),
        ListMethodArguments(null, null, "", "111"),
        ListMethodArguments(null, null, "    ", null),
        ListMethodArguments(null, "null", "null", "-1"),
        ListMethodArguments(null, "", "null", generateRandomTextByUUID()),
        ListMethodArguments(null, "    ", "null", null),
        ListMethodArguments(null, generateRandomTextByUUID(), generateRandomTextByUUID(), "1"),
        ListMethodArguments(" ", "  ", " ", "  "),
        ListMethodArguments(
            generateRandomTextByUUID(),
            generateRandomTextByUUID(),
            generateRandomTextByUUID(),
            generateRandomTextByUUID()
        ),
        ListMethodArguments("1", generateRandomTextByUUID(), "1", "1"),
        ListMethodArguments("1", "1", "", "1"),
        ListMethodArguments("1", "1", "   ", "1"),
        ListMethodArguments("", "1", "1", "1"),
        ListMethodArguments("  ", "1", "1", "1"),
        ListMethodArguments("null", null, null, null),
        ListMethodArguments(generateRandomTextByUUID(), "1", null, "1"),
        ListMethodArguments("null", "1", generateRandomTextByUUID(), "1"),
        ListMethodArguments(null, "-1", null, null),
        ListMethodArguments("-1", generateRandomTextByUUID(), null, "1"),
        ListMethodArguments("-1", "null", "-1", "-1"),
    )

    private fun validArguments() = Stream.of(
        ListMethodArguments("1", null, "1", "1"),
        ListMethodArguments("10", null, "1", "2"),
        ListMethodArguments("1", "5", null, "3"),
        ListMethodArguments("10", "15", null, null),
        ListMethodArguments("7", "7", "7", null),
    )

    private val databaseAPI by lazy {
        runBlocking {
            getOrCreateValue(KEY_STATIC_MAP_DB) {
                connectToProductionDatabaseWithTestApi(readDatabaseConfigurationFromEnv())
            }
        }
    }

    private val productionDatabaseAPI get() = databaseAPI.first

    @ParameterizedTest
    @MethodSource("invalidArguments")
    fun stories_list_with_invalid_arguments_return_400(invalidArguments: ListMethodArguments) {
        runTestSimple {
            client.makeStoryListRequest(
                limit = invalidArguments.limit,
                langID = invalidArguments.languageID,
                offset = invalidArguments.offset,
                environmentID = invalidArguments.environmentID
            )
                .badRequestOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("validArguments")
    fun stories_list_with_correct_arguments_return_200_with_correct_stories(validArguments: ListMethodArguments) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            val actualStoryList = client.makeStoryListRequest(
                limit = validArguments.limit,
                langID = validArguments.languageID,
                offset = validArguments.offset,
                environmentID = validArguments.environmentID
            )
                .okOrFail()
                .decodeAs<BaseResponse<List<Story>>>()
                .data!!

            val expectedStoryList = productionDatabaseAPI.getStories(
                StoryListFilter(
                    languageIDList = emptyList(),
                    environmentLangID = validArguments.environmentID?.toInt(),
                    limit = validArguments.limit?.toLong().zeroOnNull(),
                    offset = validArguments.offset?.toLong().zeroOnNull()
                )
            )
                .extractDataOrFail()

            val actualStoryListSize = actualStoryList.size
            val expectedStoryListSize = expectedStoryList.size
            assertEquals(expectedStoryListSize, actualStoryListSize)

            actualStoryList.zip(expectedStoryList).assertAllLeftEqualsTheirRight()
        }
    }

    private suspend fun HttpClient.makeStoryListRequest(
        langID: String?,
        offset: String?,
        limit: String?,
        environmentID: String?,
    ) = get(Routes.StoriesList.path) {
        url {
            appendToParameters(langID, Routes.StoriesList.getLangIDListArgName())
            appendToParameters(offset, Routes.StoriesList.getOffsetArgName())
            appendToParameters(limit, Routes.StoriesList.getLimitArgName())
            appendToParameters(environmentID, Routes.StoriesList.getEnvironmentLangIDArgName())
        }
    }
}