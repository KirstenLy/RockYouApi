package database.internal.value

internal enum class VoteOperationValue(val value: Byte) {
    UPVOTE(1),
    DOWNVOTE(-1),
}