package database.internal.creator

import database.external.contract.ProductionDatabaseAPI
import database.external.exception.DefaultLanguageMissedException
import database.external.exception.LanguagesMissedException
import database.external.exception.TranslationForLanguageMissedException
import database.external.exception.TranslationsMissedException
import database.external.result.GetAllLanguageWithTranslationsResult
import database.internal.ProductionDatabaseAPIImpl
import database.internal.executor.*
import rockyouapi.Database

/** [ProductionDatabaseAPI] creator */
@Throws(
    LanguagesMissedException::class,
    DefaultLanguageMissedException::class,
    TranslationsMissedException::class,
    TranslationForLanguageMissedException::class
)
internal suspend fun createProductionDatabaseAPI(database: Database): ProductionDatabaseAPI {

    // Prefetch some regular required data
    val readAllLanguageWithTranslationsListExecutor = ReadAllLanguageWithTranslationsListExecutor(database)
    val getSupportedLanguageListResult = readAllLanguageWithTranslationsListExecutor.execute()
    val supportedLanguageList = when (getSupportedLanguageListResult) {
        is GetAllLanguageWithTranslationsResult.Data -> getSupportedLanguageListResult.languageFullList
        is GetAllLanguageWithTranslationsResult.LanguagesNotExist -> throw LanguagesMissedException()
        is GetAllLanguageWithTranslationsResult.DefaultLanguageMissed -> throw DefaultLanguageMissedException()
        is GetAllLanguageWithTranslationsResult.TranslationsNotExist -> throw TranslationsMissedException()
        is GetAllLanguageWithTranslationsResult.TranslationsMissed -> throw TranslationForLanguageMissedException()
        is GetAllLanguageWithTranslationsResult.Error -> throw getSupportedLanguageListResult.t
    }

    val readAuthorsForSingleContentExecutor = ReadAuthorsForSingleContentExecutor(database)
    val readLanguagesForSingleContentExecutor = ReadLanguagesForSingleContentExecutor(database)
    val readTagsForSingleContentExecutor = ReadTagsForSingleContentExecutor(database)
    val readGroupsForContentExecutor = ReadGroupsForContentExecutor(database)
    val readCharactersForSingleContentExecutor = ReadCharactersForSingleContentExecutor(database)

    val readAuthorsForMultipleContentExecutor = ReadAuthorsForMultipleContentExecutor(database)
    val readLanguagesForMultipleContentExecutor = ReadLanguagesForMultipleContentExecutor(database)
    val readTagsForMultipleContentExecutor = ReadTagsForMultipleContentExecutor(database)
    val readGroupsForMultipleContentExecutor = ReadGroupsForMultipleContentExecutor(database)
    val readCharactersForMultipleContentExecutor = ReadCharactersForMultipleContentExecutor(database)

    val readPictureListExecutor = ReadPictureListExecutor(
        database = database,
        languageList = supportedLanguageList,
        readAuthorsForMultipleContentExecutor = readAuthorsForMultipleContentExecutor,
        readLanguagesForMultipleContentExecutor = readLanguagesForMultipleContentExecutor,
        readTagsForMultipleContentExecutor = readTagsForMultipleContentExecutor,
        readGroupsForMultipleContentExecutor = readGroupsForMultipleContentExecutor,
        readCharactersForMultipleContentExecutor = readCharactersForMultipleContentExecutor
    )

    val getVideoListExecutor = GetVideoListExecutor(
        database = database,
        languageFullList = supportedLanguageList,
        readAuthorsForMultipleContentExecutor = readAuthorsForMultipleContentExecutor,
        readLanguagesForMultipleContentExecutor = readLanguagesForMultipleContentExecutor,
        readTagsForMultipleContentExecutor = readTagsForMultipleContentExecutor,
        readGroupsForMultipleContentExecutor = readGroupsForMultipleContentExecutor,
        readCharactersForMultipleContentExecutor = readCharactersForMultipleContentExecutor
    )

    val readStoryListExecutor = ReadStoryListExecutor(
        database = database,
        languageList = supportedLanguageList,
        readAuthorsForMultipleContentExecutor = readAuthorsForMultipleContentExecutor,
        readLanguagesForMultipleContentExecutor = readLanguagesForMultipleContentExecutor,
        readTagsForMultipleContentExecutor = readTagsForMultipleContentExecutor,
        readGroupsForMultipleContentExecutor = readGroupsForMultipleContentExecutor,
        readCharactersForMultipleContentExecutor = readCharactersForMultipleContentExecutor,
        readChaptersExecutor = ReadChaptersExecutor(
            database = database,
            languageFullList = supportedLanguageList,
            readAuthorsForMultipleContentExecutor = readAuthorsForMultipleContentExecutor,
            readLanguagesForContentListExecutor = readLanguagesForMultipleContentExecutor,
            readTagsForContentListExecutor = readTagsForMultipleContentExecutor,
        ),
    )

    val readChaptersExecutor = ReadChaptersExecutor(
        database = database,
        languageFullList = supportedLanguageList,
        readAuthorsForMultipleContentExecutor = readAuthorsForMultipleContentExecutor,
        readLanguagesForContentListExecutor = readLanguagesForMultipleContentExecutor,
        readTagsForContentListExecutor = readTagsForMultipleContentExecutor
    )

    return ProductionDatabaseAPIImpl(
        readPictureListExecutor = readPictureListExecutor,
        readPictureByIDExecutor = ReadPictureByIDExecutor(
            database = database,
            languageList = supportedLanguageList,
            readAuthorsForSingleContentExecutor = readAuthorsForSingleContentExecutor,
            readLanguagesForSingleContentExecutor = readLanguagesForSingleContentExecutor,
            readTagsForSingleContentExecutor = readTagsForSingleContentExecutor,
            readGroupsForSingleContentExecutor = readGroupsForContentExecutor,
            readCharactersForSingleContentExecutor = readCharactersForSingleContentExecutor
        ),
        getVideoListExecutor = getVideoListExecutor,
        readVideoByIDExecutor = ReadVideoByIDExecutor(
            database = database,
            languageList = supportedLanguageList,
            readAuthorsForSingleContentExecutor = readAuthorsForSingleContentExecutor,
            readLanguagesForSingleContentExecutor = readLanguagesForSingleContentExecutor,
            readTagsForSingleContentExecutor = readTagsForSingleContentExecutor,
            readGroupsForContentExecutor = readGroupsForContentExecutor,
            readCharactersForSingleContentExecutor = readCharactersForSingleContentExecutor
        ),
        readStoryListExecutor = readStoryListExecutor,
        readStoryByIDExecutor = ReadStoryByIDExecutor(
            database = database,
            languageList = supportedLanguageList,
            readChaptersRequestExecutor = readChaptersExecutor,
            readAuthorsForSingleContentExecutor = readAuthorsForSingleContentExecutor,
            readLanguagesForSingleContentExecutor = readLanguagesForSingleContentExecutor,
            readTagsForSingleContentExecutor = readTagsForSingleContentExecutor,
            readGroupsForSingleContentExecutor = readGroupsForContentExecutor,
            readCharactersForSingleContentExecutor = readCharactersForSingleContentExecutor
        ),
        readChapterTextByIDExecutor = ReadChapterTextByIDExecutor(
            database = database
        ),
        readContentByIDListExecutor = ReadContentByIDListExecutor(
            database = database,
            readPictureByIDExecutor = ReadPictureByIDExecutor(
                database = database,
                languageList = supportedLanguageList,
                readAuthorsForSingleContentExecutor = readAuthorsForSingleContentExecutor,
                readLanguagesForSingleContentExecutor = readLanguagesForSingleContentExecutor,
                readTagsForSingleContentExecutor = readTagsForSingleContentExecutor,
                readGroupsForSingleContentExecutor = readGroupsForContentExecutor,
                readCharactersForSingleContentExecutor = readCharactersForSingleContentExecutor
            ),
            readVideoByIDExecutor = ReadVideoByIDExecutor(
                database = database,
                languageList = supportedLanguageList,
                readAuthorsForSingleContentExecutor = readAuthorsForSingleContentExecutor,
                readLanguagesForSingleContentExecutor = readLanguagesForSingleContentExecutor,
                readTagsForSingleContentExecutor = readTagsForSingleContentExecutor,
                readGroupsForContentExecutor = readGroupsForContentExecutor,
                readCharactersForSingleContentExecutor = readCharactersForSingleContentExecutor
            ),
            readStoryByIDExecutor = ReadStoryByIDExecutor(
                database = database,
                languageList = supportedLanguageList,
                readChaptersRequestExecutor = readChaptersExecutor,
                readAuthorsForSingleContentExecutor = readAuthorsForSingleContentExecutor,
                readLanguagesForSingleContentExecutor = readLanguagesForSingleContentExecutor,
                readTagsForSingleContentExecutor = readTagsForSingleContentExecutor,
                readGroupsForSingleContentExecutor = readGroupsForContentExecutor,
                readCharactersForSingleContentExecutor = readCharactersForSingleContentExecutor
            ),
            readStoryChapterByIDExecutor = ReadChapterByIDExecutor(
                database = database,
                languageList = supportedLanguageList,
                readAuthorsForSingleContentExecutor = readAuthorsForSingleContentExecutor,
                readLanguagesForSingleContentExecutor = readLanguagesForSingleContentExecutor,
                readTagsForSingleContentExecutor = readTagsForSingleContentExecutor,
            ),
        ),
        readUserByLoginExecutor = ReadUserByLoginExecutor(
            database = database
        ),
        readCommentsExecutor = ReadCommentsExecutor(
            database = database
        ),
        createUserExecutor = CreateUserExecutor(
            database = database
        ),
        checkUserCredentialsExecutor = CheckUserCredentialsExecutor(
            database = database
        ),
        checkRefreshTokenExecutor = CheckRefreshTokenExecutor(
            database = database
        ),
        updateRefreshTokenExecutor = UpdateRefreshTokenExecutor(
            database = database
        ),
        removeRefreshTokenExecutor = RemoveRefreshTokenExecutor(
            database = database
        ),
        createCommentExecutor = CreateCommentExecutor(
            database = database
        ),
        createOrDeleteFavoriteExecutor = CreateOrDeleteFavoriteExecutor(
            database = database
        ),
        createReportExecutor = CreateReportExecutor(
            database = database
        ),
        createOrDeleteVoteExecutor = CreateOrDeleteVoteExecutor(
            database = database
        ),
        readUserFavoriteExecutor = ReadUserFavoriteExecutor(
            database = database
        ),
        getUserVoteHistoryExecutor = GetUserVoteHistoryExecutor(
            database = database
        ),
        readUserFullInfoExecutor = ReadUserFullInfoExecutor(
            database = database
        ),
        readUpdateContentRequestListForUserExecutor = ReadUpdateContentRequestListForUserExecutor(
            database = database
        ),
        createActualizeContentRequestExecutor = CreateActualizeContentRequestExecutor(
            database = database
        ),
        createUploadRequestExecutor = CreateUploadRequestExecutor(
            database = database
        )
    )
}