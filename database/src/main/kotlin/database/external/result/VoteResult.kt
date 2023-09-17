package database.external.result

/** @see database.external.DatabaseAPI.vote */
sealed interface VoteResult {

    data object OK : VoteResult

    // TODO: Это же нужно сделать для избранного
    data object ContentNotExist : VoteResult

    data object AlreadyVoted : VoteResult

    data object AlreadyDownVoted : VoteResult

    data class Error(val t: Throwable) : VoteResult
}