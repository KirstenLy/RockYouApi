package rockyouapi.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

// TODO: Покрыть тестами
internal class UserTokensManager(private val tokenLiveTimeInSeconds: Int) {

    private val userTokens: MutableList<UserTokenInfo> = mutableListOf()
    private val userTokensMutex = Mutex()

    /** Put token. Remember call time. */
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

    suspend fun isTokenExistAndValid(token: String): Boolean = userTokensMutex.withLock {
        val existedTokenInfo = userTokens.firstOrNull { it.token == token } ?: return false
        val timeDiffBetweenTokenCreationAndNow = ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.now())
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