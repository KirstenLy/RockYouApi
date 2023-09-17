package rockyouapi.auth

import java.time.LocalDateTime

/**
 * Token info. Token is used for users-only operations: vote, add/remove from favorite, e.t.c.
 * @param tokenCreationTime used to detect is token outdated. Token lifetime set by env.
 * */
internal data class UserTokenInfo(val userID: Int, val token: String, val tokenCreationTime: LocalDateTime)
