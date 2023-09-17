package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
class Comment(val user: User, val text: String)