package database.external.model.tag

/** Tag of content. Simplified model of [TagFull]. */
data class TagSimple(val id: Int, val name: String, val isOneOfMainTag: Boolean)