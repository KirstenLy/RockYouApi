package database.external.contract

import database.external.filter.*
import database.external.model.*
import database.external.model.story.Story
import database.external.model.user.UserSimple
import database.external.model.user.UserFull
import database.external.operation.FavoriteOperation
import database.external.operation.VoteOperation
import database.external.result.*
import database.external.result.common.*

/** Contract of the database. */
interface ProductionDatabaseAPI {


    /** Register new user.*/
    suspend fun register(login: String, password: String): RegisterUserResult

    /** Check is login match with password.*/
    suspend fun checkUserCredentials(name: String, password: String): CheckUserCredentialsResult

    /** Check user's refresh token.*/
    suspend fun checkIsRefreshTokenExistAndBelongToUser(userID: Int, token: String): SimpleBooleanResult

    /** Update user's refresh token.*/
    suspend fun updateRefreshToken(userID: Int, token: String?): UpdateRefreshTokenResult

    /** Remove refresh token.*/
    suspend fun removeRefreshToken(token: String): SimpleUnitResult

    /** Get user basic info by login. */
    suspend fun getUserByLogin(userLogin: String): SimpleOptionalDataResult<UserSimple>

    /** Get user full info by id. */
    suspend fun getUserFullInfoByID(userID: Int): SimpleOptionalDataResult<UserFull>

    /** Get user full info by login. */
    suspend fun getUserFullInfoByLogin(login: String): SimpleOptionalDataResult<UserFull>


    /** Get pictures by [PictureListFilter].*/
    suspend fun getPictures(filter: PictureListFilter): SimpleListResult<Picture>

    /** Get picture by [PictureByIDFilter]. */
    suspend fun getPictureByID(filter: PictureByIDFilter): SimpleOptionalDataResult<Picture>

    /** Get videos by [VideoListFilter].*/
    suspend fun getVideos(filter: VideoListFilter): SimpleListResult<Video>

    /** Get video by [VideoByIDFilter]. */
    suspend fun getVideoByID(filter: VideoByIDFilter): SimpleOptionalDataResult<Video>

    /** Get stories by [StoryListFilter].*/
    suspend fun getStories(filter: StoryListFilter): SimpleListResult<Story>

    /** Get story by [StoryByIDFilter]. */
    suspend fun getStoryByID(filter: StoryByIDFilter): SimpleOptionalDataResult<Story>

    /** Get chapter text by chapterID. */
    suspend fun getStoryChapterTextByID(chapterID: Int): SimpleOptionalDataResult<String>

    /** Get content list by contentID. */
    suspend fun getContentByID(contentIDList: List<Int>, environmentID: Int?): SimpleDataResult<GetContentByIDResult>


    /** Add comment. */
    suspend fun addComment(userID: Int, contentID: Int, commentText: String): AddCommentResult

    /** Get comments. */
    suspend fun getComments(filter: CommentListFilter): SimpleListResult<Comment>


    /** Vote for content. Increment/decrement content rating as result. */
    suspend fun vote(userID: Int, contentID: Int, operation: VoteOperation): VoteResult

    /** Get user vote history for all time. */
    suspend fun getUserVoteHistory(userID: Int): SimpleListResult<Vote>


    /** Add or remove content to/from favorite. */
    suspend fun addOrRemoveFavorite(
        operation: FavoriteOperation,
        userID: Int,
        contentID: Int
    ): AddOrRemoveFavoriteResult

    /** Get user favorite contentID. */
    suspend fun getUserFavoriteIDList(userID: Int): SimpleListResult<Int>


    /** Report content. If [userID] null, report not associated with any user. */
    suspend fun report(contentID: Int, reportText: String, userID: Int?): ReportResult


    /** Create request to actualize content information, like author/tag/e.t.c. */
    suspend fun createActualizeContentRequest(contentID: Int, requestText: String, userID: Int?): ActualizeContentResult

    /** Read request, created by [createActualizeContentRequest]. */
    suspend fun readUpdateContentRequestListForUser(userID: Int): SimpleListResult<UpdateContentRequest>


    /** Create request to upload new content. If [userID] null, request not associated with any user. */
    suspend fun createUploadContentRequest(fileName: String, userID: Int?): SimpleUnitResult
}