package database.internal.executor

import database.external.filter.CommentListFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleListResult.Data
import database.internal.entity.comment.CommentEntity
import database.internal.entity.comment.CommentTable
import database.internal.entity.toDomain
import database.internal.utils.applyLimitableIfNeeded
import declaration.entity.Comment

internal class GetCommentsRequestExecutor {

    fun execute(filter: CommentListFilter): SimpleListResult<Comment> {
        return CommentEntity
            .find { CommentTable.contentID eq filter.contentID }
            .applyLimitableIfNeeded(filter)
            .map(CommentEntity::toDomain)
            .let(::Data)
    }
}