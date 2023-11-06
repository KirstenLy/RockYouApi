package rockyouapi.model

import kotlinx.serialization.Serializable

/** Comment of some content. */
@Serializable
internal data class Comment(
    val id: Int,
    val contentID: Int,
    val userID: Int,
    val userName: String,
    val text: String,
    val creationDate: String
)