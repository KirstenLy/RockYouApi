package database.internal.test.model

import database.external.test.*

internal class TestModelsStorage(
    val languages: List<TestLanguage>,
    val users: List<TestUser>,
    val usersAuthData: List<TestUserAuthData>,
    val authors: List<TestAuthor>,
    val contentRegisters: List<TestContentRegister>,
    val pictures: List<TestPicture>,
    val videos: List<TestVideo>,
    val stories: List<TestStory>,
    val storyChapters: List<TestStoryChapter>, // TODO: Это не нужно
    val storyNodes: List<Pair<Int, List<TestStoryNode>>>,
    val favorites: List<TestFavorite>,
    val tags: List<TestTag>,
    val comments: List<TestComment>,
)