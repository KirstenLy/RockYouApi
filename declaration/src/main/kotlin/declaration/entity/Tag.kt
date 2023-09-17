package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
data class Tag(val id: Int, val name: String)