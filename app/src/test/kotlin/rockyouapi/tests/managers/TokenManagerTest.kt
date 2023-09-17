package rockyouapi.tests.managers

import rockyouapi.TIME_TEST_TOKEN_LIFETIME_IN_SEC
import rockyouapi.auth.UserTokensManager
import rockyouapi.delayByTokenLifetimeAndOneSecAhead
import rockyouapi.runTestInConfiguredApplicationWithoutDBConnection
import kotlin.random.Random
import kotlin.test.Test

/** @see rockyouapi.route.comment.commentListRoute */
internal class TokenManagerTest {

    @Test
    fun token_expiration_work_correct_test_1() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val manager = UserTokensManager(TIME_TEST_TOKEN_LIFETIME_IN_SEC)

            val tokenOwner = Random.nextInt()
            val token = manager.putUserToken(tokenOwner)
            val isTokenValid = manager.isTokenExistAndValid(token)
            assert(isTokenValid) { "Failed to remember user token" }

            val tokenOwnerFromManager = manager.getTokenOwnerID(token)
            assert(tokenOwnerFromManager == tokenOwner) { "Failed to link user and his token" }

            delayByTokenLifetimeAndOneSecAhead()
            val isTokenInvalid = manager.isTokenExistAndValid(token).not()
            assert(isTokenInvalid) { "Token expiration broken" }
        }
    }

    @Test
    fun token_expiration_work_correct_test_2() {
        runTestInConfiguredApplicationWithoutDBConnection {
            val manager = UserTokensManager(TIME_TEST_TOKEN_LIFETIME_IN_SEC)

            val tokenOwners = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
            val tokens = listOf(
                manager.putUserToken(tokenOwners[0]),
                manager.putUserToken(tokenOwners[1]),
                manager.putUserToken(tokenOwners[2]),
            )
            val tokenOwnersWithTheirTokens = tokenOwners.zip(tokens)
            tokenOwnersWithTheirTokens.forEach { (tokenOwner, token) ->
                val isTokenValid = manager.isTokenExistAndValid(token)
                assert(isTokenValid) { "Failed to remember user token" }

                val itTokenOwnerCorrect = manager.getTokenOwnerID(token)
                assert(itTokenOwnerCorrect == tokenOwner) { "Failed to link user and his token"}
            }

            delayByTokenLifetimeAndOneSecAhead()

            val isAllTokenRemoved = tokenOwnersWithTheirTokens.all { (_, token) ->
                manager.isTokenExistAndValid(token).not()
            }
            assert(isAllTokenRemoved) { "Token expiration broken" }
        }
    }
}