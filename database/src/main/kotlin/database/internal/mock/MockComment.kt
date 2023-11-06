package database.internal.mock

import database.internal.model.DBComment
import java.time.LocalDateTime

internal val comment1 = DBComment(
    id = 1,
    userID = user1.id,
    contentID = picture1.id,
    text = "Hello world!",
    creationDate = LocalDateTime.of(2021, 6, 1, 3, 3)
)

internal val comment2 = DBComment(
    id = 2,
    userID = user2.id,
    contentID = picture10.id,
    text = "Hey! What is it?",
    creationDate = LocalDateTime.of(2021, 6, 2, 3, 3)
)

internal val comment3 = DBComment(
    id = 3,
    userID = user3.id,
    contentID = story11.id,
    text = "I like it",
    creationDate = LocalDateTime.of(2021, 6, 3, 3, 3)
)

internal val comment4 = DBComment(
    id = 4,
    userID = user4.id,
    contentID = storyChapter12.id,
    text = "No way...",
    creationDate = LocalDateTime.of(2021, 6, 4, 3, 3)
)

internal val comment5 = DBComment(
    id = 5,
    userID = user5.id,
    contentID = picture5.id,
    text = "What's her name?",
    creationDate = LocalDateTime.of(2021, 6, 5, 3, 3)
)

internal val comment6 = DBComment(
    id = 6,
    userID = user6.id,
    contentID = videoContent8.id,
    text = "What i'm looking at?",
    creationDate = LocalDateTime.of(2021, 6, 6, 3, 3)
)

internal val comment7 = DBComment(
    id = 7,
    userID = user7.id,
    contentID = videoContent14.id,
    text = "Add me to friends please",
    creationDate = LocalDateTime.of(2021, 6, 7, 3, 3)
)

internal val comment8 = DBComment(
    id = 8,
    userID = user8.id,
    contentID = story5.id,
    text = "Good job",
    creationDate = LocalDateTime.of(2021, 7, 1, 3, 3)
)

internal val comment9 = DBComment(
    id = 9,
    userID = user9.id,
    contentID = picture10.id,
    text = "Dislike, it's gross",
    creationDate = LocalDateTime.of(2022, 1, 1, 1, 1)
)

internal val comment10 = DBComment(
    id = 10,
    userID = user8.id,
    contentID = videoContent13.id,
    text = "Would you like to buy some adv?",
    creationDate = LocalDateTime.of(2022, 2, 1, 1, 1)
)

internal val comment11 = DBComment(
    id = 11,
    userID = user7.id,
    contentID = picture7.id,
    text = "Post more like this please",
    creationDate = LocalDateTime.of(2022, 3, 1, 1, 1)
)

internal val comment12 = DBComment(
    id = 12,
    userID = user6.id,
    contentID = videoContent10.id,
    text = "It's the best",
    creationDate = LocalDateTime.of(2022, 3, 2, 1, 1)
)

internal val comment13 = DBComment(
    id = 13,
    userID = user5.id,
    contentID = storyChapter12.id,
    text = "LMAO",
    creationDate = LocalDateTime.of(2022, 3, 3, 1, 1)
)

internal val comment14 = DBComment(
    id = 14,
    userID = user4.id,
    contentID = storyChapter28.id,
    text = "Content not found, how to fix this error?",
    creationDate = LocalDateTime.of(2022, 3, 4, 1, 1)
)

internal val comment15 = DBComment(
    id = 15,
    userID = user3.id,
    contentID = picture4.id,
    text = "Old joke...",
    creationDate = LocalDateTime.of(2022, 3, 5, 1, 1)
)

internal val comment16 = DBComment(
    id = 16,
    userID = user2.id,
    contentID = videoContent7.id,
    text = "Ahahahahaa",
    creationDate = LocalDateTime.of(2022, 4, 1, 1, 1)
)

internal val comment17 = DBComment(
    id = 17,
    userID = user1.id,
    contentID = videoContent5.id,
    text = "How to change video quality?",
    creationDate = LocalDateTime.of(2022, 4, 2, 1, 1)
)

internal val comment18 = DBComment(
    id = 18,
    userID = user2.id,
    contentID = videoContent4.id,
    text = "Video broken! Fix it please!",
    creationDate = LocalDateTime.of(2022, 4, 3, 1, 1)
)

internal val comment19 = DBComment(
    id = 19,
    userID = user3.id,
    contentID = picture2.id,
    text = "Forever in my favorite",
    creationDate = LocalDateTime.of(2022, 4, 4, 1, 1)
)

internal val comment20 = DBComment(
    id = 20,
    userID = user4.id,
    contentID = story5.id,
    text = "What's point of this???",
    creationDate = LocalDateTime.of(2023, 1, 1, 1, 1)
)

internal val comment21 = DBComment(
    id = 21,
    userID = user5.id,
    contentID = story6.id,
    text = "Author is idiot",
    creationDate = LocalDateTime.of(2023, 1, 1, 2, 2)
)

