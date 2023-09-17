package database.internal.creator.production

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.mysql.cj.jdbc.MysqlDataSource
import database.external.DatabaseAPI
import database.internal.AvailableLanguageModel
import database.internal.DatabaseAPIImpl
import database.internal.LanguageTranslationModel
import database.internal.entity.auth_data.AuthDataTable
import database.internal.entity.author.AuthorTable
import database.internal.entity.chapter.ChapterTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.favorite.FavoriteTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.picture.PictureTable
import database.internal.entity.chapter.relation.RelationChapterAndAuthorTable
import database.internal.entity.chapter.relation.RelationChapterAndLanguageTable
import database.internal.entity.chapter.relation.RelationChapterAndTagTable
import database.internal.entity.lang.RelationLangAndTranslationTable
import database.internal.entity.lang_translation.LangTranslationTable
import database.internal.entity.picture.relation.RelationPictureAndAuthorTable
import database.internal.entity.picture.relation.RelationPictureAndLanguageTable
import database.internal.entity.picture.relation.RelationPictureAndTagTable
import database.internal.entity.story.relation.RelationStoryAndAuthorTable
import database.internal.entity.story.relation.RelationStoryAndChapterTable
import database.internal.entity.story.relation.RelationStoryAndLanguageTable
import database.internal.entity.story.relation.RelationStoryAndTagTable
import database.internal.entity.tag.RelationTagAndTranslationTable
import database.internal.entity.video.relation.RelationVideoAndAuthorTable
import database.internal.entity.video.relation.RelationVideoAndLanguageTable
import database.internal.entity.video.relation.RelationVideoAndTagTable
import database.internal.entity.report.ReportTable
import database.internal.entity.story.StoryTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.user.UserTable
import database.internal.entity.video.VideoTable
import database.internal.entity.vote_history.VoteHistoryTable
import database.internal.executor.*
import database.internal.executor.GetCommentsRequestExecutor
import database.internal.executor.GetPictureListRequestExecutor
import database.internal.executor.GetVideoByIDRequestExecutor
import database.internal.executor.GetVideosListRequestExecutor
import database.internal.test.fillFullByGeneratedContent
import database.internal.test.model.TestModelsStorage
import declaration.DatabaseConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import rockyouapi.DBTest

/**
 * Database connected to real database created into file system when test start.
 * Database created, connected and filled by builder.
 * */
