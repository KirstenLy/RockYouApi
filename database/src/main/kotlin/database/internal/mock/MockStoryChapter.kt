package database.internal.mock

import database.internal.model.DBStoryChapter
import kotlin.random.Random

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

// ID    Story Name
// --------------------------------------------------------------------
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

// ID   Story Name                                                          Chapter name
// -------------------------------------------------------------------------------------
// 1    How to win the world cup                                            How to win the world cup? That is easy!
// 2    How to win the world cup                                            Pick the right player
// 3    1984                                                                Why is 1984?
// 4    1984                                                                Censorship is growing
// 5    1984                                                                Ministry if truth
// 6    1984                                                                No one escape big brother
// 7    Tales from a techie: funny real life stories from tech support      My first day as tech support
// 8    Tales from a techie: funny real life stories from tech support      As i seems like mike Mike Wazowski???
// 9    Tales from a techie: funny real life stories from tech support      What's wrong with all off this people?
// 10   Tales from a techie: funny real life stories from tech support      Calm down yourself
// 11   How to tell if your cat is plotting to kill you                     Is your cat sit on your face at night?
// 12   How to tell if your cat is plotting to kill you                     When 'meow' is more that you thinking
// 13   How to tell if your cat is plotting to kill you                     Your cat is not fed? You better run
// 14   Star trek cats                                                      Do you think cats are from our planet? Here 10 proofs you are wrong!
// 15   Star trek cats                                                      University is not a limit!
// 16   Ragweed’s farm dog handbook                                         The secret is simple...
// 17   The early days of the computer                                      All starts in the stone age...
// 18   The early days of the computer                                      ...and in iron age inevitably things happened...
// 19   The early days of the computer                                      ...sow now we here, with dishwasher, starts by voice commands
// 20   Hollywood: the oral history                                         First bricks
// 21   Hollywood: the oral history                                         People is the central
// 22   A promised land                                                     America before Christofer Columb
// 23   A promised land                                                     World War 2: what it was about?
// 24   A promised land                                                     2000 and above. Who will make America great again?
// 25   The Idiot                                                           Where is my keys?
// 26   The Idiot                                                           Where is my breakfast?
// 27   The Idiot                                                           WHERE AM I???
// 28   Level up                                                            Concept of most popular games
// 29   Level up                                                            Diablo and Minecraft as genre creators
// 30   Level up                                                            Is Witcher battle system perfect enought?
// 31   The Age of A.I                                                      How AI trully work?
// 32   The Age of A.I                                                      How realistic 'Terminator' scenario?
// 33   The Age of A.I                                                      Analyze or be analyzed
// 34   The Age of A.I                                                      Summary about AI
// 35   The Age of A.I                                                      What is ChatGPT internally?
// 36   Don’t put the boats away                                            What is boat? Baby don't hurt me
// 37   A Crocodile in the Family                                           How to tell their kids they are demons from hell?
// 38   A Crocodile in the Family                                           School, boys and first sex
// 39   A Crocodile in the Family                                           Authority in the family
// 40   Britney Spears, the woman in me?                                    Hello, i'm Britney
// 41   Britney Spears, the woman in me?                                    What is this book about?
// 42   Britney Spears, the woman in me?                                    My first steps to music
// 43   Britney Spears, the woman in me?                                    It there any life after first million?
// 44   Britney Spears, the woman in me?                                    My best fans list

internal val storyChapter1 = DBStoryChapter(
    id = storyChapterIDList[0],
    storyID = storyIDList[0],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author1.id),
    userID = user6.id,
    tagsIDs = listOf(tag8.id),
    text = "It' easy, i swear!"
)

internal val storyChapter2 = DBStoryChapter(
    id = storyChapterIDList[1],
    storyID = storyIDList[0],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author1.id),
    userID = user8.id,
    tagsIDs = listOf(tag8.id, tag1.id),
    text = "Just pick Ronaldo! What is the problem?"
)


internal val storyChapter3 = DBStoryChapter(
    id = storyChapterIDList[2],
    storyID = storyIDList[1],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author7.id, author8.id, author9.id),
    userID = user9.id,
    tagsIDs = listOf(tag3.id),
    text = "1984 is a great book, according to internet rating"
)

internal val storyChapter4 = DBStoryChapter(
    id = storyChapterIDList[3],
    storyID = storyIDList[1],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = null,
    userID = user2.id,
    tagsIDs = listOf(tag3.id),
    text = "From the one side, it's good: kids, idiots and radicals can't access destructive content. From the other side, someone use it to silent voices."
)

internal val storyChapter5 = DBStoryChapter(
    id = storyChapterIDList[4],
    storyID = storyIDList[1],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author4.id),
    userID = user8.id,
    tagsIDs = listOf(tag3.id),
    text = "If i tell you 100 times that existences person not exist, will you agree with me?"
)

