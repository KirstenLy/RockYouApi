package database.internal.entity.lang

import database.internal.entity.lang_translation.LangTranslationTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationLangAndTranslationTable : IntIdTable("RelationLangAndTranslation.sq") {

    val langID = reference("langID", LanguageTable)

    val translationID = reference("translationID", LangTranslationTable)
}