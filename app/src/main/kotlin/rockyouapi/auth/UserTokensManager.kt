package rockyouapi.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.time.Duration

/** Simple manager to manage user tokens. */
internal class UserTokensManager(private val tokenLiveTimeInSeconds: Int) {

    private val userTokens: MutableList<UserTokenInfo> = mutableListOf()
    private val userTokensMutex = Mutex()

    /**
     * Create and store token for user with [userID]. Return it.
     * Store token until [isTokenExistAndValid] called on expired token, or until server restarted.
     *
     * Recreate stored token if for [userID] argument token already exist.
     * New token will have full live time.
     * */
    suspend fun putUserToken(userID: Int): String = userTokensMutex.withLock {
        val existedTokenInfo = userTokens.firstOrNull { it.userID == userID }
        if (existedTokenInfo != null) userTokens.remove(existedTokenInfo)

        val userToken = UUID.randomUUID().toString()
        val userTokenInfo = UserTokenInfo(
            userID = userID,
            token = userToken,
            tokenCreationTime = LocalDateTime.now(),
        )
        userTokens.add(userTokenInfo)
        userToken
    }

    /**
     * Check is token exist and valid by lifetime.
     * If token expired, remove it from store.
     * */
    suspend fun isTokenExistAndValid(token: String): Boolean = userTokensMutex.withLock {
        val existedTokenInfo = userTokens.firstOrNull { it.token == token } ?: return false
        val timeDiffBetweenTokenCreationAndNow = ChronoUnit.SECONDS.between(
            existedTokenInfo.tokenCreationTime,
            LocalDateTime.now()
        )
        val isExistedTokenExpired = timeDiffBetweenTokenCreationAndNow > tokenLiveTimeInSeconds
        return if (isExistedTokenExpired) {
            userTokens.remove(existedTokenInfo)
            false
        } else {
            true
        }
    }

    suspend fun getTokenOwnerID(token: String): Int? = userTokensMutex.withLock {
        val existedTokenInfo = userTokens.firstOrNull { it.token == token } ?: return null
        return@withLock existedTokenInfo.userID
    }
}