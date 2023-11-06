package database.internal.mock

import database.internal.model.DBPicture

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

// ID || Picture name
// ----------
// 1     AI generated cat. It's awesome!
// 2     Golden Retriever is best family dog
// 3     "What's your favorite scary movie?" (c)
// 4     "The most important thing in life will always be family!" (c)
// 5     I try to clone my cat...and that was happen!
// 6     Dog ran onto the football field
// 7     "Hello, Peter"(c)
// 8     Freddi Fish is still best game for kids
// 9     Joe Biden in \"Shaolin Soccer\" movie"
// 10    Mike Tyson in Mortal Kombat 14
// 11    Cat meow translator
// 12    Jonh Wick VS Max Payne
// 13    My grandpa celebrates 100th birthday. Long time to live
// 14    Barack Obama announces his candidacy for election
// 15    Evolution of Lara Croft game

internal val picture1 = DBPicture(
    id = pictureIDList[0],
    url = "https://avavatar.com/images/full/16/G1pPgmZwgOUOrMjU.jpg",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = listOf(author1.id),
    userID = user1.id,
    tagsIDs = listOf(tag1.id, tag9.id),
    groups = listOf(contentGroup1.id)
)

internal val picture2 = DBPicture(
    id = pictureIDList[1],
    url = "https://avatarko.com/img/kartinka/33/zhivotnye_sobaka_voda_zolotistyi_retriever_32802.jpg",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = listOf(author5.id),
    userID = user5.id,
    tagsIDs = listOf(tag2.id, tag7.id),
    groups = emptyList()
)

internal val picture3 = DBPicture(
    id = pictureIDList[2],
    url = "https://avatarko.com/img/kartinka/21/film_maska_telefon_nozh_20453.jpg",
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author7.id, author8.id),
    userID = user8.id,
    tagsIDs = listOf(tag6.id),
    groups = emptyList()
)

internal val picture4 = DBPicture(
    id = pictureIDList[3],
    url = "https://i.ytimg.com/vi/MbvE4yNR9a4/maxresdefault.jpg",
    languageID = 1,
    availableLanguagesIDs = listOf(1, 2, 3),
    authorsIDs = null,
    userID = user1.id,
    tagsIDs = listOf(tag4.id, tag6.id, tag7.id),
    groups = emptyList()
)

internal val picture5 = DBPicture(
    id = pictureIDList[4],
    url = "https://en.wikipedia.org/wiki/Cat#/media/File:Cat_August_2010-4.jpg",
    languageID = 3,
    availableLanguagesIDs = listOf(3, 4),
    authorsIDs = listOf(author9.id),
    userID = user4.id,
    tagsIDs = listOf(tag1.id, tag9.id),
    groups = emptyList()
)

internal val picture6 = DBPicture(
    id = pictureIDList[5],
    url = "https://static.boredpanda.com/blog/wp-content/uploads/2020/12/93c67b69-552b-410f-aa5b-750b2a29361c-5fecd3d6ef64e__700.jpg",
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author8.id),
    userID = user4.id,
    tagsIDs = listOf(tag2.id, tag8.id),
    groups = listOf(contentGroup1.id)
)

internal val picture7 = DBPicture(
    id = pictureIDList[6],
    url = "https://en.meming.world/images/en/e/e2/Hello%2C_Peter.jpg",
    languageID = 4,
    availableLanguagesIDs = listOf(4, 1),
    authorsIDs = null,
    userID = user2.id,
    tagsIDs = listOf(tag6.id),
    groups = emptyList()
)

internal val picture8 = DBPicture(
    id = pictureIDList[7],
    url = "https://en.wikipedia.org/wiki/Freddi_Fish#/media/File:Freddi_Fish_&_Luther.gif",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = listOf(author4.id),
    userID = user1.id,
    tagsIDs = listOf(tag7.id, tag10.id),
    groups = emptyList()
)

internal val picture9 = DBPicture(
    id = pictureIDList[8],
    url = "https://en.wikipedia.org/wiki/Joe_Biden#/media/File:Joe_Biden_presidential_portrait.jpg",
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author4.id, author8.id, author6.id, author3.id),
    userID = user5.id,
    tagsIDs = listOf(tag3.id, tag4.id, tag5.id, tag6.id, tag8.id),
    groups = listOf(contentGroup1.id, contentGroup2.id)
)

internal val picture10 = DBPicture(
    id = pictureIDList[9],
    url = "https://sportsnaut.com/wp-content/uploads/2021/08/USATSI_15246716-1024x683.jpg",
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author3.id),
    userID = user3.id,
    tagsIDs = listOf(tag4.id, tag5.id, tag10.id),
    groups = emptyList()
)

internal val picture11 = DBPicture(
    id = pictureIDList[10],
    url = "https://sportsnaut.com/wp-content/uploads/2021/08/USATSI_15246716-1024x683.jpg",
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author7.id),
    userID = user7.id,
    tagsIDs = listOf(tag1.id, tag9.id),
    groups = emptyList()
)

internal val picture12 = DBPicture(
    id = pictureIDList[11],
    url = "https://upload.wikimedia.org/wikipedia/en/9/98/John_Wick_TeaserPoster.jpg",
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author1.id, author2.id),
    userID = user5.id,
    tagsIDs = listOf(tag6.id, tag10.id),
    groups = emptyList()
)

internal val picture13 = DBPicture(
    id = pictureIDList[12],
    url = "https://as2.ftcdn.net/v2/jpg/01/34/21/49/1000_F_134214988_PvjuxDxYR2Su42YUbRBFRl3l5EX1ZuIp.jpg",
    languageID = null,
    availableLanguagesIDs = null,
    authorsIDs = listOf(author3.id),
    userID = user6.id,
    tagsIDs = listOf(tag7.id),
    groups = listOf(contentGroup2.id)
)

internal val picture14 = DBPicture(
    id = pictureIDList[13],
    url = "https://as2.ftcdn.net/v2/jpg/00/51/74/77/1000_F_51747781_T1BqnqOXKV6Cbmm6BbJnOdV2juQYNWVg.jpg",
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author6.id, author7.id, author8.id, author9.id),
    userID = user6.id,
    tagsIDs = listOf(tag3.id),
    groups = emptyList()
)

internal val picture15 = DBPicture(
    id = pictureIDList[14],
    url = "https://as2.ftcdn.net/v2/jpg/03/35/38/13/1000_F_335381367_ZigGbpEP2cfKTA9wIu0gJrgb7NO70V8Q.jpg",
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author9.id),
    userID = user9.id,
    tagsIDs = listOf(tag10.id),
    groups = listOf(contentGroup2.id)
)

internal val pictureList = listOf(
    picture1,
    picture2,
    picture3,
    picture4,
    picture5,
    picture6,
    picture7,
    picture8,
    picture9,
    picture10,
    picture11,
    picture12,
    picture13,
    picture14,
    picture15
)