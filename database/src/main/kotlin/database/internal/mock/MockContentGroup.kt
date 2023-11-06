package database.internal.mock

import database.internal.model.DBContentGroup

internal val contentGroup1 = DBContentGroup(
    id = 1,
    title = "Avengers comic book",
)

internal val contentGroup2 = DBContentGroup(
    id = 2,
    title = "Rules of war",
)

internal val contentGroup3 = DBContentGroup(
    id = 3,
    title = "House m.s S1",
)

internal val contentGroup4 = DBContentGroup(
    id = 4,
    title = "House m.s S2",
)

internal val contentGroup5 = DBContentGroup(
    id = 5,
    title = "Great Gatsby stories",
)

internal val contentGroupList = listOf(
    contentGroup1,
    contentGroup2,
    contentGroup3,
    contentGroup4,
    contentGroup5,
)