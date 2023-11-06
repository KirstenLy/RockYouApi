package rockyouapi

import common.storage.StaticMapStorage.getOrCreateValue
import common.utils.zeroOnNull
import database.external.DatabaseFeature.connectToProductionDatabaseWithTestApi
import database.external.filter.CommentListFilter
import database.external.model.Comment
import database.external.reader.readDatabaseConfigurationFromEnv
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import rockyouapi.base.KEY_STATIC_MAP_DB
import rockyouapi.base.runTest
import rockyouapi.base.runTestSimple
import rockyouapi.responce.BaseResponse
import rockyouapi.route.Routes
import rockyouapi.utils.*
import java.util.stream.Stream

/** @see rockyouapi.route.comment.commentListRoute */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentListRouteTest {

    private fun invalidArguments() = Stream.of(
        CommentListArgumentsFull(null, null, null),
        CommentListArgumentsFull("1", null, null),
        CommentListArgumentsFull("null", null, null),
        CommentListArgumentsFull("", null, null),
        CommentListArgumentsFull("   ", null, null),
        CommentListArgumentsFull(null, "1", null),
        CommentListArgumentsFull(null, "null", null),
        CommentListArgumentsFull(null, "", null),
        CommentListArgumentsFull(null, "   ", null),
        CommentListArgumentsFull(null, null, "1"),
        CommentListArgumentsFull(null, null, "null"),
        CommentListArgumentsFull(null, null, ""),
        CommentListArgumentsFull(null, null, "  "),
        CommentListArgumentsFull("1", null, "1"),
        CommentListArgumentsFull("null", null, "null"),
        CommentListArgumentsFull("", null, ""),
        CommentListArgumentsFull(" ", null, " "),
        CommentListArgumentsFull("1", "null", null),
        CommentListArgumentsFull("1", "1", "null"),
        CommentListArgumentsFull("1", "1", ""),
        CommentListArgumentsFull("1", "1", "  "),
        CommentListArgumentsFull("1", "", "1"),
        CommentListArgumentsFull("1", "  ", "1"),
        CommentListArgumentsFull("1", "null", "1"),
        CommentListArgumentsFull("-1", null, null),
        CommentListArgumentsFull(null, "-1", null),
        CommentListArgumentsFull("-1", "-1", null),
        CommentListArgumentsFull(null, "-1", "-1"),
        CommentListArgumentsFull(null, null, "-1"),
    )

    private fun validArguments() = Stream.of(
        CommentListArgumentsSmall("1", "1"),
        CommentListArgumentsSmall("10", "10"),
        CommentListArgumentsSmall("100", "100"),
        CommentListArgumentsSmall("1", "10"),
        CommentListArgumentsSmall("10", "1"),
        CommentListArgumentsSmall("5", "15"),
        CommentListArgumentsSmall("15", "5"),
        CommentListArgumentsSmall("7", "14"),
        CommentListArgumentsSmall("14", "7"),
        CommentListArgumentsSmall("5", null),
        CommentListArgumentsSmall("15", null),
        CommentListArgumentsSmall("7", "0"),
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
    fun comments_list_with_invalid_arguments_must_return_400(invalidArgument: CommentListArgumentsFull) {
        runTestSimple {
            client.makeCommentListRequest(
                contentID = invalidArgument.contentID,
                limit = invalidArgument.limit,
                offset = invalidArgument.offset,
            )
                .badRequestOrFail()
        }
    }

    @ParameterizedTest
    @MethodSource("validArguments")
    fun comments_list_with_valid_arguments_return_200_with_correct_comments(validArgument: CommentListArgumentsSmall) {
        runTest(productionDatabaseAPI = productionDatabaseAPI) {
            testDatabaseAPI.getRandomContentIDList(10).forEach { contentID ->
                val actualCommentList = client.makeCommentListRequest(
                    contentID = contentID.toString(),
                    limit = validArgument.limit,
                    offset = validArgument.offset,
                )
                    .okOrFail()
                    .decodeAs<BaseResponse<List<Comment>>>()
                    .data!!

                val expectedCommentList = productionDatabaseAPI.getComments(
                    CommentListFilter(
                        contentID = contentID,
                        limit = validArgument.limit.toLong(),
                        offset = validArgument.offset?.toLong().zeroOnNull()
                    )
                )
                    .extractDataOrFail()

                val actualCommentsCount = actualCommentList.size
                val expectedCommentsCount = expectedCommentList.size
                assertEquals(expectedCommentsCount, actualCommentsCount)

                val isAllActualCommentsBelongToContent = actualCommentList.all { it.contentID == contentID }
                assert(isAllActualCommentsBelongToContent)

                actualCommentList
                    .zip(expectedCommentList)
                    .assertAllLeftEqualsTheirRight()
            }
        }
    }

    internal class CommentListArgumentsFull(
        val contentID: String?,
        val limit: String?,
        val offset: String?,
    )

    internal class CommentListArgumentsSmall(
        val limit: String,
        val offset: String?,
    )

    private suspend fun HttpClient.makeCommentListRequest(
        contentID: String?,
        limit: String?,
        offset: String?,
    ) = get(Routes.CommentList.path) {
        url {
            appendToParameters(contentID, Routes.CommentList.getContentIDArgName())
            appendToParameters(limit, Routes.CommentList.getContentLimitArgName())
            appendToParameters(offset, Routes.CommentList.getContentOffsetArgName())
        }
    }
}