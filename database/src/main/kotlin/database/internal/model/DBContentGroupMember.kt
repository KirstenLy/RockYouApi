package database.internal.model

/** Content group member. */
internal data class DBContentGroupMember(
    val id: Int,
    val groupID: Int,
    val contentID: Int,
    val orderIDX: Int,
)