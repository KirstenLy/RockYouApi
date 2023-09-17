package rockyouapi.model

import declaration.entity.User
import kotlinx.serialization.Serializable

/** Model, returned when user login or registered. */
@Serializable
internal data class UserWithToken(val user: User, val token: String)