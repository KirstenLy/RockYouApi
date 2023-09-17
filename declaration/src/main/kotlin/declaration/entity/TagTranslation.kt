package declaration.entity

import kotlinx.serialization.Serializable

@Serializable
class TagTranslation(val langID: Int, val translation: String)