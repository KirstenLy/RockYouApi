package rockyouapi.utils

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import rockyouapi.route.Routes

/** Make login request, return user with tokens. */
internal suspend fun HttpClient.makeAuthLoginRequest(login: String?, password: String?) =
    post(Routes.AuthLogin.path) {

        setBody(FormDataContent(Parameters.build {
            login?.let { append(Routes.AuthLogin.getLoginArgName(), login) }
            password?.let { append(Routes.AuthLogin.getPasswordArgName(), password) }
            build()
        }))

        url {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

/** Make register request, return user with tokens. */
internal suspend fun HttpClient.makeAuthRegisterRequest(login: String?, password: String?) =
    post(Routes.AuthRegister.path) {

        setBody(FormDataContent(Parameters.build {
            login?.let { append(Routes.AuthRegister.getLoginArgName(), login) }
            password?.let { append(Routes.AuthRegister.getPasswordArgName(), password) }
            build()
        }))

        url {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }