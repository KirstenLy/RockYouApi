package rockyouapi.model.user

/** User role. */
internal enum class UserRole(val roleValue: Int) {
    MEMBER(1),
    MODERATOR(2),
    ADMIN(3)
}