internal val storyChapter6 = DBStoryChapter(
    id = storyChapterIDList[5],
    storyID = storyIDList[1],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author5.id),
    userID = user6.id,
    tagsIDs = listOf(tag3.id),
    text = "That is not secret. Look at China. I still don's understand, is it good practice or bad?"
)

internal val storyChapter7 = DBStoryChapter(
    id = storyChapterIDList[6],
    storyID = storyIDList[2],
    languageID = 3,
    availableLanguagesIDs = listOf(3, 4),
    authorsIDs = null,
    userID = user1.id,
    tagsIDs = listOf(tag5.id, tag9.id),
    text = "I was ready for the best. I will help people to solve their problem! I was so mistaken..."
)

internal val storyChapter8 = DBStoryChapter(
    id = storyChapterIDList[7],
    storyID = storyIDList[2],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author3.id),
    userID = user3.id,
    tagsIDs = emptyList(),
    text = "First of all - my boss give me a book with a 1000 pages and say me to read this as soon as possible. Who he think i am? Genius?"
)

internal val storyChapter9 = DBStoryChapter(
    id = storyChapterIDList[8],
    storyID = storyIDList[2],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author7.id, author6.id),
    userID = user2.id,
    tagsIDs = listOf(tag5.id, tag9.id, tag10.id),
    text = "First day was the worst. All clients bully my. I lost my faith to humanity."
)

internal val storyChapter10 = DBStoryChapter(
    id = storyChapterIDList[9],
    storyID = storyIDList[2],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = null,
    userID = user8.id,
    tagsIDs = listOf(tag5.id, tag9.id),
    text = "I'm quickly understood, that soul calm is key to success. So i calm myself. Now i head of tech!"
)

internal val storyChapter11 = DBStoryChapter(
    id = storyChapterIDList[10],
    storyID = storyIDList[3],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author2.id),
    userID = user1.id,
    tagsIDs = listOf(tag1.id, tag5.id),
    text = "Sitting in the face is not only about humans. Cats do it to. But what their intentions? No one known..."
)

internal val storyChapter12 = DBStoryChapter(
    id = storyChapterIDList[11],
    storyID = storyIDList[3],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author4.id),
    userID = user8.id,
    tagsIDs = listOf(tag1.id, tag2.id, tag5.id),
    text = "Cats 'meow' when they want to eat or play. But what if they want to curse you? It's truly possible!"
)

internal val storyChapter13 = DBStoryChapter(
    id = storyChapterIDList[12],
    storyID = storyIDList[3],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author1.id, author2.id, author3.id, author4.id),
    userID = user1.id,
    tagsIDs = listOf(tag1.id, tag5.id),
    text = "If you not feed your cat, it will feed yourself. Think about it."
)

internal val storyChapter14 = DBStoryChapter(
    id = storyChapterIDList[13],
    storyID = storyIDList[4],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author1.id, author5.id, author4.id),
    userID = user1.id,
    tagsIDs = listOf(tag1.id, tag5.id, tag9.id),
    text = "USA and USSR launch animals into space. Some cats are still there and they build a colony. We have proofs!"
)

internal val storyChapter15 = DBStoryChapter(
    id = storyChapterIDList[14],
    storyID = storyIDList[4],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author9.id),
    userID = user6.id,
    tagsIDs = listOf(tag1.id, tag5.id, tag9.id),
    text = "We think cats connect to another universes. They has no limits..."
)

internal val storyChapter16 = DBStoryChapter(
    id = storyChapterIDList[15],
    storyID = storyIDList[5],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author8.id),
    userID = user6.id,
    tagsIDs = listOf(tag3.id),
    text = "...and i write this secret in my next book. Search it in shops!"
)

internal val storyChapter17 = DBStoryChapter(
    id = storyChapterIDList[16],
    storyID = storyIDList[6],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author7.id),
    userID = user6.id,
    tagsIDs = listOf(tag9.id),
    text = "Stone age was age of computers, but nuclear war wipe it all. We saw it in fallout game."
)

internal val storyChapter18 = DBStoryChapter(
    id = storyChapterIDList[17],
    storyID = storyIDList[6],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author7.id),
    userID = user6.id,
    tagsIDs = listOf(tag9.id),
    text = "In iron age one man found a pip boy, and civilization restarted"
)

internal val storyChapter19 = DBStoryChapter(
    id = storyChapterIDList[18],
    storyID = storyIDList[7],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author6.id),
    userID = user6.id,
    tagsIDs = listOf(tag9.id, tag3.id),
    text = "We are full of computers nowadays. We hope this will help up and not punch us in new dark age."
)


