package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
data class Lang(val id: Byte, val name: String)