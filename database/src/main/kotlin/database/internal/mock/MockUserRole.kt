package database.internal.mock

import database.internal.model.DBUserRole

internal val userRole1 = DBUserRole(1, "Member")
internal val userRole2 = DBUserRole(2, "Moderator")
internal val userRole3 = DBUserRole(3, "Admin")

internal val allUserRole = listOf(userRole1, userRole2, userRole3)