internal val storyChapter20 = DBStoryChapter(
    id = storyChapterIDList[19],
    storyID = storyIDList[7],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author6.id),
    userID = user6.id,
    tagsIDs = emptyList(),
    text = "Hollywood was started from land buying and from a big enthusiasm of some people, who like movies"
)

internal val storyChapter21 = DBStoryChapter(
    id = storyChapterIDList[20],
    storyID = storyIDList[7],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author5.id),
    userID = user6.id,
    tagsIDs = listOf(tag4.id, tag6.id),
    text = "But the heart of Hollywood is start, of course. That is what people like, and that is the central."
)

internal val storyChapter22 = DBStoryChapter(
    id = storyChapterIDList[21],
    storyID = storyIDList[8],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author4.id),
    userID = user6.id,
    tagsIDs = listOf(tag3.id),
    text = "Well, before Europe came to America, it was wild lands because of it's isolation."
)

internal val storyChapter23 = DBStoryChapter(
    id = storyChapterIDList[22],
    storyID = storyIDList[8],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author3.id),
    userID = user6.id,
    tagsIDs = listOf(tag3.id, tag4.id),
    text = "World war two was the greatest tragedy, and greatest change to America. And we take this chance."
)

internal val storyChapter24 = DBStoryChapter(
    id = storyChapterIDList[23],
    storyID = storyIDList[8],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author2.id),
    userID = user4.id,
    tagsIDs = listOf(tag3.id),
    text = "Now we have several presidents: Bush, Obama, Tramp, Biden. Are they make America great? I don't know."
)

internal val storyChapter25 = DBStoryChapter(
    id = storyChapterIDList[24],
    storyID = storyIDList[9],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author1.id),
    userID = user5.id,
    tagsIDs = listOf(tag5.id),
    text = "I lost my keys. It already happened yesterday. Hmm. Wait. How i get inside my house if i lost my keys?"
)

internal val storyChapter26 = DBStoryChapter(
    id = storyChapterIDList[25],
    storyID = storyIDList[9],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = null,
    userID = user6.id,
    tagsIDs = listOf(tag5.id),
    text = "I just cooked my breakfast and its...gone. Who steal it? Reveal yourself! Ahh, i found it."
)

internal val storyChapter27 = DBStoryChapter(
    id = storyChapterIDList[26],
    storyID = storyIDList[9],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author7.id, author8.id),
    userID = user7.id,
    tagsIDs = listOf(tag5.id),
    text = "It was Austria, not Australia! F**k!"
)

internal val storyChapter28 = DBStoryChapter(
    id = storyChapterIDList[27],
    storyID = storyIDList[10],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author7.id, author8.id),
    userID = user8.id,
    tagsIDs = listOf(tag9.id),
    text = "Concept of most popular games is simple. Work hard, learn on other products and listen to your clients."
)

internal val storyChapter29 = DBStoryChapter(
    id = storyChapterIDList[28],
    storyID = storyIDList[10],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author7.id, author8.id),
    userID = user9.id,
    tagsIDs = listOf(tag9.id),
    text = "D2 and Minecraft has core mechanic that was reused by a lot of games after them. So yes, D2 and Minecraft are core."
)

internal val storyChapter30 = DBStoryChapter(
    id = storyChapterIDList[29],
    storyID = storyIDList[10],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author7.id),
    userID = user1.id,
    tagsIDs = listOf(tag9.id),
    text = "Well, Witcher battle system is good, but not as good as Gothic 3 system. Try it, and you will not regret."
)

internal val storyChapter31 = DBStoryChapter(
    id = storyChapterIDList[30],
    storyID = storyIDList[11],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author2.id),
    userID = user2.id,
    tagsIDs = listOf(tag9.id),
    text = "I don't really know how AI works, but i must to sell this book to make money for my wife, she want a new house. I hope you understand."
)

internal val storyChapter32 = DBStoryChapter(
    id = storyChapterIDList[31],
    storyID = storyIDList[11],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author1.id, author9.id),
    userID = user3.id,
    tagsIDs = listOf(tag9.id),
    text = "As far as i know, people are so scared by possibility of this scenario, that they make all to prevent it, even if it about destroy all their AI progress."
)

internal val storyChapter33 = DBStoryChapter(
    id = storyChapterIDList[32],
    storyID = storyIDList[11],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author1.id, author9.id),
    userID = user4.id,
    tagsIDs = listOf(tag9.id, tag10.id),
    text = "All your actions placed in dataset to analyze, so analyze what your doing to be unpredictable."
)

internal val storyChapter34 = DBStoryChapter(
    id = storyChapterIDList[33],
    storyID = storyIDList[11],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author1.id, author9.id),
    userID = user5.id,
    tagsIDs = listOf(tag9.id, tag10.id),
    text = "Summary, AI is a great technology that replace all of us in the feature. Bad news for people."
)

