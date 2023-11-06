package database.external.model.user

/** User of content. Simplified model of [UserFull]. */
data class UserSimple(val id: Int, val name: String, val role: UserRole? = null)