package database.internal.index

internal enum class UserRoleIndex(val index: Int) {
    MEMBER(1),
    MODERATOR(2),
    ADMIN(3)
}