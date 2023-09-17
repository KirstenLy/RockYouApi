package rockyouapi

import rockyouapi.route.auth.authCheckUserCredentialsRoute
import rockyouapi.route.auth.authCreateUserRoute
import rockyouapi.route.comment.commentAddRoute
import rockyouapi.route.comment.commentListRoute
import rockyouapi.route.favorite.addToFavoriteRoute
import rockyouapi.route.favorite.removeFromFavoriteRoute
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import rockyouapi.route.picture.pictureListRoute
import rockyouapi.route.picture.pictureReadByIDRoute
import rockyouapi.route.search.searchByTextRoute
import rockyouapi.route.story.storyListRoute
import rockyouapi.route.story.storyReadByIDRoute
import rockyouapi.route.story.storyReadChapterTextByIDRoute
import rockyouapi.route.video.videoListRoute
import rockyouapi.route.video.videoReadByIDRoute
import rockyouapi.route.vote.vote
import database.external.DatabaseAPI
import database.external.DatabaseFeature.connectToProductionDatabase
import declaration.DatabaseConnectionConfig
import io.ktor.server.auth.*
import kotlinx.coroutines.launch
import rockyouapi.auth.UserIDPrincipal
import rockyouapi.route.report.reportRoute
import rockyouapi.auth.UserTokensManager
import java.lang.IllegalStateException

// TODO: ЧТо если limit == 0?
// TODO: Перепроверить, что всё привязывается к RegisterID, а не к таблицам сущностей.
// TODO: Все execute обернуть в try catch
// TODO: Убедиться, что запросы читают только нужные колонки, чтобы запросы выполнялись быстрее. Так можно ускорить подсчёт комментариев, выбирая только id
// TODO: exposed-crypt
// TODO: Некоторые вещи проще сразу выкачать из БД при запуске сервера и хранить в памяти
// TODO: selectCommentsCount не оптимален, т.к работает через селект, нужно перестать дёргать комментарии и как - то дёргать сразу их количество
// TODO: Даунвоут, добить воут, сейчас всегда плюс идёт в бд, и вотетайп торчит наружу
// TODO: Когда пользователь авторизуется, нужно отдавать его VoteHistory
// TODO: Придумать, как отвечать текстовкой если пользователь не авторизован, сейчас плагин свою подсовывает
// TODO: Ссылка на хранилище с файлами должно задаваться извне
// TODO: Шифрование пароля пользователя
// TODO: Поудалять не используемые Entity
// TODO: Проверять длинну пароля при создании
// TODO: Логировать время выполнения запроса в экзекьюторах, прямо в Kotlin соде, мб для этого даже либы есть от Ktor/Exposed
// TODO: Тесты поиска по тексту не полны, нужно их сделать гибче и учесть лимит поисковой строки, как минимум так мб и максимум
// TODO: Нужно заменить несколько запросов в сущностях одним подзапросом. Как вариант, помержить все джойны кроме первого в один большой джойн
// TODO: Реализовать предложение контента
// TODO: Реализовать вопросы от пользователя и чтение вопросов с ответами на них
fun main(args: Array<String>) {
    EngineMain.main(args)
}

internal fun Application.module() {
    launch(coroutineContext) {
        val databaseAPI = connectToProductionDatabase(environment.readDatabaseConnectionConfig())
        val userTokensManager = UserTokensManager(environment.readUserTokenLifeTime())

        configurePlugins(userTokensManager)
        configureRouting(databaseAPI, userTokensManager)
    }
}

private fun Application.configurePlugins(tokensStorage: UserTokensManager) {
    install(ContentNegotiation) { json() }
    install(AutoHeadResponse)
    install(IgnoreTrailingSlash)
    install(Resources)

    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                val token = tokenCredential.token
                if (tokensStorage.isTokenExistAndValid(token)) {
                    tokensStorage.getTokenOwnerID(token)?.let(::UserIDPrincipal)
                } else {
                    null
                }
            }
        }
    }
}

private fun Application.configureRouting(databaseAPI: DatabaseAPI, tokensManager: UserTokensManager) {
    routing {
        authCreateUserRoute(databaseAPI, tokensManager, this@configureRouting.environment.readUserPasswordMinLength())
        authCheckUserCredentialsRoute(databaseAPI, tokensManager)

        pictureListRoute(databaseAPI)
        pictureReadByIDRoute(databaseAPI)

        videoListRoute(databaseAPI)
        videoReadByIDRoute(databaseAPI)

        storyListRoute(databaseAPI)
        storyReadByIDRoute(databaseAPI)
        storyReadChapterTextByIDRoute(databaseAPI)

        searchByTextRoute(databaseAPI, this@configureRouting.environment.readSearchByTextMinimumTextLength())

        vote(databaseAPI)

        commentListRoute(databaseAPI)
        commentAddRoute(databaseAPI)

        addToFavoriteRoute(databaseAPI)
        removeFromFavoriteRoute(databaseAPI)

        reportRoute(databaseAPI)
    }
}

//region Parse environment variables
private fun ApplicationEnvironment.readDatabaseConnectionConfig() = DatabaseConnectionConfig(
    url = readDatabaseURL(),
    driver = readDatabaseDriver(),
    user = readDatabaseUser(),
    password = readDatabaseUserPassword(),
)

private fun ApplicationEnvironment.readDatabaseURL() = config
    .propertyOrNull("ktor.deployment.dbURL")
    ?.getString()
    ?.takeIf(String::isNotBlank)
    ?: throw IllegalStateException("Не удалось получить URL базы данных, конфигурация сервера прервана")

private fun ApplicationEnvironment.readDatabaseDriver() = config
    .propertyOrNull("ktor.deployment.dbDriver")
    ?.getString()
    ?.takeIf(String::isNotBlank)
    ?: throw IllegalStateException("Не удалось получить драйвер базы данных, конфигурация сервера прервана")

private fun ApplicationEnvironment.readDatabaseUser() = config
    .propertyOrNull("ktor.deployment.dbUser")
    ?.getString()
    ?.takeIf(String::isNotBlank)
    ?: throw IllegalStateException("Не удалось получить пользователя базы данных, конфигурация сервера прервана")

private fun ApplicationEnvironment.readDatabaseUserPassword() = config
    .propertyOrNull("ktor.deployment.dbPassword")
    ?.getString()
    ?.takeIf(String::isNotBlank)
    ?: throw IllegalStateException("Не удалось получить пароль пользователя базы данных, конфигурация сервера прервана")

private fun ApplicationEnvironment.readUserTokenLifeTime() = config
    .propertyOrNull("ktor.deployment.userTokenLifeTime")
    ?.getString()
    ?.takeIf(String::isNotBlank)
    ?.toIntOrNull()
    ?: throw IllegalStateException("Не удалось получить время жизни токена пользователя, конфигурация сервера прервана")

private fun ApplicationEnvironment.readUserPasswordMinLength() = config
    .propertyOrNull("ktor.deployment.userPasswordMinLength")
    ?.getString()
    ?.takeIf(String::isNotBlank)
    ?.toIntOrNull()
    ?: throw IllegalStateException("Не удалось получить минимальную длинну пароля пользователя, конфигурация сервера прервана")

private fun ApplicationEnvironment.readSearchByTextMinimumTextLength() = config
    .propertyOrNull("ktor.deployment.searchByTextMinimumTextLength")
    ?.getString()
    ?.takeIf(String::isNotBlank)
    ?.toIntOrNull()
    ?: throw IllegalStateException("Не удалось получить ограничение на минимальное количество символов при поиске по тексту, конфигурация сервера прервана")
//endregion

