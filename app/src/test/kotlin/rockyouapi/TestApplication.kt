package rockyouapi

import common.pairToRandomValueFrom
import common.takeRandomValues
import common.times
import database.external.ContentType
import database.external.DatabaseAPI
import database.external.DatabaseFeature.getMockDatabase
import database.external.DatabaseFeature.connectToTestDatabase
import database.external.test.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import rockyouapi.auth.UserIDPrincipal
import rockyouapi.auth.UserTokensManager
import rockyouapi.route.auth.authLoginRoute
import rockyouapi.route.auth.authRegisterRoute
import rockyouapi.route.comment.commentAddRoute
import rockyouapi.route.comment.commentListRoute
import rockyouapi.route.favorite.addOrRemoveFavoriteRoute
import rockyouapi.route.picture.pictureListRoute
import rockyouapi.route.picture.pictureReadByIDRoute
import rockyouapi.route.report.reportRoute
import rockyouapi.route.search.searchByTextRoute
import rockyouapi.route.story.storyListRoute
import rockyouapi.route.story.storyReadByIDRoute
import rockyouapi.route.story.storyReadChapterTextByIDRoute
import rockyouapi.route.video.videoListRoute
import rockyouapi.route.video.videoReadByIDRoute
import rockyouapi.route.vote.voteRoute

/** Configure test requirement without database and launch test into it. */
internal fun runTestInConfiguredApplicationWithoutDBConnection(
    tokenLiveTimeInSeconds: Int = TIME_TEST_TOKEN_LIFETIME_IN_SEC,
    passwordMinimumLength: Int = LENGTH_TEST_PASSWORD_MINIMUM_LENGTH,
    action: suspend ApplicationTestBuilder.() -> Unit
) {
    testApplication {
        val tokenManager = UserTokensManager(tokenLiveTimeInSeconds)

        configureEnv()
        configurePlugins(tokenManager)
        configureRouting(getMockDatabase(), tokenManager, passwordMinimumLength)

        action()
    }
}

/** Configure test requirement with database and launch test into it. */
internal fun runTestInConfiguredApplicationWithDBFullFilledFromScratch(
    tokenLiveTimeInSeconds: Int = TIME_TEST_TOKEN_LIFETIME_IN_SEC,
    passwordMinimumLength: Int = LENGTH_TEST_PASSWORD_MINIMUM_LENGTH,
    action: suspend ApplicationTestBuilder.(testEnv: TestEnv) -> Unit
) {
    testApplication {
        val users = getDefaultUsers()
        val usersAuthData = getUsersDefaultAuthData()
        val authors = getDefaultAuthors()
        val languages = getDefaultLanguages()
        val languagesIDs = languages.map(TestLanguage::id)
        val tags = generateTestTags(availableLanguagesIDs = languagesIDs)
        val usersIDs = users.map(TestUser::id)
        val pictures = generateTestPictures(
            availableUsersIDs = usersIDs,
            availableAuthorsIDs = authors.map(TestAuthor::id),
            availableLanguagesIDs = languagesIDs,
            availableTagsIDs = tags.map(TestTag::id)
        )
        val videos = generateTestVideos(
            availableUsersIDs = usersIDs,
            availableAuthorsIDs = authors.map(TestAuthor::id),
            availableLanguagesIDs = languagesIDs,
            availableTagsIDs = tags.map(TestTag::id)
        )

        val reservedChaptersCountList = List(DEFAULT_CONTENT_ENTITY_SIZE) { (1..20).random() }
        val reservedChaptersTotalCount = reservedChaptersCountList.sum()

        // TODO: Описать, почему reserved
        val registers = generateContentRegisters(
            picturesSize = pictures.size,
            videosSize = videos.size,
            storiesSize = DEFAULT_CONTENT_ENTITY_SIZE,
            chaptersSize = reservedChaptersTotalCount
        )

        val storiesRegisterRecords = registers.filter { it.contentType == ContentType.STORY.typeID }

        val stories1 = generateTestStories(
            storiesRegisterRecords = storiesRegisterRecords,
            reservedChaptersCountList = reservedChaptersCountList,
            availableUsersIDs = usersIDs,
            availableAuthorsIDs = authors.map(TestAuthor::id),
            availableLanguagesIDs = languagesIDs,
            availableTagsIDs = tags.map(TestTag::id)
        )

        val comments = registers
            .map(TestContentRegister::id)
            .times(20)
            .pairToRandomValueFrom(users)
            .takeRandomValues()
            .map { (registerID, user) -> TestCommentCreationData(registerID, user.id, user.name) }
            .let(::generateTestComments)

        val favorites = generateTestFavorites(
            availableContentIDs = registers.map(TestContentRegister::id),
            availableUsersIDs = usersIDs
        )

        val dbAPI = connectToTestDatabase(
            dataBuilder = {
                setLanguages(languages)
                setUsers(users)
                setUsersAuthData(usersAuthData)
                setAuthors(authors)
                setContentRegisters(registers)
                setPictures(pictures)
                setVideos(videos)
//                setStories(stories)
                setStories(stories1)
//                setStoryChapters(storiesChapters)
//                setStoriesNodes(storiesNodes)
                setFavorites(favorites)
                setTags(tags)
                setComments(comments)
                build()
            },
        )

        val tokenManager = UserTokensManager(tokenLiveTimeInSeconds)
        configureEnv()
        configurePlugins(tokenManager)
        configureRouting(dbAPI.mainDatabaseAPI, tokenManager, passwordMinimumLength)

        val testModels = TestModelsStorage(
            languages = languages,
            users = users,
            usersAuthData = usersAuthData,
            authors = authors,
            contentRegisters = registers,
            pictures = pictures,
            videos = videos,
            stories = stories1,
//            stories = stories,
//            storyChapters = storiesChapters,
            storyChapters = emptyList(),
//            storyNodes = storiesNodes,
            storyNodes = emptyList(),
            favorites = favorites,
            tags = tags,
            comments = comments,
        )
        action(TestEnv(tokenManager, testModels, dbAPI.additionalDatabaseAPI))
    }
}

internal fun ApplicationTestBuilder.configureEnv() {
    environment {
        config = MapApplicationConfig()
    }
}

internal fun ApplicationTestBuilder.configurePlugins(tokensStorage: UserTokensManager) {
    install(ContentNegotiation) { json() }
    install(IgnoreTrailingSlash)

    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                val token = tokenCredential.token
                if (tokensStorage.isTokenExistAndValid(token)) {
                    tokensStorage.getTokenOwnerID(token)?.let(::UserIDPrincipal)
                } else {
                    null
                }
            }
        }
    }
}

internal fun ApplicationTestBuilder.configureRouting(
    databaseAPI: DatabaseAPI,
    tokensStorage: UserTokensManager,
    passwordMinimumLength: Int
) {
    routing {
        authRegisterRoute(databaseAPI, tokensStorage, passwordMinimumLength)
        authLoginRoute(databaseAPI, tokensStorage)

        pictureListRoute(databaseAPI)
        pictureReadByIDRoute(databaseAPI)

        videoListRoute(databaseAPI)
        videoReadByIDRoute(databaseAPI)

        storyListRoute(databaseAPI)
        storyReadByIDRoute(databaseAPI)
        storyReadChapterTextByIDRoute(databaseAPI)

        searchByTextRoute(databaseAPI, 3)

        addOrRemoveFavoriteRoute(databaseAPI)

        reportRoute(databaseAPI, tokensStorage)

        voteRoute(databaseAPI)

        commentAddRoute(databaseAPI)
        commentListRoute(databaseAPI)
    }
}