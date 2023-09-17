package database.internal.creator

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.mysql.cj.jdbc.MysqlDataSource
import database.external.DatabaseAPI
import database.internal.DatabaseAPIImpl1
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
import database.internal.executor.GetPictureByIDRequestExecutor
import declaration.DatabaseConnectionConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import rockyouapi.DBTest

internal fun initProductionDatabase(connectionConfig: DatabaseConnectionConfig): DatabaseAPI {
//    createDB(connectionConfig)


    val dataSource = MysqlDataSource()
    dataSource.databaseName = "DBTest"
    dataSource.createDatabaseIfNotExist = true
    dataSource.setURL(connectionConfig.url)
    dataSource.user = connectionConfig.user
    dataSource.password = connectionConfig.password

    // TODO: ТЕСТЫ???
    val driver = dataSource.asJdbcDriver()
    val database = DBTest(driver)
    try {
        DBTest.Schema.create(driver)
    } catch (t: Throwable) {
        Unit
    }

    // Prefetch some regular required data
    val availableLanguagesIDs = database.languageQueries
        .selectAll()
        .executeAsList()

    return DatabaseAPIImpl1(
        db = database,
        getPictureByIDRequestExecutor = GetPictureByIDRequestExecutor(
            database = database,
            supportedLanguages = availableLanguagesIDs
        )
    )
}

private fun createDB(connectionConfig: DatabaseConnectionConfig) {
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