internal fun initProductionDatabase(connectionConfig: DatabaseConfiguration): DatabaseAPI {
    val database = createDB(connectionConfig)

    // TODO: Тут нужен метод в духе dropAllTables
//    if (connectionConfig.isNeedToDropTablesAndFillByMocks) {
//        database.fillFullByGeneratedContent()
//    }

    // Prefetch some regular required data
    val availableLanguagesResult = database.languageQueries
        .selectAllWithTranslations()
        .executeAsList()

    val availableLanguages = mutableListOf<AvailableLanguageModel>()
    availableLanguagesResult.groupBy { it.id }.forEach { t, u ->
        availableLanguages.add(
            AvailableLanguageModel(
                languageID = t.toByte(),
                isDefault = u.all { it.isDefault },
                translations = u.map { LanguageTranslationModel(it.langID.toByte(), it.envID.toByte(),  it.translation) }
            )
        )
    }

    val getPictureByIDRequestExecutor = GetPictureByIDRequestExecutor(
        database = database,
        availableLanguages = availableLanguages
    )

    val getVideoByIDRequestExecutor = GetVideoByIDRequestExecutor(
        database = database,
        availableLanguages = availableLanguages
    )

    val getChaptersRequestExecutor = GetChaptersRequestExecutor(
        database = database,
        availableLanguages = availableLanguages
    )
    val getStoryNodesRequestExecutor = GetStoryNodesRequestExecutor(
        database = database,
        supportedLanguages = availableLanguages
    )

    val getStoryByIDRequestExecutor = GetStoryByIDRequestExecutor(
        database = database,
        availableLanguages = availableLanguages,
        storyChaptersRequestExecutor = getChaptersRequestExecutor,
    )

    return DatabaseAPIImpl(
        getPictureListRequestExecutor = GetPictureListRequestExecutor(
            database = database,
            availableLanguages = availableLanguages,
        ),
        getPictureByIDRequestExecutor = getPictureByIDRequestExecutor,
        getVideoByIDRequestExecutor = GetVideoByIDRequestExecutor(
            database = database,
            availableLanguages = availableLanguages
        ),
        getVideosListRequestExecutor = GetVideosListRequestExecutor(
            database = database,
            availableLanguages = availableLanguages
        ),
        getStoriesChaptersRequestExecutor = getChaptersRequestExecutor,
        getStoriesRequestExecutor = GetStoriesRequestExecutor(
            database = database,
            supportedLanguages = availableLanguages,
            storyByIDRequestExecutor = GetStoryByIDRequestExecutor(
                database = database,
                availableLanguages = availableLanguages,
                storyChaptersRequestExecutor = getChaptersRequestExecutor,
            ),
        ),
        getStoryByIDRequestExecutor = GetStoryByIDRequestExecutor(
            database = database,
            availableLanguages = availableLanguages,
            storyChaptersRequestExecutor = getChaptersRequestExecutor,
        ),
        getChapterTextByIDRequestExecutor = GetChapterTextByIDRequestExecutor(
            database = database
        ),
        getContentByTextRequestExecutor = GetContentByTextRequestExecutor(
            getPicturesByTextRequestExecutor = GetPicturesByTextRequestExecutor(
                database = database,
                getPictureByIDRequestExecutor = getPictureByIDRequestExecutor
            ),
            getVideosByTextRequestExecutor = GetVideosByTextRequestExecutor(
                database = database,
                getVideoByIDRequestExecutor = getVideoByIDRequestExecutor,
            ),
            getStoriesByTextRequestExecutor = GetStoriesByTextRequestExecutor(
                database = database,
                getStoryByIDRequestExecutor = getStoryByIDRequestExecutor
            ),
            getChaptersByTextRequestExecutor = GetChaptersByTextRequestExecutor(
                database = database,
                getStoryByIDRequestExecutor = getChaptersRequestExecutor
            )
        ),
        getCommentsRequestExecutor = GetCommentsRequestExecutor(
            database = database
        ),
        createUserRequestExecutor = CreateUserRequestExecutor(
            database = database
        ),
        checkUserCredentialsExecutor = CheckUserCredentialsExecutor(
            database = database
        ),
        addCommentRequestExecutor = AddCommentRequestExecutor(
            database = database
        ),
        addOrRemoveFavoriteRequestExecutor = AddOrRemoveFavoriteRequestExecutor(
            database = database
        ),
        reportRequestExecutor = ReportRequestExecutor(
            database = database
        ),
        voteRequestExecutor = VoteRequestExecutor(
            database = database
        )
    )
}

internal fun createDB(connectionConfig: DatabaseConfiguration): DBTest {
    val driver = MysqlDataSource().apply {
        databaseName = "DBTest"
        createDatabaseIfNotExist = true
        setURL(connectionConfig.url)
        user = connectionConfig.user
        password = connectionConfig.password
    }.asJdbcDriver()

    if (connectionConfig.isNeedToDropTablesAndFillByMocks) {
        driver.dropDatabase("DBTest")
        val database = DBTest(driver)
        DBTest.Schema.migrate(
            driver = driver,
            oldVersion = 1,
            newVersion = DBTest.Schema.version
        )
        database.fillFullByGeneratedContent()
        return database
    }

    val isDatabaseExist = driver.executeIsDatabaseExist()
    if (!isDatabaseExist) {
        DBTest.Schema.migrate(
            driver = driver,
            oldVersion = 1,
            newVersion = DBTest.Schema.version
        )
        return DBTest(driver)
    }

    val currentDatabaseVersion = driver.executeGetDatabaseVersion()
    if (currentDatabaseVersion != DBTest.Schema.version) {
        DBTest.Schema.migrate(
            driver = driver,
            oldVersion = currentDatabaseVersion,
            newVersion = DBTest.Schema.version
        )
    }

    return DBTest(driver)
}

