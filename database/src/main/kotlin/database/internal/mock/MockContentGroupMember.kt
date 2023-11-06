package database.internal.mock

import database.internal.model.DBContentGroupMember

internal val contentGroupMember1 = DBContentGroupMember(
    id = 1,
    groupID = contentGroup1.id,
    contentID = pictureContent9.id,
    orderIDX = 1,
)

internal val contentGroupMember2 = DBContentGroupMember(
    id = 2,
    groupID = contentGroup1.id,
    contentID = pictureContent1.id,
    orderIDX = 2,
)

internal val contentGroupMember3 = DBContentGroupMember(
    id = 3,
    groupID = contentGroup1.id,
    contentID = pictureContent6.id,
    orderIDX = 3,
)

internal val contentGroupMember4 = DBContentGroupMember(
    id = 4,
    groupID = contentGroup2.id,
    contentID = pictureContent9.id,
    orderIDX = 1,
)

internal val contentGroupMember5 = DBContentGroupMember(
    id = 5,
    groupID = contentGroup2.id,
    contentID = pictureContent13.id,
    orderIDX = 2,
)

internal val contentGroupMember6 = DBContentGroupMember(
    id = 6,
    groupID = contentGroup2.id,
    contentID = pictureContent15.id,
    orderIDX = 3,
)

internal val contentGroupMember7 = DBContentGroupMember(
    id = 7,
    groupID = contentGroup3.id,
    contentID = videoContent2.id,
    orderIDX = 2,
)

internal val contentGroupMember8 = DBContentGroupMember(
    id = 8,
    groupID = contentGroup3.id,
    contentID = videoContent7.id,
    orderIDX = 1,
)

internal val contentGroupMember9 = DBContentGroupMember(
    id = 9,
    groupID = contentGroup4.id,
    contentID = videoContent7.id,
    orderIDX = 2,
)

internal val contentGroupMember10 = DBContentGroupMember(
    id = 10,
    groupID = contentGroup4.id,
    contentID = videoContent14.id,
    orderIDX = 1,
)

internal val contentGroupMember11 = DBContentGroupMember(
    id = 11,
    groupID = contentGroup5.id,
    contentID = storyContent6.id,
    orderIDX = 4,
)

internal val contentGroupMember12 = DBContentGroupMember(
    id = 12,
    groupID = contentGroup5.id,
    contentID = storyContent4.id,
    orderIDX = 3,
)

internal val contentGroupMember13 = DBContentGroupMember(
    id = 13,
    groupID = contentGroup5.id,
    contentID = storyContent2.id,
    orderIDX = 2,
)

internal val contentGroupMember14 = DBContentGroupMember(
    id = 14,
    groupID = contentGroup5.id,
    contentID = storyContent1.id,
    orderIDX = 1,
)

internal val allContentGroupMember = listOf(
    contentGroupMember1,
    contentGroupMember2,
    contentGroupMember3,
    contentGroupMember4,
    contentGroupMember5,
    contentGroupMember6,
    contentGroupMember7,
    contentGroupMember8,
    contentGroupMember9,
    contentGroupMember10,
    contentGroupMember11,
    contentGroupMember12,
    contentGroupMember13,
    contentGroupMember14,
)