internal val comment22 = DBComment(
    id = 22,
    userID = user6.id,
    contentID = videoContent7.id,
    text = "Oh! I bet i can't handle it.",
    creationDate = LocalDateTime.of(2023, 1, 2, 2, 2)
)

internal val comment23 = DBComment(
    id = 23,
    userID = user7.id,
    contentID = story15.id,
    text = "I laugh so hard, TY!",
    creationDate = LocalDateTime.of(2023, 2, 2, 2, 2)
)

internal val comment24 = DBComment(
    id = 24,
    userID = user8.id,
    contentID = picture1.id,
    text = "It's a London?",
    creationDate = LocalDateTime.of(2023, 4, 3, 3, 3)
)

internal val comment25 = DBComment(
    id = 25,
    userID = user9.id,
    contentID = picture1.id,
    text = "It's good, but you can better",
    creationDate = LocalDateTime.of(2023, 5, 5, 5, 5)
)

internal val comment26 = DBComment(
    id = 26,
    userID = user8.id,
    contentID = picture8.id,
    text = "Author, write me PM please, i have a proposition",
    creationDate = LocalDateTime.of(2023, 6, 6, 6, 6)
)

internal val comment27 = DBComment(
    id = 27,
    userID = user7.id,
    contentID = videoContent14.id,
    text = "What a song?",
    creationDate = LocalDateTime.of(2023, 6, 7, 7, 7)
)

internal val comment28 = DBComment(
    id = 28,
    userID = user6.id,
    contentID = picture1.id,
    text = "I don't think it's a good place to discuss politic",
    creationDate = LocalDateTime.of(2023, 6, 8, 8, 8)
)

internal val comment29 = DBComment(
    id = 29,
    userID = user5.id,
    contentID = videoContent12.id,
    text = "I swear i see Batman on 0:34!",
    creationDate = LocalDateTime.of(2023, 7, 1, 1, 1)
)

internal val comment30 = DBComment(
    id = 30,
    userID = user4.id,
    contentID = story4.id,
    text = "That is not true, i was there",
    creationDate = LocalDateTime.of(2023, 7, 2, 2, 2)
)

internal val comment31 = DBComment(
    id = 31,
    userID = user3.id,
    contentID = storyChapter18.id,
    text = "Service is totally broken, how can i upvote content?",
    creationDate = LocalDateTime.of(2023, 7, 3, 3, 3)
)

internal val comment32 = DBComment(
    id = 32,
    userID = user2.id,
    contentID = storyChapter20.id,
    text = "I report this shit",
    creationDate = LocalDateTime.of(2023, 7, 4, 4, 4)
)

internal val comment33 = DBComment(
    id = 33,
    userID = user1.id,
    contentID = picture13.id,
    text = "@moderator, ban please",
    creationDate = LocalDateTime.of(2023, 7, 5, 5, 5)
)

internal val comment34 = DBComment(
    id = 34,
    userID = user2.id,
    contentID = picture9.id,
    text = "What a nice kitty!",
    creationDate = LocalDateTime.of(2023, 7, 6, 6, 6)
)

internal val comment35 = DBComment(
    id = 35,
    userID = user3.id,
    contentID = story9.id,
    text = "Admin, author of content is 'Elza', change please.",
    creationDate = LocalDateTime.of(2023, 7, 7, 7, 7)
)

internal val comment36 = DBComment(
    id = 36,
    userID = user4.id,
    contentID = videoContent15.id,
    text = "It's from some movie or...?",
    creationDate = LocalDateTime.of(2024, 1, 1, 1, 1)
)

internal val comment37 = DBComment(
    id = 37,
    userID = user5.id,
    contentID = videoContent15.id,
    text = "It's brilliant",
    creationDate = LocalDateTime.of(2024, 1, 1, 2, 2)
)

internal val comment38 = DBComment(
    id = 38,
    userID = user6.id,
    contentID = picture2.id,
    text = "Where i can buy same?",
    creationDate = LocalDateTime.of(2024, 1, 2, 1, 1)
)

internal val comment39 = DBComment(
    id = 39,
    userID = user6.id,
    contentID = picture3.id,
    text = "I want to support author, how can i do this?",
    creationDate = LocalDateTime.of(2024, 1, 2, 2, 2)
)

internal val comment40 = DBComment(
    id = 40,
    userID = user7.id,
    contentID = storyChapter1.id,
    text = "Upvoted, thank you",
    creationDate = LocalDateTime.of(2024, 3, 3, 3, 3)
)

internal val allComment = listOf(
    comment1,
    comment2,
    comment3,
    comment4,
    comment5,
    comment6,
    comment7,
    comment8,
    comment9,
    comment10,
    comment11,
    comment12,
    comment13,
    comment14,
    comment15,
    comment16,
    comment17,
    comment18,
    comment19,
    comment20,
    comment21,
    comment22,
    comment23,
    comment24,
    comment25,
    comment26,
    comment27,
    comment28,
    comment29,
    comment30,
    comment31,
    comment32,
    comment33,
    comment34,
    comment35,
    comment36,
    comment37,
    comment38,
    comment39,
    comment40,
)