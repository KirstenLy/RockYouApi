package rockyouapi.route

/** Hierarchy to describe routes and their available arguments. */
internal sealed class Routes(val path: String) {

    /** @see rockyouapi.route.auth.authRegisterRoute */
    data object AuthRegister : Routes("/register") {
        fun getLoginArgName() = "login"
        fun getPasswordArgName() = "password"
    }

    /** @see rockyouapi.route.auth.authLoginRoute */
    data object AuthLogin : Routes("/login") {
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
        fun getEnvLangIDArgName() = "envLangID"
    }

    /** @see rockyouapi.route.story.storyReadChapterTextByIDRoute */
    data object ReadChapterTextByID : Routes("/chapter-text") {
        fun getChapterIDArgName() = "chapterID"
    }

    /** @see rockyouapi.route.search.searchByTextRoute */
    data object SearchByText : Routes("/search") {
        fun getSearchTextArgName() = "searchText"
    }

    /** @see rockyouapi.route.vote.voteRoute */
    data object Vote : Routes("/vote") {
        fun getContentIDArgName() = "contentID"
        fun getOperationTypeArgName() = "operationType"
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

    /** @see rockyouapi.route.favorite.addOrRemoveFavoriteRoute */
    data object FavoriteAddOrRemove : Routes("/add-or-remove-favorite") {
        fun getOperationTypeArgName() = "operationType"
        fun getContentIDArgName() = "contentID"
    }

    /** @see rockyouapi.route.report.reportRoute */
    data object Report : Routes("/report") {
        fun getContentIDArgName() = "contentID"
        fun getReportIDArgName() = "report"
    }
}