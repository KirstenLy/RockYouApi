package database.internal

@Deprecated("")
enum class VoteType(val voteValue: Byte) {
    UPVOTE(1),
    DOWNVOTE(-1)
}