package database.internal.creator.test

import database.external.DatabaseAPI
import database.external.filter.*
import database.external.operation.FavoriteOperation
import database.external.operation.VoteOperation
import database.external.result.*
import declaration.GetContentByTextResult
import declaration.entity.*
import declaration.entity.story.StoryNew

/**
 * Database that returns not valuable responses.
 * Used to mocking.
 * */
internal fun initMockDatabaseApi() = object : DatabaseAPI {

    override suspend fun register(login: String, password: String): RegisterUserResult {
        return RegisterUserResult.SameUserAlreadyExist
    }

    override suspend fun checkUserCredentials(name: String, password: String): CheckUserCredentialsResult {
        return CheckUserCredentialsResult.UserNotExist
    }

    override suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture> {
        return SimpleListResult.Data(emptyList())
    }

    override suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture> {
        return SimpleOptionalDataResult.DataNotFounded()
    }

    override suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video> {
        return SimpleListResult.Data(emptyList())
    }

    override suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video> {
        return SimpleOptionalDataResult.DataNotFounded()
    }

    override suspend fun getStories(filter: StoryListFilter): SimpleListResult<StoryNew> {
        return SimpleListResult.Data(emptyList())
    }

    override suspend fun getStoryByID(filter: StoryByIDFilter): SimpleOptionalDataResult<StoryNew> {
        return SimpleOptionalDataResult.DataNotFounded()
    }

    override suspend fun getStoryChapterTextByID(contentID: Int): SimpleOptionalDataResult<String> {
        return SimpleOptionalDataResult.DataNotFounded()
    }

    override suspend fun getContentByText(text: String): SimpleDataResult<GetContentByTextResult> {
        return SimpleDataResult.Data(GetContentByTextResult())
    }

    override suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment> {
        return SimpleListResult.Data(emptyList())
    }

    override suspend fun vote(userID: Int, contentID: Int, operation: VoteOperation): VoteResult {
        return VoteResult.AlreadyVoted
    }

    override suspend fun addComment(userID: Int, contentID: Int, commentText: String): SimpleUnitResult {
        return SimpleUnitResult.Ok
    }

    override suspend fun addOrRemoveFavorite(
        operation: FavoriteOperation,
        userID: Int,
        contentID: Int
    ): AddOrRemoveFavoriteResult {
        return AddOrRemoveFavoriteResult.NotInFavorite
    }

    override suspend fun report(contentID: Int, reportText: String, userID: Int?): SimpleUnitResult {
        return SimpleUnitResult.Ok
    }
}