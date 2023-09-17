package database.internal.executor.common

import database.internal.ContentType
import database.internal.Language
import database.internal.entity.author.AuthorTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.lang.LanguageTable
import database.internal.entity.lang_translation.LangTranslationTable
import database.internal.entity.tag.TagTable
import database.internal.entity.tag_translation.TagTranslationTable
import database.internal.entity.user.UserTable
import declaration.entity.Author
import declaration.entity.Lang
import declaration.entity.Tag
import declaration.entity.User
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull

internal fun FieldSet.selectByContentType(contentType: ContentType) = select {
    ContentRegisterTable.contentType eq contentType.typeID
}

internal fun ResultRow.tryToGetLangInfo(): Lang? {
    val langID = getOrNull(LanguageTable.id)
    val langTranslation = getOrNull(LangTranslationTable.translation)
    return when {
        langID != null && langTranslation != null -> Lang(langID.value, langTranslation)
        else -> null
    }
}

internal fun ResultRow.tryToGetUserInfo(): User? {
    val userID = getOrNull(UserTable.id)
    val userName = getOrNull(UserTable.name)
    return when {
        userID != null && userName != null -> User(userID.value, userName)
        else -> null
    }
}

internal fun ResultRow.tryToGetAuthorInfo(): Author? {
    val authorID = getOrNull(AuthorTable.id)
    val authorName = getOrNull(AuthorTable.name)
    return when {
        authorID != null && authorName != null -> Author(authorID.value, authorName)
        else -> null
    }
}

internal fun ResultRow.tryToGetTag(): Tag? {
    val tagID = getOrNull(TagTable.id)?.value
    val tagTranslation = getOrNull(TagTranslationTable.translation)
    return when {
        tagID != null && tagTranslation != null -> Tag(tagID, tagTranslation)
        else -> null
    }
}

internal fun getLangIDCondition(langID: Int?, langIDColumn: Column<EntityID<Int>?>): Op<Boolean> = when {
    langID == null -> Op.TRUE
    else -> langIDColumn.isNotNull() and (langIDColumn eq langID)
}

internal fun isLangIDSupported(langID: Int) = langID in Language.entries.map { it.langID }
