package database.external.model.tag

/** Full model of supported tag. */
data class TagFull(
    val tagID: Int,
    val translations: List<TagTranslation>
)