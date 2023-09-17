package database.internal.entity.lang

import org.jetbrains.exposed.dao.id.IntIdTable

internal object LanguageTable : IntIdTable("Language") {

    val isDefault = bool("isDefault")
}