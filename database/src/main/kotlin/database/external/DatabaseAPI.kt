package database.external

import database.external.filter.*
import database.external.result.*
import database.internal.VoteType
import declaration.GetContentByTextResult
import declaration.entity.*

interface DatabaseAPI {

    //region Auth
    suspend fun createUser(login: String, password: String): CreateUserResult

    suspend fun isUserCredentialsCorrect(name: String, password: String): CheckUserCredentialsResult
    //endregion


    //region Read data
    suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture>

    suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture>

    suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video>

    suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video>

    suspend fun getStories(filter: StoryListFilter): SimpleListResult<Story>

    suspend fun getStoryByID(contentID: Int): SimpleOptionalDataResult<Story>

    suspend fun getStoryChapterTextByID(contentID: Int): SimpleOptionalDataResult<String>

    suspend fun getContentByText(text: String): SimpleDataResult<GetContentByTextResult>

    suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment>
    //endregion


    //region Comment
    suspend fun addComment(userID: Int, contentID: Int, commentText: String): SimpleUnitResult
    //endregion


    //region Vote
    suspend fun upvote(userID: Int, contentID: Int, voteType: VoteType): VoteResult
    //endregion


    //region Favorite
    suspend fun addToFavorite(userID: Int, contentID: Int): SimpleUnitResult

    suspend fun removeFromFavorite(userID: Int, contentID: Int): SimpleUnitResult
    //endregion


    //region Report
    suspend fun report(contentID: Int, reason: String, userID: Int?): SimpleUnitResult
    //endregion
}