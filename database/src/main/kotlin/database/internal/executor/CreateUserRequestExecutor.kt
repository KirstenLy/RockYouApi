package database.internal.executor

import database.external.result.CreateUserResult
import database.internal.entity.auth_data.AuthDataTable
import database.internal.executor.common.selectUserByLogin
import org.jetbrains.exposed.sql.insertAndGetId

internal class CreateUserRequestExecutor {

    fun execute(login: String, password: String): CreateUserResult {
        return try {
            val alreadyExistedUser = selectUserByLogin(login)

            if (alreadyExistedUser != null) {
                return CreateUserResult.SameUserAlreadyExist
            }

            val insertedUserID = insertUser(login, password)
            CreateUserResult.Ok(insertedUserID.value)
        } catch (t: Throwable) {
            CreateUserResult.UnexpectedError(t)
        }
    }

    private fun insertUser(login: String, password: String) = AuthDataTable.insertAndGetId {
        it[AuthDataTable.login] = login
        it[AuthDataTable.password] = password
    }
}