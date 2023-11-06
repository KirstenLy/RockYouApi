package database.internal.model

/** Tag model to fill test database. */
internal data class DBTag(val id: Int, val translations: List<DBTagTranslation>)