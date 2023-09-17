package rockyouapi

import database.external.test.database.DatabaseAPIAdditionalScenarios
import database.external.test.*
import rockyouapi.auth.UserTokensManager

internal class TestEnv(
    val tokenManager: UserTokensManager,
    val modelsStorage: TestModelsStorage,
    val testDBApi: DatabaseAPIAdditionalScenarios
)

internal class TestModelsStorage(
    val languages: List<TestLanguage>,
    val users: List<TestUser>,
    val usersAuthData: List<TestUserAuthData>,
    val authors: List<TestAuthor>,
    val contentRegisters: List<TestContentRegister>,
    val pictures: List<TestPicture>,
    val videos: List<TestVideo>,
    val stories: List<TestStory>,
    val storyChapters: List<TestStoryChapter>,
    val storyNodes: List<Pair<Int, List<TestStoryNode>>>,
    val favorites: List<TestFavorite>,
    val tags: List<TestTag>,
    val comments: List<TestComment>,
)