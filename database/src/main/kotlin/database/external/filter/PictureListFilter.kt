package database.external.filter

import database.internal.contract.Limitable

/** @see database.external.DatabaseAPI.getPictures */
data class PictureListFilter(
    val langID: Int? = null,
    val environmentLangID: Int? = null,
    override val limit: Int?,
    override val offset: Long?,
) : Limitable