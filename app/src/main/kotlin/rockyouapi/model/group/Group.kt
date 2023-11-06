package rockyouapi.model.group

import kotlinx.serialization.Serializable

/** Some group of content, f.e group of picture can be comics. */
@Serializable
internal data class Group(val name: String, val memberList: List<GroupMember>)