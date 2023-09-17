package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
class User(val id: Int, val name: String)