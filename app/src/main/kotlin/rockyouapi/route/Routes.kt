package rockyouapi.route

/** Hierarchy to describe routes and their available arguments. */
internal sealed class Routes(val path: String) {

    /** Important strings that make sense in routing context */
    companion object {

        /** Name of JWT Auth provider. */
        internal const val JWT_AUTHORIZATION_PROVIDER_KEY = "auth-jwt"
    }

    /** @see rockyouapi.route.auth.authRegisterRoute */
    data object AuthRegister : Routes("/v1/auth/register") {
        fun getLoginArgName() = "login"
        fun getPasswordArgName() = "password"
    }

    /** @see rockyouapi.route.auth.authLoginRoute */
    data object AuthLogin : Routes("/v1/auth/login") {
        fun getLoginArgName() = "login"
        fun getPasswordArgName() = "password"
    }

    /** @see rockyouapi.route.auth.authLogoutRoute */
    data object AuthLogout : Routes("/v1/auth/logout") {
        fun getUserIDArgName() = "userID"
    }

    /** @see rockyouapi.route.auth.authRefreshRoute */
    data object AuthRefreshAccessToken : Routes("/v1/auth/token") {
        fun getUserIDArgName() = "userID"
    }

    /** @see rockyouapi.route.user.readUserFullInfo */
    data object UserFullInfo : Routes("/v1/user/full-info") {
        fun getUserIDArgName() = "userID"
    }

    /** @see rockyouapi.route.content.readContentByIDRoute */
    data object ContentReadByIDList : Routes("/v1/content/read-by-id-list") {
        fun getContentIDListArgName() = "contentIDList"
        fun getEnvironmentLangIDArgName() = "envLangID"
    }

    /** @see rockyouapi.route.picture.pictureListRoute */
    data object PictureList : Routes("/v1/pictures") {
        fun getLimitArgName() = "limit"
        fun getOffsetArgName() = "offset"
        fun getEnvironmentLangIDArgName() = "envLangID"
        fun getSearchTextArgName() = "text"
        fun getLangIDListArgName() = "langIDs"
        fun getAuthorIDListArgName() = "authorIDs"
        fun getTagIDListArgName() = "tagIDs"
        fun getUserIDListArgName() = "userIDs"
    }

    /** @see rockyouapi.route.picture.pictureReadByIDRoute  */
    data object PictureByID : Routes("/v1/picture") {
        fun getPictureIDArgName() = "pictureID"
        fun getEnvLangIDArgName() = "envLangID"
    }

    /** @see rockyouapi.route.video.videoListRoute  */
    data object VideoList : Routes("/v1/videos") {
        fun getLimitArgName() = "limit"
        fun getOffsetArgName() = "offset"
        fun getEnvironmentLangIDArgName() = "envLangID"
        fun getSearchTextArgName() = "text"
        fun getLangIDListArgName() = "langIDList"
        fun getAuthorIDListArgName() = "authorIDList"
        fun getTagIDListArgName() = "tagIDList"
        fun getUserIDListArgName() = "userIDList"
    }

    /** @see rockyouapi.route.video.videoReadByIDRoute  */
    data object VideoByID : Routes("/v1/video") {
        fun getVideoIDArgName() = "videoID"
        fun getEnvLangIDArgName() = "envLangID"
    }

    /** @see rockyouapi.route.story.storyListRoute */
    data object StoriesList : Routes("/v1/stories") {
        fun getLimitArgName() = "limit"
        fun getOffsetArgName() = "offset"
        fun getEnvironmentLangIDArgName() = "envLangID"
        fun getSearchTextArgName() = "text"
        fun getLangIDListArgName() = "langIDList"
        fun getAuthorIDListArgName() = "authorIDList"
        fun getTagIDListArgName() = "tagIDList"
        fun getUserIDListArgName() = "userIDList"
    }

    /** @see rockyouapi.route.story.storyReadByIDRoute */
    data object StoryByID : Routes("/v1/story") {
        fun getStoryIDArgName() = "storyID"
        fun getEnvLangIDArgName() = "envLangID"
    }

    /** @see rockyouapi.route.story.storyReadChapterTextByIDRoute */
    data object ReadChapterTextByID : Routes("/v1/chapter-text") {
        fun getChapterIDArgName() = "chapterID"
    }

    /** @see rockyouapi.route.vote.voteRoute */
    data object Vote : Routes("/v1/vote") {
        fun getContentIDArgName() = "contentID"
        fun getOperationTypeArgName() = "operationType"
    }

    /** @see rockyouapi.route.comment.commentListRoute */
    data object CommentList : Routes("/v1/comments") {
        fun getContentIDArgName() = "contentID"
        fun getContentLimitArgName() = "limit"
        fun getContentOffsetArgName() = "offset"
    }

    /** @see rockyouapi.route.comment.commentAddRoute */
    data object CommentAdd : Routes("/v1/add-comment") {
        fun getUserIDArgName() = "userID"
        fun getContentIDArgName() = "contentID"
        fun getCommentTextArgName() = "commentText"
    }

    /** @see rockyouapi.route.favorite.addOrRemoveFavoriteRoute */
    data object FavoriteAddOrRemove : Routes("/v1/add-or-remove-favorite") {
        fun getOperationTypeArgName() = "operationType"
        fun getUserID() = "userID"
        fun getContentIDArgName() = "contentID"
    }

    /** @see rockyouapi.route.report.reportRoute */
    data object Report : Routes("/v1/report") {
        fun getContentIDArgName() = "contentID"
        fun getUserIDArgName() = "getUserID"
        fun getReportTextArgName() = "report"
    }

    /** @see rockyouapi.route.request.createActualizeContentRequestRoute */
    data object ActualizeContentRequestCreate : Routes("/v1/request/actualize-content") {
        fun getContentIDArgName() = "contentID"
        fun getRequestTextArgName() = "requestText"
        fun getUserIDArgName() = "userID"
    }

    /** @see rockyouapi.route.upload.uploadContentRoute */
    data object Upload : Routes("/v1/add-content") {
        fun getContentIDArgName() = "contentID"
    }
}