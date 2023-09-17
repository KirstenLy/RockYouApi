package database.external

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mysql.cj.jdbc.MysqlDataSource
import database.external.filter.*
import database.external.result.*
import database.internal.VoteType
import database.internal.creator.initMockDatabaseApi
import database.internal.creator.initProductionDatabase
import database.internal.creator.initTestDatabaseApi
import database.internal.executor.TestReportRequestExecutor
import declaration.DatabaseConnectionConfig
import declaration.GetContentByTextResult
import declaration.entity.Comment
import declaration.entity.Picture
import declaration.entity.Story
import declaration.entity.Video
import rockyouapi.DBTest
import kotlin.random.Random

/** API of Database feature.*/
object DatabaseFeature {

    /** Init production database. Filled by predefined SQL script. */
    fun connectToProductionDatabase(connectionConfig: DatabaseConnectionConfig) =
        initProductionDatabase(connectionConfig)

    /** Init test database. Database filled by not valuable responses */
    fun connectToMockDatabase() = initMockDatabaseApi()

    /** Init test database. Database constructed by builder. */
    fun connectToTestDatabase(dataBuilder: TestDatabaseBuilder.() -> DatabaseAPI) = initTestDatabaseApi(dataBuilder)

    fun connectToTestDatabase1(): DatabaseAPI {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DBTest.Schema.create(driver)
        val db = DBTest(driver)
        return object : DatabaseAPI {
            private var kek: DBTest = db

            override suspend fun createUser(login: String, password: String): CreateUserResult {
                TODO("Not yet implemented")
            }

            override suspend fun isUserCredentialsCorrect(name: String, password: String): CheckUserCredentialsResult {
                TODO("Not yet implemented")
            }

            override suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture> {
                TODO("Not yet implemented")
            }

            override suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
                TODO("Not yet implemented")
            }

            override suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video> {
                TODO("Not yet implemented")
            }

            override suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video> {
                TODO("Not yet implemented")
            }

            override suspend fun getStories(filter: StoryListFilter): SimpleListResult<Story> {
                TODO("Not yet implemented")
            }

            override suspend fun getStoryByID(contentID: Int): SimpleOptionalDataResult<Story> {
                TODO("Not yet implemented")
            }

            override suspend fun getStoryChapterTextByID(contentID: Int): SimpleOptionalDataResult<String> {
                TODO("Not yet implemented")
            }

            override suspend fun getContentByText(text: String): SimpleDataResult<GetContentByTextResult> {
                TODO("Not yet implemented")
            }

            override suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment> {
                TODO("Not yet implemented")
            }

            override suspend fun addComment(userID: Int, contentID: Int, commentText: String): SimpleUnitResult {
                TODO("Not yet implemented")
            }

            override suspend fun upvote(userID: Int, contentID: Int, voteType: VoteType): VoteResult {
                TODO("Not yet implemented")
            }

            override suspend fun addToFavorite(userID: Int, contentID: Int): SimpleUnitResult {
                TODO("Not yet implemented")
            }

            override suspend fun removeFromFavorite(userID: Int, contentID: Int): SimpleUnitResult {
                TODO("Not yet implemented")
            }

            override suspend fun report(contentID: Int, reason: String, userID: Int?): SimpleUnitResult {
                val a = TestReportRequestExecutor(kek)
                a.execute(Random.nextInt(), contentID, reason, userID)
                return SimpleUnitResult.Ok
            }
        }
    }
}