package database.internal.entity.vote_history

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class VoteHistoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VoteHistoryEntity>(VoteHistoryTable)

    val userID by VoteHistoryTable.userID

    val vote by VoteHistoryTable.vote
}