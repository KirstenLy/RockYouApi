package rockyouapi.model

import kotlinx.serialization.Serializable

/** Language of content. Name can be translated on several languages. */
@Serializable
internal data class Language(val id: Int, val name: String)