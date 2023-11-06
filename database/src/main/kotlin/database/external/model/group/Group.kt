package database.external.model.group

/** Some group of content, f.e group of picture can be comics. */
data class Group(val name: String, val memberList: List<GroupMember>)