package database.internal.executor

import database.external.result.RegisterUserResult
import database.internal.entity.toDomain
import declaration.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import rockyouapi.DBTest
import java.time.LocalDateTime

/**
 * Create user. Return it after creation.
 * Check if user with same login already exist.
 * Don't check login and password conditions, it is caller's task.
 * To avoid race condition, one registration can be executed at one moment.
 * */
internal class CreateUserRequestExecutor(private val database: DBTest) {

    // To prevent two or more register at the same time
    private val insertUserLock = Mutex()

    suspend fun execute(login: String, password: String): RegisterUserResult = withContext(Dispatchers.IO) {
        insertUserLock.withLock {
            try {
                val alreadyExistedUser = database.userProdQueries
                    .selectAllByName(login)
                    .executeAsOneOrNull()

                if (alreadyExistedUser != null) {
                    return@withLock RegisterUserResult.SameUserAlreadyExist
                }

                // TODO: Тут вставка вроде не самая безопасная
                val user = database.userProdQueries.transactionWithResult {
                    val lastInsertedUserID = database.userProdQueries
                        .selectLastInsertedUserID()
                        .executeAsOne()
                    database.userProdQueries.insert(
                        id = null,
                        name = login
                    )

                    val insertedUserID = database.userProdQueries.selectLastInsertedUserID().executeAsOne()
                    val lastInsertedAuthDataID = database.userAuthDataQueries
                        .selectLastInsertedAuthDataID()
                        .executeAsOne()
//                    database.userAuthDataQueries.insert(
//                        id = lastInsertedAuthDataID + 1,
//                        userID = insertedUserID,
//                        password = password
//                    )

                   database.userProdQueries.selectByID(insertedUserID).executeAsOne()
                }

                return@withLock RegisterUserResult.Ok(User(user.id, user.name))
            } catch (t: Throwable) {
                return@withLock RegisterUserResult.UnexpectedError(t)
            }
        }
    }
}