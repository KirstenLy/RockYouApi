package database.internal.creator

import database.external.DatabaseAPI
import database.external.filter.*
import database.external.result.*
import database.internal.VoteType
import declaration.GetContentByTextResult
import declaration.entity.*

/** Database return not valuable responses. */
internal fun initMockDatabaseApi() = object : DatabaseAPI {

    override suspend fun createUser(login: String, password: String): CreateUserResult {
        return CreateUserResult.Ok(-1)
    }

    override suspend fun isUserCredentialsCorrect(name: String, password: String): CheckUserCredentialsResult {
        return CheckUserCredentialsResult.Ok(-1)
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

    override suspend fun getStories(filter: StoryListFilter): SimpleListResult<Story> {
        return SimpleListResult.Data(emptyList())
    }

    override suspend fun getStoryByID(contentID: Int): SimpleOptionalDataResult<Story> {
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

    override suspend fun addComment(userID: Int, contentID: Int, commentText: String): SimpleUnitResult {
        return SimpleUnitResult.Ok
    }

    override suspend fun upvote(userID: Int, contentID: Int, voteType: VoteType): VoteResult {
        return VoteResult.AlreadyVoted
    }

    override suspend fun addToFavorite(userID: Int, contentID: Int): SimpleUnitResult {
        return SimpleUnitResult.Ok
    }

    override suspend fun removeFromFavorite(userID: Int, contentID: Int): SimpleUnitResult {
        return SimpleUnitResult.Ok
    }

    override suspend fun report(contentID: Int, reason: String, userID: Int?): SimpleUnitResult {
        return SimpleUnitResult.Ok
    }
}