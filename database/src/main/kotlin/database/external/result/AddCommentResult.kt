package database.external.result

/** @see database.external.contract.ProductionDatabaseAPI.addComment */
sealed interface AddCommentResult {

    /** Comment added. */
    data object Ok : AddCommentResult

    /** User not exists. */
    data object UserNotExists : AddCommentResult

    /** Content not exists. */
    data object ContentNotExists : AddCommentResult

    /** Error. */
    class Error(val t: Throwable) : AddCommentResult
}