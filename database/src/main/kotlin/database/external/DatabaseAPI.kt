package database.external

import database.external.filter.*
import database.external.operation.FavoriteOperation
import database.external.operation.VoteOperation
import database.external.result.*
import declaration.GetContentByTextResult
import declaration.entity.Comment
import declaration.entity.Picture
import declaration.entity.Video
import declaration.entity.story.StoryNew

/** Contract of the database. */
interface DatabaseAPI {



    //region Auth
    suspend fun register(login: String, password: String): RegisterUserResult

    suspend fun checkUserCredentials(name: String, password: String): CheckUserCredentialsResult
    //endregion



    //region Read data
    suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture>

    suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture>

    suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video>

    suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video>

    suspend fun getStories(filter: StoryListFilter): SimpleListResult<StoryNew>

    suspend fun getStoryByID(filter: StoryByIDFilter): SimpleOptionalDataResult<StoryNew>

    suspend fun getStoryChapterTextByID(contentID: Int): SimpleOptionalDataResult<String>

    suspend fun getContentByText(text: String): SimpleDataResult<GetContentByTextResult>
    //endregion



    //region Comment
    suspend fun addComment(userID: Int, contentID: Int, commentText: String): SimpleUnitResult

    suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment>
    //endregion



    //region Vote
    suspend fun vote(userID: Int, contentID: Int, operation: VoteOperation): VoteResult
    //endregion



    //region Favorite
    suspend fun addOrRemoveFavorite(
        operation: FavoriteOperation,
        userID: Int,
        contentID: Int
    ): AddOrRemoveFavoriteResult
    //endregion



    //region Report
    suspend fun report(contentID: Int, reportText: String, userID: Int?): SimpleUnitResult
    //endregion
}