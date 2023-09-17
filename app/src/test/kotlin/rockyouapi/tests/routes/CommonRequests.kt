package rockyouapi.tests.routes

import declaration.entity.Comment
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import rockyouapi.appendToParameters
import rockyouapi.decodeAs
import rockyouapi.model.UserWithToken
import rockyouapi.route.Routes

/** Login. Put token to storage. */
internal suspend inline fun HttpClient.makeAuthLoginRequestWithDecodedResponse(
    login: String?,
    password: String?,
    onFinishedByError: (errorCode: HttpStatusCode) -> UserWithToken
): UserWithToken {
    val requestResult = post(Routes.AuthLogin.path) {
        url {
            appendToParameters(login, Routes.AuthLogin.getLoginArgName())
            appendToParameters(password, Routes.AuthLogin.getPasswordArgName())
        }
    }
    return when (requestResult.status) {
        HttpStatusCode.OK -> requestResult.decodeAs<UserWithToken>()
        else -> onFinishedByError(requestResult.status)
    }
}

/** Read comments list. */
internal suspend inline fun HttpClient.makeCommentListRequest(
    contentID: String?,
    limit: String?,
    offset: String?,
    onFinishedByError: (errorCode: HttpStatusCode) -> List<Comment>
): List<Comment> {
    val requestResult = get(Routes.CommentList.path) {
        url {
            appendToParameters(contentID, Routes.CommentList.getContentIDArgName())
            appendToParameters(limit, Routes.CommentList.getContentLimitArgName())
            appendToParameters(offset, Routes.CommentList.getContentOffsetArgName())
        }
    }
    return when (requestResult.status) {
        HttpStatusCode.OK -> requestResult.decodeAs<List<Comment>>()
        else -> onFinishedByError(requestResult.status)
    }
}