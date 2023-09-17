package database.external.result

/** @see database.external.DatabaseAPI.upvote */
sealed interface VoteResult {

    data object OK : VoteResult

    data object AlreadyVoted : VoteResult

    data object AlreadyDownVoted : VoteResult

    data class Error(val t: Throwable) : VoteResult
}