internal val storyChapter35 = DBStoryChapter(
    id = storyChapterIDList[34],
    storyID = storyIDList[11],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author3.id),
    userID = user6.id,
    tagsIDs = listOf(tag9.id, tag10.id),
    text = "ChapGPT is first step to global information analyzer. It's a feature that already there."
)

internal val storyChapter36 = DBStoryChapter(
    id = storyChapterIDList[35],
    storyID = storyIDList[12],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author5.id),
    userID = user6.id,
    tagsIDs = listOf(tag7.id),
    text = "Boat? Oh, yes, it's a funny story. My boat was eaten by shark. It eat me too. You don't believe me? Why?"
)

internal val storyChapter37 = DBStoryChapter(
    id = storyChapterIDList[36],
    storyID = storyIDList[13],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = null,
    userID = user7.id,
    tagsIDs = listOf(tag7.id),
    text = "Kids are loud and destructive. It's good for you to watch them their bad sides, if they available to understand it."
)

internal val storyChapter38 = DBStoryChapter(
    id = storyChapterIDList[37],
    storyID = storyIDList[13],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = null,
    userID = user8.id,
    tagsIDs = listOf(tag7.id, tag5.id),
    text = "It's better for father to make a conversation about sex as quick as possible to prevent unpredictable pregnant."
)

internal val storyChapter39 = DBStoryChapter(
    id = storyChapterIDList[38],
    storyID = storyIDList[13],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = null,
    userID = user9.id,
    tagsIDs = listOf(tag7.id, tag5.id, tag3.id),
    text = "Kid should love mother and respect father. It's a good scenario."
)

internal val storyChapter40 = DBStoryChapter(
    id = storyChapterIDList[39],
    storyID = storyIDList[14],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author7.id),
    userID = user1.id,
    tagsIDs = listOf(tag3.id),
    text = "Hello! i'm popular pop singer. Buy my music on Amazon!"
)

internal val storyChapter41 = DBStoryChapter(
    id = storyChapterIDList[40],
    storyID = storyIDList[14],
    languageID = 1,
    availableLanguagesIDs = listOf(1),
    authorsIDs = listOf(author7.id),
    userID = user2.id,
    tagsIDs = listOf(tag4.id),
    text = "This book is about how i cool. Hah. In fact, i don't need any book, because it's obvious."
)

internal val storyChapter42 = DBStoryChapter(
    id = storyChapterIDList[41],
    storyID = storyIDList[14],
    languageID = 2,
    availableLanguagesIDs = listOf(2),
    authorsIDs = listOf(author7.id),
    userID = user3.id,
    tagsIDs = listOf(tag4.id, tag5.id),
    text = "My first step in music was when i fallen to mud. I felt so musical! Next day i wrote album about it."
)

internal val storyChapter43 = DBStoryChapter(
    id = storyChapterIDList[42],
    storyID = storyIDList[14],
    languageID = 3,
    availableLanguagesIDs = listOf(3),
    authorsIDs = listOf(author7.id),
    userID = user4.id,
    tagsIDs = emptyList(),
    text = "First million is first real troubles: a lot of criminals and taxes came to you life. But after you manage it, all will be fine!"
)

internal val storyChapter44 = DBStoryChapter(
    id = storyChapterIDList[43],
    storyID = storyIDList[14],
    languageID = 4,
    availableLanguagesIDs = listOf(4),
    authorsIDs = listOf(author7.id),
    userID = user5.id,
    tagsIDs = listOf(tag3.id),
    text = "My best fans: father, mother, sister, cat Tom and mouse Jerry."
)

internal val storyChapters = listOf(
    storyChapter1,
    storyChapter2,
    storyChapter3,
    storyChapter4,
    storyChapter5,
    storyChapter6,
    storyChapter7,
    storyChapter8,
    storyChapter9,
    storyChapter10,
    storyChapter11,
    storyChapter12,
    storyChapter13,
    storyChapter14,
    storyChapter15,
    storyChapter16,
    storyChapter17,
    storyChapter18,
    storyChapter19,
    storyChapter20,
    storyChapter21,
    storyChapter22,
    storyChapter23,
    storyChapter24,
    storyChapter25,
    storyChapter26,
    storyChapter27,
    storyChapter28,
    storyChapter29,
    storyChapter30,
    storyChapter31,
    storyChapter32,
    storyChapter33,
    storyChapter34,
    storyChapter35,
    storyChapter36,
    storyChapter37,
    storyChapter38,
    storyChapter39,
    storyChapter40,
    storyChapter41,
    storyChapter42,
    storyChapter43,
    storyChapter44
)