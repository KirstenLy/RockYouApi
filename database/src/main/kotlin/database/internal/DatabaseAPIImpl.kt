package database.internal

import database.external.DatabaseAPI
import database.external.filter.*
import database.external.result.*
import database.internal.executor.*
import database.internal.utils.dbQuery
import declaration.GetContentByTextResult
import declaration.entity.Comment
import declaration.entity.Picture
import declaration.entity.Story
import declaration.entity.Video

internal class DatabaseAPIImpl : DatabaseAPI {

    // TODO: Это всё должно поехать в конструктор
    // TODO: Если Execute будет интерфейсом, в тестовой реализации можно будет всё подменять
    private val getPicturesRequestExecutor = GetPicturesRequestExecutor()
    private val getPictureByIDRequestExecutorLegacy = GetPictureByIDRequestExecutorLegacy()

    private val getVideosRequestExecutor = GetVideosRequestExecutor()
    private val getVideoByIDRequestExecutor = GetVideoByIDRequestExecutor()

    private val getStoriesChaptersRequestExecutor = GetChaptersRequestExecutor()
    private val getStoriesRequestExecutor = GetStoriesRequestExecutor(getStoriesChaptersRequestExecutor)
    private val getStoryByIDRequestExecutor = GetStoryByIDRequestExecutor(getStoriesChaptersRequestExecutor)
    private val getChaptersRequestExecutor = GetChaptersRequestExecutor()
    private val getChapterTextByIDRequestExecutor = GetChapterTextByIDRequestExecutor()

    private val addCommentRequestExecutor = AddCommentRequestExecutor()

    private val getContentByTextRequestExecutor = GetContentByTextRequestExecutor(
        getPicturesByTextRequestExecutor = GetPicturesByTextRequestExecutor(getPictureByIDRequestExecutorLegacy),
        getVideosByTextRequestExecutor = GetVideosByTextRequestExecutor(getVideoByIDRequestExecutor),
        getStoriesByTextRequestExecutor = GetStoriesByTextRequestExecutor(getStoryByIDRequestExecutor),
        getChaptersByTextRequestExecutor = GetChaptersByTextRequestExecutor(getChaptersRequestExecutor)
    )

    private val getCommentsRequestExecutor = GetCommentsRequestExecutor()

    private val voteRequestExecutor = VoteRequestExecutor()

    private val createUserRequestExecutor = CreateUserRequestExecutor()

    private val checkUserCredentialsRequestExecutor = CheckUserCredentialsRequestExecutor()

    private val addToFavoriteRequestExecutor = AddToFavoriteRequestExecutor()
    private val removeFromFavoriteRequestExecutor = RemoveFromFavoriteRequestExecutor()
    private val reportRequestExecutor = ReportRequestExecutor()

    override suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture> = dbQuery {
        getPicturesRequestExecutor.execute(filter)
    }

    override suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> = dbQuery {
        getPictureByIDRequestExecutorLegacy.execute(filter)
    }

    override suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video> = dbQuery {
        getVideosRequestExecutor.execute(filter)
    }

    override suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video> = dbQuery {
        getVideoByIDRequestExecutor.execute(filter)
    }

    override suspend fun getStories(filter: StoryListFilter): SimpleListResult<Story> = dbQuery {
        getStoriesRequestExecutor.execute(filter)
    }

    override suspend fun getStoryByID(contentID: Int): SimpleOptionalDataResult<Story> = dbQuery {
        getStoryByIDRequestExecutor.execute(contentID)
    }

    override suspend fun getStoryChapterTextByID(contentID: Int): SimpleOptionalDataResult<String> = dbQuery {
        getChapterTextByIDRequestExecutor.execute(contentID)
    }

    override suspend fun getContentByText(text: String): SimpleDataResult<GetContentByTextResult> = dbQuery {
        getContentByTextRequestExecutor.execute(text)
    }

    override suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment> = dbQuery {
        getCommentsRequestExecutor.execute(filter)
    }

    override suspend fun addComment(userID: Int, contentID: Int, commentText: String): SimpleUnitResult = dbQuery {
        addCommentRequestExecutor.execute(userID, contentID, commentText)
    }

    override suspend fun upvote(userID: Int, contentID: Int, voteType: VoteType): VoteResult = dbQuery {
        voteRequestExecutor.execute(userID, contentID, voteType)
    }

    override suspend fun createUser(login: String, password: String): CreateUserResult = dbQuery {
        createUserRequestExecutor.execute(login, password)
    }

    override suspend fun isUserCredentialsCorrect(name: String, password: String): CheckUserCredentialsResult =
        dbQuery { checkUserCredentialsRequestExecutor.execute(name, password) }

    override suspend fun addToFavorite(userID: Int, contentID: Int): SimpleUnitResult = dbQuery {
        addToFavoriteRequestExecutor.execute(userID, contentID)
    }

    override suspend fun removeFromFavorite(userID: Int, contentID: Int): SimpleUnitResult = dbQuery {
        removeFromFavoriteRequestExecutor.execute(userID, contentID)
    }

    override suspend fun report(contentID: Int, reason: String, userID: Int?): SimpleUnitResult = dbQuery {
        reportRequestExecutor.execute(contentID, reason, userID)
    }
}