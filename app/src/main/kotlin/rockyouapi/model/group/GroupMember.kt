package rockyouapi.model.group

import kotlinx.serialization.Serializable

/** Part of [Group]. */
@Serializable
internal data class GroupMember(val contentID: Int, val name: String, val order: Int)