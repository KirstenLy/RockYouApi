package database.internal.mock

import common.utils.generateRandomTextByUUID
import database.internal.model.DBForkVariant
import database.internal.model.DBStory
import database.internal.model.DBStoryNode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

// ID || Story name
// ----------
// 1     How to win the world cup
// 2     1984
// 3     Tales from a techie: funny real life stories from tech support
// 4     How to tell if your cat is plotting to kill you
// 5     Star trek cats
// 6     Ragweed’s farm dog handbook
// 7     The early days of the computer
// 8     Hollywood: the oral history
// 9     A promised land
// 10    The Idiot
// 11    Level up
// 12    The Age of A.I
// 13    Don’t put the boats away
// 14    A Crocodile in the Family
// 15    Britney Spears, the woman in me?

internal val story1 = DBStory(
    id = storyIDList[0],
    languageID = 2,
    availableLanguagesIDs = listOf(2, 3),
    authorsIDs = listOf(author1.id),
    userID = user6.id,
    tagsIDs = listOf(tag8.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(
        listOf(
            DBStoryNode.Chapter(storyChapter1.id),
            DBStoryNode.Chapter(storyChapter2.id),
        )
    )
)

internal val story2 = DBStory(
    id = storyIDList[1],
    languageID = 3,
    availableLanguagesIDs = listOf(3, 4),
    authorsIDs = listOf(author1.id),
    userID = user8.id,
    tagsIDs = listOf(tag3.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(
        listOf(
            DBStoryNode.Chapter(storyChapter3.id),
            DBStoryNode.Chapter(storyChapter4.id),
            DBStoryNode.Chapter(storyChapter5.id),
            DBStoryNode.Chapter(storyChapter6.id),
        )
    )
)

internal val story3 = DBStory(
    id = storyIDList[2],
    languageID = 2,
    availableLanguagesIDs = listOf(2, 1),
    authorsIDs = listOf(author7.id, author8.id, author9.id),
    userID = user9.id,
    tagsIDs = listOf(tag5.id, tag9.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(
        listOf(
            DBStoryNode.Chapter(storyChapter7.id),
            DBStoryNode.ForkNode(
                listOf(
                    DBForkVariant(
                        variantText = "Variant1",
                        nodes = listOf(
                            DBStoryNode.Chapter(storyChapter8.id)
                        )
                    ),
                    DBForkVariant(
                        variantText = "Variant2",
                        nodes = listOf(
                            DBStoryNode.Chapter(storyChapter9.id),
                            DBStoryNode.Chapter(storyChapter10.id),
                        )
                    )
                )
            ),
        )
    )
)

internal val story4 = DBStory(
    id = storyIDList[3],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = null,
    userID = user2.id,
    tagsIDs = listOf(tag5.id, tag1.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story5 = DBStory(
    id = storyIDList[4],
    languageID = 1,
    availableLanguagesIDs = listOf(1, 3),
    authorsIDs = listOf(author4.id),
    userID = user8.id,
    tagsIDs = listOf(tag1.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story6 = DBStory(
    id = storyIDList[5],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author5.id),
    userID = user6.id,
    tagsIDs = listOf(tag2.id, tag5.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story7 = DBStory(
    id = storyIDList[6],
    languageID = 3,
    availableLanguagesIDs = listOf(3, 1),
    authorsIDs = null,
    userID = user1.id,
    tagsIDs = listOf(tag9.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story8 = DBStory(
    id = storyIDList[7],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author3.id),
    userID = user3.id,
    tagsIDs = listOf(tag4.id, tag6.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story9 = DBStory(
    id = storyIDList[8],
    languageID = 3,
    availableLanguagesIDs = listOf(3, 1, 2),
    authorsIDs = listOf(author1.id, author9.id, author2.id, author8.id),
    userID = user2.id,
    tagsIDs = listOf(tag3.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story10 = DBStory(
    id = storyIDList[9],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author6.id, author7.id, author8.id),
    userID = user8.id,
    tagsIDs = listOf(tag5.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story11 = DBStory(
    id = storyIDList[10],
    languageID = language3.id,
    availableLanguagesIDs = listOf(language3.id),
    authorsIDs = listOf(author5.id),
    userID = user1.id,
    tagsIDs = listOf(tag10.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story12 = DBStory(
    id = storyIDList[11],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author7.id),
    userID = user8.id,
    tagsIDs = listOf(tag9.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story13 = DBStory(
    id = storyIDList[12],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author6.id, author7.id, author8.id, author9.id),
    userID = user1.id,
    tagsIDs = listOf(tag7.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story14 = DBStory(
    id = storyIDList[13],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author1.id, author5.id, author3.id),
    userID = user1.id,
    tagsIDs = listOf(tag3.id, tag7.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val story15 = DBStory(
    id = storyIDList[14],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author7.id),
    userID = user6.id,
    tagsIDs = listOf(tag3.id),
    scheme = Json.encodeToString<List<DBStoryNode>>(emptyList())
)

internal val storyList = listOf(
    story1,
    story2,
    story3,
    story4,
    story5,
    story6,
    story7,
    story8,
    story9,
    story10,
    story11,
    story12,
    story13,
    story14,
    story15
)