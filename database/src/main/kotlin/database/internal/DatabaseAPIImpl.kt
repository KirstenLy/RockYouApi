package database.internal

import database.external.DatabaseAPI
import database.external.filter.*
import database.external.operation.FavoriteOperation
import database.external.operation.VoteOperation
import database.external.result.*
import database.internal.executor.*
import declaration.GetContentByTextResult
import declaration.entity.*
import declaration.entity.story.StoryNew

internal class DatabaseAPIImpl(
    private val getPictureByIDRequestExecutor: GetPictureByIDRequestExecutor,
    private val getPictureListRequestExecutor: GetPictureListRequestExecutor,
    private val getVideoByIDRequestExecutor: GetVideoByIDRequestExecutor,
    private val getVideosListRequestExecutor: GetVideosListRequestExecutor,
    private val getStoriesChaptersRequestExecutor: GetChaptersRequestExecutor,
    private val getStoriesRequestExecutor: GetStoriesRequestExecutor,
    private val getStoryByIDRequestExecutor: GetStoryByIDRequestExecutor,
    private val getChapterTextByIDRequestExecutor: GetChapterTextByIDRequestExecutor,
    private val getContentByTextRequestExecutor: GetContentByTextRequestExecutor,
    private val getCommentsRequestExecutor: GetCommentsRequestExecutor,
    private val createUserRequestExecutor: CreateUserRequestExecutor,
    private val checkUserCredentialsExecutor: CheckUserCredentialsExecutor,
    private val addCommentRequestExecutor: AddCommentRequestExecutor,
    private val addOrRemoveFavoriteRequestExecutor: AddOrRemoveFavoriteRequestExecutor,
    private val reportRequestExecutor: ReportRequestExecutor,
    private val voteRequestExecutor: VoteRequestExecutor
) : DatabaseAPI {


    override suspend fun register(login: String, password: String): RegisterUserResult {
        return createUserRequestExecutor.execute(login, password)
    }

    override suspend fun checkUserCredentials(name: String, password: String): CheckUserCredentialsResult {
        return checkUserCredentialsExecutor.execute(name, password)
    }


    override suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture> {
        return getPictureListRequestExecutor.execute(filter)
    }

    override suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
        return getPictureByIDRequestExecutor.execute(filter)
    }

    override suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video> {
        return getVideosListRequestExecutor.execute(filter)
    }

    override suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video> {
        return getVideoByIDRequestExecutor.execute(filter)
    }

    override suspend fun getStories(filter: StoryListFilter): SimpleListResult<StoryNew> {
        return getStoriesRequestExecutor.execute(filter)
    }

    override suspend fun getStoryByID(filter: StoryByIDFilter): SimpleOptionalDataResult<StoryNew> {
        return getStoryByIDRequestExecutor.execute(filter)
    }

    override suspend fun getStoryChapterTextByID(contentID: Int): SimpleOptionalDataResult<String> {
        return getChapterTextByIDRequestExecutor.execute(contentID)
    }


    override suspend fun getContentByText(text: String): SimpleDataResult<GetContentByTextResult> {
        return getContentByTextRequestExecutor.execute(text)
    }


    override suspend fun addComment(userID: Int, contentID: Int, commentText: String): SimpleUnitResult {
        return addCommentRequestExecutor.execute(userID, contentID, commentText)
    }

    override suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment> {
        return getCommentsRequestExecutor.execute(filter)
    }


    override suspend fun vote(userID: Int, contentID: Int, operation: VoteOperation): VoteResult {
        return voteRequestExecutor.execute(userID, contentID, operation)
    }


    override suspend fun addOrRemoveFavorite(
        operation: FavoriteOperation,
        userID: Int,
        contentID: Int
    ): AddOrRemoveFavoriteResult {
        return addOrRemoveFavoriteRequestExecutor.execute(operation, userID, contentID)
    }


    override suspend fun report(contentID: Int, reportText: String, userID: Int?): SimpleUnitResult {
        return reportRequestExecutor.execute(contentID, reportText, userID)
    }
}