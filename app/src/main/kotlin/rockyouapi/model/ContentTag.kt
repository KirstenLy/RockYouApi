package rockyouapi.model

import kotlinx.serialization.Serializable

/** Tag of content. */
@Serializable
internal data class ContentTag(val id: Int, val name: String, val isOneOfMainTag: Boolean)