package database.external.filter

import database.internal.contract.Limitable

/** @see database.external.DatabaseAPI.getPictures */
data class PictureListFilter(
    val langID: Int? = null,
    val environmentLangID: Byte? = null,
    override val limit: Long,
    override val offset: Long?,
) : Limitable