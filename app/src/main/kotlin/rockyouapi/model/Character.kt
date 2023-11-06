package rockyouapi.model

import kotlinx.serialization.Serializable

/** Content character. */
@Serializable
internal data class Character(val id: Int, val name: String)