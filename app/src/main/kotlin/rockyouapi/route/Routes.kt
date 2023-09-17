package rockyouapi.route

/** Hierarchy to describe routes and their available arguments. */
internal sealed class Routes(val path: String) {

    /** @see rockyouapi.route.auth.authCreateUserRoute */
    data object AuthRegister : Routes("/register") {
        fun getLoginArgName() = "login"
        fun getPasswordArgName() = "password"
    }

    /** @see rockyouapi.route.auth.authCheckUserCredentialsRoute */
    data object AuthCheckUserCredentials : Routes("/login") {
        fun getLoginArgName() = "login"
        fun getPasswordArgName() = "password"
    }

    /** @see rockyouapi.route.picture.pictureListRoute */
    data object PictureList : Routes("/pictures") {
        fun getLangIDArgName() = "langID"
        fun getEnvLangIDArgName() = "envLangID"
        fun getOffsetArgName() = "offset"
        fun getLimitArgName() = "limit"
    }

    /** @see rockyouapi.route.picture.pictureReadByIDRoute  */
    data object PictureByID : Routes("/picture") {
        fun getPictureIDArgName() = "pictureID"
        fun getEnvLangIDArgName() = "envLangID"
    }

    /** @see rockyouapi.route.video.videoListRoute  */
    data object VideoList : Routes("/videos") {
        fun getLangIDArgName() = "langID"
        fun getEnvLangIDArgName() = "envLangID"
        fun getOffsetArgName() = "offset"
        fun getLimitArgName() = "limit"
    }

    /** @see rockyouapi.route.video.videoReadByIDRoute  */
    data object VideoByID : Routes("/video") {
        fun getVideoIDArgName() = "videoID"
        fun getEnvLangIDArgName() = "envLangID"
    }

    /** @see rockyouapi.route.story.storyListRoute */
    data object StoriesList : Routes("/stories") {
        fun getLangIDArgName() = "langID"
        fun getOffsetArgName() = "offset"
        fun getLimitArgName() = "limit"
    }

    data object StoryByID : Routes("/story") {
        fun getStoryIDArgName() = "storyID"
    }

    /** @see rockyouapi.route.story.storyReadChapterTextByIDRoute */
    data object ReadChapterTextByID : Routes("/chapterText") {
        fun getChapterIDArgName() = "chapterID"
    }

    /** @see rockyouapi.route.search.searchByTextRoute */
    data object SearchByText : Routes("/search") {
        fun getSearchTextArgName() = "searchText"
    }

    /** @see rockyouapi.route.vote.vote */
    data object Vote : Routes("/vote") {
        fun getUserIDArgName() = "userID"
        fun getContentIDArgName() = "contentID"
        fun getVoteTypeArgName() = "voteType"
    }

    /** @see rockyouapi.route.comment.commentListRoute */
    data object CommentList : Routes("/comments") {
        fun getContentIDArgName() = "contentID"
        fun getContentLimitArgName() = "limit"
        fun getContentOffsetArgName() = "offset"
    }

    /** @see rockyouapi.route.comment.commentAddRoute */
    data object CommentAdd : Routes("/add-comment") {
        fun getContentIDArgName() = "contentID"
        fun getCommentTextArgName() = "commentText"
    }

    /** @see rockyouapi.route.favorite.addToFavoriteRoute */
    data object FavoriteAdd : Routes("/favorite_add") {
        fun getContentIDArgName() = "contentID"
    }

    /** @see rockyouapi.route.favorite.removeFromFavoriteRoute */
    data object FavoriteRemove : Routes("/favorite_remove") {
        fun getContentIDArgName() = "contentID"
    }

    /** @see rockyouapi.route.report.reportRoute */
    data object Report : Routes("/report") {
        fun getContentIDArgName() = "contentID"
        fun getReportIDArgName() = "report"
        fun getUserIDArgName() = "userID"
    }
}