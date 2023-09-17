package database.external.filter

import database.internal.contract.Limitable

/** @see database.external.DatabaseAPI.getVideos */
data class VideoListFilter(
    val langID: Int? = null,
    val environmentLangID: Byte? = null,
    override val offset: Long?,
    override val limit: Long
) : Limitable