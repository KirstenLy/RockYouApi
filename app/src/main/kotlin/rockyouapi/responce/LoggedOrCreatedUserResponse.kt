package rockyouapi.responce

import kotlinx.serialization.Serializable
import rockyouapi.model.Vote
import rockyouapi.model.user.UserFull

/** Model, returned when user login or registered. */
@Serializable
internal data class LoggedOrCreatedUserResponse(
    val user: UserFull,
    val accessToken: String,
    val refreshToken: String,
    val voteHistory: List<Vote>,
    val favorite: List<Int>
)