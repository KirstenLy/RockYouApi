package database.internal

import database.external.contract.ProductionDatabaseAPI
import database.external.filter.*
import database.external.model.*
import database.external.model.story.Story
import database.external.model.user.UserSimple
import database.external.model.user.UserFull
import database.external.model.user.UserRole
import database.external.operation.FavoriteOperation
import database.external.operation.VoteOperation
import database.external.result.*
import database.external.result.common.*
import database.internal.executor.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** @see [ProductionDatabaseAPI]. */
internal class ProductionDatabaseAPIImpl(
    private val readPictureByIDExecutor: ReadPictureByIDExecutor,
    private val readPictureListExecutor: ReadPictureListExecutor,
    private val readVideoByIDExecutor: ReadVideoByIDExecutor,
    private val getVideoListExecutor: GetVideoListExecutor,
    private val readStoryListExecutor: ReadStoryListExecutor,
    private val readStoryByIDExecutor: ReadStoryByIDExecutor,
    private val readChapterTextByIDExecutor: ReadChapterTextByIDExecutor,
    private val readContentByIDListExecutor: ReadContentByIDListExecutor,
    private val readUserByLoginExecutor: ReadUserByLoginExecutor,
    private val readCommentsExecutor: ReadCommentsExecutor,
    private val createUserExecutor: CreateUserExecutor,
    private val readUserFullInfoExecutor: ReadUserFullInfoExecutor,
    private val checkUserCredentialsExecutor: CheckUserCredentialsExecutor,
    private val checkRefreshTokenExecutor: CheckRefreshTokenExecutor,
    private val updateRefreshTokenExecutor: UpdateRefreshTokenExecutor,
    private val removeRefreshTokenExecutor: RemoveRefreshTokenExecutor,
    private val createCommentExecutor: CreateCommentExecutor,
    private val createOrDeleteFavoriteExecutor: CreateOrDeleteFavoriteExecutor,
    private val readUserFavoriteExecutor: ReadUserFavoriteExecutor,
    private val createReportExecutor: CreateReportExecutor,
    private val createOrDeleteVoteExecutor: CreateOrDeleteVoteExecutor,
    private val getUserVoteHistoryExecutor: GetUserVoteHistoryExecutor,
    private val createActualizeContentRequestExecutor: CreateActualizeContentRequestExecutor,
    private val readUpdateContentRequestListForUserExecutor: ReadUpdateContentRequestListForUserExecutor,
    private val createUploadRequestExecutor: CreateUploadRequestExecutor
) : ProductionDatabaseAPI {

    /** Used to synchronize critical auth scenarios. */
    private val authLock = Mutex()

    override suspend fun register(login: String, password: String): RegisterUserResult {
        return authLock.withLock {
            createUserExecutor.execute(
                login,
                password,
                UserRole.MEMBER
            )
        }
    }

    override suspend fun checkUserCredentials(name: String, password: String): CheckUserCredentialsResult {
        return checkUserCredentialsExecutor.execute(name, password)
    }

    override suspend fun checkIsRefreshTokenExistAndBelongToUser(userID: Int, token: String): SimpleBooleanResult {
        return authLock.withLock { checkRefreshTokenExecutor.execute(userID, token) }
    }

    override suspend fun updateRefreshToken(userID: Int, token: String?): UpdateRefreshTokenResult {
        return authLock.withLock { updateRefreshTokenExecutor.execute(userID, token) }
    }

    override suspend fun removeRefreshToken(token: String): SimpleUnitResult {
        return authLock.withLock { removeRefreshTokenExecutor.execute(token) }
    }


    override suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture> {
        return readPictureListExecutor.execute(filter)
    }

    override suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
        return readPictureByIDExecutor.execute(filter)
    }

    override suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video> {
        return getVideoListExecutor.execute(filter)
    }

    override suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video> {
        return readVideoByIDExecutor.execute(filter)
    }

    override suspend fun getStories(filter: StoryListFilter): SimpleListResult<Story> {
        return readStoryListExecutor.execute(filter)
    }

    override suspend fun getStoryByID(filter: StoryByIDFilter): SimpleOptionalDataResult<Story> {
        return readStoryByIDExecutor.execute(filter)
    }

    override suspend fun getStoryChapterTextByID(chapterID: Int): SimpleOptionalDataResult<String> {
        return readChapterTextByIDExecutor.execute(chapterID)
    }


    override suspend fun getContentByID(
        contentIDList: List<Int>,
        environmentID: Int?
    ): SimpleDataResult<GetContentByIDResult> {
        return readContentByIDListExecutor.execute(contentIDList, environmentID)
    }

    override suspend fun getUserByLogin(userLogin: String): SimpleOptionalDataResult<UserSimple> {
        return authLock.withLock { readUserByLoginExecutor.execute(userLogin) }
    }

    override suspend fun getUserFullInfoByID(userID: Int): SimpleOptionalDataResult<UserFull> {
        return readUserFullInfoExecutor.execute(userID)
    }

    override suspend fun getUserFullInfoByLogin(login: String): SimpleOptionalDataResult<UserFull> {
        return readUserFullInfoExecutor.execute(login)
    }

    override suspend fun addComment(userID: Int, contentID: Int, commentText: String): AddCommentResult {
        return createCommentExecutor.execute(userID, contentID, commentText)
    }

    override suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment> {
        return readCommentsExecutor.execute(filter)
    }


    override suspend fun vote(userID: Int, contentID: Int, operation: VoteOperation): VoteResult {
        return createOrDeleteVoteExecutor.execute(contentID, userID, operation)
    }

    override suspend fun getUserVoteHistory(userID: Int): SimpleListResult<Vote> {
        return getUserVoteHistoryExecutor.execute(userID)
    }


    override suspend fun addOrRemoveFavorite(
        operation: FavoriteOperation,
        userID: Int,
        contentID: Int
    ): AddOrRemoveFavoriteResult {
        return createOrDeleteFavoriteExecutor.execute(operation, userID, contentID)
    }

    override suspend fun getUserFavoriteIDList(userID: Int): SimpleListResult<Int> {
        return readUserFavoriteExecutor.execute(userID)
    }


    override suspend fun report(contentID: Int, reportText: String, userID: Int?): ReportResult {
        return createReportExecutor.execute(contentID, reportText, userID)
    }

    override suspend fun createActualizeContentRequest(
        contentID: Int,
        requestText: String,
        userID: Int?
    ): ActualizeContentResult {
        return createActualizeContentRequestExecutor.execute(contentID, requestText, userID)
    }

    override suspend fun readUpdateContentRequestListForUser(userID: Int): SimpleListResult<UpdateContentRequest> {
        return readUpdateContentRequestListForUserExecutor.execute(userID)
    }

    override suspend fun createUploadContentRequest(fileName: String, userID: Int?): SimpleUnitResult {
        return createUploadRequestExecutor.execute(userID, fileName)
    }
}