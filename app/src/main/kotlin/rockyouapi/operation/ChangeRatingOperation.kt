package rockyouapi.operation

/** ENUM to define what user can do with content rating. */
internal enum class ChangeRatingOperation(val operationArgument: Int) {
    DOWNVOTE(0),
    UPVOTE(1)
}