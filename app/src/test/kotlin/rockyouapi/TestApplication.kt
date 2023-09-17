package rockyouapi

import common.pairToRandomValueFrom
import rockyouapi.route.picture.pictureListRoute
import rockyouapi.route.picture.pictureReadByIDRoute
import rockyouapi.route.search.searchByTextRoute
import rockyouapi.route.story.storyListRoute
import rockyouapi.route.story.storyReadByIDRoute
import rockyouapi.route.story.storyReadChapterTextByIDRoute
import rockyouapi.route.video.videoListRoute
import rockyouapi.route.video.videoReadByIDRoute
import database.external.DatabaseAPI
import database.external.DatabaseFeature.connectToMockDatabase
import database.external.DatabaseFeature.connectToTestDatabase
import database.external.TestDatabaseBuilder
import database.external.model.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*

/** Configure test requirement with database and launch test into it. */
internal fun runTestInConfiguredApplicationWithoutDBConnection(
    action: suspend ApplicationTestBuilder.() -> Unit
) {
    testApplication {
        configureEnv()
        configurePlugins()
        configureRouting(connectToMockDatabase())

        action()
    }
}

/** Configure test requirement without database and launch test into it. */ // TODO: Убрать дефолтный dataBuilder отсюда
internal fun runTestInConfiguredApplicationWithDBConnection(
    dataBuilder: TestDatabaseBuilder.() -> DatabaseAPI = { build() },
    action: suspend ApplicationTestBuilder.() -> Unit
) {
    testApplication {
        val dbAPI = connectToTestDatabase(dataBuilder)

        configureEnv()
        configurePlugins()
        configureRouting(dbAPI)

        action()
    }
}

internal fun runTestInConfiguredApplicationWithDBFullFilledFromScratch(
    action: suspend ApplicationTestBuilder.(testModels: TestModelsContainer) -> Unit
) {
    testApplication {
        val users = getDefaultUsers()
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

        val registers = generateContentRegisters()
        val comments = registers
            .getRegisterIDsByContentType(1)
            .pairToRandomValueFrom(usersIDs)
            .let(::generateTestComments)

        val dbAPI = connectToTestDatabase(
            dataBuilder = {
                setLanguages(languages)
                setUsers(users)
                setAuthors(authors)
                setContentRegisters(registers)
                setPictures(pictures)
                setVideos(generateTestVideos())
                setStories(generateTestStories())
                setStoryChapters(generateTestStoriesChapters())
                setTags(tags)
                setComments(comments)
                build()
            },
        )

        configureEnv()
        configurePlugins()
        configureRouting(dbAPI)

        val testModels = TestModelsContainer(
            languages = languages,
            users = users,
            authors = authors,
            contentRegisters = registers,
            pictures = pictures,
            videos = generateTestVideos(),
            stories = generateTestStories(),
            storyChapters = generateTestStoriesChapters(),
            tags = tags,
            comments = comments,
        )
        action(testModels)
    }
}

internal fun ApplicationTestBuilder.configureEnv() {
    environment {
        config = MapApplicationConfig()
    }
}

internal fun ApplicationTestBuilder.configurePlugins() {
    install(ContentNegotiation) { json() }
    install(IgnoreTrailingSlash)
}

internal fun ApplicationTestBuilder.configureRouting(databaseAPI: DatabaseAPI) {
    routing {
        pictureListRoute(databaseAPI)
        pictureReadByIDRoute(databaseAPI)

        videoListRoute(databaseAPI)
        videoReadByIDRoute(databaseAPI)

        storyListRoute(databaseAPI)
        storyReadByIDRoute(databaseAPI)
        storyReadChapterTextByIDRoute(databaseAPI)

        searchByTextRoute(databaseAPI, 3)
    }
}