package rockyouapi.model

import kotlinx.serialization.Serializable

/** Vote for content, part of vote history. */
@Serializable
internal data class Vote(val contentID: Int, val vote: Int)