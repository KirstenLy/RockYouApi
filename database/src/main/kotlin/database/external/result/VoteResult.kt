package database.external.result

/** @see database.external.contract.ProductionDatabaseAPI.vote */
sealed interface VoteResult {

    /** Content voted. */
    data object OK : VoteResult

    /** Failed to vote, content not exist. */
    data object ContentNotExist : VoteResult

    /** Failed to vote, user not exists. */
    data object UserNotExist : VoteResult

    /** Failed to vote, user already upvoted this content. */
    data object AlreadyVoted : VoteResult

    /** Failed to vote, user already downvoted this content. */
    data object AlreadyDownVoted : VoteResult

    /** Error. */
    data class Error(val t: Throwable) : VoteResult
}