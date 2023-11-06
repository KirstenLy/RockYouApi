package rockyouapi.model.user

import kotlinx.serialization.Serializable

/** User of content. Simplified model of [UserFull]. */
@Serializable
internal data class UserSimple(val id: Int, val name: String, val role: UserRole? = null)