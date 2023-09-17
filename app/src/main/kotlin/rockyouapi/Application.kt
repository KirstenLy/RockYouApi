package rockyouapi

import rockyouapi.route.auth.authLoginRoute
import rockyouapi.route.auth.authRegisterRoute
import rockyouapi.route.comment.commentAddRoute
import rockyouapi.route.comment.commentListRoute
import rockyouapi.route.favorite.addOrRemoveFavoriteRoute
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
import rockyouapi.route.vote.voteRoute
import database.external.DatabaseAPI
import database.external.DatabaseFeature.connectToProductionDatabase
import declaration.DatabaseConfiguration
import io.ktor.server.auth.*
import kotlinx.coroutines.launch
import rockyouapi.auth.UserIDPrincipal
import rockyouapi.route.report.reportRoute
import rockyouapi.auth.UserTokensManager
import java.lang.IllegalStateException

// TODO SQL:
    // Есть огромные проблемы в SQL больших сущностей, их нужно переписать с нуля, для этого придётся грызть SQL
    // Перепроверить, что всё привязывается к RegisterID, а не к таблицам сущностей.
    // Все execute обернуть в try catch. Во всех catch добавить лог с объяснениями происходящего.
    // Все execute должны быть suspend.
    // Пробежаться и добавить индексов + сделать одинаковые отступы по 4 таба везде в файле Tables
    // Создать триггер, чтобы обновлять рейтинг после декремента/инкремента ерйтинга
    // Во всех sql файлах убрать неименованные аргументы.
    // При обновлении рейтинга/истории голосования, можно прямо в sql писать rating = rating + 1
    // В коде vote экзекьюторе нужно звать executeAsOne, т.к по смыслу пользователь не может за один контент больше одного раза проголосовать.
    // В структуру комментариев добавить время их добавления, соотв. поддержать это в тестах.
    // Шифрование пароля пользователя.
    // Поудалять не используемые Entity.
    // Обильно обмазать тесты комментами
    // Нужно заполнять тестовую БД некоторым количеством записей об избранном, но шанс добавления записи должен быть мал, 20% мб    // Тесты поиска по тексту не полны, нужно их сделать гибче и учесть лимит поисковой строки, как минимум так мб и максимум.
    // Мапперы для нодов в рассказах написаны не оптимально, т.к создают лишние промежуточные списки.
    // Нужны отдельные модули на непосредственно ответы от БД в модуле database.
    // Разобраться, почему добавление к ID слова UNSIGNED ломает присвоение id = null
    // Дописать LIMIT = 1 в запросах, в которых очевидно ожидается одна запись
    // Поубирать касты .toByte() .toInt()


// TODO BL:
    // После авторизации нужно отдавать ещё список избранного пользователя, и мб сразу voteHistory.
    // Если нужно загрузить избранное пользователя, нужно грузить всё одним запросом, а не дрочить сервер.
    // Разделять теги на основные и второстепенные, чтобы например в рассказе было видно, что основные теги об одном, и иногда могут появиться другие теги
    // Некоторые вещи проще сразу выкачать из БД при запуске сервера и хранить в памяти. Мы уже выкачиваем языки, мб имеет смысл выкачивать теги.
    // Новая сущность - комикс, которая объединяет несколько картинок и имеет теги, рейтинг и прочее.
    // Статусы пользователей. Как минимум, статусы админов и модераторов. Мб платных юзеров, или ценных участников комьюнити, например авторов.
    // Для поиска нужны модели полегче, там большинство информации не нужно.
    // Отдельный поиск по тегам?
    // Каждому контенту приделать поле "Краткое описание", особенно актуально будет для глав
    // Добавить поле "Переводчик" для контента, если контент переведён с базового языка на дополнительный
    // Реализовать предложение контента.
    // Реализовать вопросы от пользователя и чтение ответов на них в ЛК.
    // При аворизации нужно уметь прикладывать варнинг о том, что версия не актуальна, и прикладывать дату примерного её кика. Так же нужно забривать авторизацию старых версий.
    // Помимо репорта давать возможность указать автора для контента, для этого нужен отдельный ремоут.
    // К контенту нужно иметь возможность с сервера отправлять дополнительную информацию, например ссылку на модель/автора рассказа.
    // Клиенты должны проверять, чтобы максимальная длинна сообщения не превышала 65000 символов(ограничение по типу Text)
    // В методах list нужно вешать ограничение на limit/offset, чтобы нельзя было передать limit = 99999999 и жмыхнуть сервер


// TODO KTOR:
    // Придумать, как отвечать текстовкой если пользователь не авторизован, сейчас плагин свою подсовывает.
    // Ссылка на хранилище с файлами должно задаваться извне.
    // Добавить рядом с createNullableInt ещё readNullableLong.
    // Успешная авторизация должна удалять все старые токены юзера.
    // Посмотреть конфиги и поддержать переменные которые ещё не поддержаны.
    // Логировать время выполнения запроса в экзекьюторах, прямо в Kotlin соде, мб для этого даже либы есть.
    // Клиент должен отдавать свою версию, чтобы его можно было переназначить на правильный роут, или в целом правильно сформировать ответ.
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
        authRegisterRoute(databaseAPI, tokensManager, this@configureRouting.environment.readUserPasswordMinLength())
        authLoginRoute(databaseAPI, tokensManager)

        pictureListRoute(databaseAPI)
        pictureReadByIDRoute(databaseAPI)

        videoListRoute(databaseAPI)
        videoReadByIDRoute(databaseAPI)

        storyListRoute(databaseAPI)
        storyReadByIDRoute(databaseAPI)
        storyReadChapterTextByIDRoute(databaseAPI)

        searchByTextRoute(databaseAPI, this@configureRouting.environment.readSearchByTextMinimumTextLength())

        voteRoute(databaseAPI)

        commentListRoute(databaseAPI)
        commentAddRoute(databaseAPI)

        addOrRemoveFavoriteRoute(databaseAPI)

        reportRoute(databaseAPI, tokensManager)
    }
}

//region Parse environment variables
private fun ApplicationEnvironment.readDatabaseConnectionConfig() = DatabaseConfiguration(
    url = readDatabaseURL(),
    driver = readDatabaseDriver(),
    user = readDatabaseUser(),
    password = readDatabaseUserPassword(),
    isNeedToDropTablesAndFillByMocks = true
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

