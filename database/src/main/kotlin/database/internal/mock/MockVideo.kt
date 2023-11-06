package database.internal.mock

import database.internal.model.DBVideo

// ID || Tag name
// ----------
// 1     Cat
// 2     Dog
// 3     Policy
// 4     Celebrities
// 5     Humor
// 6     Movies
// 7     Family
// 8     Football
// 9     Technologies
// 10    Games

// ID || Video name
// ----------
// 1     Best clash of clans build strategy
// 2     10 best Cristiano Ronaldo shots
// 3     Why 'Ring' is the best horror i have ever seen?
// 4     My Civilization V walkthrough
// 5     Cat VS Dog: who win in fairy fight?
// 6     Guide how to teach grandma to use cellphone
// 7     George W. Bush shoeing incident
// 8     Full Skyrim OST
// 9     House M.D S1E10
// 10    Jeeves and Wooster best scenes
// 11    Johnny Depp interview
// 12    Service dog for the blind. How they help?
// 13    Phone prank gone wrong
// 14    Elon Musk’s starlink as technology of new era
// 15    How to find secret cat in Zoo Tycoon 2?

internal val video1 = DBVideo(
    id = videoIDList[0],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author5.id),
    userID = user4.id,
    tagsIDs = listOf(tag10.id),
)

internal val video2 = DBVideo(
    id = videoIDList[1],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author5.id),
    userID = user9.id,
    tagsIDs = listOf(tag8.id),
)

internal val video3 = DBVideo(
    id = videoIDList[2],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author1.id, author5.id),
    userID = user4.id,
    tagsIDs = listOf(tag6.id),
)

internal val video4 = DBVideo(
    id = videoIDList[3],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = null,
    availableLanguagesIDs = emptyList(),
    authorsIDs = null,
    userID = user9.id,
    tagsIDs = listOf(tag10.id),
)

internal val video5 = DBVideo(
    id = videoIDList[4],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = listOf(author4.id),
    userID = user8.id,
    tagsIDs = listOf(tag1.id, tag2.id),
)

internal val video6 = DBVideo(
    id = videoIDList[5],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author6.id),
    userID = user6.id,
    tagsIDs = listOf(tag7.id, tag9.id),
)

internal val video7 = DBVideo(
    id = videoIDList[6],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = null,
    userID = user1.id,
    tagsIDs = listOf(tag3.id, tag5.id),
)

internal val video8 = DBVideo(
    id = videoIDList[7],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = null,
    userID = user5.id,
    tagsIDs = listOf(tag10.id),
)

internal val video9 = DBVideo(
    id = videoIDList[8],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 3,
    availableLanguagesIDs = listOf(3, 1),
    authorsIDs = listOf(author4.id, author8.id, author2.id, author3.id, author9.id),
    userID = user7.id,
    tagsIDs = listOf(tag6.id),
)

internal val video10 = DBVideo(
    id = videoIDList[9],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = null,
    userID = user8.id,
    tagsIDs = listOf(tag5.id, tag6.id),
)

internal val video11 = DBVideo(
    id = videoIDList[10],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = listOf(author5.id),
    userID = user5.id,
    tagsIDs = listOf(tag4.id),
)

internal val video12 = DBVideo(
    id = videoIDList[11],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author4.id),
    userID = user3.id,
    tagsIDs = listOf(tag2.id),
)

internal val video13 = DBVideo(
    id = videoIDList[12],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = listOf(author3.id, author8.id, author7.id, author6.id, author5.id),
    userID = user9.id,
    tagsIDs = listOf(tag5.id),
)

internal val video14 = DBVideo(
    id = videoIDList[13],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author1.id, author2.id, author3.id),
    userID = user2.id,
    tagsIDs = listOf(tag4.id, tag9.id),
)

internal val video15 = DBVideo(
    id = videoIDList[14],
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author7.id),
    userID = user1.id,
    tagsIDs = listOf(tag10.id, tag1.id),
)

internal val video = listOf(
    video1,
    video2,
    video3,
    video4,
    video5,
    video6,
    video7,
    video8,
    video9,
    video10,
    video11,
    video12,
    video13,
    video14,
    video15,
)