package database.internal.model

/** Language translation model to fill test database. */
internal data class DBLanguageTranslation(
    val id: Int,
    val langID: Int,
    val envID: Int,
    val name: String
)