internal fun createDBForTest(connectionConfig: DatabaseConfiguration): Pair<DBTest, TestModelsStorage> {
    val driver = MysqlDataSource().apply {
        databaseName = "DBTest"
        createDatabaseIfNotExist = true
        setURL(connectionConfig.url)
        user = connectionConfig.user
        password = connectionConfig.password
    }.asJdbcDriver()

    driver.dropDatabase("DBTest")
    val database = DBTest(driver)
    DBTest.Schema.migrate(
        driver = driver,
        oldVersion = 1,
        newVersion = DBTest.Schema.version
    )
    val storage = database.fillFullByGeneratedContent()
    return database to storage
}

private fun createDB1(connectionConfig: DatabaseConfiguration) {
    Database.connect(
        url = connectionConfig.url,
        driver = connectionConfig.driver,
        user = connectionConfig.user,
        password = connectionConfig.password
    )

    transaction {
        createAuthDataTable()

        createContentTable()
        createImageEntityTables()
        createVideoEntityTables()
        createStoryEntityTables()
        createChapterEntityTables()
        createTagEntityTables()
        createLangEntityTables()
        createUserEntityTables()
        createAuthorEntityTables()

        createVoteHistoryTable()

        createCommentTable()

        createFavoriteTable()

        createReportTable()
    }
}

//region Создание таблиц. Методы обязательно должны быть экстеншенами над Transaction!
private fun Transaction.createContentTable() {
    SchemaUtils.create(ContentRegisterTable)
}

private fun Transaction.createImageEntityTables() {
    SchemaUtils.create(PictureTable)

    SchemaUtils.create(RelationPictureAndAuthorTable)
    SchemaUtils.create(RelationPictureAndLanguageTable)
    SchemaUtils.create(RelationPictureAndTagTable)
}

private fun Transaction.createVideoEntityTables() {
    SchemaUtils.create(VideoTable)
    SchemaUtils.create(RelationVideoAndAuthorTable)
    SchemaUtils.create(RelationVideoAndLanguageTable)
    SchemaUtils.create(RelationVideoAndTagTable)
}

private fun Transaction.createStoryEntityTables() {
    SchemaUtils.create(StoryTable)
    SchemaUtils.create(RelationStoryAndTagTable)
    SchemaUtils.create(RelationStoryAndLanguageTable)
    SchemaUtils.create(RelationStoryAndAuthorTable)
    SchemaUtils.create(RelationStoryAndChapterTable)
}

private fun Transaction.createChapterEntityTables() {
    SchemaUtils.create(ChapterTable)
    SchemaUtils.create(RelationChapterAndTagTable)
    SchemaUtils.create(RelationChapterAndLanguageTable)
    SchemaUtils.create(RelationChapterAndAuthorTable)
}

private fun Transaction.createTagEntityTables() {
    SchemaUtils.create(TagTable)
    SchemaUtils.create(TagTranslationTable)
    SchemaUtils.create(RelationTagAndTranslationTable)
}

private fun Transaction.createLangEntityTables() {
    SchemaUtils.create(LanguageTable)
    SchemaUtils.create(LangTranslationTable)
    SchemaUtils.create(RelationLangAndTranslationTable)
}

private fun Transaction.createUserEntityTables() {
    SchemaUtils.create(UserTable)
}

private fun Transaction.createAuthorEntityTables() {
    SchemaUtils.create(AuthorTable)
}

private fun Transaction.createAuthDataTable() {
    SchemaUtils.create(AuthDataTable)
}

private fun Transaction.createVoteHistoryTable() {
    SchemaUtils.create(VoteHistoryTable)
}

private fun Transaction.createCommentTable() {
    SchemaUtils.create(CommentTable)
}

private fun Transaction.createFavoriteTable() {
    SchemaUtils.create(FavoriteTable)
}

private fun Transaction.createReportTable() {
    SchemaUtils.create(ReportTable)
}
//endregion