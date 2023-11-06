package database.internal.mock

import database.external.security.hashPassword
import database.internal.model.DBUserAuthData

internal val userAuthData1 = DBUserAuthData(1, 1, hashPassword(user1.name))
internal val userAuthData2 = DBUserAuthData(2, 2, hashPassword(user2.name))
internal val userAuthData3 = DBUserAuthData(3, 3, hashPassword(user3.name))
internal val userAuthData4 = DBUserAuthData(4, 4, hashPassword(user4.name))
internal val userAuthData5 = DBUserAuthData(5, 5, hashPassword(user5.name))
internal val userAuthData6 = DBUserAuthData(6, 6, hashPassword(user6.name))
internal val userAuthData7 = DBUserAuthData(7, 7, hashPassword(user7.name))
internal val userAuthData8 = DBUserAuthData(8, 8, hashPassword(user8.name))
internal val userAuthData9 = DBUserAuthData(9, 9, hashPassword(user9.name))

internal val allUserAuthData = listOf(
    userAuthData1,
    userAuthData2,
    userAuthData3,
    userAuthData4,
    userAuthData5,
    userAuthData6,
    userAuthData7,
    userAuthData8,
    userAuthData9
)