package database.internal.executor

import database.external.model.user.UserRole
import database.external.result.common.SimpleOptionalDataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import database.external.model.user.UserSimple as DeclarationUser
import migrations.User as DBUser

/** @see execute */
internal class ReadUserByLoginExecutor(private val database: Database) {

    /**
     * Get user by his name.
     *
     * Respond as:
     * - [SimpleOptionalDataResult.Data] Request finished without errors, story founded.
     * - [SimpleOptionalDataResult.DataNotFounded] Request finished without errors, story not founded.
     * - [SimpleOptionalDataResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(userName: String): SimpleOptionalDataResult<DeclarationUser> {
        return withContext(Dispatchers.IO) {
            try {
                database.selectUserQueries
                    .selectOneByName(userName)
                    .executeAsOneOrNull()
                    ?.let { SimpleOptionalDataResult.Data(it.toDomain()) }
                    ?: SimpleOptionalDataResult.DataNotFounded()

            } catch (t: Throwable) {
                SimpleOptionalDataResult.Error(t)
            }
        }
    }
}

private fun DBUser.toDomain() = DeclarationUser(
    id = id,
    name = name,
    role = UserRole.MEMBER
)