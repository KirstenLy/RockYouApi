package database.internal.entity.lang_translation

import database.internal.entity.lang.LanguageTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object LangTranslationTable : IntIdTable("LanguageTranslation") {

    val langID = reference("langID", LanguageTable)

    val translation = varchar("translation", 255)
}