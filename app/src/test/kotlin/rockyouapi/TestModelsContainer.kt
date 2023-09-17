package rockyouapi

import database.external.model.*

internal class TestModelsContainer(
    val languages: List<TestLanguage>,
    val users: List<TestUser>,
    val authors: List<TestAuthor>,
    val contentRegisters: List<TestContentRegister>,
    val pictures: List<TestPicture>,
    val videos: List<TestVideo>,
    val stories: List<TestStory>,
    val storyChapters: List<TestStoryChapter>,
    val tags: List<TestTag>,
    val comments: List<TestComment>,
)