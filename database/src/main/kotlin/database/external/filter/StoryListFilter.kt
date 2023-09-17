package database.external.filter

import database.internal.contract.Limitable

/** @see database.external.DatabaseAPI.getStories */
data class StoryListFilter(
    val langID: Int? = null,
    val environmentLangID: Int? = null,
    override val offset: Long?,
    override val limit: Int?
) : Limitable