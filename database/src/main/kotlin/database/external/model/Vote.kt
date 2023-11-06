package database.external.model

/** Vote for content, part of vote history. */
data class Vote(val contentID: Int, val vote: Int)