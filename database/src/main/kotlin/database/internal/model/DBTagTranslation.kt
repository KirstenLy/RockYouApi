package database.internal.model

/** Tag translation model to fill test database. */
internal data class DBTagTranslation(
    val id: Int,
    val tagID: Int,
    val envID: Int,
    val name: String
)