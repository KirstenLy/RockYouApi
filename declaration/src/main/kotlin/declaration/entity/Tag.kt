package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
data class Tag(val id: Short, val name: String)