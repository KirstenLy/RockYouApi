package database.internal.executor.common

import database.internal.entity.auth_data.AuthDataEntity
import database.internal.entity.auth_data.AuthDataTable
import database.internal.entity.comment.CommentTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.user.UserTable
import declaration.entity.Lang
import declaration.entity.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select

/** Select languages by their IDs. */
internal fun selectLanguages(languagesIDs: List<Int>): List<Lang> = LanguageTable
    .select { LanguageTable.id inList languagesIDs }
    .mapNotNull { it.tryToGetLangInfo() }

/** Select users by their IDs. */
internal fun selectUsers(usersIDs: List<Int>): List<User> = UserTable
    .select { UserTable.id inList usersIDs }
    .mapNotNull { it.tryToGetUserInfo() }

/** Select user by his login. */
internal fun selectUserByLogin(login: String) = AuthDataEntity
    .find { AuthDataTable.login eq login }
    .limit(1)
    .firstOrNull()

/**
 * Count comments for content by [contentIDs].
 * @return Comments count grouped by contentID: {contentID: comments count}.
 * */
internal fun selectCommentsCount(contentIDs: List<Int>): Map<Int, Int> = CommentTable.select {
    CommentTable.contentID inList contentIDs
}
    .groupBy { it[CommentTable.contentID].value }
    .mapValues { it.value.size }
