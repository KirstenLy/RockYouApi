package database.external.filter

import database.internal.contract.Limitable

/** @see database.external.DatabaseAPI.getComments */
data class CommentListFilter(
    val contentID: Int,
    override val offset: Long?,
    override val limit: Long
) : Limitable