package database.internal.mock

import database.internal.model.DBUser

internal val user1 = DBUser(1, "Homer Simpson", userRole1.id)
internal val user2 = DBUser(2, "Bender", userRole1.id)
internal val user3 = DBUser(3, "Dr.Zoidberg", userRole1.id)
internal val user4 = DBUser(4, "SpongeBob", userRole1.id)
internal val user5 = DBUser(5, "Woody Woodpecker", userRole2.id)
internal val user6 = DBUser(6, "Michael Myers", userRole3.id)
internal val user7 = DBUser(7, "Elizabeth Swann", userRole2.id)
internal val user8 = DBUser(8, "James Bond", userRole1.id)
internal val user9 = DBUser(9, "Shang Tsung", userRole1.id)

internal val allUsers = listOf(user1, user2, user3, user4, user5, user6, user7, user8, user9)