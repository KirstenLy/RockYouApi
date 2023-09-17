package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
data class Comment(val id: Int, val contentID: Int, val userID: Int, val userName: String, val text: String)