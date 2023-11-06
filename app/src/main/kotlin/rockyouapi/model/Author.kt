package rockyouapi.model

import kotlinx.serialization.Serializable

/** Author of some content. */
@Serializable
data class Author(val id: Int, val name: String)